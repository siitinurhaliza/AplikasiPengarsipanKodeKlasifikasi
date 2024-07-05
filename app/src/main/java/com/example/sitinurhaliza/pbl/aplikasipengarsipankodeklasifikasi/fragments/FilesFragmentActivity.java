package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters.KlasifikasiAdapter;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.Api;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.KlasifikasiResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilesFragmentActivity extends Fragment {

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private ListView listView;

    private ImageView calendarIcon;
    private SearchView searchView;
    private ImageView voiceSearchButton;
    private FloatingActionButton addTask;
    private KlasifikasiAdapter adapter;
    private List<KlasifikasiResponse.Klasifikasi> klasifikasiList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_files_fragment, container, false);

        // Initialize UI elements
        listView = view.findViewById(R.id.lv_listview);
        searchView = view.findViewById(R.id.search_view);
        voiceSearchButton = view.findViewById(R.id.voice_search_button);
        calendarIcon = view.findViewById(R.id.calendar);
        addTask = view.findViewById(R.id.addtask);

        if (listView == null || searchView == null || voiceSearchButton == null || calendarIcon == null) {
            Log.e("FilesFragment", "UI elements initialization failed.");
            return view; // Return early if initialization fails
        }

        addTask.setOnClickListener(v -> showCreateKlasifikasiDialog());

        // Set up event handlers and load data
        getDataFromApi();
        setupSearchView();
        setupVoiceSearch();
        setupCalendar();

        return view;
    }

    private void saveLatestCodeAndPerihal(String latestCode, String latestPerihal) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("latest_code_pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("latest_code", latestCode);
        editor.putString("latest_perihal", latestPerihal);
        editor.apply();
    }

    private void showCreateKlasifikasiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_create_klasifikasi, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText kodeEditText = dialogView.findViewById(R.id.kode);
        EditText perihalEditText = dialogView.findViewById(R.id.perihal);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            String kode = kodeEditText.getText().toString();
            String perihal = perihalEditText.getText().toString();
            if (kode.isEmpty() || perihal.isEmpty()) {
                Toast.makeText(getContext(), "Semua bidang harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                addKlasifikasi(kode, perihal, dialog);
            }
        });

        dialog.show();
    }

    private void addKlasifikasi(String kode, String perihal, AlertDialog dialog) {
        Call<KlasifikasiResponse.Klasifikasi> call = RetrofitClient.getInstance().getApi().addKlasifikasi(kode, perihal);
        call.enqueue(new Callback<KlasifikasiResponse.Klasifikasi>() {
            @Override
            public void onResponse(@NonNull Call<KlasifikasiResponse.Klasifikasi> call, @NonNull Response<KlasifikasiResponse.Klasifikasi> response) {
                if (response.isSuccessful() && response.body() != null) {
                    saveLatestCodeAndPerihal(kode, perihal); // Save the latest code and perihal
                    dialog.dismiss();
                    getDataFromApi();
                    showSuccessDialog();
                } else {
                    Toast.makeText(getContext(), "Gagal menambahkan data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<KlasifikasiResponse.Klasifikasi> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        Button closeButton = dialogView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void setupCalendar() {
        calendarIcon.setOnClickListener(v -> showCalendarBottomSheet());
    }

    private void showCalendarBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_calendar, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(view);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // Handle date selection
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            Toast.makeText(getContext(), "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
            dialog.dismiss(); // Dismiss the dialog when a date is selected
        });

        dialog.show();
    }

    private void getDataFromApi() {
        Api api = RetrofitClient.getInstance().getApi();
        Call<KlasifikasiResponse> call = api.klasifikasi("kode", "perihal"); // Updated API call method
        call.enqueue(new Callback<KlasifikasiResponse>() {
            @Override
            public void onResponse(@NonNull Call<KlasifikasiResponse> call, @NonNull Response<KlasifikasiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    klasifikasiList = response.body().getData();
                    adapter = new KlasifikasiAdapter(getContext(), klasifikasiList);
                    listView.setAdapter(adapter);
                    setupClickListeners();
                    Log.d("API_SUCCESS", "Data loaded successfully");
                } else {
                    Log.e("API_ERROR", "Response not successful or body is null");
                    Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<KlasifikasiResponse> call, @NonNull Throwable t) {
                Log.e("API_FAILURE", "Failed to load data", t);
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
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

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say something");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Your device doesn't support speech input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == requireActivity().RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && result.size() > 0) {
                String recognizedText = result.get(0);
                searchView.setQuery(recognizedText, true);
            }
        }
    }

    private void setupClickListeners() {
        listView.setOnItemClickListener((parent, view, position, id) -> {
            TextView tvKode = view.findViewById(R.id.tv_kode);
            TextView tvPerihal = view.findViewById(R.id.tv_perihal);

            if (tvKode != null && tvPerihal != null) {
                tvKode.setOnClickListener(v -> {
                    if (tvPerihal.getVisibility() == View.GONE) {
                        tvPerihal.setVisibility(View.VISIBLE);
                    } else {
                        tvPerihal.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}
