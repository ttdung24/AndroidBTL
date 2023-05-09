package com.baitaplon.view.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baitaplon.R;
import com.baitaplon.adapter.DonHangAdapter;
import com.baitaplon.adapter.GioHangAdapter;
import com.baitaplon.model.DonHang;
import com.baitaplon.model.SanPham;
import com.baitaplon.view.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotiFragment extends Fragment {
    ListView lv_Donhang;
    TextView tvDonHang;
    DonHangAdapter donHangAdapter;
    List<DonHang> listDH;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.noti_fragment, container, false);
        lv_Donhang = view.findViewById(R.id.lvDonHang);
        tvDonHang = view.findViewById(R.id.tvDonHangTrong);
        listDH = new ArrayList<>();
        donHangAdapter = new DonHangAdapter(getActivity(), listDH);
        lv_Donhang.setAdapter(donHangAdapter);
        getDonhang();
        return view;
    }

    private void getDonhang() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference myRef = database.getReference("donhang").child(user.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    DonHang dh = dataSnapshot.getValue(DonHang.class);
                    listDH.add(dh);
                }
                donHangAdapter.notifyDataSetChanged();
                if (listDH.size() > 0){
                    tvDonHang.setVisibility(View.GONE);
                }
                else{
                    lv_Donhang.setVisibility(View.GONE);
                    tvDonHang.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Lấy đơn hàng thất bại",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
