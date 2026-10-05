package com.marugame;

public class Minuman extends Menu {
    public Minuman(String nama, int harga) {
        super(nama, harga);
    }

    //Implementasi abstract method dari Menu
    public String getKategori() {
        return "Beverage";
    }
    public String getDeskripsi() {
        return "Minuman";
    }
}
