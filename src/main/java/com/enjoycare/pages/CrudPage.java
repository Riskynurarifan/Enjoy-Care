
package com.enjoycare.pages;

import com.enjoycare.data.AppData;
import java.awt.*;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CrudPage extends JPanel {

    protected final String dataName;
    protected final boolean canAddEdit, canDelete;
    protected final boolean owner;
    protected final DefaultTableModel model;
    protected final JTable table;

    public CrudPage(
            String dataName,
            boolean owner,
            boolean canAddEdit,
            boolean canDelete
    ) {

        this.dataName = dataName;
        this.owner = owner;
        this.canAddEdit = canAddEdit;
        this.canDelete = canDelete;

        this.model = AppData.table(dataName);
        this.table = PageStyle.table(model);

        setLayout(new BorderLayout(0, 14));
        setOpaque(false);

        add(
                PageStyle.pageHeading(
                        "Data " + dataName,
                        "Kelola data " + dataName.toLowerCase() + " EnjoyCare."
                ),
                BorderLayout.NORTH
        );

        JPanel center = new JPanel(new BorderLayout(0, 10));
        center.setOpaque(false);

        JPanel tools = PageStyle.toolbar();

        JTextField search = new JTextField(18);

        tools.add(new JLabel("Cari:"));
        tools.add(search);

        if (canAddEdit) {

            JButton add = PageStyle.button(
                    "+ Tambah",
                    PageStyle.GREEN
            );

            JButton edit = PageStyle.button(
                    "Edit",
                    new Color(70, 110, 145)
            );

            tools.add(add);
            tools.add(edit);

            add.addActionListener(e -> editRow(-1));

            edit.addActionListener(e -> {

                int r = table.getSelectedRow();

                if (r < 0) {
                    warn();
                } else {
                    editRow(table.convertRowIndexToModel(r));
                }

            });
        }

        if (canDelete) {

            JButton delete = PageStyle.button(
                    "Hapus",
                    new Color(177, 75, 75)
            );

            tools.add(delete);
            delete.addActionListener(e -> deleteRow());
        }

        table.setAutoCreateRowSorter(true);

        search.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {

            private void filter() {

                String text = search.getText().trim();

                javax.swing.RowFilter<DefaultTableModel, Object> f =
                        text.isEmpty()
                        ? null
                        : javax.swing.RowFilter.regexFilter(
                                "(?i)" + java.util.regex.Pattern.quote(text)
                        );

                ((javax.swing.table.TableRowSorter<DefaultTableModel>)
                        table.getRowSorter()).setRowFilter(f);
            }

            public void insertUpdate(
                    javax.swing.event.DocumentEvent e
            ) {
                filter();
            }

            public void removeUpdate(
                    javax.swing.event.DocumentEvent e
            ) {
                filter();
            }

            public void changedUpdate(
                    javax.swing.event.DocumentEvent e
            ) {
                filter();
            }
        });

        center.add(tools, BorderLayout.NORTH);
        center.add(new JScrollPane(table), BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);
    }

    // =========================================
    // FORM TAMBAH DAN EDIT
    // =========================================

    private void editRow(int row) {

        if ("Layanan".equals(dataName)) {
            editLayanan(row);
            return;
        }

        editGeneral(row);
    }

    // =========================================
    // FORM KHUSUS LAYANAN
    // =========================================

    private void editLayanan(int row) {

        int n = model.getColumnCount();

        JTextField[] fields = new JTextField[n];

        JPanel form = new JPanel(
                new GridLayout(n, 2, 8, 10)
        );

        // ID
        fields[0] = new JTextField(
                row < 0 ? generateId() :
                String.valueOf(model.getValueAt(row, 0)),
                20
        );

        fields[0].setEditable(false);

        // DROPDOWN NAMA LAYANAN
        Set<String> serviceNames = new LinkedHashSet<>();

        for (int i = 0; i < model.getRowCount(); i++) {
            serviceNames.add(
                    String.valueOf(model.getValueAt(i, 1))
            );
        }

        JComboBox<String> serviceCombo =
                new JComboBox<>(serviceNames.toArray(new String[0]));

        serviceCombo.setEditable(true);
        serviceCombo.setPreferredSize(new Dimension(200, 30));

        // HARGA
        fields[2] = new JTextField(20);

        // KETERANGAN
        fields[3] = new JTextField(20);

        if (row >= 0) {

            serviceCombo.setSelectedItem(
                    String.valueOf(model.getValueAt(row, 1))
            );

            fields[2].setText(
                    String.valueOf(model.getValueAt(row, 2))
            );

            fields[3].setText(
                    String.valueOf(model.getValueAt(row, 3))
            );

        }

        // OTOMATIS MENGISI HARGA
        serviceCombo.addActionListener(e -> {

            Object selected = serviceCombo.getSelectedItem();

            if (selected == null) {
                return;
            }

            String selectedName = selected.toString().trim();

            for (int i = 0; i < model.getRowCount(); i++) {

                String existingName =
                        String.valueOf(model.getValueAt(i, 1));

                if (existingName.equalsIgnoreCase(selectedName)) {

                    fields[2].setText(
                            String.valueOf(model.getValueAt(i, 2))
                    );

                    fields[3].setText(
                            String.valueOf(model.getValueAt(i, 3))
                    );

                    break;
                }
            }
        });

        // SUSUN FORM
        form.add(new JLabel("ID:"));
        form.add(fields[0]);

        form.add(new JLabel("Nama Layanan:"));
        form.add(serviceCombo);

        form.add(new JLabel("Harga (Rp):"));
        form.add(fields[2]);

        form.add(new JLabel("Keterangan:"));
        form.add(fields[3]);

        String title = row < 0
                ? "Tambah Layanan"
                : "Edit Layanan";

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                title,
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String id = fields[0].getText().trim();

        Object selected = serviceCombo.getSelectedItem();

        String nama = selected == null
                ? ""
                : selected.toString().trim();

        String harga = fields[2].getText().trim();
        String keterangan = fields[3].getText().trim();

        if (nama.isEmpty() || harga.isEmpty()
                || keterangan.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Semua kolom wajib diisi."
            );

            return;
        }

        // VALIDASI HARGA
        try {

            long nominal = Long.parseLong(harga);

            if (nominal <= 0) {
                throw new NumberFormatException();
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Harga harus berupa angka lebih dari 0.",
                    "Harga Tidak Valid",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        // CEGAH NAMA LAYANAN DUPLIKAT
        for (int i = 0; i < model.getRowCount(); i++) {

            if (i == row) {
                continue;
            }

            String existingName =
                    String.valueOf(model.getValueAt(i, 1));

            if (existingName.equalsIgnoreCase(nama)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Nama layanan tersebut sudah tersedia."
                );

                return;
            }
        }

        String[] values = {
            id,
            nama,
            harga,
            keterangan
        };

        if (row < 0) {
            model.addRow(values);
        } else {
            for (int i = 0; i < values.length; i++) {
                model.setValueAt(values[i], row, i);
            }
        }
    }

    // =========================================
    // ID OTOMATIS LAYANAN
    // =========================================

    private String generateId() {

        int next = 1;

        for (int i = 0; i < model.getRowCount(); i++) {

            String id = String.valueOf(model.getValueAt(i, 0));

            if (id.startsWith("LYN")) {

                try {

                    int number = Integer.parseInt(
                            id.substring(3)
                    );

                    if (number >= next) {
                        next = number + 1;
                    }

                } catch (NumberFormatException ignored) {
                }
            }
        }

        return String.format("LYN%03d", next);
    }

    // =========================================
    // FORM CRUD HALAMAN LAIN
    // =========================================

    private void editGeneral(int row) {

        int n = model.getColumnCount();

        JTextField[] fields = new JTextField[n];

        JPanel form = new JPanel(
                new GridLayout(n, 2, 8, 8)
        );

        for (int i = 0; i < n; i++) {

            form.add(
                    new JLabel(
                            String.valueOf(model.getColumnName(i)) + ":"
                    )
            );

            fields[i] = new JTextField(
                    row < 0
                    ? ""
                    : String.valueOf(model.getValueAt(row, i)),
                    20
            );

            if (i == 0 && row >= 0) {
                fields[i].setEditable(false);
            }

            form.add(fields[i]);
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                form,
                row < 0
                ? "Tambah " + dataName
                : "Edit " + dataName,
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        String[] values = new String[n];

        for (int i = 0; i < n; i++) {

            values[i] = fields[i].getText().trim();

            if (values[i].isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Semua kolom wajib diisi."
                );

                return;
            }
        }

        if (row < 0) {
            model.addRow(values);
        } else {
            for (int i = 0; i < n; i++) {
                model.setValueAt(values[i], row, i);
            }
        }
    }

    // =========================================
    // HAPUS DATA
    // =========================================

    private void deleteRow() {

        int r = table.getSelectedRow();

        if (r < 0) {
            warn();
            return;
        }

        int result = JOptionPane.showConfirmDialog(
                this,
                "Hapus data terpilih?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        if (result == JOptionPane.YES_OPTION) {

            model.removeRow(
                    table.convertRowIndexToModel(r)
            );
        }
    }

    private void warn() {

        JOptionPane.showMessageDialog(
                this,
                "Pilih data pada tabel terlebih dahulu."
        );
    }
}
