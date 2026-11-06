
package com.enjoycare;

public class DatabaseTest {

    public static void main(String[] args) {
        try {
            HibernateUtil.getSessionFactory();

            System.out.println("================================");
            System.out.println("KONEKSI HIBERNATE BERHASIL!");
            System.out.println("DATABASE ENJOYCARE TERHUBUNG!");
            System.out.println("================================");

        } catch (Throwable ex) {
            System.err.println("KONEKSI DATABASE GAGAL!");
            ex.printStackTrace();

        } finally {
            HibernateUtil.shutdown();
        }
    }
}
