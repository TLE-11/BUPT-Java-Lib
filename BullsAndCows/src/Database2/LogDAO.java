package Database2;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static Database2.Models.LogEntry;

public class LogDAO {

    private static String nowStr() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public void addLog(Connection conn, String action, String detail) throws SQLException {
        String sql = "INSERT INTO logs(time, action, detail) VALUES(?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nowStr());
            ps.setString(2, action);
            ps.setString(3, detail);
            ps.executeUpdate();
        }
    }

    public List<LogEntry> listDesc(Connection conn) throws SQLException {
        List<LogEntry> out = new ArrayList<>();
        String sql = "SELECT id, time, action, detail FROM logs ORDER BY id DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new LogEntry(
                        rs.getInt("id"),
                        rs.getString("time"),
                        rs.getString("action"),
                        rs.getString("detail")
                ));
            }
        }
        return out;
    }

    public void deleteById(Connection conn, int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM logs WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void clearAll(Connection conn) throws SQLException {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM logs");
        }
    }
}
