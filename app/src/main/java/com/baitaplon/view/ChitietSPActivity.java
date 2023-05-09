package com.baitaplon.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baitaplon.R;
import com.baitaplon.model.GioHang;
import com.baitaplon.model.SanPham;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChitietSPActivity extends AppCompatActivity {
    TextView tvTensp, tvGia, tvMota, tvNhasx, tvNamsx, tvGiam, tvTang, tvSoluong;
    ImageView imgSp, imgBack;
    Button btDat;
    String maSp;

    ArrayList<SanPham> listSp = new ArrayList<>();
    ArrayList<GioHang> listGh = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chitiet_spactivity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        maSp = intent.getStringExtra("maSP");
        initView();
        getSanpham();
        getGiohang();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(tvSoluong.getText().toString()) == 1) {
                    Toast.makeText(ChitietSPActivity.this, "Không thể giảm về 0!", Toast.LENGTH_SHORT).show();
                } else {
                    int soluongmoi = Integer.parseInt(tvSoluong.getText().toString()) - 1;
                    tvSoluong.setText(soluongmoi + "");
                }
            }
        });

        tvTang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(tvSoluong.getText().toString()) == listSp.get(listSp.size() - 1).getSlCon()) {
                    Toast.makeText(ChitietSPActivity.this, "Không thể đặt quá số lượng hàng tồn!", Toast.LENGTH_SHORT).show();

                } else {
                    int soluongmoi = Integer.parseInt(tvSoluong.getText().toString()) + 1;
                    tvSoluong.setText(soluongmoi + "");
                }
            }
        });

        btDat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null){
                    Toast.makeText(getApplication(),"Đăng nhập để thêm giỏ hàng",Toast.LENGTH_SHORT).show();

                } else {
                    boolean exists = false;
                    SanPham sp = listSp.get(0);
                    if (listGh.size() > 0) {
                        GioHang gioHang1 = new GioHang(maSp, sp.getTenSp(), sp.getImg(), sp.getGia(), listGh.get(0).getSoluong() + Integer.parseInt(tvSoluong.getText().toString()));
                        for(int i = 0; i < MainActivity.listGH.size(); i++){
                            if(MainActivity.listGH.get(i).getIdsp().equals(gioHang1.getIdsp())){
                                MainActivity.listGH.remove(i);
                                MainActivity.listGH.add(gioHang1);
                                exists = true;
                            }
                        }
                        if(exists == false){
                            MainActivity.listGH.add(gioHang1);
                        }
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
                        DatabaseReference myRef = database.getReference("giohang");
                        myRef.child(user.getUid()).child(gioHang1.getIdsp()).setValue(gioHang1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });

                    } else {
                        GioHang gioHang = new GioHang(maSp, sp.getTenSp(), sp.getImg(), sp.getGia(), Integer.parseInt(tvSoluong.getText().toString()));
                        MainActivity.listGH.add(gioHang);
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
                        DatabaseReference myRef = database.getReference("giohang");
                        myRef.child(user.getUid()).child(gioHang.getIdsp()).setValue(gioHang);
                    }
                    Toast.makeText(ChitietSPActivity.this,"Đặt hàng thành công",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChitietSPActivity.this, MainActivity.class);
                    intent.putExtra("trang", 0);
                    startActivity(intent);
                }
            }
        });
    }

    private void getGiohang() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("giohang").child(user.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listGh.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    GioHang gioHang = dataSnapshot.getValue(GioHang.class);
                    if (maSp != null && gioHang.getIdsp().equals(maSp)) {
                        listGh.add(gioHang);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChitietSPActivity.this,"Lấy sản phẩm thất bại",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSanpham() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("sanpham");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sp = dataSnapshot.getValue(SanPham.class);
                    if (sp.getIdSp().equals(maSp)){
                        listSp.add(sp);
                    }
                }
                tvTensp.setText(listSp.get(listSp.size() - 1).getTenSp());
                tvGia.setText(listSp.get(listSp.size() - 1).getGia() + "");
                tvMota.setText(listSp.get(listSp.size() - 1).getMota());
                tvNamsx.setText(listSp.get(listSp.size() - 1).getNamsx());
                tvNhasx.setText(listSp.get(listSp.size() - 1).getNhasx());
                Glide.with(ChitietSPActivity.this).load(listSp.get(listSp.size() - 1).getImg()).error(R.drawable.ic_hide_image).into(imgSp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChitietSPActivity.this,"Lấy sản phẩm thất bại",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        tvTensp = findViewById(R.id.tv_tenSP);
        tvGia = findViewById(R.id.tv_gia);
        tvMota = findViewById(R.id.tv_mota);
        tvNamsx = findViewById(R.id.tv_namsx);
        tvNhasx = findViewById(R.id.tv_nhasx);
        tvGiam = findViewById(R.id.tvGiamSL);
        tvTang = findViewById(R.id.tvTangSL);
        tvSoluong = findViewById(R.id.tvSoLuong);
        imgBack = findViewById(R.id.btBack);
        imgSp = findViewById(R.id.img_anhHang);
        btDat = findViewById(R.id.btDatHang);
    }
}