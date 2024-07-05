package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.create;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;

public class LeCreateActivity extends AppCompatActivity {

    private EditText editTextNamaBarang, editTextMerk, editTextVol, editTextSatuan, editTextHargaSatuan, editTextJumlah;
    private Button buttonSaveLembar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembar_create);

        editTextNamaBarang = findViewById(R.id.editTextNamaBarang);
        editTextMerk = findViewById(R.id.editTextMerk);
        editTextVol = findViewById(R.id.editTextVol);
        editTextSatuan = findViewById(R.id.editTextSatuan);
        editTextHargaSatuan = findViewById(R.id.editTextHargaSatuan);
        editTextJumlah = findViewById(R.id.editTextJumlah);
        buttonSaveLembar = findViewById(R.id.buttonSaveLembar);

//        buttonSaveLembar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                saveLembarData();
//            }
//        });
//    }

//    private void saveLembarData() {
//        String namaBarang = editTextNamaBarang.getText().toString();
//        String merk = editTextMerk.getText().toString();
//        int vol = Integer.parseInt(editTextVol.getText().toString());
//        String satuan = editTextSatuan.getText().toString();
//        int hargaSatuan = Integer.parseInt(editTextHargaSatuan.getText().toString());
//        int jumlah = Integer.parseInt(editTextJumlah.getText().toString());
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.121.19:8080/api/") // Ganti dengan URL API Anda
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        Api api = retrofit.create(Api.class);
//        Call<LembarResponse.LembarData> call = api.createLembar("namaBarang", "merk", 0, satuan, hargaSatuan, jumlah);
//
//        call.enqueue(new Callback<LembarResponse.LembarData>() {
//            @Override
//            public void onResponse(Call<LembarResponse.LembarData> call, Response<LembarResponse.LembarData> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    Toast.makeText(LembarCreateActivity.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
//                    finish(); // Kembali ke halaman sebelumnya
//                } else {
//                    Toast.makeText(LembarCreateActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<LembarResponse.LembarData> call, Throwable t) {
//                Toast.makeText(LembarCreateActivity.this, "Kesalahan jaringan", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    }
}
