package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models;

import java.util.List;

public class Lpbph {
    private String message;
    private int id;
    private String created_at;
    private String updated_at;
    private List<LpbphDetail> lpbphs;

    public String getMessage() {
        return message; // Getter untuk pesan
    }


    public int getId() {
        return id;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public List<LpbphDetail> getLpbphs() {
        return lpbphs;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(String createdAt) {
        this.created_at = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updated_at = updatedAt;
    }

    public void setLpbphs(List<LpbphDetail> lpbphs) {
        this.lpbphs = lpbphs;
    }
    public void setMessage(String message) {
        this.message = message; // Setter untuk pesan
    }
}
