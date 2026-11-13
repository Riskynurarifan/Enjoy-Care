package com.enjoycare.controller;

import com.enjoycare.entity.StokBarang;
import com.enjoycare.service.StokBarangService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class StokBarangController {
    @Autowired private StokBarangService service;
    public List<StokBarang> tampilkanSemua() { return service.getAll(); }
    public List<StokBarang> tampilkanAktif() { return service.getAktif(); }
    public void simpan(StokBarang barang) { service.simpan(barang); }
    public void hapus(Long id) { service.hapus(id); }
}
