package UI;

import Database2.DB;
import Database2.Schema;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class KindleBookManagerApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try (Connection conn = DB.getConn()) {
                Schema.ensureTables(conn);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(), "启动失败", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JFrame f = new JFrame("Kindle Book Manager / Kindle书籍管理");
            f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            f.setSize(1100, 700);
            f.setLocationRelativeTo(null);

            BooksPanel booksPanel = new BooksPanel();
            LogsPanel logsPanel = new LogsPanel();

            JTabbedPane tabs = new JTabbedPane();
            tabs.addTab("书籍管理 (Books)", booksPanel);
            tabs.addTab("系统日志 (Logs)", logsPanel);

            tabs.addChangeListener(e -> {
                int idx = tabs.getSelectedIndex();
                if (idx == 0) booksPanel.reloadAllAsync();
                if (idx == 1) logsPanel.reloadAsync();
            });

            f.setLayout(new BorderLayout());
            f.add(tabs, BorderLayout.CENTER);

            f.setVisible(true);
        });
    }
}
