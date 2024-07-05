package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models;

public class NpbphDetail {
    private int id;
    private int npbph_Id;
    private String nama_barang;
    private String merk;
    private int vol;
    private String satuan;
    private int harga_satuan;
    private int jumlah;

    public void setId(int id) {
        this.id = id;
    }

    public void setNpbphId(int npbphId) {
        this.npbph_Id = npbphId;
    }

    public void setNamaBarang(String namaBarang) {
        this.nama_barang = namaBarang;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public void setVol(int vol) {
        this.vol = vol;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public void setHargaSatuan(int hargaSatuan) {
        this.harga_satuan = hargaSatuan;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getId() {
        return id;
    }

    public int getNpbphId() {
        return npbph_Id;
    }

    public String getNamaBarang() {
        return nama_barang;
    }

    public String getMerk() {
        return merk;
    }

    public int getVol() {
        return vol;
    }

    public String getSatuan() {
        return satuan;
    }

    public int getHargaSatuan() {
        return harga_satuan;
    }

    public int getJumlah() {
        return jumlah;
    }
}
