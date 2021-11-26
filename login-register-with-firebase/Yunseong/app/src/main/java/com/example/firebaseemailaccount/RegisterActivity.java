package com.example.firebaseemailaccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증 관련
    private DatabaseReference mDatabaseReference; // 데이터베이스 관련
    private EditText et_email, et_pwd, et_name, et_introduce;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("FirebaseEmailAccount");

        et_email = findViewById(R.id.et_email);
        et_pwd = findViewById(R.id.et_pwd);
        btn_register = findViewById(R.id.btn_register);

        et_name = findViewById(R.id.et_name);
        et_introduce = findViewById(R.id.et_introduce);


        btn_register.setOnClickListener(v -> {
            String strEmail = et_email.getText().toString();
            String strPwd = et_pwd.getText().toString();
            String strName = et_name.getText().toString();
            String strIntroduce = et_introduce.getText().toString();

            mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) { //유저가 다 만들어졌을 때
                    if(task.isSuccessful()) {
                        FirebaseUser firebaseUser =mFirebaseAuth.getCurrentUser(); //로그인을 성공해서 가능한 것
                        UserAccount account = new UserAccount();
                        account.setEmail(firebaseUser.getEmail());
                        account.setPassword(strPwd);
                        account.setIdToken(firebaseUser.getUid());
                        account.setName(strName);
                        account.setIntroduce(strIntroduce);

                        //database에 저장
                        mDatabaseReference.child("userAccount").child(firebaseUser.getUid()).setValue(account);

                        Toast.makeText(RegisterActivity.this, "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "회원가입에 실패하셨습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        });
    }
}