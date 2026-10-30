package com.enjoycare;

import com.enjoycare.pages.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Dashboard extends JFrame {
    private static final Color SIDEBAR = new Color(20,55,43);
    private static final Color HOVER = new Color(34,78,60);
    private static final Color BG = new Color(246,248,247);
    private final String role;
    private JPanel content;
    private JLabel pageTitle, pageSubtitle;
    private JPanel activeNav;

    public Dashboard(String role) {
        if (!"Owner".equalsIgnoreCase(role) && !"Admin".equalsIgnoreCase(role)) {
            throw new IllegalArgumentException("Role tidak valid. Dashboard harus dibuka melalui LoginForm.");
        }
        this.role = role;
        setTitle("EnjoyCare | Dashboard " + this.role);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 800); setMinimumSize(new Dimension(1050, 680)); setLocationRelativeTo(null);
        JPanel root = new JPanel(new BorderLayout()); root.setBackground(BG);
        root.add(buildSidebar(), BorderLayout.WEST); root.add(buildWorkspace(), BorderLayout.CENTER);
        setContentPane(root); openPage("Dashboard");
    }
    private boolean isOwner() { return "Owner".equalsIgnoreCase(role); }

    private JPanel buildSidebar() {
        JPanel side = new JPanel(); side.setBackground(SIDEBAR); side.setPreferredSize(new Dimension(245,0));
        side.setBorder(new EmptyBorder(25,17,18,17)); side.setLayout(new BoxLayout(side,BoxLayout.Y_AXIS));
        JLabel brand = new JLabel("♥  EnjoyCare"); brand.setFont(new Font("Segoe UI",Font.BOLD,24)); brand.setForeground(Color.WHITE); brand.setAlignmentX(LEFT_ALIGNMENT); side.add(brand);
        JLabel tag = new JLabel("AC SERVICE MANAGEMENT"); tag.setFont(new Font("Segoe UI",Font.PLAIN,9)); tag.setForeground(new Color(174,198,185)); tag.setBorder(new EmptyBorder(5,3,28,0)); tag.setAlignmentX(LEFT_ALIGNMENT); side.add(tag);
        JLabel menu = new JLabel("MENU UTAMA"); menu.setFont(new Font("Segoe UI",Font.BOLD,10)); menu.setForeground(new Color(145,177,158)); menu.setBorder(new EmptyBorder(0,4,12,0)); menu.setAlignmentX(LEFT_ALIGNMENT); side.add(menu);
        addNav(side,"▦","Dashboard"); addNav(side,"♙","Pelanggan");
        if(isOwner()) addNav(side,"▤","Layanan");
        addNav(side,"⚒","Pesanan & Teknisi"); addNav(side,"◇","Pembayaran");
        if(isOwner()){addNav(side,"▥","Laporan");addNav(side,"⚙","Pengguna");}
        side.add(Box.createVerticalGlue());
        JPanel profile = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)); profile.setOpaque(false); profile.setMaximumSize(new Dimension(Integer.MAX_VALUE,52)); profile.setBorder(new EmptyBorder(8,2,12,0));
        JLabel avatar = new JLabel(isOwner()?"O":"A",SwingConstants.CENTER); avatar.setOpaque(true); avatar.setBackground(new Color(34,139,94)); avatar.setForeground(Color.WHITE); avatar.setFont(new Font("Segoe UI",Font.BOLD,15)); avatar.setPreferredSize(new Dimension(38,38));
        JPanel ident = new JPanel(); ident.setOpaque(false); ident.setLayout(new BoxLayout(ident,BoxLayout.Y_AXIS)); ident.setBorder(new EmptyBorder(4,10,0,0));
        JLabel n = new JLabel("EnjoyCare "+role); n.setForeground(Color.WHITE); n.setFont(new Font("Segoe UI",Font.BOLD,12)); JLabel a = new JLabel("Akun "+role); a.setForeground(new Color(177,199,185)); a.setFont(new Font("Segoe UI",Font.PLAIN,10)); ident.add(n);ident.add(Box.createVerticalStrut(3));ident.add(a);profile.add(avatar);profile.add(ident);side.add(profile);
        JButton logout = new JButton("↪   Keluar dari Aplikasi"); logout.setAlignmentX(LEFT_ALIGNMENT); logout.setMaximumSize(new Dimension(Integer.MAX_VALUE,40)); logout.setBackground(new Color(105,52,53)); logout.setForeground(Color.WHITE); logout.setFocusPainted(false); logout.setBorder(new EmptyBorder(8,12,8,8));
        logout.addActionListener(e->{if(JOptionPane.showConfirmDialog(this,"Yakin ingin keluar?","Logout",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){new LoginForm().setVisible(true);dispose();}}); side.add(logout); return side;
    }
    private void addNav(JPanel side,String icon,String label){
        JPanel item=new JPanel(new BorderLayout(12,0)); item.setBackground(SIDEBAR); item.setMaximumSize(new Dimension(Integer.MAX_VALUE,42)); item.setPreferredSize(new Dimension(210,42)); item.setBorder(new EmptyBorder(0,12,0,8)); item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel ic=new JLabel(icon,SwingConstants.CENTER);ic.setPreferredSize(new Dimension(22,25));ic.setForeground(new Color(160,215,184));ic.setFont(new Font("Segoe UI Symbol",Font.PLAIN,16));JLabel tx=new JLabel(label);tx.setForeground(Color.WHITE);tx.setFont(new Font("Segoe UI",Font.PLAIN,12));item.add(ic,BorderLayout.WEST);item.add(tx,BorderLayout.CENTER);
        item.addMouseListener(new MouseAdapter(){public void mouseClicked(MouseEvent e){selectNav(item);openPage(label);}public void mouseEntered(MouseEvent e){if(item!=activeNav)item.setBackground(HOVER);}public void mouseExited(MouseEvent e){if(item!=activeNav)item.setBackground(SIDEBAR);}});side.add(item);side.add(Box.createVerticalStrut(5));
    }
    private void selectNav(JPanel item){if(activeNav!=null)activeNav.setBackground(SIDEBAR);activeNav=item;activeNav.setBackground(HOVER);}
    private JPanel buildWorkspace(){
        JPanel w=new JPanel(new BorderLayout(0,22));w.setBackground(BG);w.setBorder(new EmptyBorder(28,30,25,30));
        JPanel h=new JPanel(new BorderLayout());h.setOpaque(false);JPanel titles=new JPanel();titles.setOpaque(false);titles.setLayout(new BoxLayout(titles,BoxLayout.Y_AXIS));
        pageTitle=new JLabel("Dashboard "+role);pageTitle.setFont(new Font("Segoe UI",Font.BOLD,26));pageTitle.setForeground(new Color(35,48,41));pageSubtitle=new JLabel("Ringkasan aktivitas operasional EnjoyCare.");pageSubtitle.setFont(new Font("Segoe UI",Font.PLAIN,12));pageSubtitle.setForeground(new Color(119,132,124));pageSubtitle.setBorder(new EmptyBorder(5,1,0,0));titles.add(pageTitle);titles.add(pageSubtitle);h.add(titles,BorderLayout.WEST);
        JLabel badge=new JLabel("●  "+role.toUpperCase());badge.setOpaque(true);badge.setBackground(new Color(226,241,232));badge.setForeground(new Color(34,139,94));badge.setFont(new Font("Segoe UI",Font.BOLD,10));badge.setBorder(new EmptyBorder(10,13,10,13));h.add(badge,BorderLayout.EAST);
        content=new JPanel(new BorderLayout());content.setOpaque(false);w.add(h,BorderLayout.NORTH);w.add(content,BorderLayout.CENTER);return w;
    }
    private void openPage(String name){
        JPanel page; String subtitle;
        switch(name){
            case "Dashboard": page=new DashboardPage(isOwner());subtitle="Ringkasan aktivitas operasional EnjoyCare.";break;
            case "Pelanggan": page=new PelangganPage(isOwner());subtitle="Kelola data pelanggan EnjoyCare.";break;
            case "Layanan": if(!isOwner()){denied();return;}page=new LayananPage(true);subtitle="Kelola jenis dan harga layanan.";break;
            case "Pesanan & Teknisi": page=new PesananTeknisiPage(isOwner());subtitle="Kelola pesanan dan data teknisi.";break;
            case "Pembayaran": page=new PembayaranPage(isOwner());subtitle="Pantau pembayaran dan pengajuan koreksi.";break;
            case "Laporan": if(!isOwner()){denied();return;}page=new LaporanPage();subtitle="Laporan keuangan EnjoyCare.";break;
            case "Pengguna": if(!isOwner()){denied();return;}page=new PenggunaPage();subtitle="Kelola akun pengguna aplikasi.";break;
            default:return;
        }
        pageTitle.setText(name.equals("Dashboard")?"Dashboard "+role:name);pageSubtitle.setText(subtitle);content.removeAll();content.add(page,BorderLayout.CENTER);content.revalidate();content.repaint();
    }
    private void denied(){JOptionPane.showMessageDialog(this,"Fitur ini hanya dapat diakses Owner.","Akses Ditolak",JOptionPane.WARNING_MESSAGE);}
    public static void main(String[] args){SwingUtilities.invokeLater(()->{try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}catch(Exception ignored){}new LoginForm().setVisible(true);});}
}
