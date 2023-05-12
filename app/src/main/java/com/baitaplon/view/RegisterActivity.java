package com.baitaplon.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baitaplon.R;
import com.baitaplon.model.TaiKhoan;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    protected FirebaseAuth mFirebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    EditText inputEmailReg, inputUsername, inputPassReg, inputRePassReg;
    Button btnReg, btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        mFirebaseAuth = FirebaseAuth.getInstance();
        inputEmailReg = findViewById(R.id.inputEmailRegister);
        inputUsername = findViewById(R.id.inputUsername);
        inputPassReg = findViewById(R.id.inputPasswordRegister);
        inputRePassReg = findViewById(R.id.inputRePasswordRegister);
        btnReg = findViewById(R.id.btnRegister);
        btnCancel = findViewById(R.id.btnCancel);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmailReg.getText().toString().trim();
                String username = inputUsername.getText().toString().trim();
                String pass = inputPassReg.getText().toString().trim();
                String repass = inputRePassReg.getText().toString().trim();
                if (email.equals("") || pass.equals("") || repass.equals("") || username.equals("")) {
                    Toast.makeText(RegisterActivity.this, "Vui lòng nhập đầy đủ thông tin!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (repass.equals(pass)) {
                        mFirebaseAuth.createUserWithEmailAndPassword(email, pass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    firebaseDatabase = FirebaseDatabase.getInstance("https://baitaplon-e10a2-default-rtdb.asia-southeast1.firebasedatabase.app");
                                    databaseReference = firebaseDatabase.getReference();
                                    String email = user.getEmail() + "";
                                    String id = user.getUid() + "";
                                    TaiKhoan t = new TaiKhoan(username, "", "", id, "", email, "", false);
                                    databaseReference.child("taikhoan").child(id).setValue(t);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(RegisterActivity.this, "Nhắc lại mật khẩu sai!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}