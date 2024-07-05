package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters.SpAdapter;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.create.CreateSpActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.SpResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_VOICE_INPUT = 100;
    private RecyclerView taskRecycler;
    private FloatingActionButton addTask;

    private SpAdapter spAdapter;
    private ImageView calendarIcon;
    private SearchView searchView;
    private ImageView voiceSearchButton;
    private List<SpResponse.SpData> spList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp);

        taskRecycler = findViewById(R.id.RecyclerView); // Sesuaikan ID dengan XML
        addTask = findViewById(R.id.addtask); // FloatingActionButton
        calendarIcon = findViewById(R.id.calendar);
        searchView = findViewById(R.id.search_view);
        voiceSearchButton = findViewById(R.id.voice_search_button);

        taskRecycler.setLayoutManager(new LinearLayoutManager(this));
        fetchData();

        addTask.setOnClickListener(v -> {
            Intent intent = new Intent(SpActivity.this, CreateSpActivity.class);
            startActivity(intent);
        });

        calendarIcon.setOnClickListener(v -> showCalendarBottomSheet());

        boolean isSuccess = getIntent().getBooleanExtra("isSuccess", false);
        if (isSuccess) {
            showSuccessDialog();
        }

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
    }

    private void fetchData() {
        Call<SpResponse> call = RetrofitClient.getInstance().getApi().getSp(null, null, null, null);
        call.enqueue(new Callback<SpResponse>() {
            @Override
            public void onResponse(Call<SpResponse> call, Response<SpResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    spList = response.body().getData();
                    if (spList.isEmpty()) {
                        taskRecycler.setVisibility(View.GONE);
                    } else {
                        taskRecycler.setVisibility(View.VISIBLE);
                        spAdapter = new SpAdapter(SpActivity.this, spList);
                        taskRecycler.setAdapter(spAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<SpResponse> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void filterList(String query) {
        List<SpResponse.SpData> filteredList = new ArrayList<>();
        for (SpResponse.SpData task : spList) {
            if (task.getNama().toLowerCase().contains(query.toLowerCase()) ||
                    task.getNomor().toLowerCase().contains(query.toLowerCase()) ||
                    task.getTujuan().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(task);
            }
        }
        spAdapter.updateList(filteredList);
    }

    private void showCalendarBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_calendar, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // Handle date selection
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            Toast.makeText(SpActivity.this, "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
            dialog.dismiss(); // Dismiss the dialog when a date is selected
        });

        dialog.show();
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button closeButton = dialogView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
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
