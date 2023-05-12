package com.baitaplon.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.baitaplon.R;
import com.baitaplon.adapter.SanPhamAdapter;
import com.baitaplon.model.SanPham;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SuaXoaSPActivity extends AppCompatActivity implements SanPhamAdapter.SPItemListener {
    SearchView searchView;
    Button searchButton;
    SanPhamAdapter sanPhamAdapter;
    List<SanPham> listSp;
    RecyclerView rvSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_xoa_spactivity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        listSp = new ArrayList<>();
        searchView = findViewById(R.id.searchViewSP);
        searchButton = findViewById(R.id.searchButtonSP);
        rvSearch = findViewById(R.id.rvSearchSP);
        sanPhamAdapter = new SanPhamAdapter(this, listSp);
        sanPhamAdapter.setSpItemListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvSearch.setAdapter(sanPhamAdapter);
        rvSearch.setLayoutManager(gridLayoutManager);
        getSanpham();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchView.getQuery().toString();
                getSanpham(query);
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
                    listSp.add(sp);
                }
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SuaXoaSPActivity.this,"Lấy sản phẩm thất bại",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getSanpham(String query) {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("sanpham");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    SanPham sp = dataSnapshot.getValue(SanPham.class);
                    if(sp.getTenSp().contains(query)){
                        listSp.add(sp);
                    }
                }
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SuaXoaSPActivity.this,"Lấy sản phẩm thất bại",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSPItemClick(View view, int position) {
        SanPham sp = sanPhamAdapter.getItem(position);
        Intent intent = new Intent(SuaXoaSPActivity.this, ThemSPActivity.class);
        intent.putExtra("sanpham", (SanPham) sp);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        listSp.clear();
        sanPhamAdapter.notifyDataSetChanged();
    }
}