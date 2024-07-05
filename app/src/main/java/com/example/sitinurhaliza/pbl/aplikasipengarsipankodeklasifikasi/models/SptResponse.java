
package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models;

import java.io.Serializable;
import java.util.List;

public class SptResponse {
    private List<SptData> data;

    public List<SptData> getData() {
        return data;
    }

    public void setData(List<SptData> data) {
        this.data = data;
    }

    public static class SptData implements Serializable {

        private int id;
        private String nomor;
        private String nama;
        private String tanggal;
        private String tujuan;

        // getters and setters

        public String getNomor() {
            return nomor;
        }

        public void setNomor(String nomor) {
            this.nomor = nomor;
        }
        public int getId() {
            return id;
        }

        public void setNomor(int id) {
            this.id = id;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getTanggal() {
            return tanggal;
        }

        public void setTanggal(String tanggal) {
            this.tanggal = tanggal;
        }

        public String getTujuan() {
            return tujuan;
        }

        public void setTujuan(String tujuan) {
            this.tujuan = tujuan;
        }
    }
}
