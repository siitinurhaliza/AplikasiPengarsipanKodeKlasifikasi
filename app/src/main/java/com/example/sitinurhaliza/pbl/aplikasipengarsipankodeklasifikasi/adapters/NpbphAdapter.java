package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.Api;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.api.RetrofitClient;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.Npbph;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.NpbphDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NpbphAdapter extends RecyclerView.Adapter<NpbphAdapter.ViewHolder> {

    private List<Npbph> npbphList;
    private Context context;

    public NpbphAdapter(List<Npbph> npbphList) {
        this.npbphList = npbphList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.nota, parent, false);
        return new ViewHolder(view);
    }

    public void updateList(List<Npbph> newList) {
        npbphList = newList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Npbph npbph = npbphList.get(position);
        holder.tvNomor.setText(String.valueOf(npbph.getNomor())); // Setel nilai tvNomor di sini

        holder.options.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.options);
            popupMenu.inflate(R.menu.menu_options);
            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
                if (id == R.id.action_view_details) {
                    showDetailsDialog(npbph.getNpbphs());
                    return true;
                } else if (id == R.id.action_delete) {
                    confirmDeleteNpbph(holder.getAdapterPosition(), npbph.getId());
                    return true;
                } else if (id == R.id.action_add_to_home) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                        addShortcutToHomeScreen(npbph);
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
            String pdfUrl = "http://192.168.148.19:8080/api/nota/print/" + npbph.getId();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(pdfUrl), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Intent chooser = Intent.createChooser(intent, "Open PDF");
            try {
                context.startActivity(chooser);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No PDF viewer installed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return npbphList.size();
    }

    private void confirmDeleteNpbph(int position, int npbphId) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_hapus, null);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();

        Button buttonCancel = dialogView.findViewById(R.id.button_cancel);
        Button buttonConfirm = dialogView.findViewById(R.id.button_confirm);

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        buttonConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            deleteNpbph(position, npbphId);
        });

        dialog.show();
    }

    private void deleteNpbph(int position, int npbphId) {
        Api api = RetrofitClient.getInstance().getApi();
        Call<Void> call = api.deletenpbph(npbphId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    npbphList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, npbphList.size());
                } else {
                    showErrorDialog("Failed to delete data. Please try again later.");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                showErrorDialog("Failed to delete data. Please check your internet connection and try again.");
            }
        });
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder errorDialogBuilder = new AlertDialog.Builder(context);
        errorDialogBuilder.setTitle("Error");
        errorDialogBuilder.setMessage(message);
        errorDialogBuilder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        errorDialogBuilder.show();
    }

    private void showDetailsDialog(List<NpbphDetail> details) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_details, null);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewDetails);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        NpbphDetailAdapter detailAdapter = new NpbphDetailAdapter(details);
        recyclerView.setAdapter(detailAdapter);
        builder.setView(view);
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N_MR1)
    private void addShortcutToHomeScreen(Npbph npbph) {
        ShortcutManager shortcutManager = context.getSystemService(ShortcutManager.class);

        if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported()) {
            Intent shortcutIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.221.19:8080/api/nota/print/" + npbph.getId()));
            shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            ShortcutInfo shortcutInfo = new ShortcutInfo.Builder(context, String.valueOf(npbph.getId()))
                    .setShortLabel("Nota " + npbph.getNomor())
                    .setLongLabel("Akses cepat ke Nota " + npbph.getNomor())
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvCreatedAt, tvNomor;
        RecyclerView rvNpbphDetails;
        ImageView options;
        Button btnPrint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvCreatedAt = itemView.findViewById(R.id.tv_created_at);
            tvNomor = itemView.findViewById(R.id.tv_nomor); // Inisialisasi tvNomor di sini
            options = itemView.findViewById(R.id.options);
            btnPrint = itemView.findViewById(R.id.btn_print);
        }
    }
}
