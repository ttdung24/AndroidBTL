package com.baitaplon.view;

import static android.app.PendingIntent.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baitaplon.R;
import com.baitaplon.adapter.SanPhamAdapter;
import com.baitaplon.model.DanhMuc;
import com.baitaplon.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DanhMucActivity extends AppCompatActivity implements SanPhamAdapter.SPItemListener {
    String maDanhMuc;
    ArrayList<SanPham> listSp;
    RecyclerView rvSpforDm;
    TextView tvSanpham;
    SanPhamAdapter sanPhamAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhmuc);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        maDanhMuc = intent.getStringExtra("maDanhMuc");
        initView();
        getSanpham();
    }

    private void initView() {
        listSp = new ArrayList<>();
        sanPhamAdapter = new SanPhamAdapter(getApplication(), listSp);
        sanPhamAdapter.setSpItemListener(this);
        rvSpforDm = findViewById(R.id.rvSpforDM);
        tvSanpham = findViewById(R.id.tvSanpham);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplication(), 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvSpforDm.setLayoutManager(gridLayoutManager);
        rvSpforDm.setAdapter(sanPhamAdapter);
    }

    private void getSanpham() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("sanpham");
        DatabaseReference myRef2 = database.getReference("danhmuc");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sp = dataSnapshot.getValue(SanPham.class);
                    if(sp.getMaDanhmuc().equals(maDanhMuc)){
                        listSp.add(sp);
                    }
                }
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(),"Lấy sản phẩm thất bại",Toast.LENGTH_SHORT).show();
            }
        });
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DanhMuc dm = dataSnapshot.getValue(DanhMuc.class);
                    if(dm.getMaDanhMuc().equals(maDanhMuc)){
                        tvSanpham.setText("Danh mục " + dm.getTenDanhMuc());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplication(),"Lấy danh mục thất bại",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSPItemClick(View view, int position) {
        SanPham sp = sanPhamAdapter.getItem(position);
        Intent intent = new Intent(DanhMucActivity.this, ChitietSPActivity.class);
        intent.putExtra("maSP", sp.getIdSp() + "");
        startActivity(intent);
    }
}