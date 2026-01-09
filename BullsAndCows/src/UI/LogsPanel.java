package UI;

import Database2.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.sql.Connection;
import java.util.List;

import static Database2.Models.LogEntry;

public class LogsPanel extends JPanel {

    private final LogDAO logDAO = new LogDAO();
    private final JTable table;
    private final LogsTableModel model = new LogsTableModel();

    public LogsPanel() {
        setLayout(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn = new JButton("刷新日志");
        JButton delSelectedBtn = new JButton("删除选中日志");
        JButton clearAllBtn = new JButton("清除所有日志");
        top.add(refreshBtn);
        top.add(delSelectedBtn);
        top.add(clearAllBtn);

        add(top, BorderLayout.NORTH);

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        refreshBtn.addActionListener(e -> reloadAsync());
        delSelectedBtn.addActionListener(e -> deleteSelectedAsync());
        clearAllBtn.addActionListener(e -> clearAllAsync());

        reloadAsync();
    }

    public void reloadAsync() {
        new SwingWorker<List<LogEntry>, Void>() {
            @Override protected List<LogEntry> doInBackground() throws Exception {
                try (Connection conn = DB.getConn()) {
                    Schema.ensureTables(conn);
                    return logDAO.listDesc(conn);
                }
            }
            @Override protected void done() {
                try { model.setData(get()); }
                catch (Exception ex) { showErr(ex); }
            }
        }.execute();
    }

    private void deleteSelectedAsync() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的日志", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        LogEntry le = model.getAt(row);

        int ok = JOptionPane.showConfirmDialog(this,
                "确定删除日志?\nID: " + le.id,
                "确认删除", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;

        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                try (Connection conn = DB.getConn()) {
                    Schema.ensureTables(conn);
                    logDAO.deleteById(conn, le.id);
                }
                return null;
            }
            @Override protected void done() {
                try { get(); reloadAsync(); }
                catch (Exception ex) { showErr(ex); }
            }
        }.execute();
    }

    private void clearAllAsync() {
        int ok = JOptionPane.showConfirmDialog(this,
                "确定清除所有日志？",
                "确认清除", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;

        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                try (Connection conn = DB.getConn()) {
                    Schema.ensureTables(conn);
                    logDAO.clearAll(conn);
                }
                return null;
            }
            @Override protected void done() {
                try { get(); reloadAsync(); }
                catch (Exception ex) { showErr(ex); }
            }
        }.execute();
    }

    private void showErr(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }

    static class LogsTableModel extends AbstractTableModel {
        private final String[] cols = {"ID", "时间 (Time)", "操作 (Action)", "详情 (Details)"};
        private List<LogEntry> data = List.of();

        public void setData(List<LogEntry> data) {
            this.data = data;
            fireTableDataChanged();
        }

        public LogEntry getAt(int row) { return data.get(row); }

        @Override public int getRowCount() { return data.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }
        @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

        @Override public Object getValueAt(int rowIndex, int columnIndex) {
            LogEntry le = data.get(rowIndex);
            switch (columnIndex) {
                case 0: return le.id;
                case 1: return le.time;
                case 2: return le.action;
                case 3: return le.detail;
                default: return "";
            }
        }
    }
}
