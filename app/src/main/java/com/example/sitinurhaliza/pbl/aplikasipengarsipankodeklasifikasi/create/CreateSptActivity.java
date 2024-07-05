package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.create;

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

public class CreateSptActivity extends AppCompatActivity {

    private EditText nomorEditText, namaEditText, tanggalEditText, tujuanEditText;
    private Button addTaskButton;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spt);

        nomorEditText = findViewById(R.id.nomor);
        namaEditText = findViewById(R.id.nama);
        tanggalEditText = findViewById(R.id.tanggal);
        tujuanEditText = findViewById(R.id.tujuan);
        addTaskButton = findViewById(R.id.addTask);

        tanggalEditText.setOnClickListener(v -> showDatePickerDialog());

        addTaskButton.setOnClickListener(v -> {
            String nomor = nomorEditText.getText().toString();
            String nama = namaEditText.getText().toString();
            String tanggal = tanggalEditText.getText().toString();
            String tujuan = tujuanEditText.getText().toString();

            if (nomor.isEmpty() || nama.isEmpty() || tanggal.isEmpty() || tujuan.isEmpty()) {
                Toast.makeText(CreateSptActivity.this, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                addSpt(nomor, nama, tanggal, tujuan );
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(CreateSptActivity.this,
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth);
                    tanggalEditText.setText(selectedDate);
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    private void addSpt(String nomor, String nama, String tanggal, String tujuan) {
        Call<SptResponse.SptData> call = RetrofitClient.getInstance().getApi().addSpt(nama, nomor, tanggal, tujuan);

        call.enqueue(new Callback<SptResponse.SptData>() {
            @Override
            public void onResponse(Call<SptResponse.SptData> call, Response<SptResponse.SptData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    navigateToSptActivity(true); // Menambahkan boolean isSuccess
                } else {
                    Toast.makeText(CreateSptActivity.this, "Gagal menambahkan tugas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SptResponse.SptData> call, Throwable t) {
                Toast.makeText(CreateSptActivity.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToSptActivity(boolean isSuccess) {
        Intent intent = new Intent(this, SptActivity.class);
        intent.putExtra("isSuccess", isSuccess); // Mengirimkan boolean isSuccess
        startActivity(intent);
        finish();
    }
}