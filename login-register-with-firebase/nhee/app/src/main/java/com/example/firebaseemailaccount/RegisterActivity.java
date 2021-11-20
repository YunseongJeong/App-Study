package com.example.firebaseemailaccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity
{
    // 파이어베이스 관련 객체
    private FirebaseAuth mFirebaseAuth;     // 파이어베이스 인증 - 회원가입 인증에 사용 (DB관리 역할은 못함)
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스 (서버와 연동하는 객체)

    // 회원가입 레이아웃
    private EditText mEtEmail, mEtPwd, mEtName, mEtIntroduce;  // 회원가입 입력필드
    private Button mBtnRegister;        // 회원가입 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. 필드 초기화
        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("firebaseLogin");

        mEtEmail = findViewById(R.id.et_email);
        mEtPwd = findViewById(R.id.et_pwd);
        mEtName = findViewById(R.id.et_name);
        mEtIntroduce = findViewById(R.id.et_introduce);
        mBtnRegister = findViewById(R.id.btn_register);

        // 2. 회원가입 버튼 기능 구현
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼을 누를 때 마지막으로 기록된 데이터를 저장
                String strEmail = mEtEmail.getText().toString();
                String strPwd = mEtPwd.getText().toString();
                String strName = mEtName.getText().toString();
                String strIntroduce = mEtIntroduce.getText().toString();

                // Firebase Auth 진행
                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // task : 결과를 가지고 있음.
                                    if (task.isSuccessful()) { // 가입 성공을 했다면 (createUser를 성공했다면)
                                        // 현재 회원가입이 된 유저를 가져온다. 현재 로그인이 완료된 회원에게 제공된 uid
                                        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                        UserAccount account = new UserAccount();
                                        // 로그인된 uid(고유값) - 랜덤하게 제공되는 고유값(파이어베이스에서 생성)
                                        // 유저 정보를 좀 더 쉽게 가져올 수가 있습니다 (흐음)
                                        account.setIdToken(firebaseUser.getUid());
                                        // 실제 가입된 정보가 중요하기 때문에 firebase 인증 성공한 정보를 가져오고
                                        account.setEmailId(firebaseUser.getEmail());
                                        // 마지막으로 입력했던 정보를 가져옵니다.
                                        account.setPassword(strPwd);
                                        account.setName(strName);
                                        account.setIntroduce(strIntroduce);

                                        // setValue : DB에 insert
                                        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                                        Toast.makeText(RegisterActivity.this, "회원가입 성공!!", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(RegisterActivity.this, "회원가입 실패......", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

            }
        });

    }
}