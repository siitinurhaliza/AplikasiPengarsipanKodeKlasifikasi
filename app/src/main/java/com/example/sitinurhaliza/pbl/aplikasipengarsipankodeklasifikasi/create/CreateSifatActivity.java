package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.create;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities.SifatActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.SifatResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateSifatActivity extends AppCompatActivity {

    private EditText namaEditText;
    private Button addTaskButton;
    private SifatResponse.SifatData sifatData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sifat);

        namaEditText = findViewById(R.id.nama);
        addTaskButton = findViewById(R.id.saveButton);

        // Cek apakah data diterima untuk editing
        if (getIntent().hasExtra("sifatData")) {
            sifatData = (SifatResponse.SifatData) getIntent().getSerializableExtra("sifatData");
            populateFields(sifatData);
        }

        addTaskButton.setOnClickListener(v -> {
            String nama = namaEditText.getText().toString();

            if (nama.isEmpty()) {
                Toast.makeText(CreateSifatActivity.this, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                if (sifatData != null) {
                    // Jika sifatData tidak null, berarti ini adalah update
                    updateSifat(sifatData.getId(), nama);
                } else {
                    // Jika sifatData null, berarti ini adalah penambahan baru
                    addSifat(nama);
                }
            }
        });
    }

    private void addSifat(String nama){
        Call<SifatResponse.SifatData> call = RetrofitClient.getInstance().getApi().addSifat(nama);

        call.enqueue(new Callback<SifatResponse.SifatData>() {
            @Override
            public void onResponse(Call<SifatResponse.SifatData> call, Response<SifatResponse.SifatData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    navigateToSifatActivity(true); // Menambahkan boolean isSuccess
                } else {
                    Toast.makeText(CreateSifatActivity.this, "Gagal menambahkan tugas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SifatResponse.SifatData> call, Throwable t) {
                Toast.makeText(CreateSifatActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSifat(int id, String nama) {
        Call<SifatResponse.SifatData> call = RetrofitClient.getInstance().getApi().updateSifat(id, nama);

        call.enqueue(new Callback<SifatResponse.SifatData>() {
            @Override
            public void onResponse(Call<SifatResponse.SifatData> call, Response<SifatResponse.SifatData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    navigateToSifatActivity(true); // Menambahkan boolean isSuccess
                } else {
                    Toast.makeText(CreateSifatActivity.this, "Gagal memperbarui tugas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SifatResponse.SifatData> call, Throwable t) {
                Toast.makeText(CreateSifatActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void navigateToSifatActivity(boolean isSuccess) {
        Intent intent = new Intent(this, SifatActivity.class); // Pastikan ini adalah aktivitas tujuan yang benar
        intent.putExtra("isSuccess", isSuccess);
        startActivity(intent);
        finish();
    }

    private void populateFields(SifatResponse.SifatData sifatData) {
        namaEditText.setText(sifatData.getNama());
    }
}
