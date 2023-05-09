package com.baitaplon.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baitaplon.R;
import com.baitaplon.model.TaiKhoan;
import com.baitaplon.view.CapNhatTKActivity;
import com.baitaplon.view.LoginActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserFragment extends Fragment {
    View view;
    TextView tvUsername, tvCapnhatTK, tvDangxuat;
    CircleImageView imgUser;
    private ArrayList<TaiKhoan> listTaiKhoan= new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_fragment, container, false);
        initView();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");

            DatabaseReference myRef = database.getReference("taikhoan");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                        TaiKhoan taiKhoan = dataSnapshot.getValue(TaiKhoan.class);
                        if(taiKhoan.getId_user().equals(user.getUid())){
                            listTaiKhoan.add(taiKhoan);
                        }
                    }
                    if (listTaiKhoan.get(listTaiKhoan.size() - 1).getUsername() == null){
                        tvUsername.setVisibility(View.GONE);
                    }
                    else {
                        tvUsername.setVisibility(View.VISIBLE);
                        tvUsername.setText(listTaiKhoan.get(listTaiKhoan.size() - 1).getUsername());
                    }
                    Glide.with(UserFragment.this).load(listTaiKhoan.get(listTaiKhoan.size() - 1).getAva()).error(R.drawable.avatardefault).into(imgUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(),"Lấy thông tin tài khoản thất bại",Toast.LENGTH_SHORT).show();
                }


            });
        }

        tvCapnhatTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CapNhatTKActivity.class);
                startActivity(intent);
            }
        });
        tvDangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    private void initView() {
        tvCapnhatTK = view.findViewById(R.id.tvCapnhatTK);
        tvUsername = view.findViewById(R.id.userName);
        tvDangxuat = view.findViewById(R.id.tvDangxuat);
        imgUser = view.findViewById(R.id.imgUser);
    }
}
