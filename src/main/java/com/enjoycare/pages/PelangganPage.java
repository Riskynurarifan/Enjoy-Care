package com.enjoycare.pages;

import com.enjoycare.dao.PelangganDAO;
import com.enjoycare.entity.Pelanggan;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PelangganPage extends JPanel {
    private final PelangganDAO dao = new PelangganDAO();
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Nama", "No. Telepon", "Alamat"}, 0) { public boolean isCellEditable(int r,int c){return false;} };
    private final JTable table = PageStyle.table(model);
    private final boolean owner;
    public PelangganPage(boolean owner) {
        this.owner=owner; setOpaque(false); setLayout(new BorderLayout(0,14));
        add(PageStyle.pageHeading("Data Pelanggan", "Kelola data pelanggan EnjoyCare."),BorderLayout.NORTH);
        JPanel center=new JPanel(new BorderLayout(0,10));center.setOpaque(false);JPanel tools=PageStyle.toolbar();
        JTextField search=new JTextField(18);tools.add(new JLabel("Cari:"));tools.add(search);
        JButton add=PageStyle.button("+ Tambah",PageStyle.GREEN),edit=PageStyle.button("Edit",new Color(70,110,145)),del=PageStyle.button("Hapus",new Color(177,75,75));
        tools.add(add);tools.add(edit);if(owner)tools.add(del);
        add.addActionListener(e->form(null)); edit.addActionListener(e->{Pelanggan p=selected();if(p!=null)form(p);});
        del.addActionListener(e->{Pelanggan p=selected();if(p!=null&&JOptionPane.showConfirmDialog(this,"Hapus pelanggan terpilih?","Konfirmasi",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)run(()->dao.delete(p.getIdPelanggan()));});
        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){private void f(){String q=search.getText().toLowerCase();javax.swing.table.TableRowSorter<DefaultTableModel> s=new javax.swing.table.TableRowSorter<>(model);table.setRowSorter(s);s.setRowFilter(q.isEmpty()?null:javax.swing.RowFilter.regexFilter("(?i)"+java.util.regex.Pattern.quote(q)));}public void insertUpdate(javax.swing.event.DocumentEvent e){f();}public void removeUpdate(javax.swing.event.DocumentEvent e){f();}public void changedUpdate(javax.swing.event.DocumentEvent e){f();}});
        center.add(tools,BorderLayout.NORTH);center.add(new JScrollPane(table),BorderLayout.CENTER);add(center,BorderLayout.CENTER);load();
    }
    private void load(){model.setRowCount(0);try{for(Pelanggan p:dao.getAll())model.addRow(new Object[]{p.getIdPelanggan(),p.getNamaPelanggan(),p.getNoTelepon(),p.getAlamat()});}catch(RuntimeException e){error(e);}}
    private Pelanggan selected(){int r=table.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Pilih data pelanggan terlebih dahulu.");return null;}Long id=Long.valueOf(model.getValueAt(table.convertRowIndexToModel(r),0).toString());try{return dao.getById(id);}catch(RuntimeException e){error(e);return null;}}
    private void form(Pelanggan p){boolean baru=p==null;JTextField nama=new JTextField(baru?"":p.getNamaPelanggan(),20),telp=new JTextField(baru?"":val(p.getNoTelepon()),20);JTextArea alamat=new JTextArea(4,20);alamat.setText(baru?"":val(p.getAlamat()));JPanel f=new JPanel(new GridLayout(0,2,8,8));f.add(new JLabel("Nama Pelanggan:"));f.add(nama);f.add(new JLabel("No. Telepon:"));f.add(telp);f.add(new JLabel("Alamat:"));f.add(new JScrollPane(alamat));if(JOptionPane.showConfirmDialog(this,f,baru?"Tambah Pelanggan":"Edit Pelanggan",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;if(nama.getText().trim().isEmpty()||alamat.getText().trim().isEmpty()){JOptionPane.showMessageDialog(this,"Nama dan alamat wajib diisi.");return;}Pelanggan x=baru?new Pelanggan():p;x.setNamaPelanggan(nama.getText().trim());x.setNoTelepon(telp.getText().trim());x.setAlamat(alamat.getText().trim());run(()->dao.save(x));}
    private String val(String s){return s==null?"":s;}
    private void run(Runnable r){try{r.run();load();}catch(RuntimeException e){error(e);}}
    private void error(Exception e){JOptionPane.showMessageDialog(this,"Operasi database gagal:\n"+e.getMessage(),"Database",JOptionPane.ERROR_MESSAGE);}
}
