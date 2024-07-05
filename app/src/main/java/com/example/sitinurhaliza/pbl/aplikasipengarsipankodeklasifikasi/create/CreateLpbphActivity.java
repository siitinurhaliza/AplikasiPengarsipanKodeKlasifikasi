package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.create;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities.LpbphActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.Api;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.Lpbph;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateLpbphActivity extends AppCompatActivity {

    private LinearLayout container;
    private Button tambahBaris, simpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpbhp_create);

        container = findViewById(R.id.container);
        tambahBaris = findViewById(R.id.tambah_baris);
        simpan = findViewById(R.id.simpan);

        tambahBaris.setOnClickListener(v -> tambahBaris());
        simpan.setOnClickListener(v -> simpanData());
    }

    private void tambahBaris() {
        View newRow = LayoutInflater.from(this).inflate(R.layout.item_barang, container, false);
        Button hapusBaris = newRow.findViewById(R.id.hapus_baris);
        hapusBaris.setOnClickListener(v -> container.removeView(newRow));
        container.addView(newRow);
    }

    private void simpanData() {
        for (int i = 0; i < container.getChildCount(); i++) {
            View row = container.getChildAt(i);

            EditText namaBarang = row.findViewById(R.id.nama_barang);
            EditText merk = row.findViewById(R.id.merk);
            EditText vol = row.findViewById(R.id.vol);
            EditText satuan = row.findViewById(R.id.satuan);
            EditText hargaSatuan = row.findViewById(R.id.harga_satuan);

            if (namaBarang == null || merk == null || vol == null || satuan == null || hargaSatuan == null) {
                Toast.makeText(this, "Kesalahan internal: field tidak ditemukan", Toast.LENGTH_SHORT).show();
                return;
            }

            String namaBarangText = namaBarang.getText().toString().trim();
            String merkText = merk.getText().toString().trim();
            String volText = vol.getText().toString().trim();
            String satuanText = satuan.getText().toString().trim();
            String hargaSatuanText = hargaSatuan.getText().toString().trim();

            if (namaBarangText.isEmpty() || merkText.isEmpty() || volText.isEmpty() || satuanText.isEmpty() || hargaSatuanText.isEmpty()) {
                Toast.makeText(this, "Pastikan semua field detail terisi", Toast.LENGTH_SHORT).show();
                return;
            }

            int volume;
            int hargaSatuanValue;
            try {
                volume = Integer.parseInt(volText);
                hargaSatuanValue = Integer.parseInt(hargaSatuanText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Pastikan Quantitas dan Harga Satuan berupa angka", Toast.LENGTH_SHORT).show();
                return;
            }



            // Kirim data ke server
            Api api = RetrofitClient.getInstance().getApi();
            Call<Lpbph> call = api.createLpbph(namaBarangText, merkText, volume, satuanText, hargaSatuanValue);
            call.enqueue(new Callback<Lpbph>() {
                @Override
                public void onResponse(Call<Lpbph> call, Response<Lpbph> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Lpbph lpbphResponse = response.body();
                            Toast.makeText(CreateLpbphActivity.this, lpbphResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            navigateToLpbhpActivity(true);
                        } else {
                            Toast.makeText(CreateLpbphActivity.this, "Gagal menyimpan data: Respons kosong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            // Log respons mentah untuk debugging
                            String errorBody = response.errorBody().string();
                            Log.e("LpbphCreateActivity", "Error body: " + errorBody);
                            Toast.makeText(CreateLpbphActivity.this, "Gagal menyimpan data: " + errorBody, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(CreateLpbphActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Lpbph> call, Throwable t) {
                    Toast.makeText(CreateLpbphActivity.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void navigateToLpbhpActivity(boolean isSuccess) {
        Intent intent = new Intent(this, LpbphActivity.class);
        intent.putExtra("isSuccess", isSuccess);
        startActivity(intent);
        finish();
    }
}
