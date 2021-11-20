package com.example.registerloginexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_id, et_pass, et_name, et_age;
    private Button btn_re;

    static private String TAG = "RegisterActivity.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_id = (EditText)findViewById(R.id.et_id);
        et_pass = (EditText)findViewById(R.id.et_pass);
        et_name = (EditText)findViewById(R.id.et_name);
        et_age = (EditText)findViewById(R.id.et_age);

        btn_re = (Button)findViewById(R.id.btn_re);
        btn_re.setOnClickListener(v -> {
            //String userID = et_id.getText().toString();
            //String userPassword = et_pass.getText().toString();
            //String userName = et_name.getText().toString();
            //int userAge = Integer.parseInt(et_age.getText().toString());
            String userID = "jys0972";
            String userPassword = "123445";
            String userName = "정윤성";
            int userAge = 20;


            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "onResponse: response is runned");
                    try {
                        Log.d(TAG, "onResponse: enter try");
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if(success){
                            Toast.makeText(getApplicationContext(), "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT);
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            };
            RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userName, userAge, responseListener);
            RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this); //서버에 요청하는데 volley를 사용함
            queue.add(registerRequest);
        });
    }
}