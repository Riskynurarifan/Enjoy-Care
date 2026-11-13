package com.enjoycare.entity;
import javax.persistence.*; import java.math.BigDecimal; import java.time.LocalDate; import java.time.LocalDateTime; import java.util.ArrayList; import java.util.List; import org.hibernate.annotations.CreationTimestamp;
@Entity @Table(name="pesanan") public class Pesanan {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_pesanan") private Long idPesanan;
 @Column(name="kode_pesanan",nullable=false,unique=true) private String kodePesanan;
 @Column(name="tanggal_pesanan",nullable=false) private LocalDate tanggalPesanan;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_pelanggan",nullable=false) private Pelanggan pelanggan;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_teknisi",nullable=false) private Teknisi teknisi;
 @OneToMany(mappedBy="pesanan",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.LAZY) private List<DetailPesanan> detailPesanan=new ArrayList<>();
 @OneToMany(mappedBy="pesanan",cascade=CascadeType.ALL,orphanRemoval=true,fetch=FetchType.LAZY) private List<PenggunaanStok> penggunaanStok=new ArrayList<>();
 @Column(name="status_pesanan") private String statusPesanan;
 @Column(name="total_harga",precision=12,scale=2) private BigDecimal totalHarga=BigDecimal.ZERO;
 @Column(name="catatan",columnDefinition="TEXT") private String catatan;
 @CreationTimestamp @Column(name="created_at",updatable=false) private LocalDateTime createdAt;
 public Pesanan(){} public Long getIdPesanan(){return idPesanan;} public void setIdPesanan(Long v){idPesanan=v;} public String getKodePesanan(){return kodePesanan;} public void setKodePesanan(String v){kodePesanan=v;} public LocalDate getTanggalPesanan(){return tanggalPesanan;} public void setTanggalPesanan(LocalDate v){tanggalPesanan=v;} public Pelanggan getPelanggan(){return pelanggan;} public void setPelanggan(Pelanggan v){pelanggan=v;} public Teknisi getTeknisi(){return teknisi;} public void setTeknisi(Teknisi v){teknisi=v;} public List<DetailPesanan> getDetailPesanan(){return detailPesanan;} public void setDetailPesanan(List<DetailPesanan> v){detailPesanan=v==null?new ArrayList<DetailPesanan>():v;} public List<PenggunaanStok> getPenggunaanStok(){return penggunaanStok;} public void setPenggunaanStok(List<PenggunaanStok> v){penggunaanStok=v==null?new ArrayList<PenggunaanStok>():v;} public String getStatusPesanan(){return statusPesanan;} public void setStatusPesanan(String v){statusPesanan=v;} public BigDecimal getTotalHarga(){return totalHarga;} public void setTotalHarga(BigDecimal v){totalHarga=v;} public String getCatatan(){return catatan;} public void setCatatan(String v){catatan=v;} public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}
