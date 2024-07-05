package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.UnitResponse;

import java.util.List;


public class UnitAdapter extends RecyclerView.Adapter<UnitAdapter.UnitViewHolder> {

    private List<UnitResponse.UnitData> unitList;
    private Context context;
    private UnitItemClickListener itemClickListener;

    public UnitAdapter(Context context, List<UnitResponse.UnitData> unitList, UnitItemClickListener itemClickListener) {
        this.context = context;
        this.unitList = unitList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public UnitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item3, parent, false);
        return new UnitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnitViewHolder holder, int position) {
        UnitResponse.UnitData unitData = unitList.get(position);
        holder.bind(unitData);
    }


    @Override
    public int getItemCount() {
        return unitList.size();
    }

    public void updateList(List<UnitResponse.UnitData> newList) {
        unitList.clear();
        unitList.addAll(newList);
        notifyDataSetChanged();
    }

    public class UnitViewHolder extends RecyclerView.ViewHolder {
        private TextView nama;
        private ImageView ivMore;

        public UnitViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            ivMore = itemView.findViewById(R.id.options);
        }

        public void bind(UnitResponse.UnitData unitData) {
            nama.setText(unitData.getNama());

            ivMore.setOnClickListener(v -> showOptionsDialog(unitData));
        }

        private void showOptionsDialog(UnitResponse.UnitData unitData) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Select Option")
                    .setItems(new String[]{"Delete", "Update", "Detail"}, (dialog, which) -> {
                        switch (which) {
                            case 0:
                                itemClickListener.onDeleteClick(unitData);
                                break;
                            case 1:
                                itemClickListener.onUpdateClick(unitData);
                                break;
                            case 2:
                                showDetailDialog(unitData);
                                break;
                        }
                    });
            builder.show();
        }

        private void showDetailDialog(UnitResponse.UnitData unitData) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Detail")
                    .setMessage("Name: " + unitData.getNama())
                    .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }

    public interface UnitItemClickListener {
        void onUpdateClick(UnitResponse.UnitData unitData);
        void onDeleteClick(UnitResponse.UnitData unitData);
    }
}
