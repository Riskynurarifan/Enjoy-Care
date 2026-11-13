package com.enjoycare.entity;

import javax.persistence.*;

@Entity
@Table(name = "penggunaan_stok")
public class PenggunaanStok {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_penggunaan_stok")
    private Long idPenggunaanStok;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_pesanan", nullable = false)
    private Pesanan pesanan;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "id_stok_barang", nullable = false)
    private StokBarang stokBarang;
    @Column(name = "jumlah", nullable = false)
    private Integer jumlah;
    public PenggunaanStok() { }
    public Long getIdPenggunaanStok() { return idPenggunaanStok; }
    public void setIdPenggunaanStok(Long v) { idPenggunaanStok = v; }
    public Pesanan getPesanan() { return pesanan; }
    public void setPesanan(Pesanan v) { pesanan = v; }
    public StokBarang getStokBarang() { return stokBarang; }
    public void setStokBarang(StokBarang v) { stokBarang = v; }
    public Integer getJumlah() { return jumlah; }
    public void setJumlah(Integer v) { jumlah = v; }
}
