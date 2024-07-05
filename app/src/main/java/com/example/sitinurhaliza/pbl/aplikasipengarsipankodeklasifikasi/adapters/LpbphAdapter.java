package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.Api;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.Lpbph;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.LpbphDetail;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.Npbph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LpbphAdapter extends RecyclerView.Adapter<LpbphAdapter.ViewHolder> {

    private List<Lpbph> lpbphList;
    private Context context;

    public LpbphAdapter(List<Lpbph> lpbphList) {
        this.lpbphList = lpbphList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.itemlembar, parent, false);
        return new ViewHolder(view);
    }
    public void updateList(List<Lpbph> newList) {
        lpbphList = newList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lpbph lpbph = lpbphList.get(position);
        holder.tvId.setText(String.valueOf(lpbph.getId()));

        // Menampilkan hanya bagian tanggal dari "created_at"
        String createdAt = lpbph.getCreatedAt();
        if (createdAt != null && createdAt.length() >= 10) {
            holder.tvCreatedAt.setText(createdAt.substring(0, 10));
        } else {
            holder.tvCreatedAt.setText(createdAt);
        }

        holder.options.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.options);
            popupMenu.inflate(R.menu.menu_options);
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_view_details) {
                    showDetailsDialog(lpbph.getLpbphs());
                    return true;
                } else if (id == R.id.action_delete) {
                    confirmDeleteLpbph(holder.getAdapterPosition(), lpbph.getId());
                    return true;
                } else if (id == R.id.action_add_to_home) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                        addShortcutToHomeScreen(lpbph);
                    } else {
                        Toast.makeText(context, "Fitur ini tidak didukung di perangkat Anda.", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                } else {
                    return false;
                }
            });
            popupMenu.show();
        });

        holder.btnPrint.setOnClickListener(v -> {
            // Ganti URL sesuai dengan endpoint yang sesuai
            String pdfUrl = "http://192.168.148.19:8080/api/lpbph/print/" + lpbph.getId();

            // Membuka PDF langsung dengan Intent
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Intent chooser = Intent.createChooser(intent, "Open PDF");
            try {
                context.startActivity(chooser);
            } catch (ActivityNotFoundException e) {
                // Tangani jika tidak ada aplikasi PDF viewer yang terinstal
                Toast.makeText(context, "No PDF viewer installed", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return lpbphList.size();
    }

    private void confirmDeleteLpbph(int position, int lpbphId) {
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
            deleteLpbph(position, lpbphId);
        });

        dialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void addShortcutToHomeScreen(Lpbph lpbph) {
        ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);

        if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported()) {
            Intent shortcutIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.221.19:8080/api/nota/print/" + lpbph.getId()));
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(context, String.valueOf(lpbph.getId()))
                    .setShortLabel("Lembar Permintaan " + lpbph.getId())
                    .setLongLabel("Akses cepat ke Lembar Permintaan " + lpbph.getId())
                    .setIcon(Icon.createWithResource(context, R.drawable.pdf)) // Pastikan Anda memiliki ikon ini di drawable
                    .setIntent(shortcutIntent)
                    .build();

            PendingIntent pinnedShortcutCallbackIntent = PendingIntent.getBroadcast(
                    context, 0,
                    shortcutManager.createShortcutResultIntent(shortcutInfo),
                    PendingIntent.FLAG_UPDATE_CURRENT);

            shortcutManager.requestPinShortcut(shortcutInfo, pinnedShortcutCallbackIntent.getIntentSender());
        } else {
            Toast.makeText(context, "Pin shortcut is not supported on your device.", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteLpbph(int position, int lpbphId) {
        Api api = RetrofitClient.getInstance().getApi();
        Call<Void> call = api.deletelpbph(lpbphId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Hapus data setelah mendapatkan konfirmasi dari server
                    lpbphList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, lpbphList.size());
                } else {
                    // Tangani kesalahan jika operasi hapus gagal
                    AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(context);
                    errorDialogBuilder.setTitle("Error");
                    errorDialogBuilder.setMessage("Failed to delete data. Please try again later.");
                    errorDialogBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                    errorDialogBuilder.show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // Tangani kegagalan koneksi atau permintaan
                AlertDialog.Builder failureDialogBuilder = new AlertDialog.Builder(context);
                failureDialogBuilder.setTitle("Error");
                failureDialogBuilder.setMessage("Failed to delete data. Please check your internet connection and try again.");
                failureDialogBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                failureDialogBuilder.show();
            }
        });
    }

    private void showDetailsDialog(List<LpbphDetail> details) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_details, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        LpbphDetailAdapter detailAdapter = new LpbphDetailAdapter(details);
        recyclerView.setAdapter(detailAdapter);
        builder.setView(view);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvCreatedAt;
        RecyclerView rvLpbphDetails;
        ImageView options;
        Button btnPrint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvCreatedAt = itemView.findViewById(R.id.tv_created_at);
            options = itemView.findViewById(R.id.options);
            btnPrint = itemView.findViewById(R.id.btn_print);
        }
    }
}
