package Database2;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class Schema {
    private Schema() {}

    public static void ensureTables(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS books (" +
                "  id   INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  name TEXT NOT NULL," +
                "  kind TEXT NOT NULL" +
                ")"
            );

            st.executeUpdate(
                "CREATE TABLE IF NOT EXISTS logs (" +
                "  id     INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  time   TEXT NOT NULL," +
                "  action TEXT NOT NULL," +
                "  detail TEXT NOT NULL" +
                ")"
            );
        }
    }

    /** 导入时用：只重建 books，不动 logs */
    public static void recreateBooks(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DROP TABLE IF EXISTS books");
            st.executeUpdate(
                "CREATE TABLE books (" +
                "  id   INTEGER PRIMARY KEY AUTOINCREMENT," +
                "  name TEXT NOT NULL," +
                "  kind TEXT NOT NULL" +
                ")"
            );
        }
    }
}
