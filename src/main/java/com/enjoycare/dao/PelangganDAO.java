package com.enjoycare.dao;

import com.enjoycare.HibernateUtil;
import com.enjoycare.entity.Pelanggan;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PelangganDAO {
    public List<Pelanggan> getAll() {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            return s.createQuery("FROM Pelanggan p ORDER BY p.idPelanggan DESC", Pelanggan.class).list();
        }
    }
    public Pelanggan getById(Long id) {
        try (Session s = HibernateUtil.getSessionFactory().openSession()) { return s.get(Pelanggan.class, id); }
    }
    public void save(Pelanggan data) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction();
            if (data.getKodePelanggan() == null || data.getKodePelanggan().trim().isEmpty()) data.setKodePelanggan("PLG" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
            if (data.getIdPelanggan() == null) s.save(data); else s.merge(data);
            tx.commit();
        } catch (RuntimeException e) { if (tx != null && tx.isActive()) tx.rollback(); throw e; }
    }
    public void delete(Long id) {
        Transaction tx = null;
        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
            tx = s.beginTransaction(); Pelanggan p = s.get(Pelanggan.class, id);
            if (p != null) s.delete(p); tx.commit();
        } catch (RuntimeException e) { if (tx != null && tx.isActive()) tx.rollback(); throw e; }
    }
}
