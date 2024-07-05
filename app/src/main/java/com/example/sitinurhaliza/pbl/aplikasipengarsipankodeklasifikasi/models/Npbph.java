package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models;

import java.util.List;

public class Npbph {

    private int id;
    private String nomor;
    private String created_at;

    private List<NpbphDetail> npbphs;

    public String getNomor() {
        return nomor; // Getter untuk pesan
    }


    public int getId() {
        return id;
    }


    public String getCreatedAt() {
        return created_at;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public void getCreatedAt(String created_at) {
        this.created_at = created_at;
    }

    public List<NpbphDetail> getNpbphs() {
        return npbphs;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setNpbphs(List<NpbphDetail> npbphs) {
        this.npbphs = npbphs;
    }

}
