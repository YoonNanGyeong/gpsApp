package com.ntek.testgpsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ntek.testgpsapp.Entity.User;

import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private AppDatabase db;
    TextView sign;
    TextInputEditText id, pw;
    AppCompatButton loginBtn;
    LinearLayout outSide;

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
        outSide = findViewById(R.id.layout_login_outside);
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

        final InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        outSide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 포커스 해제
                id.clearFocus();
                pw.clearFocus();

                // 키보드 내리기
                manager.hideSoftInputFromWindow(view.getWindowToken(),0);

                return false;
            }
        });

        // 로그인 버튼 이벤트
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력한 아이디, 비밀번호
                uId = id.getText().toString();
                uPw = pw.getText().toString();

                List<User> findByUserAssign = db.userDao().findByUserAssign(uId, uPw);  // db에서 해당 계정 찾기

                if(findByUserAssign.size() > 0){ //계정이 있으면 로그인 성공
                    Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("id",uId);
                    intent.putExtra("pw",uPw);

                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "로그인 실패..", Toast.LENGTH_SHORT).show();
                    return;
                }
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