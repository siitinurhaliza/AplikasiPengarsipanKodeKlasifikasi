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
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities.SptActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.SptResponse;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateSptActivity extends AppCompatActivity {

    private EditText nomorEditText, namaEditText, tanggalEditText, tujuanEditText;
    private Button updateTaskButton;
    private DatePickerDialog datePickerDialog;
    private SptResponse.SptData sptData; // Deklarasi sptData

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_spt);

        nomorEditText = findViewById(R.id.nomor);
        namaEditText = findViewById(R.id.nama);
        tanggalEditText = findViewById(R.id.tanggal);
        tujuanEditText = findViewById(R.id.tujuan);
        updateTaskButton = findViewById(R.id.addTask);

        tanggalEditText.setOnClickListener(v -> showDatePickerDialog());

        // Cek apakah data diterima untuk editing
        if (getIntent().hasExtra("sptData")) {
            sptData = (SptResponse.SptData) getIntent().getSerializableExtra("sptData");
            populateFields(sptData);
        }

        updateTaskButton.setOnClickListener(v -> {
            String nomor = nomorEditText.getText().toString();
            String nama = namaEditText.getText().toString();
            String tanggal = tanggalEditText.getText().toString();
            String tujuan = tujuanEditText.getText().toString();

            if (nomor.isEmpty() || nama.isEmpty() || tanggal.isEmpty() || tujuan.isEmpty()) {
                Toast.makeText(UpdateSptActivity.this, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                updateSpt(sptData.getId(), nomor, nama, tanggal, tujuan);
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(UpdateSptActivity.this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth);
                    tanggalEditText.setText(selectedDate);
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void updateSpt(int id, String nomor, String nama, String tanggal, String tujuan) {
        Call<SptResponse.SptData> call = RetrofitClient.getInstance().getApi().updateSpt(id, nama, nomor, tanggal, tujuan);

        call.enqueue(new Callback<SptResponse.SptData>() {
            @Override
            public void onResponse(Call<SptResponse.SptData> call, Response<SptResponse.SptData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    navigateToSptActivity(true); // Menambahkan boolean isSuccess
                } else {
                    Toast.makeText(UpdateSptActivity.this, "Gagal memperbarui tugas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SptResponse.SptData> call, Throwable t) {
                Toast.makeText(UpdateSptActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToSptActivity(boolean isSuccess) {
        Intent intent = new Intent(this, SptActivity.class);
        intent.putExtra("isSuccess", isSuccess); // Mengirimkan boolean isSuccess
        startActivity(intent);
        finish();
    }

    private void populateFields(SptResponse.SptData sptData) {
        nomorEditText.setText(sptData.getNomor());
        namaEditText.setText(sptData.getNama());
        tanggalEditText.setText(sptData.getTanggal());
        tujuanEditText.setText(sptData.getTujuan());
    }
}

