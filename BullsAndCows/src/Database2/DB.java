package Database2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DB {
    public static final String DB_FILE = "kindlebooks.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("找不到 org.sqlite.JDBC：请确认 sqlite-jdbc 已导入。", e);
        }
    }

    private DB() {}

    public static Connection getConn() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:" + DB_FILE);
    }
}
