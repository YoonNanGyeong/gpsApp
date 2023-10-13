package com.ntek.testgpsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.ntek.testgpsapp.Entity.User;

import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private AppDatabase db;
    TextView sign;
    TextInputEditText id, pw;
    AppCompatButton loginBtn;

    String uId, uPw;


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.i("LoginActivity","onPostCreate Called");

        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();


        //회원가입 버튼
        sign = findViewById(R.id.signin);

        //회원가입 버튼 클릭 시, 회원가입 페이지로 이동
        sign.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        });

        // 데이터베이스 인스턴스 생성
        db = AppDatabase.getInstance(this);

        // xml객체 뷰 바인딩
        id = findViewById(R.id.user_loginId);
        pw = findViewById(R.id.user_loginPw);
        loginBtn = findViewById(R.id.loginButton);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("LoginActivity","onStart Called");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LoginActivity","onResume Called");

        loginBtn.setOnClickListener(new View.OnClickListener() {
            List<User> findByIdList = db.userDao().findByUserId(id.getText().toString());
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LoginActivity","onPause Called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LoginActivity","onStop Called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LoginActivity","onDestroy Called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("LoginActivity","onRestart Called");
    }


}