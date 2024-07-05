package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters.NpbphAdapter;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.Api;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.Npbph;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BAFragment extends Fragment {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;
    private RecyclerView recyclerView;
    private ImageView calendarIcon;
    private SearchView searchView;
    private ImageView voiceSearchButton;
    private NpbphAdapter npbphAdapter;
    private List<Npbph> npbphList = new ArrayList<>(); // Data untuk ditampilkan
//    private TextView informasiTextView; // Tambahkan TextView untuk informasi
//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_bafragment, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        calendarIcon = view.findViewById(R.id.calendar);
        searchView = view.findViewById(R.id.search_view);
        voiceSearchButton = view.findViewById(R.id.voice_search_button);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        npbphAdapter = new NpbphAdapter(npbphList);
        recyclerView.setAdapter(npbphAdapter);

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

        fetchNpbph();

        return view;
    }

    private void filterList(String query) {
        List<Npbph> filteredList = new ArrayList<>();
        for (Npbph item : npbphList) {
            if (item.getNomor().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        npbphAdapter.updateList(filteredList); // Pastikan method updateList ada di NpbphAdapter
    }

    private void startVoiceRecognitionActivity() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "id-ID");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Katakan sesuatu");
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Pengenalan suara tidak didukung di perangkat Anda.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCalendarBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_calendar, null);
        BottomSheetDialog dialog = new BottomSheetDialog(getContext());
        dialog.setContentView(view);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            // Handle date selection
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            Toast.makeText(getContext(), "Tanggal yang dipilih: " + selectedDate, Toast.LENGTH_SHORT).show();
            dialog.dismiss(); // Menutup dialog saat tanggal dipilih
        });

        dialog.show();
    }

    private void fetchNpbph() {
        Api api = RetrofitClient.getInstance().getApi(); // Mendapatkan instance dari Retrofit API
        Call<List<Npbph>> call = api.getNpbph("namaBarang", "merk", 10, "satuan", 10000); // Mendefinisikan call untuk memanggil API

        call.enqueue(new Callback<List<Npbph>>() {
            @Override
            public void onResponse(Call<List<Npbph>> call, Response<List<Npbph>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("BAFragment", "onResponse: sukses");

                    npbphList.clear(); // Kosongkan list sebelum menambahkan data baru
                    npbphList.addAll(response.body()); // Tambahkan semua data baru ke dalam list
                    npbphAdapter.notifyDataSetChanged(); // Notifikasi adapter bahwa data telah berubah

//                    updateInformasiTextView(); // Perbarui TextView berdasarkan data

                    // Log untuk memeriksa data yang diterima
                    for (Npbph npbph : npbphList) {
                        Log.d("BAFragment", "ID: " + npbph.getId());
                        // Jika perlu, tambahkan log untuk detail data
                    }
                } else {
                    // Menampilkan toast jika request tidak berhasil
                    Log.e("BAFragment", "onResponse: gagal");
                    Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show(); // Menggunakan getContext() atau requireContext()
                }
            }

            @Override
            public void onFailure(Call<List<Npbph>> call, Throwable t) {
                // Menampilkan toast jika terjadi kegagalan saat melakukan request
                Log.e("BAFragment", "onFailure: " + t.getMessage());
                Toast.makeText(getContext(), "Kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show(); // Menggunakan getContext() atau requireContext()
            }
        });
    }

//    private void updateInformasiTextView() {
//        if (npbphList.isEmpty()) {
//            informasiTextView.setText("Tidak ada data");
//            return;
//        }
//
//        long currentTime = System.currentTimeMillis();
//
//        for (Npbph npbph : npbphList) {
//            try {
//                Date createdAtDate = dateFormat.parse(npbph.getCreatedAt());
//                if (createdAtDate != null) {
//                    long timeDiff = currentTime - createdAtDate.getTime();
//                    String informasi = getInformasiWaktu(timeDiff);
//                    informasiTextView.setText(informasi);
//                    break;
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private String getInformasiWaktu(long timeDiff) {
//        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(timeDiff);
//        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(timeDiff);
//        long diffInHours = TimeUnit.MILLISECONDS.toHours(timeDiff);
//        long diffInDays = TimeUnit.MILLISECONDS.toDays(timeDiff);
//
//        if (diffInSeconds < 60) {
//            return "Terbaru";
//        } else if (diffInMinutes < 2) {
//            return "1 menit yang lalu";
//        } else if (diffInMinutes < 5) {
//            return "5 menit yang lalu";
//        } else if (diffInHours < 1) {
//            return diffInMinutes + " menit yang lalu";
//        } else if (diffInHours < 2) {
//            return "1 jam yang lalu";
//        } else if (diffInDays < 1) {
//            return diffInHours + " jam yang lalu";
//        } else if (diffInDays < 2) {
//            return "1 hari yang lalu";
//        } else if (diffInDays < 3) {
//            return "2 hari yang lalu";
//        } else if (diffInDays < 4) {
//            return "Kemarin";
//        } else if (diffInDays < 7) {
//            return diffInDays + " hari yang lalu";
//        } else {
//            return "1 minggu yang lalu";
//        }
//    }
}
