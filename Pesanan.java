package com.marugame;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/*
 * Class Pesanan: keranjang kasir (ArrayList) + hitung total + struk
 */
public class Pesanan {
    private ArrayList<Menu> daftarMenu = new ArrayList<Menu>();
    private ArrayList<Integer> daftarJumlah = new ArrayList<Integer>();

    public void tambah(Menu menu, int jumlah) {
        int idx = daftarMenu.indexOf(menu);
        if (idx >= 0) {
            //menu yang sama: jumlahnya ditambah
            daftarJumlah.set(idx, daftarJumlah.get(idx) + jumlah);
        } else {
            daftarMenu.add(menu);
            daftarJumlah.add(jumlah);
        }
    }

    public void hapus(int index) {
        daftarMenu.remove(index);
        daftarJumlah.remove(index);
    }

    public void kosongkan() {
        daftarMenu.clear();
        daftarJumlah.clear();
    }

    public boolean isEmpty() {
        return daftarMenu.isEmpty();
    }

    public int size() {
        return daftarMenu.size();
    }

    //Grand Total (harga sudah termasuk pajak)
    public int hitungTotal() {
        int total = 0;
        for (int i = 0; i < daftarMenu.size(); ++i) {
            total += daftarMenu.get(i).hitungHarga(daftarJumlah.get(i));
        }
        return total;
    }

    //Total per kategori: "Food" atau "Beverage"
    public int hitungTotalKategori(String kategori) {
        int total = 0;
        for (int i = 0; i < daftarMenu.size(); ++i) {
            if (daftarMenu.get(i).getKategori().equals(kategori)) {
                total += daftarMenu.get(i).hitungHarga(daftarJumlah.get(i));
            }
        }
        return total;
    }

    public long hitungPajakBulat() {
        double pajak = 0;
        for (int i = 0; i < daftarMenu.size(); ++i) {
            pajak += daftarMenu.get(i).hitungPajak(daftarJumlah.get(i));
        }
        return Math.round(pajak);
    }

    public void tampilkan() {
        if (isEmpty()) {
            System.out.println("Pesanan masih kosong.");
            return;
        }
        System.out.println("------- PESANAN SAAT INI -------");
        for (int i = 0; i < daftarMenu.size(); ++i) {
            Menu m = daftarMenu.get(i);
            int qty = daftarJumlah.get(i);
            System.out.println((i + 1) + ". " + m.getNama() + " x" + qty
                    + " = Rp " + Menu.rp(m.hitungHarga(qty)));
        }
        System.out.println("--------------------------------");
        System.out.println("Grand Total : Rp " + Menu.rp(hitungTotal()));
    }

    private String baris(String label, long nilai) {
        return String.format("%-22s%10s", label, Menu.rp(nilai)) + "\n";
    }

    public String buatStruk(int nomor, String metode, long bayar) {
        long total = hitungTotal();
        String garis = "--------------------------------\n";
        String tanggal = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));

        String s = "          MARUGAME UDON\n";
        s += "   JAPANESE NOODLES & TEMPURA\n";
        s += garis;
        s += String.format("No   : TRX-%04d%n", nomor);
        s += "Date : " + tanggal + "\n";
        s += garis;
        for (int i = 0; i < daftarMenu.size(); ++i) {
            s += daftarMenu.get(i).cetakBaris(daftarJumlah.get(i)); // lewat interface Struk
        }
        s += garis;
        s += baris("Total Food", hitungTotalKategori("Food"));
        s += baris("Total Beverage", hitungTotalKategori("Beverage"));
        s += garis;
        //baris bonus paket ikut dihitung sebagai item (seperti di struk asli)
        int jumlahItem = daftarMenu.size();
        for (int i = 0; i < daftarMenu.size(); ++i) {
            if (daftarMenu.get(i) instanceof PaketTempura) {
                jumlahItem++;
            }
        }
        s += jumlahItem + " items\n";
        s += baris("Grand Total :", total);
        s += baris(metode.toUpperCase() + " :", bayar);
        if (bayar > total) {
            s += baris("Kembali :", bayar - total);
        }
        s += baris("Price inclusive of PB1 :", hitungPajakBulat());
        s += garis;
        s += "        ARIGATOU GOZAIMASU\n";
        s += "   The Prices are Inclusive Tax\n";
        return s;
    }

    //Write File: simpan struk ke file (ditambahkan di akhir file)
    public void simpanStruk(String namaFile, String struk) {
        try {
            FileWriter fw = new FileWriter(namaFile, true);
            fw.write(struk);
            fw.write(System.lineSeparator());
            fw.close();
            System.out.println(">> Struk disimpan di " + namaFile);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
