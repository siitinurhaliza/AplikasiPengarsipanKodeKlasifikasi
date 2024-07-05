package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters.NpbphAdapter;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.Api;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.Npbph;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.NpbphDetail;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NpbphActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NpbphAdapter adapter;
    private List<Npbph> npbphList = new ArrayList<>(); // Menyimpan daftar data Npbph

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_npbhp);

        Log.d("NpbphActivity", "onCreate: started");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchNpbph(); // Memanggil method untuk mengambil data Npbph dari server
    }

    private void fetchNpbph() {
        Api api = RetrofitClient.getInstance().getApi(); // Mendapatkan instance dari Retrofit API
        Call<List<Npbph>> call = api.getNpbph("namaBarang", "merk", 10, "satuan", 10000); // Mendefinisikan call untuk memanggil API

        call.enqueue(new Callback<List<Npbph>>() {
            @Override
            public void onResponse(Call<List<Npbph>> call, Response<List<Npbph>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("NpbphActivity", "onResponse: success");

                    npbphList = response.body(); // Menyimpan response body (list Npbph) ke dalam npbphList

                    // Log untuk memeriksa data yang diterima
                    for (Npbph npbph : npbphList) {
                        Log.d("NpbphActivity", "ID: " + npbph.getId());
                        for (NpbphDetail detail : npbph.getNpbphs()) {
                            Log.d("NpbphActivity", "Nama Barang: " + detail.getNamaBarang());
                            Log.d("NpbphActivity", "Merk: " + detail.getMerk());
                        }
                    }

                    // Menginisialisasi adapter dan mengatur RecyclerView
                    adapter = new NpbphAdapter(npbphList);
                    recyclerView.setAdapter(adapter);
                } else {
                    // Menampilkan toast jika request tidak berhasil
                    Log.e("NpbphActivity", "onResponse: failed");
                    Toast.makeText(NpbphActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Npbph>> call, Throwable t) {
                // Menampilkan toast jika terjadi failure saat melakukan request
                Log.e("NpbphActivity", "onFailure: " + t.getMessage());
                Toast.makeText(NpbphActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
