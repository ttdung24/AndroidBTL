package com.baitaplon.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baitaplon.R;
import com.baitaplon.adapter.DanhMucAdapter;
import com.baitaplon.adapter.SanPhamAdapter;
import com.baitaplon.model.DanhMuc;
import com.baitaplon.model.SanPham;
import com.baitaplon.view.CapNhatTKActivity;
import com.baitaplon.view.ChitietSPActivity;
import com.baitaplon.view.DanhMucActivity;
import com.baitaplon.view.GioHangActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment  implements DanhMucAdapter.DMItemListener, SanPhamAdapter.SPItemListener {

    DanhMucAdapter danhMucAdapter;
    SanPhamAdapter sanPhamAdapter;

    ArrayList<DanhMuc> listDanhmuc;
    ArrayList<SanPham> listSp;
    RecyclerView recyclerViewDanhmuc, recyclerViewSanpham;
    ImageView imgGiohang;

    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);
        initView();
        getDanhmuc();
        getSanpham();
        imgGiohang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GioHangActivity.class);
                startActivity(intent);
            }
        });
        return view;

    }

    private void getDanhmuc(){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("danhmuc");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DanhMuc danhMuc = dataSnapshot.getValue(DanhMuc.class);

                    listDanhmuc.add(danhMuc);
                }
                danhMucAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Lấy danh mục thất bại",Toast.LENGTH_SHORT).show();
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
                    if(sp.getSaoDanhgia() > 4.0){
                        listSp.add(sp);
                    }
                }
                sanPhamAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Lấy sản phẩm thất bại",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        listDanhmuc = new ArrayList<>();
        listSp = new ArrayList<>();
        danhMucAdapter = new DanhMucAdapter(getActivity(), listDanhmuc);
        danhMucAdapter.setDmItemListener(this);
        sanPhamAdapter = new SanPhamAdapter(getActivity(), listSp);
        sanPhamAdapter.setSpItemListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(getActivity(), 4);
        gridLayoutManager2.setOrientation(GridLayoutManager.VERTICAL);
        recyclerViewDanhmuc = view.findViewById(R.id.rvDanhmuc);
        recyclerViewSanpham = view.findViewById(R.id.rvSp);
        recyclerViewDanhmuc.setAdapter(danhMucAdapter);
        recyclerViewSanpham.setAdapter(sanPhamAdapter);
        recyclerViewDanhmuc.setLayoutManager(gridLayoutManager);
        recyclerViewSanpham.setLayoutManager(gridLayoutManager2);
        imgGiohang = view.findViewById(R.id.img_gioHang);

    }

    @Override
    public void onDMItemClick(View view, int position) {
        DanhMuc dm = danhMucAdapter.getItem(position);
        Intent intent = new Intent(getContext(), DanhMucActivity.class);
        intent.putExtra("maDanhMuc", dm.getMaDanhMuc());
        startActivity(intent);
    }

    @Override
    public void onSPItemClick(View view, int position) {
        SanPham sp = sanPhamAdapter.getItem(position);
        Intent intent = new Intent(getContext(), ChitietSPActivity.class);
        intent.putExtra("maSP", sp.getIdSp() + "");
        startActivity(intent);
    }
}
