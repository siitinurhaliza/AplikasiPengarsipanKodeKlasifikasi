package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.SifatResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SifatAdapter extends RecyclerView.Adapter<SifatAdapter.SifatViewHolder> {

    private List<SifatResponse.SifatData> sifatList;
    private Context context;
    private SifatItemClickListener itemClickListener;

    public SifatAdapter(Context context, List<SifatResponse.SifatData> sifatList, SifatItemClickListener itemClickListener) {
        this.context = context;
        this.sifatList = sifatList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public SifatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemsifat, parent, false);
        return new SifatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SifatViewHolder holder, int position) {
        SifatResponse.SifatData sifatData = sifatList.get(position);
        holder.bind(sifatData);
    }

    @Override
    public int getItemCount() {
        return sifatList.size();
    }

    public void updateList(List<SifatResponse.SifatData> newList) {
        sifatList.clear();
        sifatList.addAll(newList);
        notifyDataSetChanged();
    }

    public class SifatViewHolder extends RecyclerView.ViewHolder {
        private TextView nama;
        private ImageView ivMore;

        public SifatViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            ivMore = itemView.findViewById(R.id.options);
        }

        public void bind(SifatResponse.SifatData sifatData) {
            nama.setText(sifatData.getNama());

            ivMore.setOnClickListener(v -> showOptionsDialog(sifatData));
        }

        private void showOptionsDialog(SifatResponse.SifatData sifatData) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Select Option")
                    .setItems(new String[]{"Delete", "Update", "Detail"}, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                itemClickListener.onDeleteClick(sifatData);
                                break;
                            case 1:
                                itemClickListener.onUpdateClick(sifatData);
                                break;
                            case 2:
                                showDetailDialog(sifatData);
                                break;
                        }
                    });
            builder.show();
        }

        private void showDetailDialog(SifatResponse.SifatData sifatData) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Detail")
                    .setMessage("Name: " + sifatData.getNama())
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }

    public interface SifatItemClickListener {
        void onUpdateClick(SifatResponse.SifatData sifatData);
        void onDeleteClick(SifatResponse.SifatData sifatData);
    }
}
