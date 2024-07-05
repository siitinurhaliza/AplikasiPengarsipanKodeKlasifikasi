package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models;

public class LpbphDetail {
    private int id;
    private int lpbph_Id;
    private String nama_barang;
    private String merk;
    private int vol;
    private String satuan;
    private int harga_satuan;
    private int jumlah;

    public void setId(int id) {
        this.id = id;
    }

    public void setLpbphId(int lpbphId) {
        this.lpbph_Id = lpbphId;
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

    public int getLpbphId() {
        return lpbph_Id;
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
