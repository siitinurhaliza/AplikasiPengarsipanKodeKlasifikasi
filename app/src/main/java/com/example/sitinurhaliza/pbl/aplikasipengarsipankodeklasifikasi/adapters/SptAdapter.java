package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.create.CreateSpActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.create.CreateSptActivity;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.SpResponse;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.SptResponse;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.update.UpdateSptActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SptAdapter extends RecyclerView.Adapter<SptAdapter.SptViewHolder> {

    private List<SptResponse.SptData> sptList;
    private Context context;

    public SptAdapter(Context context, List<SptResponse.SptData> tasks) {
        this.context = context;
        this.sptList = tasks;
    }

    @NonNull
    @Override
    public SptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemsp, parent, false);
        return new SptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SptViewHolder holder, int position) {
        SptResponse.SptData sptData = sptList.get(position);
        holder.bind(sptData);
    }

    @Override
    public int getItemCount() {
        return sptList.size();
    }

    // Method untuk mengupdate daftar tugas
    public void updateList(List<SptResponse.SptData> newList) {
        sptList = newList;
        notifyDataSetChanged();
    }

    public class SptViewHolder extends RecyclerView.ViewHolder {
        private TextView day, date, month, nomor, nama, tujuan;
        private ImageView ivMore;

        public SptViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            month = itemView.findViewById(R.id.month);
            nomor = itemView.findViewById(R.id.nomor);
            nama = itemView.findViewById(R.id.nama);
            tujuan = itemView.findViewById(R.id.tujuan);
            ivMore = itemView.findViewById(R.id.options);
        }

        public void bind(SptResponse.SptData sptData) {
            String tanggal = sptData.getTanggal();
            day.setText(getDay(tanggal));
            date.setText(getDate(tanggal));
            month.setText(getMonth(tanggal));
            nomor.setText(sptData.getNomor());
            nama.setText(sptData.getNama());
            tujuan.setText(sptData.getTujuan());

            ivMore.setOnClickListener(v -> showOptionsDialog(sptData));
        }

        private void showOptionsDialog(SptResponse.SptData sptData) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Select Option")
                    .setItems(new String[]{"Delete", "Update", "Detail"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    confirmDelete(sptData);
                                    break;
                                case 1:
                                    editSptData(sptData);
                                    break;
                                case 2:
                                    showDetailDialog(sptData);
                                    break;
                            }
                        }
                    });
            builder.show();
        }

        private void confirmDelete(SptResponse.SptData sptData) {
            // Inflate custom dialog layout
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_hapus, null);
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(dialogView)
                    .create();

            // Get buttons from dialog
            Button buttonCancel = dialogView.findViewById(R.id.button_cancel);
            Button buttonConfirm = dialogView.findViewById(R.id.button_confirm);

            buttonCancel.setOnClickListener(v -> dialog.dismiss());

            buttonConfirm.setOnClickListener(v -> {
                dialog.dismiss();
                deleteSptData(sptData);
            });

            dialog.show();
        }

        private void deleteSptData(SptResponse.SptData sptData) {
            Call<Void> call = RetrofitClient.getInstance().getApi().deleteSpt(sptData.getId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        sptList.remove(sptData);
                        notifyDataSetChanged();
                        showSuccessDialog();
                    } else {
                        Toast.makeText(context, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void showSuccessDialog() {
            // Inflate custom success dialog layout
            View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_hapus_sukses, null);
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(dialogView)
                    .create();

            // Get the OK button from dialog
            Button buttonOk = dialogView.findViewById(R.id.button_ok);

            buttonOk.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        }

        private void editSptData(SptResponse.SptData sptData) {
            Intent intent = new Intent(context, UpdateSptActivity.class);
            intent.putExtra("sptData", sptData);
            context.startActivity(intent);
        }

        private void showDetailDialog(SptResponse.SptData sptData) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Detail Data")
                    .setMessage("Nomor: " + sptData.getNomor() + "\n" +
                            "Nama: " + sptData.getNama() + "\n" +
                            "Tanggal: " + sptData.getTanggal() + "\n" +
                            "Tujuan: " + sptData.getTujuan())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.show();
        }

        private String getDay(String tanggal) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());
            try {
                Date date = inputFormat.parse(tanggal);
                return dayFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return "";
            }
        }

        private String getDate(String tanggal) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
            try {
                Date date = inputFormat.parse(tanggal);
                return dateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return "";
            }
        }

        private String getMonth(String tanggal) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
            try {
                Date date = inputFormat.parse(tanggal);
                return monthFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                return "";
            }
        }
    }
}
