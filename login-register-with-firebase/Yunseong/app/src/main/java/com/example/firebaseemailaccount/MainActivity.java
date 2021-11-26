package com.example.firebaseemailaccount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth; //파이어베이스 인증 관련
    private DatabaseReference mDatabaseReference; // 데이터베이스 관련
    private TextView tv_name, tv_introduce;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("FirebaseEmailAccount");

        firebaseUser = mFirebaseAuth.getCurrentUser();

        tv_introduce = findViewById(R.id.tv_introduce);
        UserAccount userAccount = (UserAccount) mDatabaseReference.child(firebaseUser.getUid()).get();
        tv_introduce.setText();

        tv_name = findViewById(R.id.tv_nickname);
        tv_name.setText();

        findViewById(R.id.btn_logout).setOnClickListener(v -> {
            mFirebaseAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void deleteAccount(View view) {
        mFirebaseAuth.getCurrentUser().delete();
        mDatabaseReference.child("userAccount").child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(MainActivity.this, "DB 내용 삭제 성공", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("error: " + e.getMessage());
                Toast.makeText(MainActivity.this, "DB 내용 삭제 실패패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}