// PrintLpbphActivity.java
package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities;

import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.Api;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrintLpbphActivity extends AppCompatActivity {

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_lpbph);

        api = RetrofitClient.getInstance().getApi();

//        int lpbphId = getIntent().getIntExtra("lpbph_id", -1);
//        if (lpbphId != -1) {
//            printLpbph(lpbphId);
//        } else {
//            Toast.makeText(this, "Invalid LPBPH ID", Toast.LENGTH_SHORT).show();
//            finish();
//        }
    }

//    private void printLpbph(int id) {
//        Call<ResponseBody> call = api.getLpbphPdf(id);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    try {
//                        String base64Pdf = response.body().string();
//                        byte[] pdfAsBytes = Base64.decode(base64Pdf, Base64.DEFAULT);
//
//                        // Save or display the PDF file as needed
//                        // ...
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    Toast.makeText(PrintLpbphActivity.this, "Failed to print data", Toast.LENGTH_SHORT).show();
//                }
//            }

//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(PrintLpbphActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}
