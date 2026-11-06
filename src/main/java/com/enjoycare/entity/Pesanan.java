
package com.enjoycare.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "pesanan")
public class Pesanan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pesanan")
    private Integer idPesanan;

    @Column(name = "kode_pesanan", nullable = false, unique = true)
    private String kodePesanan;

    @Column(name = "tanggal_pesanan", nullable = false)
    private LocalDate tanggalPesanan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pelanggan", nullable = false)
    private Pelanggan pelanggan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_layanan", nullable = false)
    private Layanan layanan;

    @Column(name = "nama_teknisi")
    private String namaTeknisi;

    @Column(name = "no_telepon_teknisi")
    private String noTeleponTeknisi;

    @Column(name = "status_pesanan")
    private String statusPesanan;

    @Column(name = "jumlah_unit")
    private Integer jumlahUnit;

    @Column(name = "total_harga", precision = 12, scale = 2)
    private BigDecimal totalHarga;

    @Column(name = "catatan", columnDefinition = "TEXT")
    private String catatan;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Pesanan() {
    }

    public Integer getIdPesanan() { return idPesanan; }
    public void setIdPesanan(Integer idPesanan) { this.idPesanan = idPesanan; }

    public String getKodePesanan() { return kodePesanan; }
    public void setKodePesanan(String kodePesanan) { this.kodePesanan = kodePesanan; }

    public LocalDate getTanggalPesanan() { return tanggalPesanan; }
    public void setTanggalPesanan(LocalDate tanggalPesanan) { this.tanggalPesanan = tanggalPesanan; }

    public Pelanggan getPelanggan() { return pelanggan; }
    public void setPelanggan(Pelanggan pelanggan) { this.pelanggan = pelanggan; }

    public Layanan getLayanan() { return layanan; }
    public void setLayanan(Layanan layanan) { this.layanan = layanan; }

    public String getNamaTeknisi() { return namaTeknisi; }
    public void setNamaTeknisi(String namaTeknisi) { this.namaTeknisi = namaTeknisi; }

    public String getNoTeleponTeknisi() { return noTeleponTeknisi; }
    public void setNoTeleponTeknisi(String noTeleponTeknisi) { this.noTeleponTeknisi = noTeleponTeknisi; }

    public String getStatusPesanan() { return statusPesanan; }
    public void setStatusPesanan(String statusPesanan) { this.statusPesanan = statusPesanan; }

    public Integer getJumlahUnit() { return jumlahUnit; }
    public void setJumlahUnit(Integer jumlahUnit) { this.jumlahUnit = jumlahUnit; }

    public BigDecimal getTotalHarga() { return totalHarga; }
    public void setTotalHarga(BigDecimal totalHarga) { this.totalHarga = totalHarga; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
