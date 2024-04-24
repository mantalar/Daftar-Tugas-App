package com.example.daftartugas.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.daftartugas.R;
import com.example.daftartugas.model.Tugas;

import java.util.List;

public class TugasAdapter extends BaseAdapter {
    private final List<Tugas> mList;
    private final Context mContext;

    public TugasAdapter(List<Tugas> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_layout, parent, false);

        TextView tvTugas = convertView.findViewById(R.id.tv_tugas);
        tvTugas.setText(mList.get(position).getTugas());

        return convertView;
    }
}
