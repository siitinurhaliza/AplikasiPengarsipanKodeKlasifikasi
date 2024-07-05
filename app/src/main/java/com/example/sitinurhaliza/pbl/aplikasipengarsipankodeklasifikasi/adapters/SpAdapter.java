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
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.SpResponse;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.update.UpdateSpActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpAdapter extends RecyclerView.Adapter<SpAdapter.SpViewHolder> {

    private List<SpResponse.SpData> spList;
    private Context context;

    public SpAdapter(Context context, List<SpResponse.SpData> tasks) {
        this.context = context;
        this.spList = tasks;
    }

    @NonNull
    @Override
    public SpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemsp, parent, false);
        return new SpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpViewHolder holder, int position) {
        SpResponse.SpData spData = spList.get(position);
        holder.bind(spData);
    }

    @Override
    public int getItemCount() {
        return spList.size();
    }

    // Method untuk mengupdate daftar tugas
    public void updateList(List<SpResponse.SpData> newList) {
        spList = newList;
        notifyDataSetChanged();
    }

    public class SpViewHolder extends RecyclerView.ViewHolder {
        private TextView day, date, month, nomor, nama, tujuan;
        private ImageView ivMore;

        public SpViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.day);
            date = itemView.findViewById(R.id.date);
            month = itemView.findViewById(R.id.month);
            nomor = itemView.findViewById(R.id.nomor);
            nama = itemView.findViewById(R.id.nama);
            tujuan = itemView.findViewById(R.id.tujuan);
            ivMore = itemView.findViewById(R.id.options);
        }

        public void bind(SpResponse.SpData spData) {
            String tanggal = spData.getTanggal();
            day.setText(getDay(tanggal));
            date.setText(getDate(tanggal));
            month.setText(getMonth(tanggal));
            nomor.setText(spData.getNomor());
            nama.setText(spData.getNama());
            tujuan.setText(spData.getTujuan());

            ivMore.setOnClickListener(v -> showOptionsDialog(spData));
        }

        private void showOptionsDialog(SpResponse.SpData spData) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Select Option")
                    .setItems(new String[]{"Delete", "Update", "Detail"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    confirmDelete(spData);
                                    break;
                                case 1:
                                    editSpData(spData);
                                    break;
                                case 2:
                                    showDetailDialog(spData);
                                    break;
                            }
                        }
                    });
            builder.show();
        }

        private void confirmDelete(SpResponse.SpData spData) {
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
                deleteSpData(spData);
            });

            dialog.show();
        }

        private void deleteSpData(SpResponse.SpData spData) {
            Call<Void> call = RetrofitClient.getInstance().getApi().deleteSp(spData.getId());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        spList.remove(spData);
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

        private void editSpData(SpResponse.SpData spData) {
            Intent intent = new Intent(context, UpdateSpActivity.class);
            intent.putExtra("spData", spData);
            context.startActivity(intent);
        }

        private void showDetailDialog(SpResponse.SpData spData) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Detail Data")
                    .setMessage("Nomor: " + spData.getNomor() + "\n" +
                            "Nama: " + spData.getNama() + "\n" +
                            "Tanggal: " + spData.getTanggal() + "\n" +
                            "Tujuan: " + spData.getTujuan())
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
