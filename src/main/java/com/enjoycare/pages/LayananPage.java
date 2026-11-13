package com.enjoycare.pages;

import com.enjoycare.dao.LayananDAO;
import com.enjoycare.dao.TeknisiDAO;
import com.enjoycare.controller.StokBarangController;
import com.enjoycare.config.SpringContext;
import com.enjoycare.entity.Layanan;
import com.enjoycare.entity.Teknisi;
import com.enjoycare.entity.StokBarang;
import java.awt.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LayananPage extends JPanel {
    private final LayananDAO layananDAO = new LayananDAO();
    private final TeknisiDAO teknisiDAO = new TeknisiDAO();
    private final StokBarangController stokController = SpringContext.getBean(StokBarangController.class);
    private final DefaultTableModel layananModel = model("ID", "Nama Layanan", "Harga (Rp)", "Keterangan", "Status");
    private final DefaultTableModel teknisiModel = model("ID", "Nama Teknisi", "No. Telepon", "Status");
    private final DefaultTableModel stokModel = model("ID", "Kode Barang", "Nama Barang", "Stok", "Satuan", "Stok Minimum", "Keterangan");
    private final JTable layananTable = PageStyle.table(layananModel);
    private final JTable teknisiTable = PageStyle.table(teknisiModel);
    private final JTable stokTable = PageStyle.table(stokModel);

    public LayananPage(boolean owner) {
        setOpaque(false); setLayout(new BorderLayout(0, 14));
        add(PageStyle.pageHeading("Data Layanan, Teknisi & Stok Barang", "Owner mengelola layanan, teknisi, dan persediaan barang."), BorderLayout.NORTH);
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Layanan", buildLayanan());
        tabs.addTab("Teknisi", buildTeknisi());
        tabs.addTab("Stok Barang", buildStokBarang());
        add(tabs, BorderLayout.CENTER);
        loadLayanan(); loadTeknisi(); loadStokBarang();
    }

    private DefaultTableModel model(String... cols) { return new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r,int c){return false;} }; }
    private JPanel panel(JTable table, String addText, Runnable add, Runnable edit, Runnable delete) {
        JPanel p=new JPanel(new BorderLayout(0,10)); p.setOpaque(false); JPanel bar=PageStyle.toolbar();
        JButton a=PageStyle.button(addText,PageStyle.GREEN), e=PageStyle.button("Edit",new Color(70,110,145)), d=PageStyle.button("Hapus",new Color(177,75,75));
        bar.add(a);bar.add(e);bar.add(d);a.addActionListener(x->add.run());e.addActionListener(x->edit.run());d.addActionListener(x->delete.run());
        p.add(bar,BorderLayout.NORTH);p.add(new JScrollPane(table),BorderLayout.CENTER);return p;
    }
    private JPanel buildLayanan(){return panel(layananTable,"+ Tambah Layanan",()->formLayanan(null),()->{Layanan x=selectedLayanan();if(x!=null)formLayanan(x);},()->{Layanan x=selectedLayanan();if(x!=null&&confirm("Hapus layanan ini?"))run(()->layananDAO.delete(x.getIdLayanan()),this::loadLayanan);});}
    private JPanel buildTeknisi(){return panel(teknisiTable,"+ Tambah Teknisi",()->formTeknisi(null),()->{Teknisi x=selectedTeknisi();if(x!=null)formTeknisi(x);},()->{Teknisi x=selectedTeknisi();if(x!=null&&confirm("Hapus teknisi ini?"))run(()->teknisiDAO.delete(x.getIdTeknisi()),this::loadTeknisi);});}

    private JPanel buildStokBarang() {
        return panel(stokTable, "+ Tambah Barang", () -> formStok(null), () -> {
            StokBarang b = selectedStok(); if (b != null) formStok(b);
        }, () -> {
            StokBarang b = selectedStok();
            if (b != null && confirm("Hapus barang ini?")) run(() -> stokController.hapus(b.getIdStokBarang()), this::loadStokBarang);
        });
    }
    private StokBarang selectedStok() {
        int r = stokTable.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Pilih barang terlebih dahulu."); return null; }
        Long id = Long.valueOf(stokModel.getValueAt(stokTable.convertRowIndexToModel(r), 0).toString());
        for (StokBarang b : stokController.tampilkanSemua()) if (b.getIdStokBarang().equals(id)) return b;
        return null;
    }
    private void loadStokBarang() {
        stokModel.setRowCount(0);
        try {
            for (StokBarang b : stokController.tampilkanSemua()) {
                String status = b.getJumlahStok() <= b.getStokMinimum() ? "Stok menipis" : "Aman";
                stokModel.addRow(new Object[]{b.getIdStokBarang(), b.getKodeBarang(), b.getNamaBarang(), b.getJumlahStok(), b.getSatuan(), b.getStokMinimum(), status});
            }
        } catch (RuntimeException e) { error(e); }
    }
    private void formStok(StokBarang old) {
        boolean fresh = old == null;
        JTextField kode = new JTextField(fresh ? "" : old.getKodeBarang(), 16);
        JTextField nama = new JTextField(fresh ? "" : old.getNamaBarang(), 16);
        JTextField jumlah = new JTextField(fresh ? "0" : String.valueOf(old.getJumlahStok()), 16);
        JTextField satuan = new JTextField(fresh ? "Pcs" : old.getSatuan(), 16);
        JTextField minimum = new JTextField(fresh ? "0" : String.valueOf(old.getStokMinimum()), 16);
        JCheckBox aktif = new JCheckBox("Barang aktif", fresh || Boolean.TRUE.equals(old.getAktif()));
        JPanel f = new JPanel(new GridLayout(0, 2, 8, 8));
        f.add(new JLabel("Kode barang:")); f.add(kode); f.add(new JLabel("Nama barang:")); f.add(nama);
        f.add(new JLabel("Jumlah stok:")); f.add(jumlah); f.add(new JLabel("Satuan:")); f.add(satuan);
        f.add(new JLabel("Stok minimum:")); f.add(minimum); f.add(new JLabel("Status:")); f.add(aktif);
        if (JOptionPane.showConfirmDialog(this, f, fresh ? "Tambah Barang" : "Edit Barang", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            StokBarang b = fresh ? new StokBarang() : old;
            b.setKodeBarang(kode.getText().trim()); b.setNamaBarang(nama.getText().trim());
            b.setJumlahStok(Integer.valueOf(jumlah.getText().trim())); b.setSatuan(satuan.getText().trim());
            b.setStokMinimum(Integer.valueOf(minimum.getText().trim())); b.setAktif(aktif.isSelected());
            run(() -> stokController.simpan(b), this::loadStokBarang);
        } catch (Exception e) { error(e); }
    }

    private boolean confirm(String s){return JOptionPane.showConfirmDialog(this,s,"Konfirmasi",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;}
    private String rupiah(BigDecimal n){return new DecimalFormat("'Rp'#,##0.00",new DecimalFormatSymbols(new Locale("id","ID"))).format(n==null?BigDecimal.ZERO:n);}
    private void loadLayanan(){layananModel.setRowCount(0);try{for(Layanan x:layananDAO.getAll())layananModel.addRow(new Object[]{x.getIdLayanan(),x.getNamaLayanan(),rupiah(x.getHarga()),x.getKeterangan(),Boolean.TRUE.equals(x.getAktif())?"Aktif":"Nonaktif"});}catch(RuntimeException e){error(e);}}
    private void loadTeknisi(){teknisiModel.setRowCount(0);try{for(Teknisi x:teknisiDAO.getAll())teknisiModel.addRow(new Object[]{x.getIdTeknisi(),x.getNamaTeknisi(),x.getNoTelepon(),Boolean.TRUE.equals(x.getAktif())?"Aktif":"Nonaktif"});}catch(RuntimeException e){error(e);}}
    private Layanan selectedLayanan(){int r=layananTable.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Pilih layanan terlebih dahulu.");return null;}return layananDAO.getById(Long.valueOf(layananModel.getValueAt(layananTable.convertRowIndexToModel(r),0).toString()));}
    private Teknisi selectedTeknisi(){int r=teknisiTable.getSelectedRow();if(r<0){JOptionPane.showMessageDialog(this,"Pilih teknisi terlebih dahulu.");return null;}return teknisiDAO.getById(Long.valueOf(teknisiModel.getValueAt(teknisiTable.convertRowIndexToModel(r),0).toString()));}
    private void formLayanan(Layanan old){boolean fresh=old==null;JTextField nama=new JTextField(fresh?"":old.getNamaLayanan(),20),harga=new JTextField(fresh?"":old.getHarga().toPlainString(),20);JTextArea ket=new JTextArea(fresh?"":old.getKeterangan(),3,20);JCheckBox aktif=new JCheckBox("Layanan aktif",fresh||Boolean.TRUE.equals(old.getAktif()));JPanel f=new JPanel(new GridLayout(0,2,8,8));f.add(new JLabel("Nama layanan:"));f.add(nama);f.add(new JLabel("Harga (Rp):"));f.add(harga);f.add(new JLabel("Keterangan:"));f.add(new JScrollPane(ket));f.add(new JLabel("Status:"));f.add(aktif);if(JOptionPane.showConfirmDialog(this,f,fresh?"Tambah Layanan":"Edit Layanan",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;try{if(nama.getText().trim().isEmpty())throw new IllegalArgumentException("Nama layanan wajib diisi.");BigDecimal h=new BigDecimal(harga.getText().trim());if(h.signum()<0)throw new IllegalArgumentException("Harga tidak boleh negatif.");Layanan x=fresh?new Layanan():old;x.setNamaLayanan(nama.getText().trim());x.setHarga(h);x.setKeterangan(ket.getText().trim());x.setAktif(aktif.isSelected());run(()->layananDAO.save(x),this::loadLayanan);}catch(Exception e){error(e);}}
    private void formTeknisi(Teknisi old){boolean fresh=old==null;JTextField nama=new JTextField(fresh?"":old.getNamaTeknisi(),20),telp=new JTextField(fresh?"":old.getNoTelepon(),20);JCheckBox aktif=new JCheckBox("Teknisi aktif",fresh||Boolean.TRUE.equals(old.getAktif()));JPanel f=new JPanel(new GridLayout(0,2,8,8));f.add(new JLabel("Nama teknisi:"));f.add(nama);f.add(new JLabel("No. telepon:"));f.add(telp);f.add(new JLabel("Status:"));f.add(aktif);if(JOptionPane.showConfirmDialog(this,f,fresh?"Tambah Teknisi":"Edit Teknisi",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;try{if(nama.getText().trim().isEmpty()||telp.getText().trim().isEmpty())throw new IllegalArgumentException("Nama dan nomor telepon teknisi wajib diisi.");Teknisi x=fresh?new Teknisi():old;x.setNamaTeknisi(nama.getText().trim());x.setNoTelepon(telp.getText().trim());x.setAktif(aktif.isSelected());run(()->teknisiDAO.save(x),this::loadTeknisi);}catch(Exception e){error(e);}}
    private void run(Runnable action,Runnable reload){try{action.run();reload.run();}catch(RuntimeException e){error(e);}}
    private void error(Exception e){JOptionPane.showMessageDialog(this,"Operasi database gagal:\n"+e.getMessage(),"Database",JOptionPane.ERROR_MESSAGE);}
}
