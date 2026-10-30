package com.enjoycare.pages;
import com.enjoycare.data.AppData;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class PembayaranPage extends JPanel {
    private static final List<String[]> REQUESTS = new ArrayList<>();
    public PembayaranPage(boolean owner) {
        setOpaque(false); setLayout(new BorderLayout(0,12)); JPanel panel=PageStyle.card();
        panel.add(PageStyle.pageHeading("Data Pembayaran", owner ? "Owner dapat mengelola transaksi dan pengajuan koreksi." : "Admin hanya dapat melihat status dan mengajukan koreksi."),BorderLayout.NORTH);
        JPanel center=new JPanel(new BorderLayout(0,10));center.setOpaque(false);JPanel actions=PageStyle.toolbar();
        JTable table=PageStyle.table(AppData.table("Pembayaran"));
        if(owner){JButton add=PageStyle.button("+ Catat Pembayaran",PageStyle.GREEN),edit=PageStyle.button("Edit Transaksi",new Color(70,110,145)),del=PageStyle.button("Hapus Transaksi",new Color(177,75,75)),requests=PageStyle.button("Pengajuan Koreksi",new Color(90,110,100));actions.add(add);actions.add(edit);actions.add(del);actions.add(requests);
            add.addActionListener(e->edit(-1)); edit.addActionListener(e->{int r=table.getSelectedRow();if(r>=0)edit(table.convertRowIndexToModel(r));else warn();});
            del.addActionListener(e->{int r=table.getSelectedRow();if(r<0){warn();return;}int mrow=table.convertRowIndexToModel(r);if(JOptionPane.showConfirmDialog(this,"Hapus transaksi ini?","Konfirmasi",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)AppData.table("Pembayaran").removeRow(mrow);});
            requests.addActionListener(e->showRequests());
        } else {JButton request=PageStyle.button("Ajukan Koreksi",PageStyle.GREEN);actions.add(request);request.addActionListener(e->requestCorrection());}
        center.add(actions,BorderLayout.NORTH);center.add(new JScrollPane(table),BorderLayout.CENTER);panel.add(center,BorderLayout.CENTER);add(panel,BorderLayout.CENTER);
    }
    private void edit(int row){DefaultTableModel m=AppData.table("Pembayaran");int n=m.getColumnCount();JTextField[] f=new JTextField[n];JPanel form=new JPanel(new GridLayout(n,2,7,7));for(int i=0;i<n;i++){form.add(new JLabel(m.getColumnName(i)+":"));f[i]=new JTextField(row<0?"":String.valueOf(m.getValueAt(row,i)));if(i==0&&row>=0)f[i].setEditable(false);form.add(f[i]);}if(JOptionPane.showConfirmDialog(this,form,row<0?"Catat Pembayaran":"Edit Transaksi",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;String[] v=new String[n];for(int i=0;i<n;i++){v[i]=f[i].getText().trim();if(v[i].isEmpty()){JOptionPane.showMessageDialog(this,"Semua kolom wajib diisi.");return;}}if(row<0)m.addRow(v);else for(int i=0;i<n;i++)m.setValueAt(v[i],row,i);}
    private void requestCorrection(){JTextField id=new JTextField();JTextArea reason=new JTextArea(4,20);JPanel p=new JPanel(new GridLayout(0,1,5,5));p.add(new JLabel("ID Transaksi:"));p.add(id);p.add(new JLabel("Alasan koreksi:"));p.add(new JScrollPane(reason));if(JOptionPane.showConfirmDialog(this,p,"Ajukan Koreksi",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;if(id.getText().trim().isEmpty()||reason.getText().trim().isEmpty()){JOptionPane.showMessageDialog(this,"Semua kolom wajib diisi.");return;}REQUESTS.add(new String[]{"KOR"+String.format("%03d",REQUESTS.size()+1),id.getText().trim(),reason.getText().trim(),"Menunggu"});JOptionPane.showMessageDialog(this,"Pengajuan koreksi dicatat sementara.");}
    private void showRequests(){DefaultTableModel m=new DefaultTableModel(new String[]{"ID Koreksi","ID Transaksi","Alasan","Status"},0);for(String[] r:REQUESTS)m.addRow(r);JTable t=PageStyle.table(m);JButton approve=PageStyle.button("Setujui Pengajuan",PageStyle.GREEN);approve.addActionListener(e->{int r=t.getSelectedRow();if(r<0){warn();return;}m.setValueAt("Disetujui",r,3);});JOptionPane.showMessageDialog(this,new JScrollPane(t),"Pengajuan Koreksi",JOptionPane.PLAIN_MESSAGE);}
    private void warn(){JOptionPane.showMessageDialog(this,"Pilih transaksi terlebih dahulu.");}
}
