package com.enjoycare.entity;
import javax.persistence.*;
import java.math.BigDecimal;
@Entity @Table(name="detail_pesanan")
public class DetailPesanan {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_detail_pesanan") private Long idDetailPesanan;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="id_pesanan",nullable=false) private Pesanan pesanan;
 @ManyToOne(fetch=FetchType.EAGER) @JoinColumn(name="id_layanan",nullable=false) private Layanan layanan;
 @Column(name="jumlah_unit",nullable=false) private Integer jumlahUnit;
 @Column(name="harga_satuan",nullable=false,precision=12,scale=2) private BigDecimal hargaSatuan;
 @Column(name="subtotal",nullable=false,precision=12,scale=2) private BigDecimal subtotal;
 public DetailPesanan(){} public Long getIdDetailPesanan(){return idDetailPesanan;} public void setIdDetailPesanan(Long v){idDetailPesanan=v;} public Pesanan getPesanan(){return pesanan;} public void setPesanan(Pesanan v){pesanan=v;} public Layanan getLayanan(){return layanan;} public void setLayanan(Layanan v){layanan=v;} public Integer getJumlahUnit(){return jumlahUnit;} public void setJumlahUnit(Integer v){jumlahUnit=v;} public BigDecimal getHargaSatuan(){return hargaSatuan;} public void setHargaSatuan(BigDecimal v){hargaSatuan=v;} public BigDecimal getSubtotal(){return subtotal;} public void setSubtotal(BigDecimal v){subtotal=v;}
}
