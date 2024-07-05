package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models;

import java.util.List;

public class KlasifikasiResponse {
    private List<Klasifikasi> data;

    public List<Klasifikasi> getData() {
        return data;
    }

    public void setData(List<Klasifikasi> data) {
        this.data = data;
    }

    public static class Klasifikasi {
        private int id;
        private String kode;
        private String perihal;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getKode() {
            return kode;
        }

        public void setKode(String kode) {
            this.kode = kode;
        }

        public String getPerihal() {
            return perihal;
        }

        public void setPerihal(String perihal) {
            this.perihal = perihal;
        }
    }
}
