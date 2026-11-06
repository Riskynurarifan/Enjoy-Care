
package com.enjoycare.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "pembayaran")
public class Pembayaran {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pembayaran")
    private Integer idPembayaran;

    @Column(name = "kode_pembayaran", nullable = false, unique = true)
    private String kodePembayaran;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pesanan", nullable = false)
    private Pesanan pesanan;

    @Column(name = "tanggal_pembayaran", nullable = false)
    private LocalDate tanggalPembayaran;

    @Column(name = "metode_pembayaran")
    private String metodePembayaran;

    @Column(name = "nominal", nullable = false, precision = 12, scale = 2)
    private BigDecimal nominal;

    @Column(name = "status_pembayaran")
    private String statusPembayaran;

    @Column(name = "catatan", columnDefinition = "TEXT")
    private String catatan;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Pembayaran() {
    }

    public Integer getIdPembayaran() { return idPembayaran; }
    public void setIdPembayaran(Integer idPembayaran) { this.idPembayaran = idPembayaran; }

    public String getKodePembayaran() { return kodePembayaran; }
    public void setKodePembayaran(String kodePembayaran) { this.kodePembayaran = kodePembayaran; }

    public Pesanan getPesanan() { return pesanan; }
    public void setPesanan(Pesanan pesanan) { this.pesanan = pesanan; }

    public LocalDate getTanggalPembayaran() { return tanggalPembayaran; }
    public void setTanggalPembayaran(LocalDate tanggalPembayaran) { this.tanggalPembayaran = tanggalPembayaran; }

    public String getMetodePembayaran() { return metodePembayaran; }
    public void setMetodePembayaran(String metodePembayaran) { this.metodePembayaran = metodePembayaran; }

    public BigDecimal getNominal() { return nominal; }
    public void setNominal(BigDecimal nominal) { this.nominal = nominal; }

    public String getStatusPembayaran() { return statusPembayaran; }
    public void setStatusPembayaran(String statusPembayaran) { this.statusPembayaran = statusPembayaran; }

    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
