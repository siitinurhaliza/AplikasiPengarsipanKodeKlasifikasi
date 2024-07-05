// LpbphDetailAdapter.java
package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.LpbphDetail;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.NpbphDetail;

import java.util.List;

public class NpbphDetailAdapter extends RecyclerView.Adapter<NpbphDetailAdapter.ViewHolder> {

    private List<NpbphDetail> npbphDetailList;

    public NpbphDetailAdapter(List<NpbphDetail> npbphDetailList) {
        this.npbphDetailList = npbphDetailList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lpbph_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NpbphDetail detail = npbphDetailList.get(position);
        holder.tvNamaBarang.setText(detail.getNamaBarang());
        holder.tvMerk.setText(detail.getMerk());
        holder.tvVol.setText(String.valueOf(detail.getVol()));
        holder.tvSatuan.setText(detail.getSatuan());
        holder.tvHargaSatuan.setText(String.valueOf(detail.getHargaSatuan()));
        holder.tvJumlah.setText(String.valueOf(detail.getJumlah()));
    }

    @Override
    public int getItemCount() {
        return npbphDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaBarang, tvMerk, tvVol, tvSatuan, tvHargaSatuan, tvJumlah;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaBarang = itemView.findViewById(R.id.tv_nama_barang);
            tvMerk = itemView.findViewById(R.id.tv_merk);
            tvVol = itemView.findViewById(R.id.tv_vol);
            tvSatuan = itemView.findViewById(R.id.tv_satuan);
            tvHargaSatuan = itemView.findViewById(R.id.tv_harga_satuan);
            tvJumlah = itemView.findViewById(R.id.tv_jumlah);
        }
    }
}
