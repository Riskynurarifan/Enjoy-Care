-- EnjoyCare - skema database versi multi-layanan, master teknisi, dan stok barang.
-- Skrip bersifat additive: menggunakan CREATE TABLE IF NOT EXISTS dan tidak menghapus data lama.
CREATE DATABASE IF NOT EXISTS enjoycare CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE enjoycare;

CREATE TABLE IF NOT EXISTS pelanggan (
 id_pelanggan BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
 kode_pelanggan VARCHAR(20) NOT NULL UNIQUE,
 nama_pelanggan VARCHAR(100) NOT NULL,
 no_telepon VARCHAR(20),
 alamat TEXT,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (id_pelanggan)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS layanan (
 id_layanan BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
 kode_layanan VARCHAR(20) NOT NULL UNIQUE,
 nama_layanan VARCHAR(100) NOT NULL,
 harga DECIMAL(12,2) NOT NULL,
 keterangan TEXT,
 aktif BOOLEAN NOT NULL DEFAULT TRUE,
 PRIMARY KEY (id_layanan),
 CONSTRAINT chk_layanan_harga CHECK (harga >= 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS teknisi (
 id_teknisi BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
 nama_teknisi VARCHAR(100) NOT NULL,
 no_telepon VARCHAR(20) NOT NULL,
 aktif BOOLEAN NOT NULL DEFAULT TRUE,
 PRIMARY KEY (id_teknisi),
 INDEX idx_teknisi_nama (nama_teknisi)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS pesanan (
 id_pesanan BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
 kode_pesanan VARCHAR(20) NOT NULL UNIQUE,
 tanggal_pesanan DATE NOT NULL,
 id_pelanggan BIGINT UNSIGNED NOT NULL,
 id_teknisi BIGINT UNSIGNED NOT NULL,
 status_pesanan VARCHAR(30) NOT NULL DEFAULT 'Menunggu',
 total_harga DECIMAL(12,2) NOT NULL DEFAULT 0.00,
 catatan TEXT,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (id_pesanan),
 CONSTRAINT fk_pesanan_pelanggan FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id_pelanggan) ON UPDATE CASCADE ON DELETE RESTRICT,
 CONSTRAINT fk_pesanan_teknisi FOREIGN KEY (id_teknisi) REFERENCES teknisi(id_teknisi) ON UPDATE CASCADE ON DELETE RESTRICT,
 CONSTRAINT chk_pesanan_total CHECK (total_harga >= 0),
 INDEX idx_pesanan_tanggal (tanggal_pesanan),
 INDEX idx_pesanan_status (status_pesanan)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS detail_pesanan (
 id_detail_pesanan BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
 id_pesanan BIGINT UNSIGNED NOT NULL,
 id_layanan BIGINT UNSIGNED NOT NULL,
 jumlah_unit INT NOT NULL,
 harga_satuan DECIMAL(12,2) NOT NULL,
 subtotal DECIMAL(12,2) NOT NULL,
 PRIMARY KEY (id_detail_pesanan),
 CONSTRAINT fk_detail_pesanan FOREIGN KEY (id_pesanan) REFERENCES pesanan(id_pesanan) ON UPDATE CASCADE ON DELETE CASCADE,
 CONSTRAINT fk_detail_layanan FOREIGN KEY (id_layanan) REFERENCES layanan(id_layanan) ON UPDATE CASCADE ON DELETE RESTRICT,
 CONSTRAINT chk_detail_jumlah CHECK (jumlah_unit > 0),
 CONSTRAINT chk_detail_harga CHECK (harga_satuan >= 0 AND subtotal >= 0),
 INDEX idx_detail_pesanan (id_pesanan)
) ENGINE=InnoDB;


-- Master stok barang yang dikelola melalui tab Stok Barang pada halaman Layanan.
CREATE TABLE IF NOT EXISTS stok_barang (
 id_stok_barang BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
 kode_barang VARCHAR(30) NOT NULL UNIQUE,
 nama_barang VARCHAR(120) NOT NULL,
 satuan VARCHAR(30) NOT NULL,
 jumlah_stok INT NOT NULL DEFAULT 0,
 stok_minimum INT NOT NULL DEFAULT 0,
 aktif BOOLEAN NOT NULL DEFAULT TRUE,
 PRIMARY KEY (id_stok_barang),
 CONSTRAINT chk_stok_jumlah CHECK (jumlah_stok >= 0),
 CONSTRAINT chk_stok_minimum CHECK (stok_minimum >= 0)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS penggunaan_stok (
 id_penggunaan_stok BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
 id_pesanan BIGINT UNSIGNED NOT NULL,
 id_stok_barang BIGINT UNSIGNED NOT NULL,
 jumlah INT NOT NULL,
 PRIMARY KEY (id_penggunaan_stok),
 CONSTRAINT fk_penggunaan_pesanan FOREIGN KEY (id_pesanan) REFERENCES pesanan(id_pesanan) ON UPDATE CASCADE ON DELETE CASCADE,
 CONSTRAINT fk_penggunaan_barang FOREIGN KEY (id_stok_barang) REFERENCES stok_barang(id_stok_barang) ON UPDATE CASCADE ON DELETE RESTRICT,
 CONSTRAINT chk_penggunaan_jumlah CHECK (jumlah > 0),
 INDEX idx_penggunaan_pesanan (id_pesanan)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS pembayaran (
 id_pembayaran BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
 kode_pembayaran VARCHAR(20) NOT NULL UNIQUE,
 id_pesanan BIGINT UNSIGNED NOT NULL,
 tanggal_pembayaran DATE NOT NULL,
 metode_pembayaran VARCHAR(30) NOT NULL,
 nominal DECIMAL(12,2) NOT NULL,
 status_pembayaran VARCHAR(30) NOT NULL DEFAULT 'Belum Bayar',
 catatan TEXT,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (id_pembayaran),
 CONSTRAINT fk_pembayaran_pesanan FOREIGN KEY (id_pesanan) REFERENCES pesanan(id_pesanan) ON UPDATE CASCADE ON DELETE RESTRICT,
 CONSTRAINT chk_pembayaran_nominal CHECK (nominal >= 0),
 INDEX idx_pembayaran_tanggal (tanggal_pembayaran),
 INDEX idx_pembayaran_status (status_pembayaran)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS pengguna (
 id_pengguna BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
 username VARCHAR(50) NOT NULL UNIQUE,
 password_hash VARCHAR(255) NOT NULL,
 nama_lengkap VARCHAR(100) NOT NULL,
 role VARCHAR(20) NOT NULL,
 status VARCHAR(20) NOT NULL DEFAULT 'Aktif',
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (id_pengguna)
) ENGINE=InnoDB;

-- Isi tabel pengguna dengan akun Owner/Admin yang sesuai mekanisme hash di LoginForm.
