package com.enjoycare.dao;

import com.enjoycare.HibernateUtil;
import com.enjoycare.entity.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PesananDAO {
    public List<Pesanan> getAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("SELECT DISTINCT p FROM Pesanan p JOIN FETCH p.pelanggan JOIN FETCH p.teknisi LEFT JOIN FETCH p.detailPesanan d LEFT JOIN FETCH d.layanan ORDER BY p.idPesanan DESC", Pesanan.class).list();
        }
    }
    public Pesanan getById(Long id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            Pesanan p = s.createQuery("SELECT DISTINCT p FROM Pesanan p JOIN FETCH p.pelanggan JOIN FETCH p.teknisi LEFT JOIN FETCH p.detailPesanan d LEFT JOIN FETCH d.layanan WHERE p.idPesanan=:id", Pesanan.class).setParameter("id", id).uniqueResult();
            if (p != null) { for (PenggunaanStok u : p.getPenggunaanStok()) u.getStokBarang().getNamaBarang(); }
            return p;
        }
    }
    public void save(Pesanan data) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            if (data.getKodePesanan() == null || data.getKodePesanan().trim().isEmpty())
                data.setKodePesanan("ORD" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
            if (data.getIdPesanan() == null) {
                data.setPelanggan(s.get(Pelanggan.class, data.getPelanggan().getIdPelanggan()));
                data.setTeknisi(s.get(Teknisi.class, data.getTeknisi().getIdTeknisi()));
                for (DetailPesanan d : data.getDetailPesanan()) {
                    d.setPesanan(data); d.setLayanan(s.get(Layanan.class, d.getLayanan().getIdLayanan()));
                }
                prosesPenggunaanStok(s, data, data.getPenggunaanStok(), false);
                s.save(data);
            } else {
                Pesanan managed = s.get(Pesanan.class, data.getIdPesanan());
                managed.getPenggunaanStok().size(); managed.getDetailPesanan().size();
                managed.setTanggalPesanan(data.getTanggalPesanan());
                managed.setPelanggan(s.get(Pelanggan.class, data.getPelanggan().getIdPelanggan()));
                managed.setTeknisi(s.get(Teknisi.class, data.getTeknisi().getIdTeknisi()));
                managed.setStatusPesanan(data.getStatusPesanan()); managed.setCatatan(data.getCatatan());
                for (PenggunaanStok old : managed.getPenggunaanStok()) {
                    StokBarang stock = s.get(StokBarang.class, old.getStokBarang().getIdStokBarang());
                    stock.setJumlahStok(stock.getJumlahStok() + old.getJumlah());
                }
                managed.getPenggunaanStok().clear();
                managed.getDetailPesanan().clear();
                BigDecimal total = BigDecimal.ZERO;
                for (DetailPesanan d : data.getDetailPesanan()) {
                    DetailPesanan nd = new DetailPesanan(); nd.setPesanan(managed);
                    nd.setLayanan(s.get(Layanan.class, d.getLayanan().getIdLayanan()));
                    nd.setJumlahUnit(d.getJumlahUnit()); nd.setHargaSatuan(d.getHargaSatuan()); nd.setSubtotal(d.getSubtotal());
                    managed.getDetailPesanan().add(nd); total = total.add(nd.getSubtotal());
                }
                managed.setTotalHarga(total);
                prosesPenggunaanStok(s, managed, data.getPenggunaanStok(), true);
            }
            tx.commit();
        } catch (RuntimeException e) { if (tx != null && tx.isActive()) tx.rollback(); throw e; }
    }
    private void prosesPenggunaanStok(Session s, Pesanan pesanan, List<PenggunaanStok> daftar, boolean managed) {
        List<PenggunaanStok> input = new ArrayList<>(daftar);
        if (daftar == pesanan.getPenggunaanStok()) pesanan.getPenggunaanStok().clear();
        for (PenggunaanStok item : input) {
            StokBarang barang = s.get(StokBarang.class, item.getStokBarang().getIdStokBarang());
            if (barang == null || !Boolean.TRUE.equals(barang.getAktif())) throw new IllegalArgumentException("Barang stok tidak ditemukan atau tidak aktif.");
            if (item.getJumlah() == null || item.getJumlah() < 1) throw new IllegalArgumentException("Jumlah penggunaan barang minimal 1.");
            if (barang.getJumlahStok() < item.getJumlah()) throw new IllegalArgumentException("Stok " + barang.getNamaBarang() + " tidak mencukupi. Tersedia: " + barang.getJumlahStok());
            barang.setJumlahStok(barang.getJumlahStok() - item.getJumlah());
            PenggunaanStok saved = new PenggunaanStok(); saved.setPesanan(pesanan); saved.setStokBarang(barang); saved.setJumlah(item.getJumlah());
            pesanan.getPenggunaanStok().add(saved);
        }
    }
    public void delete(Long id) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            Pesanan p = s.get(Pesanan.class, id);
            if (p != null) p.getPenggunaanStok().size();
            if (p != null) {
                for (PenggunaanStok item : p.getPenggunaanStok()) {
                    StokBarang barang = s.get(StokBarang.class, item.getStokBarang().getIdStokBarang());
                    barang.setJumlahStok(barang.getJumlahStok() + item.getJumlah());
                }
                s.delete(p);
            }
            tx.commit();
        } catch (RuntimeException e) { if (tx != null && tx.isActive()) tx.rollback(); throw e; }
    }
}
