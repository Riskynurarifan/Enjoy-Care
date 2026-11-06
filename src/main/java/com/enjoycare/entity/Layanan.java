
package com.enjoycare.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "layanan")
public class Layanan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_layanan")
    private Integer idLayanan;

    @Column(name = "kode_layanan", nullable = false, unique = true)
    private String kodeLayanan;

    @Column(name = "nama_layanan", nullable = false)
    private String namaLayanan;

    @Column(name = "harga", nullable = false, precision = 12, scale = 2)
    private BigDecimal harga;

    @Column(name = "keterangan", columnDefinition = "TEXT")
    private String keterangan;

    @Column(name = "aktif")
    private Boolean aktif;

    public Layanan() {
    }

    public Integer getIdLayanan() {
        return idLayanan;
    }

    public void setIdLayanan(Integer idLayanan) {
        this.idLayanan = idLayanan;
    }

    public String getKodeLayanan() {
        return kodeLayanan;
    }

    public void setKodeLayanan(String kodeLayanan) {
        this.kodeLayanan = kodeLayanan;
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public void setNamaLayanan(String namaLayanan) {
        this.namaLayanan = namaLayanan;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public Boolean getAktif() {
        return aktif;
    }

    public void setAktif(Boolean aktif) {
        this.aktif = aktif;
    }
}
