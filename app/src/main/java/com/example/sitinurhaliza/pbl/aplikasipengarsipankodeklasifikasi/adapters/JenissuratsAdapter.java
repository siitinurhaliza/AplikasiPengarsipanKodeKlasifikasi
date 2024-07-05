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
import com.example.sitinurhaliza.pbl.aplikasipengarsipankodeklasifikasi.models.JenissuratsResponse;

import java.util.ArrayList;
import java.util.List;

public class JenissuratsAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<JenissuratsResponse.Jenissurats> jenissuratsList;
    private List<JenissuratsResponse.Jenissurats> jenissuratsListFull;

    public JenissuratsAdapter(Context context, List<JenissuratsResponse.Jenissurats> jenissuratsList) {
        this.context = context;
        this.jenissuratsList = jenissuratsList;
        this.jenissuratsListFull = new ArrayList<>(jenissuratsList); // Copy original list for filtering
    }

    @Override
    public int getCount() {
        return jenissuratsList.size();
    }

    @Override
    public Object getItem(int position) {
        return jenissuratsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return jenissuratsList.get(position).getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        }

        TextView kodeTextView = convertView.findViewById(R.id.tv_kode);
        final TextView keteranganTextView = convertView.findViewById(R.id.tv_perihal); // Mengubah perihal menjadi keterangan
        final ImageView expandMoreImageView = convertView.findViewById(R.id.iv_expand_more);

        JenissuratsResponse.Jenissurats jenissurat = jenissuratsList.get(position);

        kodeTextView.setText(jenissurat.getNama()); // Menggunakan nama sebagai kode
        keteranganTextView.setText(jenissurat.getKeterangan()); // Menggunakan keterangan sesuai format JSON

        expandMoreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (keteranganTextView.getVisibility() == View.GONE) {
                    keteranganTextView.setVisibility(View.VISIBLE);
                    expandMoreImageView.setImageResource(R.drawable.baseline_arrow_down_24); // Ubah ikon panah ke bawah
                } else {
                    keteranganTextView.setVisibility(View.GONE);
                    expandMoreImageView.setImageResource(R.drawable.baseline_arrow_right_24); // Ubah ikon panah ke kanan
                }
            }
        });

        return convertView;
    }



    @Override
    public Filter getFilter() {
        return jenissuratsFilter;
    }

    private Filter jenissuratsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<JenissuratsResponse.Jenissurats> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(jenissuratsListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (JenissuratsResponse.Jenissurats item : jenissuratsListFull) {
                    if (item.getNama().toLowerCase().contains(filterPattern) ||
                            item.getKeterangan().toLowerCase().contains(filterPattern)) {
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
            jenissuratsList.clear();
            jenissuratsList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
