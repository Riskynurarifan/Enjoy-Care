package com.enjoycare.service;

import com.enjoycare.dao.StokBarangDAO;
import com.enjoycare.entity.StokBarang;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StokBarangService {
    @Autowired private StokBarangDAO stokBarangDAO;
    public List<StokBarang> getAll() { return stokBarangDAO.getAll(); }
    public List<StokBarang> getAktif() { return stokBarangDAO.getAktif(); }
    public void simpan(StokBarang b) {
        if (b.getKodeBarang() == null || b.getKodeBarang().trim().isEmpty()) throw new IllegalArgumentException("Kode barang wajib diisi.");
        if (b.getNamaBarang() == null || b.getNamaBarang().trim().isEmpty()) throw new IllegalArgumentException("Nama barang wajib diisi.");
        if (b.getSatuan() == null || b.getSatuan().trim().isEmpty()) throw new IllegalArgumentException("Satuan wajib diisi.");
        if (b.getJumlahStok() == null || b.getJumlahStok() < 0) throw new IllegalArgumentException("Stok tidak boleh negatif.");
        if (b.getStokMinimum() == null || b.getStokMinimum() < 0) throw new IllegalArgumentException("Stok minimum tidak boleh negatif.");
        stokBarangDAO.save(b);
    }
    public void hapus(Long id) { stokBarangDAO.delete(id); }
}
