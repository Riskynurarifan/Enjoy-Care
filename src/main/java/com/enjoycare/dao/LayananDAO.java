package com.enjoycare.dao;

import com.enjoycare.HibernateUtil;
import com.enjoycare.entity.Layanan;
import java.util.List;
import java.util.UUID;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LayananDAO {
    public List<Layanan> getAll() { try (Session s=HibernateUtil.getSessionFactory().openSession()) { return s.createQuery("FROM Layanan l ORDER BY l.idLayanan DESC", Layanan.class).list(); } }
    public Layanan getById(Long id) { try (Session s=HibernateUtil.getSessionFactory().openSession()) { return s.get(Layanan.class,id); } }
    public void save(Layanan data) { Transaction tx=null; try(Session s=HibernateUtil.getSessionFactory().openSession()) { tx=s.beginTransaction(); if(data.getKodeLayanan()==null || data.getKodeLayanan().trim().isEmpty()) data.setKodeLayanan("LYN"+UUID.randomUUID().toString().replace("-","").substring(0,12).toUpperCase()); if(data.getIdLayanan()==null)s.save(data);else s.merge(data); tx.commit(); } catch(RuntimeException e){if(tx!=null&&tx.isActive())tx.rollback();throw e;} }
    public void delete(Long id) { Transaction tx=null; try(Session s=HibernateUtil.getSessionFactory().openSession()){tx=s.beginTransaction();Layanan x=s.get(Layanan.class,id);if(x!=null)s.delete(x);tx.commit();}catch(RuntimeException e){if(tx!=null&&tx.isActive())tx.rollback();throw e;} }
}
