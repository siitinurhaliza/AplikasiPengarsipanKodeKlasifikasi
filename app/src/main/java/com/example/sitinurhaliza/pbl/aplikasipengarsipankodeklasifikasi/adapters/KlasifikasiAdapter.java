package com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.R;
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.KlasifikasiResponse;

import java.util.ArrayList;
import java.util.List;

public class KlasifikasiAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<KlasifikasiResponse.Klasifikasi> klasifikasiList;
    private List<KlasifikasiResponse.Klasifikasi> klasifikasiListFull;

    public KlasifikasiAdapter(Context context, List<KlasifikasiResponse.Klasifikasi> klasifikasiList) {
        this.context = context;
        this.klasifikasiList = klasifikasiList;
        this.klasifikasiListFull = new ArrayList<>(klasifikasiList); // Copy original list for filtering
    }

    @Override
    public int getCount() {
        return klasifikasiList.size();
    }

    @Override
    public Object getItem(int position) {
        return klasifikasiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return klasifikasiList.get(position).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        }

        TextView kodeTextView = convertView.findViewById(R.id.tv_kode);
        final TextView perihalTextView = convertView.findViewById(R.id.tv_perihal);
        final ImageView expandMoreImageView = convertView.findViewById(R.id.iv_expand_more);

        KlasifikasiResponse.Klasifikasi klasifikasi = klasifikasiList.get(position);

        kodeTextView.setText(klasifikasi.getKode());
        perihalTextView.setText(klasifikasi.getPerihal());

        expandMoreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (perihalTextView.getVisibility() == View.GONE) {
                    perihalTextView.setVisibility(View.VISIBLE);
                    expandMoreImageView.setImageResource(R.drawable.baseline_arrow_down_24); // Ubah ikon panah ke bawah
                } else {
                    perihalTextView.setVisibility(View.GONE);
                    expandMoreImageView.setImageResource(R.drawable.baseline_arrow_right_24); // Ubah ikon panah ke kanan
                }
            }
        });

        return convertView;
    }



    @Override
    public Filter getFilter() {
        return klasifikasiFilter;
    }

    private Filter klasifikasiFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<KlasifikasiResponse.Klasifikasi> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(klasifikasiListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (KlasifikasiResponse.Klasifikasi item : klasifikasiListFull) {
                    if (item.getKode().toLowerCase().contains(filterPattern) ||
                            item.getPerihal().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            klasifikasiList.clear();
            klasifikasiList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
