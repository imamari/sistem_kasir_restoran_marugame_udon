package com.marugame;

public class Tempura extends Menu {
    public Tempura(String nama, int harga) {
        super(nama, harga);
    }

    //Implementasi abstract method dari Menu
    public String getKategori() {
        return "Food";
    }
    public String getDeskripsi() {
        return "Tempura";
    }
}
