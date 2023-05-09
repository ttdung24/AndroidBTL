package com.baitaplon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.baitaplon.R;
import com.baitaplon.model.DanhMuc;
import com.bumptech.glide.Glide;

import java.util.List;

public class DanhMucAdapter extends RecyclerView.Adapter<DanhMucAdapter.DMViewHolder> {
    Context context;
    List<DanhMuc> list;

    DMItemListener dmItemListener;

    public DanhMucAdapter(Context context, List<DanhMuc> list) {
        this.context = context;
        this.list = list;
    }

    public void setDmItemListener(DMItemListener dmItemListener) {
        this.dmItemListener = dmItemListener;
    }

    public DanhMuc getItem(int i) {
        return list.get(i);
    }

    @NonNull
    @Override
    public DMViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danhmuc, parent, false);
        return new DMViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DMViewHolder holder, int position) {
        DanhMuc danhMuc = list.get(position);
        if(danhMuc == null){
            return;
        }
        holder.nameDanhmuc.setText(danhMuc.getTenDanhMuc());
        Glide.with(context).load(danhMuc.getImgDanhmuc().trim()).error(R.drawable.images).into(holder.imgDanhmuc);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DMViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgDanhmuc;
        TextView nameDanhmuc;

        public DMViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDanhmuc = itemView.findViewById(R.id.imgDanhmuc);
            nameDanhmuc = itemView.findViewById(R.id.nameDanhmuc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (dmItemListener != null) {
                dmItemListener.onDMItemClick(view, getAdapterPosition());
            }
        }
    }

    public interface DMItemListener {
        public void onDMItemClick(View view, int position);
    }
}
