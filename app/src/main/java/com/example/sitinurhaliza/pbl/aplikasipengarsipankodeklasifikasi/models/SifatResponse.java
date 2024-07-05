// SpResponse.java
package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models;

import java.io.Serializable;
import java.util.List;

public class SifatResponse {
    private List<SifatData> data;

    public List<SifatData> getData() {
        return data;
    }

    public void setData(List<SifatData> data) {
        this.data = data;
    }

    public static class SifatData implements Serializable {

        private int id;

        private String nama;

        // getters and setters


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

    }
}
