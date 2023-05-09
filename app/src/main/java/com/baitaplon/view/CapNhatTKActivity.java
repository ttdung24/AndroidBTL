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
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baitaplon.R;
import com.baitaplon.model.TaiKhoan;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class CapNhatTKActivity extends AppCompatActivity {
    EditText inputEmail, inputUsername, inputName, inputAddress, inputPhone;
    Button btCapnhat, btCancel;
    CircleImageView avaUpdate;

    private Uri mUri;


    private ArrayList<TaiKhoan> listTaiKhoan = new ArrayList<>();
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
                Glide.with(CapNhatTKActivity.this).load(mUri).into(avaUpdate);
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_tkactivity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        initView();
        setUserInformation();
        avaUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                mActivityResultLauncher.launch(Intent.createChooser(intent,"Chọn ảnh"));
            }
        });
        btCapnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                if(user != null && mUri != null) {
                    String email = inputEmail.getText().toString().trim();
                    String username = inputUsername.getText().toString().trim();
                    String name = inputName.getText().toString().trim();
                    String diachi = inputAddress.getText().toString().trim();
                    String sdt = inputPhone.getText().toString().trim();
                    String fileExtension = "jpg";

                    String[] projection = {MediaStore.Images.Media.DISPLAY_NAME};
                    Cursor cursor = getContentResolver().query(mUri, projection, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        String fileName1 = cursor.getString(0);
                        fileExtension = fileName1.substring(fileName1.lastIndexOf(".") + 1);
                        cursor.close();
                    }
                    String fileName = UUID.randomUUID().toString() + "." + fileExtension;
                    StorageReference fileRef = storageRef.child(fileName);

                    fileRef.putFile(mUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    TaiKhoan t = new TaiKhoan(username + "", name + "", diachi + "", user.getUid() + "", sdt + "", email + "", imageUrl + "");

                                    databaseReference.child("taikhoan").child(user.getUid() + "").setValue(t).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){

                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                    Toast.makeText(CapNhatTKActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CapNhatTKActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent(CapNhatTKActivity.this, MainActivity.class);
                intent.putExtra("trang", 3);
                startActivity(intent);
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        else{

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
                    inputEmail.setText(listTaiKhoan.get(listTaiKhoan.size()-1).getEmail());
                    inputUsername.setText(listTaiKhoan.get(listTaiKhoan.size()-1).getUsername());
                    inputName.setText(listTaiKhoan.get(listTaiKhoan.size()-1).getTen());
                    inputAddress.setText(listTaiKhoan.get(listTaiKhoan.size()-1).getDiachi());
                    inputPhone.setText(listTaiKhoan.get(listTaiKhoan.size()-1).getSdt());
                    Glide.with(CapNhatTKActivity.this).load(listTaiKhoan.get(listTaiKhoan.size() - 1).getAva()).error(R.drawable.avatardefault).into(avaUpdate);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });

        }
    }
    public void setmUri(Uri mUri) {
        this.mUri = mUri;
    }


    private void initView() {
        inputEmail = findViewById(R.id.inputEmailUpdate);
        inputUsername = findViewById(R.id.inputUsernameUpdate);
        inputName = findViewById(R.id.inputNameUpdate);
        inputAddress = findViewById(R.id.inputAddressUpdate);
        inputPhone = findViewById(R.id.inputPhoneUpdate);
        btCapnhat = findViewById(R.id.btnUpdate);
        btCancel = findViewById(R.id.btnCancel);
        avaUpdate = findViewById(R.id.img_update_avatar_capnhat);
    }
}