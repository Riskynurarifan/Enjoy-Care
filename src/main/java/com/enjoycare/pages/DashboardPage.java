
package com.enjoycare.pages;

import com.enjoycare.dao.PesananDAO;
import com.enjoycare.dao.PembayaranDAO;
import com.enjoycare.dao.PelangganDAO;
import com.enjoycare.dao.LayananDAO;

import com.enjoycare.entity.Pesanan;
import com.enjoycare.entity.Pembayaran;
import com.enjoycare.entity.Pelanggan;
import com.enjoycare.entity.Layanan;

import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;

import javax.swing.*;

public class DashboardPage extends JPanel {

    public DashboardPage(boolean owner) {

        setOpaque(false);
        setLayout(new BorderLayout(0, 18));

        // =====================================
        // MENGAMBIL DATA DARI DATABASE
        // =====================================

        JPanel cards = new JPanel(
                new GridLayout(1, owner ? 4 : 3, 12, 0)
        );

        cards.setOpaque(false);

        try {

            List<Pesanan> orders =
                    new PesananDAO().getAll();

            List<Pembayaran> payments =
                    new PembayaranDAO().getAll();

            List<Pelanggan> customers =
                    new PelangganDAO().getAll();

            List<Layanan> services =
                    new LayananDAO().getAll();

            int today = 0;
            int working = 0;
            int unpaid = 0;

            int waiting = 0;
            int completed = 0;
            int monthlyOrders = 0;

            BigDecimal income = BigDecimal.ZERO;

            LocalDate currentDate = LocalDate.now();
            YearMonth currentMonth = YearMonth.now();

            // =================================
            // DATA PESANAN
            // =================================

            for (Pesanan p : orders) {

                if (p.getTanggalPesanan() != null) {

                    LocalDate date = p.getTanggalPesanan();

                    if (date.equals(currentDate)) {
                        today++;
                    }

                    if (YearMonth.from(date).equals(currentMonth)) {
                        monthlyOrders++;
                    }
                }

                String status = p.getStatusPesanan();

                if (status != null) {

                    if ("Menunggu".equalsIgnoreCase(status)) {
                        waiting++;
                    }

                    if ("Dikerjakan".equalsIgnoreCase(status)) {
                        working++;
                    }

                    if ("Selesai".equalsIgnoreCase(status)) {
                        completed++;
                    }
                }
            }

            // =================================
            // DATA PEMBAYARAN
            // =================================

            for (Pembayaran p : payments) {

                if ("Lunas".equalsIgnoreCase(
                        p.getStatusPembayaran())) {

                    if (p.getNominal() != null) {
                        income = income.add(p.getNominal());
                    }

                } else {
                    unpaid++;
                }
            }

            // =================================
            // FORMAT PENDAPATAN
            // =================================

            NumberFormat currency =
                    NumberFormat.getCurrencyInstance(
                            new Locale("id", "ID")
                    );

            String formattedIncome =
                    currency.format(income);

            // =================================
            // KARTU STATISTIK
            // =================================

            cards.add(stat(
                    "Pesanan Hari Ini",
                    String.valueOf(today),
                    "Pesanan pada tanggal hari ini",
                    "▣",
                    new Color(230, 242, 255),
                    new Color(54, 117, 184)
            ));

            cards.add(stat(
                    "Sedang Dikerjakan",
                    String.valueOf(working),
                    "Dalam proses layanan",
                    "⚒",
                    new Color(255, 243, 224),
                    new Color(190, 128, 36)
            ));

            cards.add(stat(
                    "Menunggu Pembayaran",
                    String.valueOf(unpaid),
                    "Transaksi belum lunas",
                    "◇",
                    new Color(252, 235, 235),
                    new Color(190, 82, 82)
            ));

            if (owner) {

                cards.add(stat(
                        "Pendapatan Lunas",
                        formattedIncome,
                        "Akumulasi pembayaran berstatus lunas",
                        "Rp",
                        new Color(226, 243, 232),
                        PageStyle.GREEN
                ));
            }

            // =================================
            // RINGKASAN STATUS PESANAN
            // =================================

            JPanel statusPanel = summaryPanel(
                    "Ringkasan Status Pesanan"
            );

            JPanel statusRows = new JPanel();

            statusRows.setOpaque(false);

            statusRows.setLayout(
                    new BoxLayout(statusRows, BoxLayout.Y_AXIS)
            );

            statusRows.add(summaryRow(
                    "Menunggu",
                    waiting,
                    new Color(190, 128, 36)
            ));

            statusRows.add(Box.createVerticalStrut(15));

            statusRows.add(summaryRow(
                    "Sedang Dikerjakan",
                    working,
                    new Color(54, 117, 184)
            ));

            statusRows.add(Box.createVerticalStrut(15));

            statusRows.add(summaryRow(
                    "Selesai",
                    completed,
                    new Color(34, 139, 94)
            ));

            statusPanel.add(
                    statusRows,
                    BorderLayout.CENTER
            );

            // =================================
            // RINGKASAN DATA ENJOYCARE
            // =================================

            JPanel dataPanel = summaryPanel(
                    "Ringkasan Data EnjoyCare"
            );

            JPanel dataRows = new JPanel();

            dataRows.setOpaque(false);

            dataRows.setLayout(
                    new BoxLayout(dataRows, BoxLayout.Y_AXIS)
            );

            dataRows.add(summaryRow(
                    "Total Pelanggan",
                    customers.size(),
                    PageStyle.GREEN
            ));

            dataRows.add(Box.createVerticalStrut(15));

            dataRows.add(summaryRow(
                    "Layanan Tersedia",
                    services.size(),
                    PageStyle.GREEN
            ));

            dataRows.add(Box.createVerticalStrut(15));

            dataRows.add(summaryRow(
                    "Pesanan Bulan Ini",
                    monthlyOrders,
                    PageStyle.GREEN
            ));

            dataPanel.add(
                    dataRows,
                    BorderLayout.CENTER
            );

            // =================================
            // SUSUN KEDUA PANEL
            // =================================

            JPanel summaries = new JPanel(
                    new GridLayout(1, 2, 12, 0)
            );

            summaries.setOpaque(false);

            summaries.add(statusPanel);
            summaries.add(dataPanel);

            summaries.setPreferredSize(
                    new Dimension(0, 205)
            );

            JPanel lowerContent = new JPanel(
                    new BorderLayout()
            );

            lowerContent.setOpaque(false);

            lowerContent.add(
                    summaries,
                    BorderLayout.NORTH
            );

            // =================================
            // TAMPILKAN DASHBOARD
            // =================================

            add(cards, BorderLayout.NORTH);

            add(
                    lowerContent,
                    BorderLayout.CENTER
            );

        } catch (RuntimeException e) {

            e.printStackTrace();

            cards.add(stat(
                    "Data Dashboard",
                    "-",
                    "Tidak dapat memuat data database",
                    "!",
                    new Color(252, 235, 235),
                    new Color(190, 82, 82)
            ));

            add(cards, BorderLayout.NORTH);
        }
    }

    // =========================================
    // KARTU STATISTIK
    // =========================================

    private JPanel stat(
            String title,
            String value,
            String note,
            String symbol,
            Color bg,
            Color fg
    ) {

        JPanel p = PageStyle.card();

        JPanel top = new JPanel(
                new BorderLayout()
        );

        top.setOpaque(false);

        JLabel t = new JLabel(title);

        t.setForeground(PageStyle.MUTED);

        t.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        JLabel ic = new JLabel(
                symbol,
                SwingConstants.CENTER
        );

        ic.setOpaque(true);
        ic.setBackground(bg);
        ic.setForeground(fg);

        ic.setPreferredSize(
                new Dimension(36, 36)
        );

        top.add(t);
        top.add(ic, BorderLayout.EAST);

        JLabel v = new JLabel(value);

        v.setFont(
                new Font("Segoe UI", Font.BOLD, 23)
        );

        v.setForeground(PageStyle.TEXT);

        JLabel d = new JLabel(note);

        d.setFont(
                new Font("Segoe UI", Font.PLAIN, 10)
        );

        d.setForeground(PageStyle.MUTED);

        JPanel b = new JPanel();

        b.setOpaque(false);

        b.setLayout(
                new BoxLayout(b, BoxLayout.Y_AXIS)
        );

        b.add(v);
        b.add(Box.createVerticalStrut(5));
        b.add(d);

        p.add(top, BorderLayout.NORTH);
        p.add(b, BorderLayout.CENTER);

        return p;
    }

    // =========================================
    // PANEL RINGKASAN
    // =========================================

    private JPanel summaryPanel(String title) {

        JPanel panel = PageStyle.card();

        JLabel heading = new JLabel(title);

        heading.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        heading.setForeground(PageStyle.TEXT);

        heading.setBorder(
                BorderFactory.createEmptyBorder(
                        0, 0, 18, 0
                )
        );

        panel.add(
                heading,
                BorderLayout.NORTH
        );

        return panel;
    }

    // =========================================
    // BARIS INFORMASI
    // =========================================

    private JPanel summaryRow(
            String title,
            int value,
            Color color
    ) {

        JPanel row = new JPanel(
                new BorderLayout(10, 0)
        );

        row.setOpaque(false);

        JLabel dot = new JLabel("●");

        dot.setForeground(color);

        dot.setFont(
                new Font("Segoe UI", Font.BOLD, 11)
        );

        JLabel name = new JLabel(title);

        name.setForeground(PageStyle.MUTED);

        name.setFont(
                new Font("Segoe UI", Font.PLAIN, 12)
        );

        JPanel left = new JPanel(
                new FlowLayout(
                        FlowLayout.LEFT,
                        0,
                        0
                )
        );

        left.setOpaque(false);

        left.add(dot);
        left.add(Box.createHorizontalStrut(9));
        left.add(name);

        JLabel number = new JLabel(
                String.valueOf(value)
        );

        number.setFont(
                new Font("Segoe UI", Font.BOLD, 14)
        );

        number.setForeground(PageStyle.TEXT);

        row.add(left, BorderLayout.WEST);
        row.add(number, BorderLayout.EAST);

        return row;
    }
}
