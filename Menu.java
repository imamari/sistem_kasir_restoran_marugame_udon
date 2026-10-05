package com.marugame;

public abstract class Menu implements Struk {
    private String nama;
    private int harga;

    //Constructor
    public Menu(String nama, int harga) {
        this.nama = nama;
        this.harga = harga;
    }

    //setter and getter
    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getNama() {
        return nama;
    }

    public String getNama(String note) {
        return (note + nama);
    }
    public void setHarga(int harga) {
        this.harga = harga;
    }
    public int getHarga() {
        return harga;
    }

    //Non-abstract (regular) method: ada code implementation
    public int hitungHarga(int jumlah) {
        return harga * jumlah;
    }

    //Harga sudah termasuk pajak, jadi pajaknya "dikeluarkan" dari harga
    public double hitungPajak(int jumlah) {
        return hitungHarga(jumlah) * PB1 / (1 + PB1);
    }

    //Format rupiah dengan titik: 66.000
    public static String rp(long angka) {
        return String.format("%,d", angka).replace(',', '.');
    }

    //Implementasi method dari interface Struk
    public String cetakBaris(int jumlah) {
        return nama + "\n"
                + String.format("%-22s%10s", jumlah + "x  @" + rp(harga), rp(hitungHarga(jumlah)))
                + "\n";
    }

    //Abstract Method
    public abstract String getKategori();
    public abstract String getDeskripsi();
}
