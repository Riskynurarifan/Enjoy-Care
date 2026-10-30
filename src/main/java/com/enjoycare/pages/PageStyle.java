package com.enjoycare.pages;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.JTableHeader;

public final class PageStyle {
    public static final Color GREEN = new Color(34, 139, 94);
    public static final Color TEXT = new Color(35, 48, 41);
    public static final Color MUTED = new Color(119, 132, 124);
    public static final Color LINE = new Color(229, 235, 231);
    private PageStyle() {}

    public static JPanel card() {
        JPanel p = new JPanel(new BorderLayout(0, 14));
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(LINE, 1, true), new EmptyBorder(18, 18, 18, 18)));
        return p;
    }
    public static JPanel pageHeading(String title, String subtitle) {
        JPanel p = new JPanel(); p.setOpaque(false); p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        JLabel t = new JLabel(title); t.setFont(new Font("Segoe UI", Font.BOLD, 17)); t.setForeground(TEXT);
        JLabel s = new JLabel(subtitle); s.setFont(new Font("Segoe UI", Font.PLAIN, 11)); s.setForeground(MUTED);
        p.add(t); p.add(Box.createVerticalStrut(4)); p.add(s); return p;
    }
    public static JTable table(javax.swing.table.TableModel model) {
        JTable t = new JTable(model); t.setRowHeight(34); t.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        t.setForeground(TEXT); t.setGridColor(LINE); t.setShowVerticalLines(false); t.setSelectionBackground(new Color(222, 241, 230));
        JTableHeader h = t.getTableHeader(); h.setFont(new Font("Segoe UI", Font.BOLD, 11)); h.setForeground(MUTED); h.setBackground(new Color(247, 250, 248)); h.setPreferredSize(new Dimension(0, 36));
        return t;
    }
    public static JButton button(String text, Color color) {
        JButton b = new JButton(text); b.setFont(new Font("Segoe UI", Font.BOLD, 11)); b.setForeground(Color.WHITE); b.setBackground(color);
        b.setFocusPainted(false); b.setBorderPainted(false); b.setBorder(new EmptyBorder(9, 13, 9, 13)); b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); return b;
    }
    public static JPanel toolbar() { JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0)); p.setOpaque(false); return p; }
}
