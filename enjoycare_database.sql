-- Database EnjoyCare
-- Struktur: pelanggan, layanan, pesanan (termasuk data teknisi),
-- pembayaran, dan pengguna (akun login Owner/Admin).

CREATE DATABASE IF NOT EXISTS enjoycare
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE enjoycare;

-- 1. Data pelanggan
CREATE TABLE IF NOT EXISTS pelanggan (
    id_pelanggan BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    kode_pelanggan VARCHAR(20) NOT NULL UNIQUE,
    nama_pelanggan VARCHAR(100) NOT NULL,
    no_telepon VARCHAR(20) NOT NULL,
    alamat TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Data layanan
CREATE TABLE IF NOT EXISTS layanan (
    id_layanan BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    kode_layanan VARCHAR(20) NOT NULL UNIQUE,
    nama_layanan VARCHAR(100) NOT NULL,
    harga DECIMAL(12,2) NOT NULL,
    keterangan TEXT,
    aktif BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT chk_layanan_harga CHECK (harga >= 0)
) ENGINE=InnoDB;

-- 3. Data pesanan.
-- Data teknisi disimpan langsung di tabel ini, tanpa tabel teknisi terpisah.
CREATE TABLE IF NOT EXISTS pesanan (
    id_pesanan BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    kode_pesanan VARCHAR(20) NOT NULL UNIQUE,
    tanggal_pesanan DATE NOT NULL,
    id_pelanggan BIGINT UNSIGNED NOT NULL,
    id_layanan BIGINT UNSIGNED NOT NULL,
    nama_teknisi VARCHAR(100) NOT NULL,
    no_telepon_teknisi VARCHAR(20),
    status_pesanan ENUM(
        'Menunggu',
        'Dikerjakan',
        'Selesai',
        'Dibatalkan'
    ) NOT NULL DEFAULT 'Menunggu',
    jumlah_unit INT UNSIGNED NOT NULL DEFAULT 1,
    total_harga DECIMAL(12,2) NOT NULL,
    catatan TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pesanan_pelanggan
        FOREIGN KEY (id_pelanggan) REFERENCES pelanggan(id_pelanggan)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT fk_pesanan_layanan
        FOREIGN KEY (id_layanan) REFERENCES layanan(id_layanan)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT chk_pesanan_jumlah CHECK (jumlah_unit > 0),
    CONSTRAINT chk_pesanan_total CHECK (total_harga >= 0),
    INDEX idx_pesanan_tanggal (tanggal_pesanan),
    INDEX idx_pesanan_status (status_pesanan)
) ENGINE=InnoDB;

-- 4. Pembayaran.
-- Satu pesanan boleh memiliki lebih dari satu pembayaran (misalnya DP dan pelunasan).
CREATE TABLE IF NOT EXISTS pembayaran (
    id_pembayaran BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    kode_pembayaran VARCHAR(20) NOT NULL UNIQUE,
    id_pesanan BIGINT UNSIGNED NOT NULL,
    tanggal_pembayaran DATE NOT NULL,
    metode_pembayaran ENUM(
        'Tunai',
        'Transfer',
        'QRIS',
        'Lainnya'
    ) NOT NULL,
    nominal DECIMAL(12,2) NOT NULL,
    status_pembayaran ENUM(
        'Belum Bayar',
        'DP',
        'Lunas'
    ) NOT NULL DEFAULT 'Belum Bayar',
    catatan TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pembayaran_pesanan
        FOREIGN KEY (id_pesanan) REFERENCES pesanan(id_pesanan)
        ON UPDATE CASCADE ON DELETE RESTRICT,
    CONSTRAINT chk_pembayaran_nominal CHECK (nominal >= 0),
    INDEX idx_pembayaran_tanggal (tanggal_pembayaran),
    INDEX idx_pembayaran_status (status_pembayaran)
) ENGINE=InnoDB;

-- 5. Akun aplikasi untuk login Owner dan Admin.
-- Tabel ini tetap ada meskipun halaman Pengguna di aplikasi dihapus.
CREATE TABLE IF NOT EXISTS pengguna (
    id_pengguna BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nama_lengkap VARCHAR(100) NOT NULL,
    role ENUM('Owner', 'Admin') NOT NULL,
    status ENUM('Aktif', 'Nonaktif') NOT NULL DEFAULT 'Aktif',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;
