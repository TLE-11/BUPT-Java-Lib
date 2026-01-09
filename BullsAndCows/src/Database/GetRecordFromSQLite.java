package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

/**
 * @description 从SQLite数据库中获取记录
 * @author TLE-11
 * @create 2025/12/23
 **/
public class GetRecordFromSQLite {
    public static void main(String[] args) {
        try {
            // 加载驱动
            Class.forName("org.sqlite.JDBC");

            // 建立连接
            String url = "jdbc:sqlite:kindlebooks.db";
            Connection connection = DriverManager.getConnection(url);

            // 创建Statement对象
            Statement statement = connection.createStatement();

            // 执行查询
            ResultSet resultSet = statement.executeQuery("SELECT * FROM books");

            // 处理查询结果
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String kind = resultSet.getString("kind");
                System.out.println("ID: " + id + ", Name: " + name + ", Kind: " + kind);
            }

            // 关闭资源
            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
