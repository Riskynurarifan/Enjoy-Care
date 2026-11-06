
package com.enjoycare.pages;

import com.enjoycare.data.AppData;
import java.awt.*;
import javax.swing.*;

public class DashboardPage extends JPanel {

    private final boolean owner;

    public DashboardPage(boolean owner) {
        this.owner = owner;

        setOpaque(false);
        setLayout(new BorderLayout(0, 18));

        // KARTU STATISTIK
        JPanel cards = new JPanel(
                new GridLayout(1, owner ? 4 : 3, 12, 0)
        );
        cards.setOpaque(false);

        cards.add(stat(
                "Pesanan Hari Ini",
                String.valueOf(AppData.count("Pesanan")),
                "Total pesanan contoh",
                "▣",
                new Color(230, 242, 255),
                new Color(54, 117, 184)
        ));

        cards.add(stat(
                "Sedang Dikerjakan",
                String.valueOf(countStatus("Dikerjakan")),
                "Dalam proses layanan",
                "⚒",
                new Color(255, 243, 224),
                new Color(190, 128, 36)
        ));

        cards.add(stat(
                "Menunggu Pembayaran",
                String.valueOf(countStatusPayment()),
                "Belum lunas",
                "◇",
                new Color(252, 235, 235),
                new Color(190, 82, 82)
        ));

        if (owner) {
            cards.add(stat(
                    "Pendapatan",
                    "Rp" + String.format(
                            "%,d",
                            AppData.totalPaid()
                    ).replace(',', '.'),
                    "Total nominal tercatat",
                    "Rp",
                    new Color(226, 243, 232),
                    PageStyle.GREEN
            ));
        }

        // PESANAN TERBARU
        JPanel orders = PageStyle.card();

        orders.add(
                PageStyle.pageHeading(
                        "Pesanan Terbaru",
                        "Ringkasan pesanan yang perlu dipantau."
                ),
                BorderLayout.NORTH
        );

        orders.add(
                new JScrollPane(
                        PageStyle.table(AppData.table("Pesanan"))
                ),
                BorderLayout.CENTER
        );

        // SUSUN DASHBOARD
        add(cards, BorderLayout.NORTH);
        add(orders, BorderLayout.CENTER);
    }

    private int countStatus(String s) {
        int n = 0;

        javax.swing.table.DefaultTableModel m =
                AppData.table("Pesanan");

        for (int i = 0; i < m.getRowCount(); i++) {
            if (s.equals(m.getValueAt(i, 6))) {
                n++;
            }
        }

        return n;
    }

    private int countStatusPayment() {
        int n = 0;

        javax.swing.table.DefaultTableModel m =
                AppData.table("Pembayaran");

        for (int i = 0; i < m.getRowCount(); i++) {
            if (!"Lunas".equals(m.getValueAt(i, 6))) {
                n++;
            }
        }

        return n;
    }

    private JPanel stat(
            String title,
            String value,
            String note,
            String symbol,
            Color bg,
            Color fg
    ) {
        JPanel p = PageStyle.card();

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel t = new JLabel(title);
        t.setForeground(PageStyle.MUTED);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel ic = new JLabel(symbol, SwingConstants.CENTER);
        ic.setOpaque(true);
        ic.setBackground(bg);
        ic.setForeground(fg);
        ic.setPreferredSize(new Dimension(36, 36));

        top.add(t);
        top.add(ic, BorderLayout.EAST);

        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 25));
        v.setForeground(PageStyle.TEXT);

        JLabel d = new JLabel(note);
        d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        d.setForeground(PageStyle.MUTED);

        JPanel b = new JPanel();
        b.setOpaque(false);
        b.setLayout(new BoxLayout(b, BoxLayout.Y_AXIS));

        b.add(v);
        b.add(Box.createVerticalStrut(5));
        b.add(d);

        p.add(top, BorderLayout.NORTH);
        p.add(b, BorderLayout.CENTER);

        return p;
    }
}
