package com.baitaplon.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.baitaplon.R;
import com.baitaplon.adapter.ViewPagerAdapter;
import com.baitaplon.model.GioHang;
import com.baitaplon.model.TaiKhoan;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mFirebaseAuth;
    ViewPager viewPagerMain;
    BottomNavigationView bottomNavigationView;
    public static List<GioHang> listGH;

    public static TaiKhoan taiKhoan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        viewPagerMain = findViewById(R.id.viewpagerMain);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 4);
        viewPagerMain.setAdapter(viewPagerAdapter);
        getTaikhoan();
        getGioHang();
        viewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.nav_search).setChecked(true);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.nav_notification).setChecked(true);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.nav_user).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_home:
                        viewPagerMain.setCurrentItem(0);
                        break;
                    case R.id.nav_search:
                        viewPagerMain.setCurrentItem(1);
                        break;
                    case R.id.nav_notification:
                        viewPagerMain.setCurrentItem(2);
                        break;
                    case R.id.nav_user:
                        viewPagerMain.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });

        if (listGH == null) {
            listGH = new ArrayList<>();
        }
        chuyenTrang();
    }

    private List<GioHang> getGioHang(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
            DatabaseReference myRef = database.getReference("giohang").child(user.getUid());

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listGH.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        GioHang gioHang = data.getValue(GioHang.class);
                        listGH.add(gioHang);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        else {
            listGH.clear();
        }
        return listGH;
    }
    private TaiKhoan getTaikhoan() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
            DatabaseReference myRef = database.getReference("taikhoan").child(user.getUid());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    taiKhoan = new TaiKhoan();
                    taiKhoan = snapshot.getValue(TaiKhoan.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return taiKhoan;
    }

    private void chuyenTrang() {
        int i = 0;
        Intent intent=getIntent();
        i = intent.getIntExtra("trang",0);
        viewPagerMain.setCurrentItem(i);
        switch (i) {
            case 0:
                bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                break;
            case 1:
                bottomNavigationView.getMenu().findItem(R.id.nav_search).setChecked(true);
                break;
            case 2:
                bottomNavigationView.getMenu().findItem(R.id.nav_notification).setChecked(true);
                break;
            case 3:
                bottomNavigationView.getMenu().findItem(R.id.nav_user).setChecked(true);
                break;
        }
    }
}