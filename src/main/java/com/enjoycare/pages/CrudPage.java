package com.enjoycare.pages;

import com.enjoycare.data.AppData;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CrudPage extends JPanel {
    protected final String dataName;
    protected final boolean canAddEdit, canDelete;
    protected final boolean owner;
    protected final DefaultTableModel model;
    protected final JTable table;

    public CrudPage(String dataName, boolean owner, boolean canAddEdit, boolean canDelete) {
        this.dataName = dataName; this.owner = owner; this.canAddEdit = canAddEdit; this.canDelete = canDelete;
        this.model = AppData.table(dataName); this.table = PageStyle.table(model);
        setLayout(new BorderLayout(0, 14)); setOpaque(false);
        add(PageStyle.pageHeading("Data " + dataName, "Kelola data " + dataName.toLowerCase() + " EnjoyCare."), BorderLayout.NORTH);
        JPanel center = new JPanel(new BorderLayout(0, 10)); center.setOpaque(false);
        JPanel tools = PageStyle.toolbar();
        JTextField search = new JTextField(18); tools.add(new JLabel("Cari:")); tools.add(search);
        if (canAddEdit) {
            JButton add = PageStyle.button("+ Tambah", PageStyle.GREEN);
            JButton edit = PageStyle.button("Edit", new Color(70, 110, 145));
            tools.add(add); tools.add(edit);
            add.addActionListener(e -> editRow(-1));
            edit.addActionListener(e -> { int r = table.getSelectedRow(); if (r < 0) warn(); else editRow(table.convertRowIndexToModel(r)); });
        }
        if (canDelete) {
            JButton delete = PageStyle.button("Hapus", new Color(177, 75, 75)); tools.add(delete);
            delete.addActionListener(e -> deleteRow());
        }
        table.setAutoCreateRowSorter(true);
        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void filter() { javax.swing.RowFilter<DefaultTableModel, Object> f = search.getText().trim().isEmpty() ? null : javax.swing.RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(search.getText()));
                ((javax.swing.table.TableRowSorter<DefaultTableModel>) table.getRowSorter()).setRowFilter(f); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
        });
        center.add(tools, BorderLayout.NORTH); center.add(new JScrollPane(table), BorderLayout.CENTER); add(center, BorderLayout.CENTER);
    }
    private void editRow(int row) {
        int n = model.getColumnCount(); JTextField[] fields = new JTextField[n]; JPanel form = new JPanel(new GridLayout(n, 2, 8, 8));
        for (int i = 0; i < n; i++) { form.add(new JLabel(String.valueOf(model.getColumnName(i)) + ":")); fields[i] = new JTextField(row < 0 ? "" : String.valueOf(model.getValueAt(row, i)), 20); if (i == 0 && row >= 0) fields[i].setEditable(false); form.add(fields[i]); }
        if (JOptionPane.showConfirmDialog(this, form, row < 0 ? "Tambah " + dataName : "Edit " + dataName, JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        String[] values = new String[n];
        for (int i = 0; i < n; i++) { values[i] = fields[i].getText().trim(); if (values[i].isEmpty()) { JOptionPane.showMessageDialog(this, "Semua kolom wajib diisi."); return; } }
        if (row < 0) model.addRow(values); else for (int i = 0; i < n; i++) model.setValueAt(values[i], row, i);
    }
    private void deleteRow() { int r = table.getSelectedRow(); if (r < 0) { warn(); return; } if (JOptionPane.showConfirmDialog(this, "Hapus data terpilih?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) model.removeRow(table.convertRowIndexToModel(r)); }
    private void warn() { JOptionPane.showMessageDialog(this, "Pilih data pada tabel terlebih dahulu."); }
}
