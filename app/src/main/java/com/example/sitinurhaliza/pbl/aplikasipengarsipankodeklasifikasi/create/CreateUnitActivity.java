package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.create;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities.UnitActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.UnitResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUnitActivity extends AppCompatActivity {

    private EditText namaEditText;
    private Button addTaskButton;
    private UnitResponse.UnitData unitData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_unit);

        namaEditText = findViewById(R.id.nama_unit);
        addTaskButton = findViewById(R.id.saveButton);

        // Cek apakah data diterima untuk editing
        if (getIntent().hasExtra("unitData")) {
            unitData = (UnitResponse.UnitData) getIntent().getSerializableExtra("unitData");
            populateFields(unitData);
        }

        addTaskButton.setOnClickListener(v -> {
            String nama_unit = namaEditText.getText().toString();

            if (nama_unit.isEmpty()) {
                Toast.makeText(CreateUnitActivity.this, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                if (unitData != null) {
                    // Jika sifatData tidak null, berarti ini adalah update
                    updateUnit(unitData.getId(), nama_unit);
                } else {
                    // Jika sifatData null, berarti ini adalah penambahan baru
                    addUnit(nama_unit);
                }
            }
        });
    }

    private void addUnit(String nama_unit){
        Call<UnitResponse.UnitData> call = RetrofitClient.getInstance().getApi().addUnit(nama_unit);

        call.enqueue(new Callback<UnitResponse.UnitData>() {
            @Override
            public void onResponse(Call<UnitResponse.UnitData> call, Response<UnitResponse.UnitData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    navigateToUnitActivity(true); // Menambahkan boolean isSuccess
                } else {
                    Toast.makeText(CreateUnitActivity.this, "Gagal menambahkan tugas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UnitResponse.UnitData> call, Throwable t) {
                Toast.makeText(CreateUnitActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUnit(int id, String nama_unit) {
        Call<UnitResponse.UnitData> call = RetrofitClient.getInstance().getApi().updateUnit(id, nama_unit);

        call.enqueue(new Callback<UnitResponse.UnitData>() {
            @Override
            public void onResponse(Call<UnitResponse.UnitData> call, Response<UnitResponse.UnitData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    navigateToUnitActivity(true); // Menambahkan boolean isSuccess
                } else {
                    Toast.makeText(CreateUnitActivity.this, "Gagal memperbarui tugas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UnitResponse.UnitData> call, Throwable t) {
                Toast.makeText(CreateUnitActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void navigateToUnitActivity(boolean isSuccess) {
        Intent intent = new Intent(this, UnitActivity.class); // Pastikan ini adalah aktivitas tujuan yang benar
        intent.putExtra("isSuccess", isSuccess);
        startActivity(intent);
        finish();
    }

    private void populateFields(UnitResponse.UnitData unitData) {
        namaEditText.setText(unitData.getNama());
    }
}
