package com.example.firebaseemailaccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef; // 실시간 데이터베이스 (서버와 연동하는 객체)
    private TextView tv_name, tv_introduce;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_name = findViewById(R.id.tv_name);
        tv_introduce = findViewById(R.id.tv_introduce);

        mFirebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("firebaseLogin");

        // DB에서 가져온 데이터를 textView에 설정해주는 과정
        tv_name.setText();
        tv_introduce.setText();

        Button btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그아웃하기
                mFirebaseAuth.signOut();

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void DeleteAccount(View view) {
        // 탈퇴 처리
        firebaseUser.delete(); // 현재 User의 Auth 정보를 삭제

        /* mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account); */
        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this, "DB 내용 삭제 성공!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("error: "+e.getMessage());
                Toast.makeText(MainActivity.this, "DB 삭제 실패...", Toast.LENGTH_SHORT).show();
            }
        });


    }
}