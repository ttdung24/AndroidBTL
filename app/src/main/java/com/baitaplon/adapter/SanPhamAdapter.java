package com.baitaplon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baitaplon.R;
import com.baitaplon.model.DanhMuc;
import com.baitaplon.model.SanPham;
import com.bumptech.glide.Glide;

import java.util.List;

public class SanPhamAdapter extends RecyclerView.Adapter<SanPhamAdapter.SPViewHolder> {
    Context context;
    List<SanPham> list;

    SPItemListener spItemListener;

    public void setSpItemListener(SPItemListener spItemListener) {
        this.spItemListener = spItemListener;
    }

    public SanPhamAdapter(Context context, List<SanPham> list) {
        this.context = context;
        this.list = list;
    }

    public SanPham getItem(int i) {
        return list.get(i);
    }

    @NonNull
    @Override
    public SPViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sanpham, parent, false);
        return new SPViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SPViewHolder holder, int position) {
        SanPham sp = list.get(position);
        if(sp == null){
            return;
        }
        holder.spName.setText(sp.getTenSp());
        holder.spGia.setText(sp.getGia() + "");
        holder.spSao.setText(sp.getSaoDanhgia() + "");
        Glide.with(context).load(sp.getImg().trim()).error(R.drawable.images).into(holder.spImg);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class SPViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView spImg;
        TextView spName, spGia, spSao;
        public SPViewHolder(@NonNull View itemView) {
            super(itemView);
            spName = itemView.findViewById(R.id.spName);
            spImg = itemView.findViewById(R.id.spImg);
            spSao = itemView.findViewById(R.id.spSao);
            spGia = itemView.findViewById(R.id.spGia);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (spItemListener != null) {
                spItemListener.onSPItemClick(view, getAdapterPosition());
            }
        }
    }

    public interface SPItemListener {
        public void onSPItemClick(View view, int position);
    }
}
