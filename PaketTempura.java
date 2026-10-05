package com.marugame;

public class PaketTempura extends Tempura {
    private String bonus;
    private int jumlahBonus;

    public PaketTempura(String nama, int harga, String bonus, int jumlahBonus) {
        super(nama, harga);
        this.bonus = bonus;
        this.jumlahBonus = jumlahBonus;
    }

    //setter and getter
    public void setBonus(String bonus) {
        this.bonus = bonus;
    }
    public String getBonus() {
        return bonus;
    }
    public void setJumlahBonus(int jumlahBonus) {
        this.jumlahBonus = jumlahBonus;
    }
    public int getJumlahBonus() {
        return jumlahBonus;
    }

    public String getDeskripsi() {
        return "Paket Tempura";
    }

    public String cetakBaris(int jumlah) {
        return super.cetakBaris(jumlah)
                + "    " + bonus + "\n"
                + "    " + String.format("%-18s%10s", (jumlah * jumlahBonus) + "x  @0", "0")
                + "\n";
    }
}
