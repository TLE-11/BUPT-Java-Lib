package Database2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static Database2.Models.Book;

public class BookDAO {

    public List<Book> listAll(Connection conn) throws SQLException {
        List<Book> out = new ArrayList<>();
        String sql = "SELECT id, name, kind FROM books ORDER BY id";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(new Book(rs.getInt("id"), rs.getString("name"), rs.getString("kind")));
            }
        }
        return out;
    }

    public List<Book> searchByName(Connection conn, String keyword) throws SQLException {
        List<Book> out = new ArrayList<>();
        String sql = "SELECT id, name, kind FROM books WHERE name LIKE ? ORDER BY id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(new Book(rs.getInt("id"), rs.getString("name"), rs.getString("kind")));
                }
            }
        }
        return out;
    }

    public int add(Connection conn, String name, String kind) throws SQLException {
        String sql = "INSERT INTO books(name, kind) VALUES(?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, kind);
            ps.executeUpdate();
        }

        // SQLite 获取本连接最近一次插入的自增ID
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT last_insert_rowid()")) {
            return rs.next() ? rs.getInt(1) : -1;
        }
    }


    public void update(Connection conn, int id, String name, String kind) throws SQLException {
        String sql = "UPDATE books SET name=?, kind=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, kind);
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void delete(Connection conn, int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    /** 导入：重建 books 表 + 事务 + batch */
    public int recreateAndImport(Connection conn, List<Book> books) throws SQLException {
        Schema.recreateBooks(conn);

        String sql = "INSERT INTO books(name, kind) VALUES(?,?)";
        boolean oldAuto = conn.getAutoCommit();
        conn.setAutoCommit(false);

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int batchSize = 1000;
            int i = 0;
            for (Book b : books) {
                ps.setString(1, b.name);
                ps.setString(2, b.kind);
                ps.addBatch();
                if (++i % batchSize == 0) ps.executeBatch();
            }
            ps.executeBatch();
            conn.commit();
            return books.size();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(oldAuto);
        }
    }
}
