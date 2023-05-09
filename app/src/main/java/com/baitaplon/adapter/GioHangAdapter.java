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
import com.baitaplon.model.GioHang;
import com.baitaplon.view.GioHangActivity;
import com.baitaplon.view.MainActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangAdapter extends BaseAdapter {
    Context context;
    List<GioHang> listGh;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference db;

    public GioHangAdapter(Context context, List<GioHang> listGh) {
        this.context = context;
        this.listGh = listGh;
    }


    @Override
    public int getCount() {
        if(listGh != null){
            return listGh.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return listGh.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_giohang, parent, false);
        TextView tvName, tvTangSL,tvGiamSL,tvSoLuong, tvPrice;
        ImageView imgPicture;
        tvName = view.findViewById(R.id.tvNameProduct_GH);
        tvPrice = view.findViewById(R.id.tvPrice_GH);
        tvTangSL = view.findViewById(R.id.tvTangSL_GH);
        tvGiamSL = view.findViewById(R.id.tvGiamSL_GH);
        tvSoLuong = view.findViewById(R.id.tvSoLuong_GH);
        imgPicture = view.findViewById(R.id.imgPictureProduct_GH);
        tvName.setMaxLines(2);
        GioHang gioHang = listGh.get(i);
        tvName.setText(gioHang.getTenSP());
        tvPrice.setText(gioHang.getDonGia() + "");
        Glide.with(context).load(gioHang.getImg()).error(R.drawable.avatardefault).into(imgPicture);
        tvSoLuong.setText(gioHang.getSoluong() + "");
        firebaseDatabase = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
        db = firebaseDatabase.getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tvGiamSL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(tvSoLuong.getText().toString()) == 1) {
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                            .setTitle("")
                            .setMessage("Bạn có chắc chắc chắn muốn xóa sản phẩm này")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    listGh.remove(i);
                                    db.child("giohang").child(user.getUid()).child(gioHang.getIdsp()).removeValue();
                                    notifyDataSetChanged();
                                    GioHangActivity.tongtien();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create();
                    alertDialog.show();
                    return;
                }
                int soluongmoi = Integer.parseInt(tvSoLuong.getText().toString()) - 1;
                tvSoLuong.setText(soluongmoi + "");
                MainActivity.listGH.get(i).setSoluong(soluongmoi);
                db.child("giohang").child(user.getUid()).child(gioHang.getIdsp()).child("soluong").setValue(soluongmoi);
                GioHangActivity.tongtien();
            }
        });

        tvTangSL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int soluongmoi = Integer.parseInt(tvSoLuong.getText().toString()) + 1;
                tvSoLuong.setText(soluongmoi + "");
                MainActivity.listGH.get(i).setSoluong(soluongmoi);
                notifyDataSetChanged();
                db.child("giohang").child(user.getUid()).child(gioHang.getIdsp()).child("soluong").setValue(soluongmoi);
                GioHangActivity.tongtien();

            }
        });
        return view;
    }
}
