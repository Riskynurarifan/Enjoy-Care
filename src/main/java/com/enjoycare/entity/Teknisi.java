package com.enjoycare.entity;
import javax.persistence.*;
@Entity @Table(name="teknisi")
public class Teknisi {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(name="id_teknisi") private Long idTeknisi;
 @Column(name="nama_teknisi",nullable=false,length=100) private String namaTeknisi;
 @Column(name="no_telepon",nullable=false,length=20) private String noTelepon;
 @Column(name="aktif",nullable=false) private Boolean aktif=true;
 public Teknisi(){} public Long getIdTeknisi(){return idTeknisi;} public void setIdTeknisi(Long v){idTeknisi=v;} public String getNamaTeknisi(){return namaTeknisi;} public void setNamaTeknisi(String v){namaTeknisi=v;} public String getNoTelepon(){return noTelepon;} public void setNoTelepon(String v){noTelepon=v;} public Boolean getAktif(){return aktif;} public void setAktif(Boolean v){aktif=v;}
 @Override public String toString(){return namaTeknisi;}
}
