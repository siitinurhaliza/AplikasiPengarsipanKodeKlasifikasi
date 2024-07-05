package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models;

import java.util.List;

public class JenissuratsResponse {
    private List<Jenissurats> data;

    public List<Jenissurats> getData() {
        return data;
    }

    public void setData(List<Jenissurats> data) {
        this.data = data;
    }

    public static class Jenissurats {
        private int id;
        private String nama;
        private String keterangan;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getKeterangan() {
            return keterangan;
        }

        public void setKeterangan(String keterangan) {
            this.keterangan = keterangan;
        }
    }
}
