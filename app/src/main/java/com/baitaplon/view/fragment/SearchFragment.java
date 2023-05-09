package com.baitaplon.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baitaplon.R;
import com.baitaplon.adapter.SanPhamAdapter;
import com.baitaplon.model.SanPham;
import com.baitaplon.view.ChitietSPActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment implements SanPhamAdapter.SPItemListener {
    SearchView searchView;
    Button searchButton;
    SanPhamAdapter sanPhamAdapter;
    List<SanPham> listSp;
    RecyclerView rvSearch;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        listSp = new ArrayList<>();
        searchView = view.findViewById(R.id.searchView);
        searchButton = view.findViewById(R.id.searchButton);
        rvSearch = view.findViewById(R.id.rvSearch);
        sanPhamAdapter = new SanPhamAdapter(getActivity(), listSp);
        sanPhamAdapter.setSpItemListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        rvSearch.setAdapter(sanPhamAdapter);
        rvSearch.setLayoutManager(gridLayoutManager);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = searchView.getQuery().toString();
                getSanpham(query);
            }
        });
        return view;
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
                Toast.makeText(getActivity(),"Lấy sản phẩm thất bại",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSPItemClick(View view, int position) {
        SanPham sp = sanPhamAdapter.getItem(position);
        Intent intent = new Intent(getContext(), ChitietSPActivity.class);
        intent.putExtra("maSP", sp.getIdSp() + "");
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        listSp.clear();
        sanPhamAdapter.notifyDataSetChanged();
    }
}
