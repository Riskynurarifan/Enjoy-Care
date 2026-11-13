package com.enjoycare.entity;

import javax.persistence.*;

@Entity
@Table(name = "stok_barang")
public class StokBarang {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_stok_barang")
    private Long idStokBarang;
    @Column(name = "kode_barang", nullable = false, unique = true, length = 30)
    private String kodeBarang;
    @Column(name = "nama_barang", nullable = false, length = 120)
    private String namaBarang;
    @Column(name = "satuan", nullable = false, length = 30)
    private String satuan;
    @Column(name = "jumlah_stok", nullable = false)
    private Integer jumlahStok = 0;
    @Column(name = "stok_minimum", nullable = false)
    private Integer stokMinimum = 0;
    @Column(name = "aktif", nullable = false)
    private Boolean aktif = true;

    public StokBarang() {}
    public Long getIdStokBarang() { return idStokBarang; }
    public void setIdStokBarang(Long v) { idStokBarang = v; }
    public String getKodeBarang() { return kodeBarang; }
    public void setKodeBarang(String v) { kodeBarang = v; }
    public String getNamaBarang() { return namaBarang; }
    public void setNamaBarang(String v) { namaBarang = v; }
    public String getSatuan() { return satuan; }
    public void setSatuan(String v) { satuan = v; }
    public Integer getJumlahStok() { return jumlahStok; }
    public void setJumlahStok(Integer v) { jumlahStok = v; }
    public Integer getStokMinimum() { return stokMinimum; }
    public void setStokMinimum(Integer v) { stokMinimum = v; }
    public Boolean getAktif() { return aktif; }
    public void setAktif(Boolean v) { aktif = v; }
    @Override public String toString() { return namaBarang; }
}
