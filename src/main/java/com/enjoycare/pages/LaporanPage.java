package com.enjoycare.pages;
import com.enjoycare.data.AppData;
import java.awt.*;
import javax.swing.*;
public class LaporanPage extends JPanel {
    public LaporanPage() { setOpaque(false); setLayout(new BorderLayout()); JPanel p=PageStyle.card();p.add(PageStyle.pageHeading("Laporan Keuangan","Ringkasan berdasarkan data dummy."),BorderLayout.NORTH);String html="<html><div style='font-family:Segoe UI;font-size:14px'>Jumlah pesanan: <b>"+AppData.count("Pesanan")+"</b><br><br>Jumlah transaksi: <b>"+AppData.count("Pembayaran")+"</b><br><br>Total pembayaran tercatat: <b>Rp"+String.format("%,d",AppData.totalPaid()).replace(',', '.')+"</b><br><br><i>Data ini hanya data contoh dan belum berasal dari database.</i></div></html>";JLabel info=new JLabel(html);info.setForeground(PageStyle.TEXT);p.add(info,BorderLayout.CENTER);add(p,BorderLayout.CENTER);}
}
