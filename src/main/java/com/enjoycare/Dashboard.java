
package com.enjoycare;

import com.enjoycare.pages.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Dashboard extends JFrame {

    private static final Color SIDEBAR = new Color(20, 55, 43);
    private static final Color HOVER = new Color(34, 78, 60);
    private static final Color ACTIVE = new Color(34, 105, 76);
    private static final Color BG = new Color(246, 248, 247);

    private final String role;
    private JPanel content;
    private JLabel pageTitle, pageSubtitle;
    private JButton activeNav;

    public Dashboard(String role) {
        if (!"Owner".equalsIgnoreCase(role)
                && !"Admin".equalsIgnoreCase(role)) {
            throw new IllegalArgumentException("Role tidak valid.");
        }

        this.role = role;

        setTitle("EnjoyCare | Dashboard " + role);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1360, 820);
        setMinimumSize(new Dimension(1100, 700));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildWorkspace(), BorderLayout.CENTER);

        setContentPane(root);
        openPage("Dashboard");
    }

    private boolean isOwner() {
        return "Owner".equalsIgnoreCase(role);
    }

    // =========================================
    // SIDEBAR
    // =========================================

    private JPanel buildSidebar() {

        JPanel side = new JPanel();
        side.setBackground(SIDEBAR);
        side.setPreferredSize(new Dimension(255, 0));
        side.setBorder(new EmptyBorder(25, 17, 20, 17));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));

        // LOGO
        JPanel brand = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        brand.setOpaque(false);
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        brand.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JLabel heart = new JLabel("♥ ");
        heart.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heart.setForeground(new Color(115, 220, 166));

        JLabel brandName = new JLabel("EnjoyCare");
        brandName.setFont(new Font("Segoe UI", Font.BOLD, 23));
        brandName.setForeground(Color.WHITE);

        brand.add(heart);
        brand.add(brandName);
        side.add(brand);

        JLabel tag = new JLabel("AC SERVICE MANAGEMENT");
        tag.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        tag.setForeground(new Color(174, 198, 185));
        tag.setBorder(new EmptyBorder(5, 4, 35, 0));
        tag.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(tag);

        JLabel menu = new JLabel("MENU UTAMA");
        menu.setFont(new Font("Segoe UI", Font.BOLD, 10));
        menu.setForeground(new Color(145, 177, 158));
        menu.setBorder(new EmptyBorder(0, 5, 13, 0));
        menu.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(menu);

        // MENU DENGAN IKON VEKTOR
        addNav(side, "dashboard", "Dashboard");
        addNav(side, "customer", "Pelanggan");

        if (isOwner()) {
            addNav(side, "service", "Layanan");
        }

        addNav(side, "orders", "Pesanan & Teknisi");
        addNav(side, "payment", "Pembayaran");

        if (isOwner()) {
            addNav(side, "report", "Laporan");
            addNav(side, "settings", "Pengguna");
        }

        side.add(Box.createVerticalGlue());

        // PROFIL
        JPanel profile = new JPanel(new BorderLayout(11, 0));
        profile.setOpaque(false);
        profile.setAlignmentX(Component.LEFT_ALIGNMENT);
        profile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        profile.setBorder(new EmptyBorder(5, 2, 12, 0));

        JLabel avatar = new JLabel(
                isOwner() ? "O" : "A",
                SwingConstants.CENTER
        );
        avatar.setOpaque(true);
        avatar.setBackground(new Color(34, 139, 94));
        avatar.setForeground(Color.WHITE);
        avatar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        avatar.setPreferredSize(new Dimension(40, 40));

        JPanel identity = new JPanel();
        identity.setOpaque(false);
        identity.setLayout(new BoxLayout(identity, BoxLayout.Y_AXIS));

        JLabel name = new JLabel("EnjoyCare " + role);
        name.setForeground(Color.WHITE);
        name.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel account = new JLabel("Akun " + role);
        account.setForeground(new Color(177, 199, 185));
        account.setFont(new Font("Segoe UI", Font.PLAIN, 10));

        identity.add(Box.createVerticalGlue());
        identity.add(name);
        identity.add(Box.createVerticalStrut(4));
        identity.add(account);
        identity.add(Box.createVerticalGlue());

        profile.add(avatar, BorderLayout.WEST);
        profile.add(identity, BorderLayout.CENTER);
        side.add(profile);

        // LOGOUT
        JButton logout = new JButton("Keluar dari Aplikasi");
        logout.setIcon(new NavIcon("logout", Color.WHITE));
        logout.setIconTextGap(12);
        logout.setHorizontalAlignment(SwingConstants.CENTER);
        logout.setAlignmentX(Component.LEFT_ALIGNMENT);
        logout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        logout.setPreferredSize(new Dimension(215, 42));
        logout.setBackground(new Color(112, 54, 55));
        logout.setForeground(Color.WHITE);
        logout.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logout.setFocusPainted(false);
        logout.setBorderPainted(false);
        logout.setOpaque(true);
        logout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        logout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                logout.setBackground(new Color(139, 65, 66));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logout.setBackground(new Color(112, 54, 55));
            }
        });

        logout.addActionListener(e -> {
            int answer = JOptionPane.showConfirmDialog(
                    this,
                    "Yakin ingin keluar dari EnjoyCare?",
                    "Konfirmasi Logout",
                    JOptionPane.YES_NO_OPTION
            );

            if (answer == JOptionPane.YES_OPTION) {
                new LoginForm().setVisible(true);
                dispose();
            }
        });

        side.add(logout);

        return side;
    }

    // =========================================
    // TOMBOL NAVIGASI
    // =========================================

    private void addNav(JPanel side, String icon, String label) {

        JButton item = new JButton(label);
        item.setIcon(new NavIcon(icon, new Color(160, 215, 184)));
        item.setIconTextGap(15);
        item.setHorizontalAlignment(SwingConstants.LEFT);

        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        item.setPreferredSize(new Dimension(220, 45));

        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setForeground(new Color(220, 235, 226));
        item.setBackground(SIDEBAR);

        item.setFocusPainted(false);
        item.setBorderPainted(false);
        item.setContentAreaFilled(true);
        item.setOpaque(true);
        item.setBorder(new EmptyBorder(0, 15, 0, 8));

        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        if ("Dashboard".equals(label)) {
            activeNav = item;
            item.setBackground(ACTIVE);
            item.setForeground(Color.WHITE);
            item.setIcon(new NavIcon(icon, Color.WHITE));
            item.setFont(new Font("Segoe UI", Font.BOLD, 13));
        }

        item.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (item != activeNav) {
                    item.setBackground(HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (item != activeNav) {
                    item.setBackground(SIDEBAR);
                }
            }
        });

        item.addActionListener(e -> {
            selectNav(item, icon);
            openPage(label);
        });

        side.add(item);
        side.add(Box.createVerticalStrut(5));
    }

    private void selectNav(JButton item, String icon) {

        if (activeNav != null) {
            activeNav.setBackground(SIDEBAR);
            activeNav.setForeground(new Color(220, 235, 226));

            String oldIcon = getIconName(activeNav.getText());
            activeNav.setIcon(
                    new NavIcon(oldIcon, new Color(160, 215, 184))
            );

            activeNav.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }

        activeNav = item;
        activeNav.setBackground(ACTIVE);
        activeNav.setForeground(Color.WHITE);
        activeNav.setIcon(new NavIcon(icon, Color.WHITE));
        activeNav.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    private String getIconName(String label) {
        switch (label) {
            case "Dashboard": return "dashboard";
            case "Pelanggan": return "customer";
            case "Layanan": return "service";
            case "Pesanan & Teknisi": return "orders";
            case "Pembayaran": return "payment";
            case "Laporan": return "report";
            case "Pengguna": return "settings";
            default: return "dashboard";
        }
    }

    // =========================================
    // IKON VEKTOR - TIDAK MEMAKAI KARAKTER FONT
    // =========================================

    private static class NavIcon implements Icon {

        private final String type;
        private final Color color;

        NavIcon(String type, Color color) {
            this.type = type;
            this.color = color;
        }

        @Override
        public int getIconWidth() {
            return 20;
        }

        @Override
        public int getIconHeight() {
            return 20;
        }

        @Override
        public void paintIcon(
                Component c,
                Graphics g,
                int x,
                int y
        ) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.translate(x, y);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(
                    1.7f,
                    BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND
            ));

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            switch (type) {

                case "dashboard":
                    g2.drawRoundRect(2, 2, 6, 6, 1, 1);
                    g2.drawRoundRect(12, 2, 6, 6, 1, 1);
                    g2.drawRoundRect(2, 12, 6, 6, 1, 1);
                    g2.drawRoundRect(12, 12, 6, 6, 1, 1);
                    break;

                case "customer":
                    g2.drawOval(7, 2, 6, 6);
                    g2.drawArc(3, 9, 14, 12, 0, 180);
                    break;

                case "service":
                    g2.drawRoundRect(3, 3, 14, 14, 2, 2);
                    g2.drawLine(6, 7, 14, 7);
                    g2.drawLine(6, 11, 14, 11);
                    g2.drawLine(6, 15, 11, 15);
                    break;

                case "orders":
                    g2.drawRoundRect(4, 2, 12, 16, 2, 2);
                    g2.drawLine(7, 7, 13, 7);
                    g2.drawLine(7, 10, 13, 10);
                    g2.drawLine(7, 13, 11, 13);
                    break;

                case "payment":
                    g2.drawRoundRect(2, 4, 16, 12, 2, 2);
                    g2.drawLine(3, 8, 17, 8);
                    g2.drawLine(5, 13, 9, 13);
                    break;

                case "report":
                    g2.drawLine(3, 17, 18, 17);
                    g2.drawLine(4, 17, 4, 11);
                    g2.drawLine(9, 17, 9, 7);
                    g2.drawLine(14, 17, 14, 3);
                    g2.drawLine(18, 17, 18, 9);
                    break;

                case "settings":
                    g2.drawOval(6, 6, 8, 8);
                    g2.drawOval(8, 8, 4, 4);
                    g2.drawLine(10, 1, 10, 5);
                    g2.drawLine(10, 15, 10, 19);
                    g2.drawLine(1, 10, 5, 10);
                    g2.drawLine(15, 10, 19, 10);
                    g2.drawLine(3, 3, 6, 6);
                    g2.drawLine(14, 14, 17, 17);
                    g2.drawLine(14, 6, 17, 3);
                    g2.drawLine(3, 17, 6, 14);
                    break;

                case "logout":
                    g2.drawLine(2, 10, 13, 10);
                    g2.drawLine(9, 6, 13, 10);
                    g2.drawLine(9, 14, 13, 10);
                    g2.drawLine(15, 4, 18, 4);
                    g2.drawLine(18, 4, 18, 16);
                    g2.drawLine(18, 16, 15, 16);
                    break;
            }

            g2.dispose();
        }
    }

    // =========================================
    // WORKSPACE
    // =========================================

    private JPanel buildWorkspace() {

        JPanel workspace = new JPanel(new BorderLayout(0, 22));
        workspace.setBackground(BG);
        workspace.setBorder(new EmptyBorder(28, 30, 25, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titles = new JPanel();
        titles.setOpaque(false);
        titles.setLayout(new BoxLayout(titles, BoxLayout.Y_AXIS));

        pageTitle = new JLabel("Dashboard " + role);
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        pageTitle.setForeground(new Color(35, 48, 41));

        pageSubtitle = new JLabel(
                "Ringkasan aktivitas operasional EnjoyCare."
        );
        pageSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pageSubtitle.setForeground(new Color(119, 132, 124));
        pageSubtitle.setBorder(new EmptyBorder(5, 1, 0, 0));

        titles.add(pageTitle);
        titles.add(pageSubtitle);

        header.add(titles, BorderLayout.WEST);

        JLabel badge = new JLabel("●  " + role.toUpperCase());
        badge.setOpaque(true);
        badge.setBackground(new Color(226, 241, 232));
        badge.setForeground(new Color(34, 139, 94));
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setBorder(new EmptyBorder(10, 13, 10, 13));

        header.add(badge, BorderLayout.EAST);

        content = new JPanel(new BorderLayout());
        content.setOpaque(false);

        workspace.add(header, BorderLayout.NORTH);
        workspace.add(content, BorderLayout.CENTER);

        return workspace;
    }

    // =========================================
    // MEMBUKA HALAMAN
    // =========================================

    private void openPage(String name) {

        JPanel page;
        String subtitle;

        switch (name) {

            case "Dashboard":
                page = new DashboardPage(isOwner());
                subtitle = "Ringkasan aktivitas operasional EnjoyCare.";
                break;

            case "Pelanggan":
                page = new PelangganPage(isOwner());
                subtitle = "Kelola data pelanggan EnjoyCare.";
                break;

            case "Layanan":
                if (!isOwner()) {
                    denied();
                    return;
                }
                page = new LayananPage(true);
                subtitle = "Kelola jenis dan harga layanan.";
                break;

            case "Pesanan & Teknisi":
                page = new PesananTeknisiPage(isOwner());
                subtitle = "Kelola pesanan dan data teknisi.";
                break;

            case "Pembayaran":
                page = new PembayaranPage(isOwner());
                subtitle = "Pantau pembayaran dan pengajuan koreksi.";
                break;

            case "Laporan":
                if (!isOwner()) {
                    denied();
                    return;
                }
                page = new LaporanPage();
                subtitle = "Laporan keuangan EnjoyCare.";
                break;

            case "Pengguna":
                if (!isOwner()) {
                    denied();
                    return;
                }
                page = new PenggunaPage();
                subtitle = "Kelola akun pengguna aplikasi.";
                break;

            default:
                return;
        }

        pageTitle.setText(
                name.equals("Dashboard") ? "Dashboard " + role : name
        );

        pageSubtitle.setText(subtitle);

        content.removeAll();
        content.add(page, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }

    private void denied() {
        JOptionPane.showMessageDialog(
                this,
                "Fitur ini hanya dapat diakses Owner.",
                "Akses Ditolak",
                JOptionPane.WARNING_MESSAGE
        );
    }

    // =========================================
    // MAIN
    // =========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
            }

            new LoginForm().setVisible(true);
        });
    }
}
