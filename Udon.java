package com.marugame;

public class Udon extends Menu {
    public Udon(String nama, int harga) {
        super(nama, harga);
    }

    //Implementasi abstract method dari Menu
    public String getKategori() {
        return "Food";
    }
    public String getDeskripsi() {
        return "Udon";
    }
}
