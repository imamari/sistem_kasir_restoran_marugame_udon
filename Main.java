import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.marugame.Makanan;
import com.marugame.Menu;
import com.marugame.Minuman;
import com.marugame.PaketTempura;
import com.marugame.Pesanan;
import com.marugame.Tempura;
import com.marugame.Udon;

/*
 * Main Program - Sistem Kasir Marugame Udon
 * Menu dan harga mengikuti struk Marugame Udon Tangcity Mall (18-09-2026).
 * Daftar menu dibaca dari src/menu.txt, struk disimpan ke src/struk.txt
 * Format menu.txt per baris:
 *   Udon,nama,harga
 *   Lauk,nama,harga
 *   Tempura,nama,harga
 *   Paket,nama,harga,namaBonus
 *   Minuman,nama,harga
 */
public class Main {

    //Read File: baca menu.txt lalu buat object sesuai kategorinya
    static ArrayList<Menu> bacaMenu(String namaFile) {
        //Polymorphic variables: semua jenis menu disimpan sebagai Menu
        ArrayList<Menu> daftarMenu = new ArrayList<Menu>();
        try {
            File myObj = new File(namaFile);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data.trim().isEmpty()) {
                    continue;
                }
                String[] str = data.split(",");
                try {
                    String kategori = str[0].trim();
                    String nama = str[1].trim();
                    int harga = Integer.parseInt(str[2].trim());

                    if (kategori.equals("Udon")) {
                        daftarMenu.add(new Udon(nama, harga));
                    } else if (kategori.equals("Lauk")) {
                        daftarMenu.add(new Makanan(nama, harga));
                    } else if (kategori.equals("Tempura")) {
                        daftarMenu.add(new Tempura(nama, harga));
                    } else if (kategori.equals("Paket")) {
                        daftarMenu.add(new PaketTempura(nama, harga, str[3].trim(), 1));
                    } else if (kategori.equals("Minuman")) {
                        daftarMenu.add(new Minuman(nama, harga));
                    } else {
                        System.out.println("Kategori tidak dikenal, baris dilewati: " + data);
                    }
                } catch (Exception e) {
                    System.out.println("Format baris salah, baris dilewati: " + data);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File " + namaFile + " tidak ditemukan.");
            e.printStackTrace();
        }
        return daftarMenu;
    }

    //Baca angka dari kasir (ulang kalau input bukan angka)
    static int bacaAngka(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = input.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println(">> Masukkan angka yang valid.");
            }
        }
    }

    static void tampilMenu(ArrayList<Menu> daftarMenu) {
        System.out.println("\n=========== DAFTAR MENU ===========");
        for (int i = 0; i < daftarMenu.size(); ++i) {
            Menu m = daftarMenu.get(i);
            String info = "";
            if (m instanceof PaketTempura) {          // cek subclass paling bawah dulu
                info = " (gratis " + ((PaketTempura) m).getBonus() + ")";
            }
            System.out.println((i + 1) + ". [" + m.getDeskripsi() + "] " + m.getNama()
                    + " - Rp " + Menu.rp(m.getHarga()) + info);
        }
    }

    static void tambahPesanan(ArrayList<Menu> daftarMenu, Pesanan pesanan, Scanner input) {
        tampilMenu(daftarMenu);
        int pilih = bacaAngka(input, "Nomor menu (0 = batal): ");
        if (pilih == 0) {
            return;
        }
        if (pilih < 1 || pilih > daftarMenu.size()) {
            System.out.println(">> Nomor menu tidak valid");
            return;
        }
        Menu m = daftarMenu.get(pilih - 1);
        int qty = bacaAngka(input, "Jumlah: ");
        if (qty < 1) {
            System.out.println(">> Jumlah harus lebih dari 0");
            return;
        }
        pesanan.tambah(m, qty);
        System.out.println(">> Ditambahkan: " + m.getNama() + " x" + qty);
    }

    static void hapusPesanan(Pesanan pesanan, Scanner input) {
        if (pesanan.isEmpty()) {
            System.out.println("Pesanan masih kosong.");
            return;
        }
        pesanan.tampilkan();
        int nomor = bacaAngka(input, "Nomor item yang dihapus (0 = batal): ");
        if (nomor >= 1 && nomor <= pesanan.size()) {
            pesanan.hapus(nomor - 1);
            System.out.println(">> Item dihapus");
        } else if (nomor != 0) {
            System.out.println(">> Nomor item tidak valid");
        }
    }

    //return true kalau pembayaran berhasil
    static boolean bayar(Pesanan pesanan, int nomor, Scanner input) {
        if (pesanan.isEmpty()) {
            System.out.println("Pesanan masih kosong.");
            return false;
        }
        pesanan.tampilkan();
        long total = pesanan.hitungTotal();

        int metode = bacaAngka(input, "Metode bayar (1 = Tunai, 2 = QRIS, 0 = batal): ");
        long bayar;
        String namaMetode;
        if (metode == 1) {
            namaMetode = "Tunai";
            while (true) {
                bayar = bacaAngka(input, "Uang diterima: Rp ");
                if (bayar >= total) {
                    break;
                }
                System.out.println(">> Uang kurang Rp " + Menu.rp(total - bayar));
            }
        } else if (metode == 2) {
            namaMetode = "QRIS";
            bayar = total;
        } else {
            return false;
        }

        String struk = pesanan.buatStruk(nomor, namaMetode, bayar);
        System.out.println("\n" + struk);
        pesanan.simpanStruk("src/struk.txt", struk);
        pesanan.kosongkan(); // siap untuk pelanggan berikutnya
        return true;
    }

    public static void main(String[] args) {
        ArrayList<Menu> daftarMenu = bacaMenu("src/menu.txt");
        if (daftarMenu.isEmpty()) {
            System.out.println("Menu kosong, program berhenti.");
            return;
        }

        Scanner input = new Scanner(System.in);
        Pesanan pesanan = new Pesanan();
        int nomorTransaksi = 1;

        boolean lanjut = true;
        while (lanjut) {
            System.out.println("\n===== KASIR MARUGAME UDON =====");
            System.out.println("1. Lihat menu");
            System.out.println("2. Tambah pesanan");
            System.out.println("3. Lihat pesanan");
            System.out.println("4. Hapus item pesanan");
            System.out.println("5. Bayar & cetak struk");
            System.out.println("0. Keluar");
            int pilih = bacaAngka(input, "Pilih: ");

            switch (pilih) {
                case 1:
                    tampilMenu(daftarMenu);
                    break;
                case 2:
                    tambahPesanan(daftarMenu, pesanan, input);
                    break;
                case 3:
                    pesanan.tampilkan();
                    break;
                case 4:
                    hapusPesanan(pesanan, input);
                    break;
                case 5:
                    if (bayar(pesanan, nomorTransaksi, input)) {
                        nomorTransaksi++;
                    }
                    break;
                case 0:
                    lanjut = false;
                    break;
                default:
                    System.out.println(">> Pilihan tidak valid");
            }
        }
        System.out.println("Terima kasih!");
        input.close();
    }
}
