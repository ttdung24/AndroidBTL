package com.baitaplon.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baitaplon.R;
import com.baitaplon.model.DonHang;
import com.baitaplon.model.GioHang;
import com.baitaplon.view.GioHangActivity;
import com.baitaplon.view.MainActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DonHangAdapter extends BaseAdapter {
    Context context;
    List<DonHang> listDH;

    public DonHangAdapter(Context context,  List<DonHang> listDH) {
        this.context = context;
        this.listDH = listDH;
    }


    @Override
    public int getCount() {
        if(listDH != null){
            return listDH.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return listDH.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang, parent, false);
        TextView tvName, tvPrice;
        ImageView imgPicture;
        tvName = view.findViewById(R.id.tvNameDH);
        tvPrice = view.findViewById(R.id.tvTongtienDH);
        tvName.setMaxLines(2);
        DonHang dh = listDH.get(i);
        tvName.setText("Đơn hàng " + dh.getMaDh() + " đã được nhận và đang trong quá trình vận chuyển");
        tvPrice.setText(dh.getTongTien() + "");
        return view;
    }
}
