package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.update;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities.SpActivity;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.SpResponse;


import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateSpActivity extends AppCompatActivity {

    private EditText nomorEditText, namaEditText, tanggalEditText, tujuanEditText;
    private Button updateTaskButton;
    private DatePickerDialog datePickerDialog;
    private SpResponse.SpData spData; // Deklarasi spData

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_sp);

        nomorEditText = findViewById(R.id.nomor);
        namaEditText = findViewById(R.id.nama);
        tanggalEditText = findViewById(R.id.tanggal);
        tujuanEditText = findViewById(R.id.tujuan);
        updateTaskButton = findViewById(R.id.addTask);

        tanggalEditText.setOnClickListener(v -> showDatePickerDialog());

        // Cek apakah data diterima untuk editing
        if (getIntent().hasExtra("spData")) {
            spData = (SpResponse.SpData) getIntent().getSerializableExtra("spData");
            populateFields(spData);
        }

        updateTaskButton.setOnClickListener(v -> {
            String nomor = nomorEditText.getText().toString();
            String nama = namaEditText.getText().toString();
            String tanggal = tanggalEditText.getText().toString();
            String tujuan = tujuanEditText.getText().toString();

            if (nomor.isEmpty() || nama.isEmpty() || tanggal.isEmpty() || tujuan.isEmpty()) {
                Toast.makeText(UpdateSpActivity.this, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                updateSp(spData.getId(), nomor, nama, tanggal, tujuan);
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(UpdateSpActivity.this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth);
                    tanggalEditText.setText(selectedDate);
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void updateSp(int id, String nomor, String nama, String tanggal, String tujuan) {
        Call<SpResponse.SpData> call = RetrofitClient.getInstance().getApi().updateSp(id, nama, nomor, tanggal, tujuan);

        call.enqueue(new Callback<SpResponse.SpData>() {
            @Override
            public void onResponse(Call<SpResponse.SpData> call, Response<SpResponse.SpData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    navigateToSpActivity(true); // Menambahkan boolean isSuccess
                } else {
                    Toast.makeText(UpdateSpActivity.this, "Gagal memperbarui tugas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SpResponse.SpData> call, Throwable t) {
                Toast.makeText(UpdateSpActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToSpActivity(boolean isSuccess) {
        Intent intent = new Intent(this, SpActivity.class);
        intent.putExtra("isSuccess", isSuccess); // Mengirimkan boolean isSuccess
        startActivity(intent);
        finish();
    }

    private void populateFields(SpResponse.SpData spData) {
        nomorEditText.setText(spData.getNomor());
        namaEditText.setText(spData.getNama());
        tanggalEditText.setText(spData.getTanggal());
        tujuanEditText.setText(spData.getTujuan());
    }
}

