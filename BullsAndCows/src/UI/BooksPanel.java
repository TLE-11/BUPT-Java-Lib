package UI;

import Database2.*;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static Database2.Models.Book;

public class BooksPanel extends JPanel {

    private final BookDAO bookDAO = new BookDAO();
    private final LogDAO logDAO = new LogDAO();

    private final JTable table;
    private final BooksTableModel model = new BooksTableModel();

    private final JTextField searchField = new JTextField(28);

    public BooksPanel() {
        setLayout(new BorderLayout());

        // 顶部：左按钮 + 右搜索
        JPanel top = new JPanel(new BorderLayout());

        JPanel leftBtns = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton importBtn = new JButton("导入索引文件");
        JButton addBtn = new JButton("添加书籍");
        JButton delBtn = new JButton("删除书籍");
        JButton editBtn = new JButton("编辑书籍");
        JButton showAllBtn = new JButton("显示全部书籍");
        JButton refreshBtn = new JButton("刷新");

        leftBtns.add(importBtn);
        leftBtns.add(addBtn);
        leftBtns.add(delBtn);
        leftBtns.add(editBtn);
        leftBtns.add(showAllBtn);
        leftBtns.add(refreshBtn);

        JPanel rightSearch = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton searchBtn = new JButton("搜索");
        rightSearch.add(new JLabel("书名:"));
        rightSearch.add(searchField);
        rightSearch.add(searchBtn);

        top.add(leftBtns, BorderLayout.WEST);
        top.add(rightSearch, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // 表格
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 绑定事件
        refreshBtn.addActionListener(e -> reloadAllAsync());
        showAllBtn.addActionListener(e -> reloadAllAsync());
        searchBtn.addActionListener(e -> searchAsync());
        importBtn.addActionListener(e -> importIndexAsync());
        addBtn.addActionListener(e -> addBookAsync());
        editBtn.addActionListener(e -> editBookAsync());
        delBtn.addActionListener(e -> deleteBookAsync());

        reloadAllAsync();
    }

    public void reloadAllAsync() {
        new SwingWorker<List<Book>, Void>() {
            @Override protected List<Book> doInBackground() throws Exception {
                try (Connection conn = DB.getConn()) {
                    Schema.ensureTables(conn);
                    return bookDAO.listAll(conn);
                }
            }
            @Override protected void done() {
                try { model.setData(get()); }
                catch (Exception ex) { showErr(ex); }
            }
        }.execute();
    }

    private void searchAsync() {
        String kw = searchField.getText().trim();
        if (kw.isEmpty()) { reloadAllAsync(); return; }

        new SwingWorker<List<Book>, Void>() {
            @Override protected List<Book> doInBackground() throws Exception {
                try (Connection conn = DB.getConn()) {
                    Schema.ensureTables(conn);
                    List<Book> res = bookDAO.searchByName(conn, kw);
                    logDAO.addLog(conn, "Search", "搜索关键字: " + kw);
                    return res;
                }
            }
            @Override protected void done() {
                try { model.setData(get()); }
                catch (Exception ex) { showErr(ex); }
            }
        }.execute();
    }

    private void addBookAsync() {
        BookEditDialog dlg = new BookEditDialog(SwingUtilities.getWindowAncestor(this),
                "添加书籍", "", "");
        dlg.setVisible(true);
        if (!dlg.isOk()) return;

        String name = dlg.getNameValue();
        String kind = dlg.getKindValue();
        if (name.isEmpty() || kind.isEmpty()) {
            JOptionPane.showMessageDialog(this, "书名和类型不能为空", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                try (Connection conn = DB.getConn()) {
                    Schema.ensureTables(conn);
                    int id = bookDAO.add(conn, name, kind);
                    logDAO.addLog(conn, "Add Book", "添加书籍: " + name);
                }
                return null;
            }
            @Override protected void done() {
                try { get(); reloadAllAsync(); }
                catch (Exception ex) { showErr(ex); }
            }
        }.execute();
    }

    private void editBookAsync() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要编辑的书籍", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Book b = model.getAt(row);

        BookEditDialog dlg = new BookEditDialog(SwingUtilities.getWindowAncestor(this),
                "编辑书籍", b.name, b.kind);
        dlg.setVisible(true);
        if (!dlg.isOk()) return;

        String name = dlg.getNameValue();
        String kind = dlg.getKindValue();
        if (name.isEmpty() || kind.isEmpty()) {
            JOptionPane.showMessageDialog(this, "书名和类型不能为空", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                try (Connection conn = DB.getConn()) {
                    Schema.ensureTables(conn);
                    bookDAO.update(conn, b.id, name, kind);
                    logDAO.addLog(conn, "Edit Book", "编辑书籍 ID: " + b.id);
                }
                return null;
            }
            @Override protected void done() {
                try { get(); reloadAllAsync(); }
                catch (Exception ex) { showErr(ex); }
            }
        }.execute();
    }

    private void deleteBookAsync() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请先选择要删除的书籍", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Book b = model.getAt(row);

        int ok = JOptionPane.showConfirmDialog(this,
                "确定删除?\nID: " + b.id + "\nTitle: " + b.name,
                "确认删除", JOptionPane.OK_CANCEL_OPTION);
        if (ok != JOptionPane.OK_OPTION) return;

        new SwingWorker<Void, Void>() {
            @Override protected Void doInBackground() throws Exception {
                try (Connection conn = DB.getConn()) {
                    Schema.ensureTables(conn);
                    bookDAO.delete(conn, b.id);
                    logDAO.addLog(conn, "Delete Book", "删除书籍 ID: " + b.id + ", Title: " + b.name);
                }
                return null;
            }
            @Override protected void done() {
                try { get(); reloadAllAsync(); }
                catch (Exception ex) { showErr(ex); }
            }
        }.execute();
    }

    private void importIndexAsync() {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("选择 kindlebooks_index.txt");
        int r = fc.showOpenDialog(this);
        if (r != JFileChooser.APPROVE_OPTION) return;

        Path indexPath = fc.getSelectedFile().toPath();

        new SwingWorker<Integer, Void>() {
            String importLogName;

            @Override protected Integer doInBackground() throws Exception {
                List<Book> books = IndexParser.parseIndexWithFallback(indexPath);

                // 写入导入日志文件
                String ts = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                importLogName = "import_log_" + ts + ".txt";
                try (BufferedWriter bw = Files.newBufferedWriter(Path.of(importLogName))) {
                    bw.write("源文件: " + indexPath.toAbsolutePath());
                    bw.newLine();
                    bw.write("导入时间: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    bw.newLine();
                    bw.write("导入数量: " + books.size());
                    bw.newLine();
                    bw.newLine();
                    for (Book b : books) {
                        bw.write(b.kind + "\t" + b.name);
                        bw.newLine();
                    }
                }

                try (Connection conn = DB.getConn()) {
                    Schema.ensureTables(conn);
                    int n = bookDAO.recreateAndImport(conn, books);

                    // 按你图里的顺序：先 Import，再 Import File
                    logDAO.addLog(conn, "Import", "成功导入 " + n + " 本书籍");
                    logDAO.addLog(conn, "Import File", "导入日志已保存至: " + importLogName);
                    return n;
                }
            }

            @Override protected void done() {
                try {
                    int n = get();
                    reloadAllAsync();
                    JOptionPane.showMessageDialog(BooksPanel.this,
                            "导入完成：共 " + n + " 本书籍\n导入日志：" + importLogName,
                            "导入成功", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    showErr(ex);
                }
            }
        }.execute();
    }

    private void showErr(Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
    }

    // ===== TableModel =====
    static class BooksTableModel extends AbstractTableModel {
        private final String[] cols = {"ID", "书名 (Title)", "类型 (Type)"};
        private List<Book> data = List.of();

        public void setData(List<Book> data) {
            this.data = data;
            fireTableDataChanged();
        }

        public Book getAt(int row) { return data.get(row); }

        @Override public int getRowCount() { return data.size(); }
        @Override public int getColumnCount() { return cols.length; }
        @Override public String getColumnName(int column) { return cols[column]; }
        @Override public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }

        @Override public Object getValueAt(int rowIndex, int columnIndex) {
            Book b = data.get(rowIndex);
            switch (columnIndex) {
                case 0: return b.id;
                case 1: return b.name;
                case 2: return b.kind;
                default: return "";
            }
        }
    }
}
