package com.marugame;

public class Makanan extends Menu {
    public Makanan(String nama, int harga) {
        super(nama, harga);
    }

    //Implementasi abstract method dari Menu
    public String getKategori() {
        return "Food";
    }
    public String getDeskripsi() {
        return "Makanan";
    }
}
