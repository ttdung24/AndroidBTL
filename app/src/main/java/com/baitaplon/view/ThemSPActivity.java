package com.baitaplon.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baitaplon.R;
import com.baitaplon.adapter.SpinnerAdapter;
import com.baitaplon.model.DanhMuc;
import com.baitaplon.model.SanPham;
import com.baitaplon.model.TaiKhoan;
import com.baitaplon.view.fragment.UserFragment;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

public class ThemSPActivity extends AppCompatActivity {

    ImageView imgAnhsp;
    EditText tenSp, namSx, nhaSx, mota, gia, soluong;
    Spinner spinner;
    Button them, sua, xoa;
    Uri mUri;

    ImageView back;

    ArrayList<DanhMuc> listDanhmuc = new ArrayList<>();
    ArrayList<SanPham> listSp = new ArrayList<>();

    SpinnerAdapter dmAdapter;
    final private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode() == RESULT_OK){
                Intent intent = result.getData();
                if(intent == null){
                    return;
                }
                mUri = intent.getData();
                setmUri(mUri);
                Glide.with(ThemSPActivity.this).load(mUri).into(imgAnhsp);
            }
        }
    });

    private void setmUri(Uri mUri) {
        this.mUri = mUri;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_spactivity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initView();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        dmAdapter = new SpinnerAdapter(getApplicationContext(), listDanhmuc);
        spinner.setAdapter(dmAdapter);
        getDanhmuc();
        getSanpham();
        Intent intent = getIntent();
        SanPham sanPham = (SanPham) intent.getSerializableExtra("sanpham");
        spinner.setAdapter(dmAdapter);
        imgAnhsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                mActivityResultLauncher.launch(Intent.createChooser(intent,"Chọn ảnh"));
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) view).setTextColor(Color.BLACK);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if (sanPham == null) {
            sua.setVisibility(View.GONE);
            xoa.setVisibility(View.GONE);
        } else {
            them.setVisibility(View.GONE);
            Glide.with(ThemSPActivity.this).load(sanPham.getImg()).error(R.drawable.ic_hide_image).into(imgAnhsp);
            tenSp.setText(sanPham.getTenSp());
            nhaSx.setText(sanPham.getNhasx());
            namSx.setText(sanPham.getNamsx());
            mota.setText(sanPham.getMota());
            gia.setText(sanPham.getGia() + "");
            soluong.setText(sanPham.getSlCon() + "");
            spinner.setSelection(Integer.parseInt(sanPham.getMaDanhMuc().substring(2)) - 1);
        }
        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                String nameSp = tenSp.getText().toString().trim();
                String nhasx = nhaSx.getText().toString().trim();
                String namsx = namSx.getText().toString().trim();
                String moTa = mota.getText().toString().trim();
                String giA = gia.getText().toString().trim();
                String soLuong = soluong.getText().toString().trim();
                String fileExtension = "jpg";
                String madanhmuc = listDanhmuc.get((Integer) spinner.getSelectedItem()).getMaDanhMuc();
                String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
                Cursor cursor = getContentResolver().query(mUri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    String fileName1 = cursor.getString(0);
                    fileExtension = fileName1.substring(fileName1.lastIndexOf(".") + 1);
                    cursor.close();
                }
                String fileName = UUID.randomUUID().toString() + "." + fileExtension;
                StorageReference fileRef = storageRef.child(fileName);
                if (nameSp.equals("") || nhasx.equals("") || namsx.equals("") || moTa.equals("") || giA.equals("") || soLuong.equals("")) {
                    Toast.makeText(ThemSPActivity.this, "Vui lòng nhập đủ thông tin cá nhân", Toast.LENGTH_SHORT).show();
                } else {
                    SanPham.id = listSp.size() + 1;
                    fileRef.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    SanPham sp = new SanPham(nhasx, namsx, imageUrl + "", madanhmuc, moTa, nameSp, Integer.parseInt(giA), Integer.parseInt(soLuong), 4.5f);

                                    databaseReference.child("sanpham").child(sp.getIdSp()).setValue(sp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                    Toast.makeText(ThemSPActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    imgAnhsp.setImageResource(R.drawable.ic_hide_image);
                    tenSp.setText("");
                    nhaSx.setText("");
                    namSx.setText("");
                    mota.setText("");
                    gia.setText("");
                    soluong.setText("");
                    spinner.setSelection(0);
                }

            }
        });

        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                String nameSp = tenSp.getText().toString().trim();
                String nhasx = nhaSx.getText().toString().trim();
                String namsx = namSx.getText().toString().trim();
                String moTa = mota.getText().toString().trim();
                String giA = gia.getText().toString().trim();
                String soLuong = soluong.getText().toString().trim();
                String fileExtension = "jpg";
                String madanhmuc = listDanhmuc.get((Integer) spinner.getSelectedItem()).getMaDanhMuc();
                String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
                Cursor cursor = getContentResolver().query(mUri, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    String fileName1 = cursor.getString(0);
                    fileExtension = fileName1.substring(fileName1.lastIndexOf(".") + 1);
                    cursor.close();
                }
                String fileName = UUID.randomUUID().toString() + "." + fileExtension;
                StorageReference fileRef = storageRef.child(fileName);
                if (nameSp.equals("") || nhasx.equals("") || namsx.equals("") || moTa.equals("") || giA.equals("") || soLuong.equals("")) {
                    Toast.makeText(ThemSPActivity.this, "Vui lòng nhập đủ thông tin cá nhân", Toast.LENGTH_SHORT).show();
                } else {
                    fileRef.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    SanPham sp = new SanPham(sanPham.getIdSp(), nhasx, namsx, imageUrl + "", madanhmuc, moTa, nameSp, Integer.parseInt(giA), Integer.parseInt(soLuong), 4.5f);
                                    databaseReference.child("sanpham").child(sanPham.getIdSp()).setValue(sp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                    Toast.makeText(ThemSPActivity.this, "Sửa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("sanpham").child(sanPham.getIdSp()).removeValue();
                finish();
            }
        });
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
                dmAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThemSPActivity.this,"Lấy danh mục thất bại",Toast.LENGTH_SHORT).show();
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThemSPActivity.this,"Lấy sản phẩm thất bại",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initView() {
        imgAnhsp = findViewById(R.id.imgThemSp);
        tenSp = findViewById(R.id.inputNameSp);
        nhaSx = findViewById(R.id.inputNhasx);
        namSx = findViewById(R.id.inputNamsx);
        mota = findViewById(R.id.inputMota);
        gia = findViewById(R.id.inputGia);
        soluong = findViewById(R.id.inputSoluong);
        spinner = findViewById(R.id.spinner);
        them = findViewById(R.id.btThem);
        sua = findViewById(R.id.btSua);
        xoa = findViewById(R.id.btXoa);
        back = findViewById(R.id.btBackSp);
        listDanhmuc = new ArrayList<>();
    }
}