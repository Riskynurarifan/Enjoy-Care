package com.enjoycare.pages;
import java.awt.*;
import javax.swing.*;
public class PesananTeknisiPage extends JPanel {
    public PesananTeknisiPage(boolean owner) { setOpaque(false); setLayout(new BorderLayout()); JTabbedPane tabs=new JTabbedPane(); tabs.addTab("Pesanan",new CrudPage("Pesanan",owner,true,owner)); tabs.addTab("Data Teknisi",new CrudPage("Teknisi",owner,true,owner)); add(tabs,BorderLayout.CENTER); }
}
