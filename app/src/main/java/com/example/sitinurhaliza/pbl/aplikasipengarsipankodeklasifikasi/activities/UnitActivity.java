package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters.UnitAdapter;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.UnitResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnitActivity extends AppCompatActivity implements UnitAdapter.UnitItemClickListener {

    private static final int REQUEST_CODE_VOICE_INPUT = 100;
    private RecyclerView taskRecycler;
    private UnitAdapter unitAdapter;
    private Context context;
    private List<UnitResponse.UnitData> unitList = new ArrayList<>();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        taskRecycler = findViewById(R.id.RecyclerView);
        taskRecycler.setLayoutManager(new LinearLayoutManager(this));
        unitAdapter = new UnitAdapter(this, unitList, this);
        taskRecycler.setAdapter(unitAdapter);

        fetchData();

        FloatingActionButton addTask = findViewById(R.id.addtask);
        addTask.setOnClickListener(v -> showCreateUnitDialog());

        ImageView calendarIcon = findViewById(R.id.calendar);
        calendarIcon.setOnClickListener(v -> showCalendarBottomSheet());

        searchView = findViewById(R.id.search_view);
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

        ImageView voiceSearchButton = findViewById(R.id.voice_search_button);
        voiceSearchButton.setOnClickListener(v -> startVoiceRecognitionActivity());
    }

    private void fetchData() {
        Call<UnitResponse> call = RetrofitClient.getInstance().getApi().getUnit("nama_unit");
        call.enqueue(new Callback<UnitResponse>() {
            @Override
            public void onResponse(Call<UnitResponse> call, Response<UnitResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    unitList = response.body().getData();
                    if (unitList.isEmpty()) {
                        taskRecycler.setVisibility(View.GONE);
                    } else {
                        taskRecycler.setVisibility(View.VISIBLE);
                        unitAdapter.updateList(unitList);
                    }
                } else {
                    Toast.makeText(UnitActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UnitResponse> call, Throwable t) {
                Toast.makeText(UnitActivity.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterList(String query) {
        List<UnitResponse.UnitData> filteredList = new ArrayList<>();
        for (UnitResponse.UnitData unit : unitList) {
            if (unit.getNama().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(unit);
            }
        }
        unitAdapter.updateList(filteredList);
    }

    private void showCalendarBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_calendar, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);
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
                // Menggunakan teks hasil pengenalan suara sebagai query pencarian
                searchView.setQuery(result.get(0), true);
            }
        }
    }

    private void showCreateUnitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.activity_create_unit, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText namaEditText = dialogView.findViewById(R.id.nama_unit);
        Button saveButton = dialogView.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(v -> {
            String nama_unit = namaEditText.getText().toString();
            if (nama_unit.isEmpty()) {
                Toast.makeText(UnitActivity.this, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                addUnit(nama_unit, dialog);
            }
        });

        dialog.show();
    }

    private void addUnit(String nama_unit, AlertDialog dialog) {
        Call<UnitResponse.UnitData> call = RetrofitClient.getInstance().getApi().addUnit(nama_unit);
        call.enqueue(new Callback<UnitResponse.UnitData>() {
            @Override
            public void onResponse(Call<UnitResponse.UnitData> call, Response<UnitResponse.UnitData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dialog.dismiss();
                    fetchData();
                    showSuccessDialog();
                } else {
                    Toast.makeText(UnitActivity.this, "Gagal menambahkan data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UnitResponse.UnitData> call, Throwable t) {
                Toast.makeText(UnitActivity.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onUpdateClick(UnitResponse.UnitData unitData) {
        showUpdateUnitDialog(unitData);
    }

    private void showUpdateUnitDialog(UnitResponse.UnitData unitData) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_update_unit, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        EditText namaEditText = dialogView.findViewById(R.id.nama);
        Button updateButton = dialogView.findViewById(R.id.saveButton);

        namaEditText.setText(unitData.getNama());

        updateButton.setOnClickListener(v -> {
            String updatedNama = namaEditText.getText().toString();
            if (updatedNama.isEmpty()) {
                Toast.makeText(UnitActivity.this, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                updateUnit(unitData.getId(), updatedNama, dialog);
            }
        });

        dialog.show();
    }

    private void updateUnit(int id, String updatedNama, AlertDialog dialog) {
        Call<UnitResponse.UnitData> call = RetrofitClient.getInstance().getApi().updateUnit(id, updatedNama);
        call.enqueue(new Callback<UnitResponse.UnitData>() {
            @Override
            public void onResponse(Call<UnitResponse.UnitData> call, Response<UnitResponse.UnitData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dialog.dismiss();
                    fetchData();
                    showSuccessDialog();
                } else {
                    Toast.makeText(UnitActivity.this, "Gagal memperbarui data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UnitResponse.UnitData> call, Throwable t) {
                Toast.makeText(UnitActivity.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onDeleteClick(UnitResponse.UnitData unitData) {
        confirmDelete(unitData);
    }

    private void confirmDelete(UnitResponse.UnitData unitData) {
        // Inflate custom dialog layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_hapus, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        // Get buttons from dialog
        Button buttonCancel = dialogView.findViewById(R.id.button_cancel);
        Button buttonConfirm = dialogView.findViewById(R.id.button_confirm);

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        buttonConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            deleteUnit(unitData);
        });

        dialog.show();
    }

    private void deleteUnit(UnitResponse.UnitData unitData) {
        Call<Void> call = RetrofitClient.getInstance().getApi().deleteUnit(unitData.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    unitList.remove(unitData);
                    unitAdapter.notifyDataSetChanged();
                    showDeleteSuccessDialog();
                } else {
                    Toast.makeText(UnitActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UnitActivity.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteSuccessDialog() {
        // Inflate custom success dialog layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_hapus_sukses, null);
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        // Get the OK button from dialog
        Button buttonOk = dialogView.findViewById(R.id.button_ok);

        buttonOk.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
