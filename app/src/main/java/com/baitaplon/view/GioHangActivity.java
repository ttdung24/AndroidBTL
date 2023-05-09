package com.baitaplon.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baitaplon.R;
import com.baitaplon.adapter.GioHangAdapter;
import com.baitaplon.model.DonHang;
import com.baitaplon.model.TaiKhoan;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class GioHangActivity extends AppCompatActivity {
    public static TaiKhoan tk;
    ListView lv_GioHang;
    TextView tvGioHangTrong;
    LinearLayout llbottom;

    CheckBox cbThanhtoan;

    Button btThanhtoan;

    static TextView tvTongtien;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Intent intent = getIntent();
        initView();
        getTaikhoan();
        setGioHang();
        tongtien();
        btThanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tk.getTen().equals("") || tk.getDiachi().equals("") || tk.getSdt().equals("")) {
                    Toast.makeText(GioHangActivity.this, "Vui lòng cập nhật thông tin tài khoản", Toast.LENGTH_SHORT).show();
                } else {
                    DonHang dh = new DonHang(MainActivity.listGH, tvTongtien.getText().toString(), cbThanhtoan.isChecked());
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
                    DatabaseReference db = firebaseDatabase.getReference("donhang");
                    db.child(tk.getId_user()).child(dh.getMaDh()).setValue(dh);
                    MainActivity.listGH.clear();
                    db = firebaseDatabase.getReference("giohang");
                    db.child(tk.getId_user()).removeValue();
                    Toast.makeText(getApplication(),"Đặt hàng thành công",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
        
    }

    private void initView() {
        lv_GioHang = findViewById(R.id.lvGioHang);
        tvGioHangTrong = findViewById(R.id.tvGioHAngTrong);
        llbottom = findViewById(R.id.bottom);
        tvTongtien = findViewById(R.id.tvTongTien);
        cbThanhtoan = findViewById(R.id.cbThanhtoan);
        btThanhtoan = findViewById(R.id.btThanhtoan);
    }

    public static void tongtien(){
        DecimalFormat formatPrice = new DecimalFormat("###,###,###");
        long tongtien = 0;
        for(int i = 0 ; i < MainActivity.listGH.size(); i++){
            tongtien += (MainActivity.listGH.get(i).getDonGia() * MainActivity.listGH.get(i).getSoluong());
        }
        tvTongtien.setText("Tổng tiền: " + formatPrice.format(tongtien) +"đ");
    }

    public void setGioHang(){
        if (MainActivity.listGH.size() > 0){
            GioHangAdapter adapterProduct = new GioHangAdapter(lv_GioHang.getContext(), MainActivity.listGH);
            lv_GioHang.setAdapter(adapterProduct);
            tvGioHangTrong.setVisibility(View.GONE);
        }
        else{
            lv_GioHang.setVisibility(View.GONE);
            tvGioHangTrong.setVisibility(View.VISIBLE);
            llbottom.setVisibility(View.GONE);
        }
    }

    private TaiKhoan getTaikhoan() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
            DatabaseReference myRef = database.getReference("taikhoan").child(user.getUid());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    tk = new TaiKhoan();
                    tk = snapshot.getValue(TaiKhoan.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return tk;
    }
}