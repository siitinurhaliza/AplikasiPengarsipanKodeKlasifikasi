package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters.JenissuratsAdapter;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.Api;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.JenissuratsResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JenisActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private ListView listView;
    private JenissuratsAdapter adapter;
    private SearchView searchView;
    private ImageView voiceSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jenis);

        listView = findViewById(R.id.lv_listview);
        searchView = findViewById(R.id.search_view);
        voiceSearchButton = findViewById(R.id.voice_search_button);

        FloatingActionButton addTask = findViewById(R.id.addtask);
        addTask.setOnClickListener(v -> showCreateSifatDialog());

        ImageView calendarIcon = findViewById(R.id.calendar);
        calendarIcon.setOnClickListener(v -> showCalendarBottomSheet());

        getDataFromApi();
        setupSearchView();
        setupVoiceSearch();
    }

    private void showCalendarBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_calendar, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
        dialog.show();
    }

    private void getDataFromApi() {
        Api api = RetrofitClient.getInstance().getApi();
        Call<JenissuratsResponse> call = api.jenissurats("nama", "keterangan");
        call.enqueue(new Callback<JenissuratsResponse>() {
            @Override
            public void onResponse(Call<JenissuratsResponse> call, Response<JenissuratsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<JenissuratsResponse.Jenissurats> jenissuratsList = response.body().getData();
                    adapter = new JenissuratsAdapter(JenisActivity.this, jenissuratsList);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(JenisActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JenissuratsResponse> call, Throwable t) {
                Toast.makeText(JenisActivity.this, "Failed to load data: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adapter != null) {
                    adapter.getFilter().filter(newText);
                }
                return false;
            }
        });
    }

    private void setupVoiceSearch() {
        voiceSearchButton.setOnClickListener(v -> startVoiceInput());
    }

    private void showCreateSifatDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_jenis, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText namaEditText = dialogView.findViewById(R.id.nama);
        EditText keteranganEditText = dialogView.findViewById(R.id.keterangan);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            String nama = namaEditText.getText().toString();
            String keterangan = keteranganEditText.getText().toString();
            if (nama.isEmpty() || keterangan.isEmpty()) {
                Toast.makeText(JenisActivity.this, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                addJenis(nama, keterangan, dialog);
            }
        });

        dialog.show();
    }

    private void addJenis(String nama, String keterangan, AlertDialog dialog) {
        Api api = RetrofitClient.getInstance().getApi();
        Call<JenissuratsResponse.Jenissurats> call = api.addJenis(nama, keterangan);
        call.enqueue(new Callback<JenissuratsResponse.Jenissurats>() {
            @Override
            public void onResponse(Call<JenissuratsResponse.Jenissurats> call, Response<JenissuratsResponse.Jenissurats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dialog.dismiss();
                    getDataFromApi();
                    showSuccessDialog();
                } else {
                    Toast.makeText(JenisActivity.this, "Gagal menambahkan data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JenissuratsResponse.Jenissurats> call, Throwable t) {
                Toast.makeText(JenisActivity.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button closeButton = dialogView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Your device doesn't support speech input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && result.size() > 0) {
                String recognizedText = result.get(0);
                searchView.setQuery(recognizedText, true);
            }
        }
    }
}
