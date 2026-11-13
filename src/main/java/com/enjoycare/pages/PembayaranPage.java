
package com.enjoycare.pages;

import com.enjoycare.dao.PembayaranDAO;
import com.enjoycare.dao.PesananDAO;
import com.enjoycare.entity.Pembayaran;
import com.enjoycare.entity.Pesanan;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PembayaranPage extends JPanel {

    private static final List<String[]> REQUESTS = new ArrayList<>();

    private final boolean owner;
    private final PembayaranDAO dao = new PembayaranDAO();
    private final PesananDAO pesananDAO = new PesananDAO();

    private final DefaultTableModel model = new DefaultTableModel(
        new String[]{
            "ID", "ID Pesanan", "Tanggal", "Metode",
            "Nominal", "Status", "Catatan"
        }, 0
    ) {
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    private final JTable table = PageStyle.table(model);

    public PembayaranPage(boolean owner) {
        this.owner = owner;

        setOpaque(false);
        setLayout(new BorderLayout(0, 14));

        add(
            PageStyle.pageHeading(
                "Data Pembayaran",
                owner
                    ? "Owner dapat mengelola transaksi dan pengajuan koreksi."
                    : "Admin dapat melihat pembayaran dan mengajukan koreksi."
            ),
            BorderLayout.NORTH
        );

        JPanel c = new JPanel(new BorderLayout(0, 10));
        c.setOpaque(false);

        JPanel t = PageStyle.toolbar();

        if (owner) {
            JButton a = PageStyle.button("+ Catat Pembayaran", PageStyle.GREEN);
            JButton e = PageStyle.button("Edit Transaksi", new Color(70, 110, 145));
            JButton d = PageStyle.button("Hapus Transaksi", new Color(177, 75, 75));
            JButton rq = PageStyle.button("Pengajuan Koreksi", new Color(90, 110, 100));

            t.add(a);
            t.add(e);
            t.add(d);
            t.add(rq);

            a.addActionListener(x -> form(null));

            e.addActionListener(x -> {
                Pembayaran p = selected();
                if (p != null) {
                    form(p);
                }
            });

            d.addActionListener(x -> {
                Pembayaran p = selected();

                if (p != null &&
                    JOptionPane.showConfirmDialog(
                        this,
                        "Hapus transaksi ini?",
                        "Konfirmasi",
                        JOptionPane.YES_NO_OPTION
                    ) == JOptionPane.YES_OPTION) {

                    run(() -> dao.delete(p.getIdPembayaran()));
                }
            });

            rq.addActionListener(x -> showRequests());

        } else {
            JButton rq = PageStyle.button("Ajukan Koreksi", PageStyle.GREEN);

            t.add(rq);
            rq.addActionListener(x -> requestCorrection());
        }

        c.add(t, BorderLayout.NORTH);
        c.add(new JScrollPane(table), BorderLayout.CENTER);

        add(c, BorderLayout.CENTER);

        load();
    }

    // Format Rupiah: Rp200.000,00
    private String formatRupiah(BigDecimal nominal) {
        DecimalFormatSymbols symbols =
            new DecimalFormatSymbols(new Locale("id", "ID"));

        DecimalFormat format =
            new DecimalFormat("'Rp'#,##0.00", symbols);

        return format.format(nominal);
    }

    private void load() {
        model.setRowCount(0);

        try {
            for (Pembayaran p : dao.getAll()) {
                model.addRow(new Object[]{
                    p.getIdPembayaran(),
                    p.getPesanan().getIdPesanan(),
                    p.getTanggalPembayaran(),
                    p.getMetodePembayaran(),
                    formatRupiah(p.getNominal()),
                    p.getStatusPembayaran(),
                    p.getCatatan()
                });
            }
        } catch (RuntimeException e) {
            error(e);
        }
    }

    private Pembayaran selected() {
        int r = table.getSelectedRow();

        if (r < 0) {
            JOptionPane.showMessageDialog(
                this,
                "Pilih transaksi terlebih dahulu."
            );
            return null;
        }

        try {
            return dao.getById(
                Long.valueOf(
                    model.getValueAt(
                        table.convertRowIndexToModel(r), 0
                    ).toString()
                )
            );
        } catch (RuntimeException e) {
            error(e);
            return null;
        }
    }

    private void form(Pembayaran old) {
        boolean n = old == null;

        List<Pesanan> orders = pesananDAO.getAll();

        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Buat pesanan terlebih dahulu."
            );
            return;
        }

        JComboBox<Pesanan> order =
            new JComboBox<>(orders.toArray(new Pesanan[0]));

        order.setRenderer(new DefaultListCellRenderer() {
            public Component getListCellRendererComponent(
                JList<?> l, Object v, int i, boolean s, boolean f
            ) {
                super.getListCellRendererComponent(l, v, i, s, f);

                if (v instanceof Pesanan) {
                    setText(
                        "Pesanan #" + ((Pesanan) v).getIdPesanan()
                        + " - "
                        + ((Pesanan) v).getPelanggan().getNamaPelanggan()
                    );
                }

                return this;
            }
        });

        JTextField date = new JTextField(
            n ? LocalDate.now().toString()
              : old.getTanggalPembayaran().toString(), 12
        );

        JTextField amount = new JTextField(
            n ? "" : old.getNominal().toPlainString(), 12
        );

        JComboBox<String> method = new JComboBox<>(
            new String[]{"Tunai", "Transfer", "QRIS", "Lainnya"}
        );

        JComboBox<String> status = new JComboBox<>(
            new String[]{"Belum Bayar", "DP", "Lunas"}
        );

        JTextArea note = new JTextArea(
            n ? "" : old.getCatatan(), 3, 18
        );

        if (!n) {
            for (int i = 0; i < order.getItemCount(); i++) {
                if (order.getItemAt(i).getIdPesanan()
                    .equals(old.getPesanan().getIdPesanan())) {
                    order.setSelectedIndex(i);
                    break;
                }
            }

            method.setSelectedItem(old.getMetodePembayaran());
            status.setSelectedItem(old.getStatusPembayaran());
        }

        JPanel f = new JPanel(new GridLayout(0, 2, 7, 7));

        add(f, "Pesanan", order);
        add(f, "Tanggal (YYYY-MM-DD)", date);
        add(f, "Metode", method);
        add(f, "Nominal (Rp)", amount);
        add(f, "Status", status);
        add(f, "Catatan", new JScrollPane(note));

        if (JOptionPane.showConfirmDialog(
            this,
            f,
            n ? "Catat Pembayaran" : "Edit Pembayaran",
            JOptionPane.OK_CANCEL_OPTION
        ) != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            LocalDate d = LocalDate.parse(date.getText().trim());

            BigDecimal a =
                new BigDecimal(amount.getText().trim());

            if (a.signum() < 0) {
                throw new IllegalArgumentException(
                    "Nominal tidak boleh negatif."
                );
            }

            Pembayaran p = n ? new Pembayaran() : old;

            p.setPesanan((Pesanan) order.getSelectedItem());
            p.setTanggalPembayaran(d);
            p.setMetodePembayaran((String) method.getSelectedItem());
            p.setNominal(a);
            p.setStatusPembayaran((String) status.getSelectedItem());
            p.setCatatan(note.getText().trim());

            run(() -> dao.save(p));

        } catch (Exception e) {
            error(e);
        }
    }

    private void requestCorrection() {
        JTextField id = new JTextField();
        JTextArea reason = new JTextArea(4, 20);

        JPanel p = new JPanel(new GridLayout(0, 1, 5, 5));

        p.add(new JLabel("ID Transaksi:"));
        p.add(id);
        p.add(new JLabel("Alasan koreksi:"));
        p.add(new JScrollPane(reason));

        if (JOptionPane.showConfirmDialog(
            this,
            p,
            "Ajukan Koreksi",
            JOptionPane.OK_CANCEL_OPTION
        ) != JOptionPane.OK_OPTION) {
            return;
        }

        if (id.getText().trim().isEmpty()
            || reason.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Semua kolom wajib diisi."
            );
            return;
        }

        REQUESTS.add(new String[]{
            "KOR" + String.format("%03d", REQUESTS.size() + 1),
            id.getText().trim(),
            reason.getText().trim(),
            "Menunggu"
        });

        JOptionPane.showMessageDialog(
            this,
            "Pengajuan koreksi dicatat selama aplikasi berjalan."
        );
    }

    private void showRequests() {
        DefaultTableModel m = new DefaultTableModel(
            new String[]{"ID Koreksi", "ID Transaksi", "Alasan", "Status"}, 0
        );

        for (String[] r : REQUESTS) {
            m.addRow(r);
        }

        JTable t = PageStyle.table(m);

        JOptionPane.showMessageDialog(
            this,
            new JScrollPane(t),
            "Pengajuan Koreksi",
            JOptionPane.PLAIN_MESSAGE
        );
    }

    private void add(JPanel p, String l, Component c) {
        p.add(new JLabel(l + ":"));
        p.add(c);
    }

    private void run(Runnable r) {
        try {
            r.run();
            load();
        } catch (RuntimeException e) {
            error(e);
        }
    }

    private void error(Exception e) {
        JOptionPane.showMessageDialog(
            this,
            "Operasi database gagal:\n" + e.getMessage(),
            "Database",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
