package Database2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @description 向SQLite数据库中添加记录
 * @author TLE-11
 * @create 2025/12/23
 **/
public class AddRecordToSQLite {
    public static void main(String[] args) {
        // 加载数据库驱动
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }

        // 连接到数据库
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:kindlebooks.db")){
            conn.setAutoCommit(false);
            // 创建一个Statement对象
            Statement stmt = conn.createStatement();
            // 如果表存在，则删除重建
            // stmt.executeUpdate("drop table if exists books");

            // 创建一个新表
            stmt.executeUpdate("create table if not exists books (id integer primary key,"
            + "name varchar(256), kind varchar(16))");

            // 插入数据
            stmt.executeUpdate("insert into books (name, kind) values" +
            "('test123.pdf', 'pdf')");
            stmt.executeUpdate("insert into books (name, kind) values" +
            "('test456.pdf', 'pdf')");
            stmt.executeUpdate("insert into books (name, kind) values" +
            "('test789.pdf', 'pdf')");

            // 提交更改
            conn.commit();
            System.out.println("数据添加成功");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
