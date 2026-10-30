package com.enjoycare;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginForm extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblPesan;

    private final Color hijau = new Color(22, 128, 78);
    private final Color hijauGelap = new Color(20, 91, 59);
    private final Color teks = new Color(35, 61, 47);

    public LoginForm() {
        setTitle("Login - EnjoyCare");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 580);
        setMinimumSize(new Dimension(760, 500));
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel utama = new JPanel(new GridLayout(1, 2));
        utama.add(panelBrand());
        utama.add(panelLogin());
        setContentPane(utama);
    }

    private JPanel panelBrand() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(hijauGelap);

        JPanel isi = new JPanel();
        isi.setOpaque(false);
        isi.setLayout(new BoxLayout(isi, BoxLayout.Y_AXIS));

        JLabel ikon = new JLabel("\u2764");
        ikon.setFont(new Font("SansSerif", Font.PLAIN, 62));
        ikon.setForeground(Color.WHITE);
        ikon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel judul = new JLabel("EnjoyCare");
        judul.setFont(new Font("SansSerif", Font.BOLD, 34));
        judul.setForeground(Color.WHITE);
        judul.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subjudul = new JLabel("Kebersihan, Ketelitian, Cepat");
        subjudul.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subjudul.setForeground(new Color(220, 241, 227));
        subjudul.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel deskripsi = new JLabel("<html><div style='text-align:center;'>"
                + "Selamat datang kembali!<br>Silakan masuk untuk melanjutkan."
                + "</div></html>");
        deskripsi.setFont(new Font("SansSerif", Font.PLAIN, 14));
        deskripsi.setForeground(Color.WHITE);
        deskripsi.setAlignmentX(Component.CENTER_ALIGNMENT);

        isi.add(ikon);
        isi.add(Box.createVerticalStrut(18));
        isi.add(judul);
        isi.add(Box.createVerticalStrut(10));
        isi.add(subjudul);
        isi.add(Box.createVerticalStrut(35));
        isi.add(deskripsi);
        panel.add(isi);
        return panel;
    }

    private JPanel panelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 248, 246));

        JPanel kartu = new JPanel();
        kartu.setBackground(Color.WHITE);
        kartu.setPreferredSize(new Dimension(340, 390));
        kartu.setBorder(new EmptyBorder(30, 28, 25, 28));
        kartu.setLayout(new BoxLayout(kartu, BoxLayout.Y_AXIS));

        JLabel judul = new JLabel("Selamat Datang");
        judul.setFont(new Font("SansSerif", Font.BOLD, 25));
        judul.setForeground(teks);
        judul.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel petunjuk = new JLabel("Masuk ke akun EnjoyCare Anda");
        petunjuk.setFont(new Font("SansSerif", Font.PLAIN, 13));
        petunjuk.setForeground(Color.GRAY);
        petunjuk.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        styleInput(txtUsername);
        styleInput(txtPassword);

        lblPesan = new JLabel(" ");
        lblPesan.setFont(new Font("SansSerif", Font.PLAIN, 12));
        lblPesan.setForeground(new Color(190, 50, 50));
        lblPesan.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(hijau);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnLogin.setPreferredSize(new Dimension(280, 44));

        JLabel footer = new JLabel("EnjoyCare - Login");
        footer.setFont(new Font("SansSerif", Font.PLAIN, 12));
        footer.setForeground(Color.GRAY);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        kartu.add(judul);
        kartu.add(Box.createVerticalStrut(8));
        kartu.add(petunjuk);
        kartu.add(Box.createVerticalStrut(28));
        kartu.add(label("Username"));
        kartu.add(Box.createVerticalStrut(7));
        kartu.add(txtUsername);
        kartu.add(Box.createVerticalStrut(18));
        kartu.add(label("Password"));
        kartu.add(Box.createVerticalStrut(7));
        kartu.add(txtPassword);
        kartu.add(Box.createVerticalStrut(15));
        kartu.add(btnLogin);
        kartu.add(Box.createVerticalStrut(8));
        kartu.add(lblPesan);
        kartu.add(Box.createVerticalGlue());
        kartu.add(footer);

        btnLogin.addActionListener(e -> prosesLogin());
        txtPassword.addActionListener(e -> prosesLogin());
        panel.add(kartu);
        return panel;
    }

    private JLabel label(String tulisan) {
        JLabel label = new JLabel(tulisan);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(teks);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleInput(JTextField input) {
        input.setFont(new Font("SansSerif", Font.PLAIN, 14));
        input.setForeground(teks);
        input.setBackground(Color.WHITE);
        input.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 222, 214)),
                new EmptyBorder(10, 12, 10, 12)));
        input.setMaximumSize(new Dimension(Integer.MAX_VALUE, 43));
        input.setPreferredSize(new Dimension(280, 43));
        input.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void prosesLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblPesan.setText("Username dan password wajib diisi!");
            return;
        }

        String role = null;
        if (username.equals("owner") && password.equals("owner123")) {
            role = "Owner";
        } else if (username.equals("admin") && password.equals("admin123")) {
            role = "Admin";
        }

        if (role != null) {
            JOptionPane.showMessageDialog(this,
                    "Login berhasil. Selamat datang, " + role + "!",
                    "Login Berhasil", JOptionPane.INFORMATION_MESSAGE);
            new Dashboard(role).setVisible(true);
            dispose();
        } else {
            lblPesan.setText("Username atau password salah!");
            txtPassword.setText("");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Gunakan tampilan default.
            }
            new LoginForm().setVisible(true);
        });
    }
}
