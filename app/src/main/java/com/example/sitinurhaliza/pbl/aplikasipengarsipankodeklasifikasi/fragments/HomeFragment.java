package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities.FragmentActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities.JenisActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities.LpbphActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities.MediaActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities.SifatActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities.SpActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities.SptActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.activities.UnitActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters.SptAdapter;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.SptResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ImageView gambar;
    private ImageView gambar1;
    private TextView detailspt;
    private SptAdapter sptAdapter;
    private RecyclerView taskRecycler;
    private List<SptResponse.SptData> sptList = new ArrayList<>();
    private TextView kodeTextView;
    private TextView perihalTextView;
    private ImageView calendarIcon;

    private ArrayList<String> searchableContent = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        LinearLayout jenis = view.findViewById(R.id.jenis);
        LinearLayout unit = view.findViewById(R.id.unit);
        LinearLayout web = view.findViewById(R.id.website);
        LinearLayout sp = view.findViewById(R.id.sp);
        LinearLayout spt = view.findViewById(R.id.spt);
        LinearLayout lembar = view.findViewById(R.id.lembar);
        detailspt  = view.findViewById(R.id.spt1);
        gambar1 = view.findViewById(R.id.klasifikasi);
        gambar = view.findViewById(R.id.sifat);
        kodeTextView = view.findViewById(R.id.kode);
        perihalTextView = view.findViewById(R.id.perihal);
        calendarIcon = view.findViewById(R.id.calender);
        taskRecycler = view.findViewById(R.id.RecyclerView); // Sesuaikan ID dengan XML

        calendarIcon.setOnClickListener(v -> showCalendarBottomSheet());
        taskRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        // Menggunakan getContext()
        fetchData();

        jenis.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), JenisActivity.class);
            startActivity(intent);
        });

        unit.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UnitActivity.class);
            startActivity(intent);
        });
        lembar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LpbphActivity.class);
            startActivity(intent);
        });

//        web.setOnClickListener(v -> {
//            Intent intent = new Intent(
//                    Intent.ACTION_VIEW,
//                    Uri.parse("https://haicoding.com/courses/mobile-programming/lesson/konfigurasi-android-virtual-device/")
//            );
//            startActivity(intent);
//        });

        web.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MediaActivity.class);
            startActivity(intent);
        });

        gambar.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SifatActivity.class);
            startActivity(intent);
        });

        sp.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SpActivity.class);
            startActivity(intent);
        });

        spt.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SptActivity.class);
            startActivity(intent);
        });

        detailspt.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SptActivity.class);
            startActivity(intent);
        });

        if (gambar1 == null) {
            Log.e("HomeFragment", "gambar1 initialization failed.");
            return view; // Return early if initialization fails
        }

        gambar1.setOnClickListener(v -> {
            if (getActivity() instanceof FragmentActivity) {
                ((FragmentActivity) getActivity()).tampilFilesFragment();
            }
        });

        // Set the latest code and perihal to the TextViews
        if (kodeTextView != null && perihalTextView != null) {
            String latestCode = getLatestCode();
            String latestPerihal = getLatestPerihal();
            if (latestCode != null) {
                kodeTextView.setText(latestCode);
            }
            if (latestPerihal != null) {
                perihalTextView.setText(latestPerihal);
            }
        }

        return view;
    }

    private void showCalendarBottomSheet() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_calendar, null);
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        dialog.setContentView(view);

        CalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            Toast.makeText(getContext(), "Selected Date: " + selectedDate, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void fetchData() {
        Call<SptResponse> call = RetrofitClient.getInstance().getApi().getSpt(null, null, null, null);
        call.enqueue(new Callback<SptResponse>() {
            @Override
            public void onResponse(Call<SptResponse> call, Response<SptResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sptList = response.body().getData();
                    if (sptList.isEmpty()) {
                        taskRecycler.setVisibility(View.GONE);
                    } else {
                        taskRecycler.setVisibility(View.VISIBLE);
                        sptAdapter = new SptAdapter(getContext(), sptList);
                        taskRecycler.setAdapter(sptAdapter);
                    }
                } else {
                    Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SptResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getLatestCode() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("latest_code_pref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("latest_code", "No Code Available");
    }

    private String getLatestPerihal() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("latest_code_pref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("latest_perihal", "No Perihal Available");
    }
}
