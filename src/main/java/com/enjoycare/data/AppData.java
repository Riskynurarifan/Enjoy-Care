package com.enjoycare.data;

import javax.swing.table.DefaultTableModel;
import java.util.LinkedHashMap;
import java.util.Map;

public final class AppData {
    private static final Map<String, DefaultTableModel> TABLES = new LinkedHashMap<>();
    private AppData() {}

    static {
        add("Pelanggan", new String[]{"ID", "Nama", "No. Telepon", "Alamat"},
            new String[]{"PLG001", "Budi Santoso", "081234567801", "Depok"},
            new String[]{"PLG002", "Siti Aminah", "081234567802", "Jakarta Selatan"},
            new String[]{"PLG003", "Andi Wijaya", "081234567803", "Bogor"});
        add("Layanan", new String[]{"ID", "Nama Layanan", "Harga", "Keterangan"},
            new String[]{"LYN001", "Cuci AC", "75000", "Cuci AC split per unit"},
            new String[]{"LYN002", "Tambah Freon", "150000", "Pengisian freon"},
            new String[]{"LYN003", "Service AC", "200000", "Pemeriksaan dan perbaikan"});
        add("Teknisi", new String[]{"ID", "Nama Teknisi", "No. Telepon", "Status"},
            new String[]{"TKN001", "Dedi", "081299991111", "Tersedia"},
            new String[]{"TKN002", "Rian", "081299992222", "Bertugas"},
            new String[]{"TKN003", "Agus", "081299993333", "Tersedia"});
        add("Pesanan", new String[]{"ID Pesanan", "Tanggal", "Pelanggan", "Layanan", "Teknisi", "Total", "Status"},
            new String[]{"ORD001", "30-10-2026", "Budi Santoso", "Cuci AC", "Dedi", "150000", "Selesai"},
            new String[]{"ORD002", "30-10-2026", "Siti Aminah", "Service AC", "Rian", "200000", "Dikerjakan"},
            new String[]{"ORD003", "30-10-2026", "Andi Wijaya", "Tambah Freon", "Agus", "150000", "Menunggu"});
        add("Pembayaran", new String[]{"ID Transaksi", "ID Pesanan", "Pelanggan", "Tanggal", "Metode", "Nominal", "Status"},
            new String[]{"TRX001", "ORD001", "Budi Santoso", "30-10-2026", "Transfer", "150000", "Lunas"},
            new String[]{"TRX002", "ORD002", "Siti Aminah", "30-10-2026", "Tunai", "100000", "DP"},
            new String[]{"TRX003", "ORD003", "Andi Wijaya", "30-10-2026", "Transfer", "0", "Belum Bayar"});
        add("Pengguna", new String[]{"ID", "Username", "Role", "Status"},
            new String[]{"USR001", "owner", "Owner", "Aktif"},
            new String[]{"USR002", "admin", "Admin", "Aktif"});
    }

    private static void add(String key, String[] columns, String[]... rows) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        for (String[] row : rows) model.addRow(row);
        TABLES.put(key, model);
    }

    public static DefaultTableModel table(String key) { return TABLES.get(key); }
    public static int count(String key) { return TABLES.get(key).getRowCount(); }
    public static long totalPaid() {
        long total = 0;
        DefaultTableModel m = table("Pembayaran");
        for (int i = 0; i < m.getRowCount(); i++) {
            try { total += Long.parseLong(String.valueOf(m.getValueAt(i, 5))); }
            catch (NumberFormatException ignored) {}
        }
        return total;
    }
}
