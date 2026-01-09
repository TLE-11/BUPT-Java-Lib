package UI;

import javax.swing.*;
import java.awt.*;

public class BookEditDialog extends JDialog {
    private boolean ok = false;
    private final JTextField nameField = new JTextField(40);
    private final JTextField kindField = new JTextField(10);

    public BookEditDialog(Window owner, String title, String initName, String initKind) {
        super(owner, title, ModalityType.APPLICATION_MODAL);

        nameField.setText(initName == null ? "" : initName);
        kindField.setText(initKind == null ? "" : initKind);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("书名(Title):"), c);
        c.gridx = 1;
        form.add(nameField, c);

        c.gridx = 0; c.gridy = 1;
        form.add(new JLabel("类型(Type):"), c);
        c.gridx = 1;
        form.add(kindField, c);

        JButton okBtn = new JButton("确定");
        JButton cancelBtn = new JButton("取消");
        okBtn.addActionListener(e -> { ok = true; dispose(); });
        cancelBtn.addActionListener(e -> dispose());

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btns.add(okBtn);
        btns.add(cancelBtn);

        setLayout(new BorderLayout());
        add(form, BorderLayout.CENTER);
        add(btns, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isOk() { return ok; }
    public String getNameValue() { return nameField.getText().trim(); }
    public String getKindValue() { return kindField.getText().trim(); }
}
