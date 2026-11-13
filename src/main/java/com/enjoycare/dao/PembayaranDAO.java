package com.enjoycare.dao;

import com.enjoycare.HibernateUtil;
import com.enjoycare.entity.Pembayaran;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PembayaranDAO {
    public List<Pembayaran> getAll() { try(Session s=HibernateUtil.getSessionFactory().openSession()){return s.createQuery("SELECT DISTINCT p FROM Pembayaran p JOIN FETCH p.pesanan ps JOIN FETCH ps.pelanggan ORDER BY p.idPembayaran DESC",Pembayaran.class).list();} }
    public Pembayaran getById(Long id) { try(Session s=HibernateUtil.getSessionFactory().openSession()){return s.createQuery("SELECT p FROM Pembayaran p JOIN FETCH p.pesanan ps JOIN FETCH ps.pelanggan WHERE p.idPembayaran = :id", Pembayaran.class).setParameter("id", id).uniqueResult();} }
    public void save(Pembayaran data) { Transaction tx=null; try(Session s=HibernateUtil.getSessionFactory().openSession()){tx=s.beginTransaction(); if(data.getKodePembayaran()==null||data.getKodePembayaran().trim().isEmpty())data.setKodePembayaran("PAY"+UUID.randomUUID().toString().replace("-","").substring(0,12).toUpperCase()); if(data.getIdPembayaran()==null)s.save(data);else s.merge(data);tx.commit();}catch(RuntimeException e){if(tx!=null&&tx.isActive())tx.rollback();throw e;} }
    public void delete(Long id) { Transaction tx=null; try(Session s=HibernateUtil.getSessionFactory().openSession()){tx=s.beginTransaction();Pembayaran p=s.get(Pembayaran.class,id);if(p!=null)s.delete(p);tx.commit();}catch(RuntimeException e){if(tx!=null&&tx.isActive())tx.rollback();throw e;} }
}
