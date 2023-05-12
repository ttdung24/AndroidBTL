package com.baitaplon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baitaplon.R;
import com.baitaplon.model.DanhMuc;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {
    Context context;
    List<DanhMuc> listDM;

    public SpinnerAdapter(Context context, List<DanhMuc> listDM) {
        this.context = context;
        this.listDM = listDM;
    }

    @Override
    public int getCount() {
        return listDM.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View item = LayoutInflater.from(context).inflate(R.layout.item_spinner, viewGroup, false);
        TextView tv = item.findViewById(R.id.tvspinner);
        tv.setText(listDM.get(i).getTenDanhMuc());
        return item;
    }
}
