package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models;

public class Barang {
    private String nama_barang;
    private String merk;
    private int vol;
    private String satuan;
    private int harga_satuan;

    public Barang(String namaBarang, String merk, int volume, String satuan, int hargaSatuan) {
        this.nama_barang = namaBarang;
        this.merk = merk;
        this.vol = volume;
        this.satuan = satuan;
        this.harga_satuan = hargaSatuan;
    }

    // Getter dan setter (opsional)
    public String getNamaBarang() {
        return nama_barang;
    }

    public void setNamaBarang(String namaBarang) {
        this.nama_barang = namaBarang;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public int getVolume() {
        return vol;
    }

    public void setVolume(int volume) {
        this.vol = volume;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public int getHargaSatuan() {
        return harga_satuan;
    }

    public void setHargaSatuan(int hargaSatuan) {
        this.harga_satuan = hargaSatuan;
    }
}

