package com.enjoycare.pages;

import com.enjoycare.dao.PesananDAO;
import com.enjoycare.entity.DetailPesanan;
import com.enjoycare.entity.Pesanan;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LaporanPage extends JPanel {
    private final PesananDAO dao=new PesananDAO();
    private final DefaultTableModel model=new DefaultTableModel(new String[]{"ID Pesanan","Kode Pesanan","Tanggal","Nama Pelanggan","Teknisi","Total Harga","Status"},0){public boolean isCellEditable(int r,int c){return false;}};
    private final JTable table=PageStyle.table(model);
    public LaporanPage(){setOpaque(false);setLayout(new BorderLayout(0,14));add(PageStyle.pageHeading("Laporan Pesanan","Pilih pesanan untuk melihat bukti pesanan seperti struk atau mencetaknya."),BorderLayout.NORTH);JPanel content=new JPanel(new BorderLayout(0,10));content.setOpaque(false);JPanel bar=PageStyle.toolbar();JButton view=PageStyle.button("Lihat Struk",PageStyle.GREEN),print=PageStyle.button("Cetak Struk",new Color(70,110,145)),refresh=PageStyle.button("Muat Ulang",new Color(100,115,105));bar.add(view);bar.add(print);bar.add(refresh);view.addActionListener(e->{Pesanan p=selected();if(p!=null)showReceipt(p,false);});print.addActionListener(e->{Pesanan p=selected();if(p!=null)showReceipt(p,true);});refresh.addActionListener(e->load());table.addMouseListener(new java.awt.event.MouseAdapter(){public void mouseClicked(java.awt.event.MouseEvent e){if(e.getClickCount()==2){Pesanan p=selected();if(p!=null)showReceipt(p,false);}}});content.add(bar,BorderLayout.NORTH);content.add(new JScrollPane(table),BorderLayout.CENTER);add(content,BorderLayout.CENTER);load();}
    private String money(BigDecimal v){return new DecimalFormat("'Rp'#,##0.00",new DecimalFormatSymbols(new Locale("id","ID"))).format(v==null?BigDecimal.ZERO:v);}
    private void load(){model.setRowCount(0);try{List<Pesanan> list=dao.getAll();for(Pesanan p:list)model.addRow(new Object[]{p.getIdPesanan(),p.getKodePesanan(),p.getTanggalPesanan(),p.getPelanggan().getNamaPelanggan(),p.getTeknisi().getNamaTeknisi(),money(p.getTotalHarga()),p.getStatusPesanan()});}catch(RuntimeException e){error(e);}}
    private Pesanan selected(){int r=table.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Pilih pesanan pada tabel laporan terlebih dahulu.");return null;}try{return dao.getById(Long.valueOf(model.getValueAt(table.convertRowIndexToModel(r),0).toString()));}catch(Exception ex){error(ex);return null;}}
    private String receiptHtml(Pesanan p){StringBuilder h=new StringBuilder("<html><body style='font-family:Segoe UI;font-size:12px;color:#233029'>");h.append("<h2 style='text-align:center'>ENJOYCARE</h2><p style='text-align:center'>BUKTI PESANAN / STRUK</p><hr>");h.append("<b>No. Pesanan:</b> ").append(p.getKodePesanan()).append("<br><b>Tanggal:</b> ").append(p.getTanggalPesanan()).append("<br><br><b>Nama Pelanggan:</b> ").append(esc(p.getPelanggan().getNamaPelanggan())).append("<br><b>No. Telepon:</b> ").append(esc(p.getPelanggan().getNoTelepon())).append("<br><b>Alamat:</b> ").append(esc(p.getPelanggan().getAlamat())).append("<br><br>");h.append("<b>Rincian Layanan</b><table width='100%' cellspacing='0' cellpadding='5' border='1' style='border-collapse:collapse'><tr><th align='left'>Layanan</th><th>Jumlah</th><th align='right'>Harga Satuan</th><th align='right'>Subtotal</th></tr>");for(DetailPesanan d:p.getDetailPesanan())h.append("<tr><td>").append(esc(d.getLayanan().getNamaLayanan())).append("</td><td align='center'>").append(d.getJumlahUnit()).append("</td><td align='right'>").append(money(d.getHargaSatuan())).append("</td><td align='right'>").append(money(d.getSubtotal())).append("</td></tr>");h.append("</table><h3 style='text-align:right'>Total: ").append(money(p.getTotalHarga())).append("</h3><hr><b>Teknisi:</b> ").append(esc(p.getTeknisi().getNamaTeknisi())).append("<br><b>No. Telepon Teknisi:</b> ").append(esc(p.getTeknisi().getNoTelepon())).append("<br><b>Status Pesanan:</b> ").append(esc(p.getStatusPesanan())).append("<br><b>Catatan:</b> ").append(esc(p.getCatatan())).append("<p style='text-align:center;margin-top:24px'>Terima kasih telah menggunakan layanan EnjoyCare.</p></body></html>");return h.toString();}
    private String esc(String s){if(s==null)return "-";return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;").replace("\n","<br>");}
    private void showReceipt(Pesanan p,boolean print){JEditorPane pane=new JEditorPane("text/html",receiptHtml(p));pane.setEditable(false);pane.setCaretPosition(0);if(print){try{pane.print(null,null);}catch(Exception e){error(e);}return;}JScrollPane scroll=new JScrollPane(pane);scroll.setPreferredSize(new Dimension(620,560));JOptionPane.showMessageDialog(this,scroll,"Struk Pesanan - "+p.getKodePesanan(),JOptionPane.PLAIN_MESSAGE);}
    private void error(Exception e){JOptionPane.showMessageDialog(this,"Gagal memuat laporan:\n"+e.getMessage(),"Database",JOptionPane.ERROR_MESSAGE);}
}
