package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters.LpbphAdapter;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.Api;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.create.CreateLpbphActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.Lpbph;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.LpbphDetail;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LpbphActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_VOICE_INPUT = 100;

    private RecyclerView recyclerView;
    private FloatingActionButton addTask;
    private LpbphAdapter adapter;
    private ImageView calendarIcon;
    private SearchView searchView;
    private ImageView voiceSearchButton;
    private List<Lpbph> lpbphList = new ArrayList<>(); // Menyimpan daftar asli data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpbph);

        Log.d("LpbphActivity", "onCreate: started");

        recyclerView = findViewById(R.id.recyclerView);
        calendarIcon = findViewById(R.id.calendar);
        searchView = findViewById(R.id.search_view);
        voiceSearchButton = findViewById(R.id.voice_search_button);
        addTask = findViewById(R.id.addtask); // FloatingActionButton
        addTask.setOnClickListener(v -> {
            Intent intent = new Intent(LpbphActivity.this, CreateLpbphActivity.class);
            startActivity(intent);
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        voiceSearchButton.setOnClickListener(v -> startVoiceRecognitionActivity());
        calendarIcon.setOnClickListener(v -> showCalendarBottomSheet());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchLpbph();
    }

    private void filterList(String query) {
        List<Lpbph> filteredList = new ArrayList<>();
        for (Lpbph task : lpbphList) {
            for (LpbphDetail detail : task.getLpbphs()) {
                if (detail.getNamaBarang().toLowerCase().contains(query.toLowerCase()) ||
                        detail.getMerk().toLowerCase().contains(query.toLowerCase()) ||
                        detail.getSatuan().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(task);
                    break;
                }
            }
        }
        adapter.updateList(filteredList); // Pastikan method updateList ada di LpbphAdapter
    }

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");
        try {
            startActivityForResult(intent, REQUEST_CODE_VOICE_INPUT);
        } catch (Exception e) {
            Toast.makeText(this, "Voice recognition is not supported on your device.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCalendarBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_calendar, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // Handle date selection
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            Toast.makeText(LpbphActivity.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
            dialog.dismiss(); // Dismiss the dialog when a date is selected
        });

        dialog.show();
    }

    private void fetchLpbph() {
        Api api = RetrofitClient.getInstance().getApi();
        Call<List<Lpbph>> call = api.getLpbph("namaBarang", "merk",10,"satuan",10000);
        call.enqueue(new Callback<List<Lpbph>>() {
            @Override
            public void onResponse(Call<List<Lpbph>> call, Response<List<Lpbph>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("LpbphActivity", "onResponse: success");
                    lpbphList = response.body();
                    for (Lpbph lpbph : lpbphList) {
                        Log.d("LpbphActivity", "ID: " + lpbph.getId());
                        Log.d("LpbphActivity", "Created At: " + lpbph.getCreatedAt());
                        Log.d("LpbphActivity", "Updated At: " + lpbph.getUpdatedAt());
                        for (LpbphDetail detail : lpbph.getLpbphs()) {
                            Log.d("LpbphActivity", "Nama Barang: " + detail.getNamaBarang());
                            Log.d("LpbphActivity", "Merk: " + detail.getMerk());
                        }
                    }
                    adapter = new LpbphAdapter(lpbphList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("LpbphActivity", "onResponse: failed");
                    Toast.makeText(LpbphActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Lpbph>> call, Throwable t) {
                Log.e("LpbphActivity", "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_VOICE_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                searchView.setQuery(result.get(0), true);
            }
        }
    }
}
