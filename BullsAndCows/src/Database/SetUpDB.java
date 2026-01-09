package Database;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @description 创建SQLite数据库
 * @author TLE-11
 * @create 2025/12/23
 **/
public class SetUpDB {

    // 你给的索引文件绝对路径：直接运行就用这个
    private static final String INDEX_ABS =
            "/Users/Apple/IdeaProjects/Java_Class/2025.9.23/BullsAndCows/src/Database/kindlebooks_index.txt";

    // 数据库文件名（会生成在 Working Directory 下）
    private static final String DB_FILE = "kindlebooks.db";

    // 题目要求的类型：pdf, mobi, epub, azw3, html, txt（只收小写，才能得到 17520）
    private static final String[] ALLOWED = {"pdf", "mobi", "epub", "azw3", "html", "txt"};

    static class Book {
        String name;
        String kind;
        Book(String name, String kind) { this.name = name; this.kind = kind; }
    }

    private static boolean endsWithAllowedLowercaseExt(String filename) {
        for (String k : ALLOWED) {
            if (filename.endsWith("." + k)) return true; // 大小写敏感：只认小写
        }
        return false;
    }

    private static String getKind(String filename) {
        int i = filename.lastIndexOf('.');
        return (i >= 0 && i < filename.length() - 1) ? filename.substring(i + 1) : "";
    }

    private static List<Book> parseIndex(Path indexPath, Charset cs) throws IOException {
        List<Book> books = new ArrayList<>(20000);
        String currentDir = ""; // 必须从空开始，只用于记录索引里的 "./xxx" 前缀

        try (BufferedReader br = Files.newBufferedReader(indexPath, cs)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // 目录行：./xxx:
                if (line.startsWith("./") && line.endsWith(":")) {
                    currentDir = line.substring(0, line.length() - 1); // 去掉末尾 ':'
                    continue;
                }

                // 只保留指定后缀（小写）
                if (!endsWithAllowedLowercaseExt(line)) continue;

                String kind = getKind(line);
                String fullName;

                if (line.startsWith("./")) {
                    fullName = line;
                } else if (!currentDir.isEmpty()) {
                    fullName = currentDir + "/" + line;
                } else {
                    fullName = line; // 兜底
                }

                books.add(new Book(fullName, kind));
            }
        }
        return books;
    }

    // 先 UTF-8，失败再尝试 GBK（防止文件编码不是 UTF-8）
    private static List<Book> parseIndexWithFallback(Path indexPath) throws IOException {
        try {
            return parseIndex(indexPath, StandardCharsets.UTF_8);
        } catch (MalformedInputException e) {
            return parseIndex(indexPath, Charset.forName("GBK"));
        }
    }

    private static void recreateTable(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DROP TABLE IF EXISTS books");
            st.executeUpdate("CREATE TABLE books (id INTEGER PRIMARY KEY, name VARCHAR(256), kind VARCHAR(16))");
        }
    }

/**
 * 向数据库批量插入书籍数据的方法
 * @param conn 数据库连接对象
 * @param books 待插入的书籍列表
 * @param useTransaction 是否使用事务
 * @return 执行操作所花费的纳秒数
 * @throws SQLException 如果数据库访问出错
 */
    private static long insertBooks(Connection conn, List<Book> books, boolean useTransaction) throws SQLException {
    // SQL插入语句，使用参数占位符
        String sql = "INSERT INTO books(name, kind) VALUES (?, ?)";
    // 保存原始的自动提交状态
        boolean oldAuto = conn.getAutoCommit();

    // 根据useTransaction参数设置自动提交模式
        conn.setAutoCommit(!useTransaction); // useTransaction=true -> autoCommit=false

    // 记录开始时间
        long start = System.nanoTime();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            if (useTransaction) {

            // 使用事务批量插入
                int batchSize = 1000; // 批处理大小
                int i = 0; // 计数器
                for (Book b : books) {
                // 设置参数
                    ps.setString(1, b.name);
                    ps.setString(2, b.kind);
                    ps.addBatch(); // 添加到批处理
                // 达到批处理大小时执行批处理
                    if (++i % batchSize == 0) ps.executeBatch();
                }
            // 执行剩余的批处理
                ps.executeBatch();
            // 提交事务
                conn.commit();
            } else {
                // 不开事务：每次 executeUpdate 都会提交
                for (Book b : books) {
                    ps.setString(1, b.name);
                    ps.setString(2, b.kind);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            if (useTransaction) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            throw e;
        } finally {
            conn.setAutoCommit(oldAuto);
        }

        return System.nanoTime() - start;
    }

    private static long count(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) AS c FROM books")) {
            return rs.next() ? rs.getLong("c") : 0;
        }
    }

    public static void main(String[] args) {
        System.out.println("Working dir = " + System.getProperty("user.dir"));
        Path indexPath = Paths.get(INDEX_ABS);
        System.out.println("Index path  = " + indexPath);
        System.out.println("Index exists= " + Files.exists(indexPath));

        // 1) 加载驱动
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("找不到 org.sqlite.JDBC：请确认 sqlite-jdbc jar 已导入。");
            e.printStackTrace();
            return;
        }

        // 2) 解析索引
        List<Book> books;
        try {
            if (!Files.exists(indexPath)) {
                System.err.println("索引文件不存在：请确认 INDEX_ABS 路径是否正确。");
                return;
            }
            books = parseIndexWithFallback(indexPath);
        } catch (IOException e) {
            System.err.println("读取索引文件失败：请确认路径/编码正确。");
            e.printStackTrace();
            return;
        }

        System.out.println("解析到可入库记录数 = " + books.size());

        // 3) 建表 + 插入 + 对比事务耗时
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE)) {

            recreateTable(conn);
            long tNoTx = insertBooks(conn, books, false);
            long c1 = count(conn);

            recreateTable(conn);
            long tTx = insertBooks(conn, books, true);
            long c2 = count(conn);

            System.out.printf(Locale.ROOT, "不使用事务：count=%d, time=%.2f ms%n", c1, tNoTx / 1_000_000.0);
            System.out.printf(Locale.ROOT, "使用事务：  count=%d, time=%.2f ms%n", c2, tTx / 1_000_000.0);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
