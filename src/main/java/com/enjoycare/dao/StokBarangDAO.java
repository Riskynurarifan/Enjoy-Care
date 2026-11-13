package com.enjoycare.dao;

import com.enjoycare.HibernateUtil;
import com.enjoycare.entity.StokBarang;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository
public class StokBarangDAO {
    public List<StokBarang> getAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM StokBarang ORDER BY namaBarang", StokBarang.class).list();
        }
    }
    public List<StokBarang> getAktif() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM StokBarang WHERE aktif = true ORDER BY namaBarang", StokBarang.class).list();
        }
    }
    public StokBarang getById(Long id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) { return s.get(StokBarang.class, id); }
    }
    public void save(StokBarang barang) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            if (barang.getIdStokBarang() == null) s.save(barang);
            else {
                StokBarang db = s.get(StokBarang.class, barang.getIdStokBarang());
                if (db == null) throw new IllegalArgumentException("Barang tidak ditemukan.");
                db.setKodeBarang(barang.getKodeBarang()); db.setNamaBarang(barang.getNamaBarang());
                db.setSatuan(barang.getSatuan()); db.setJumlahStok(barang.getJumlahStok());
                db.setStokMinimum(barang.getStokMinimum()); db.setAktif(barang.getAktif());
            }
            tx.commit();
        } catch (RuntimeException e) { if (tx != null && tx.isActive()) tx.rollback(); throw e; }
    }
    public void delete(Long id) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction(); StokBarang b = s.get(StokBarang.class, id);
            if (b != null) s.delete(b); tx.commit();
        } catch (RuntimeException e) { if (tx != null && tx.isActive()) tx.rollback(); throw e; }
    }
}
