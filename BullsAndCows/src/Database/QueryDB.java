package Database;

import java.sql.*;
import java.util.Locale;
import java.util.Scanner;

/**
 * @description 查询SQLite数据库
 * @author TLE-11
 * @create 2025/12/23
 **/
public class QueryDB {

    public static void main(String[] args) {

        String dbFile = "kindlebooks.db";
        String keyword = null;

        if (args.length >= 1) dbFile = args[0];
        if (args.length >= 2) keyword = args[1];

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("找不到 org.sqlite.JDBC：请确认 sqlite-jdbc jar 已导入。");
            e.printStackTrace();
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile)) {

            // 统计各类电子书数量，并按数量降序
            System.out.println("=== 各类型数量（降序）===");
            String statSql = "SELECT kind, COUNT(*) AS cnt FROM books GROUP BY kind ORDER BY cnt DESC";
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(statSql)) {
                while (rs.next()) {
                    System.out.printf(Locale.ROOT, "%-6s %d%n", rs.getString("kind"), rs.getLong("cnt"));
                }
            }

            // 从命令行/控制台接收查询关键字，返回 name 部分符合的记录
            if (keyword == null) {
                System.out.print("\n请输入要查询的关键词（直接回车退出）：");
                Scanner sc = new Scanner(System.in);
                keyword = sc.nextLine().trim();
            }

            if (keyword == null || keyword.isEmpty()) {
                System.out.println("未输入关键词，结束。");
                return;
            }

            System.out.println("\n=== 查询结果：name LIKE %keyword% ===");
            String qSql = "SELECT id, name, kind FROM books WHERE name LIKE ? ORDER BY id";
            try (PreparedStatement ps = conn.prepareStatement(qSql)) {
                ps.setString(1, "%" + keyword + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    int n = 0;
                    while (rs.next()) {
                        n++;
                        System.out.printf(Locale.ROOT,
                                "%4d) id:%d  %s  (%s)%n",
                                n, rs.getInt("id"), rs.getString("name"), rs.getString("kind"));
                    }
                    System.out.println("共找到 " + n + " 条。");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
