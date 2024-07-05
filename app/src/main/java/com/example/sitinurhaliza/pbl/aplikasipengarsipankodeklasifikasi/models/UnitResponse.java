// SpResponse.java
package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models;

import java.io.Serializable;
import java.util.List;

public class UnitResponse {
    private List<UnitData> data;

    public List<UnitData> getData() {
        return data;
    }

    public void setData(List<UnitData> data) {
        this.data = data;
    }

    public static class UnitData implements Serializable {

        private int id;

        private String nama_unit;

        // getters and setters


        public int getId() {
            return id;
        }

        public void setNomor(int id) {
            this.id = id;
        }

        public String getNama() {
            return nama_unit;
        }

        public void setNama(String nama_unit) {
            this.nama_unit = nama_unit;
        }

    }
}
