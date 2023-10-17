package com.ntek.testgpsapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ntek.testgpsapp.GpsService;
import com.ntek.testgpsapp.R;
import com.ntek.testgpsapp.persistance.AppDatabase;
import com.ntek.testgpsapp.persistance.Entity.Gps;
import com.ntek.testgpsapp.persistance.Entity.User;

import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private AppDatabase db; //데이터베이스
    TextView sign;  //회원가입 버튼
    TextInputEditText id, pw;   //아이디, 비밀번호 입력필드
    AppCompatButton loginBtn;   //로그인 버튼
    LinearLayout outSide;   //입력필드 외 영역

    String uId, uPw;    //아이디, 비밀번호 입력값

    Intent gpsInt;  // gpsService intent
    int gpsSeq; //위치정보 순번


    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION=1001;



    @SuppressLint("MissingPermission")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.i("LoginActivity", "onPostCreate Called");


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
        accessGps(this);    //위치정보 권한 요청 메소드
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("LoginActivity","onResume Called");

        /*
        * 입력필드 외 영역 터치(포커스해제 키보드 내림)
        * */
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
                        // 위치정보 서비스 시작
//                        gpsInt = new Intent(LoginActivity.this, GpsService.class);
//                        gpsInt.putExtra("id",uId);ㅅ
//                        startForegroundService(gpsInt);
                        Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();

                        Intent logInt = new Intent(LoginActivity.this,MainActivity.class);
                        logInt.putExtra("id",uId);  //메인으로 로그인한 아이디값 전달

                        startActivity(logInt);  //메인화면으로 이동
                }else{
                    Toast.makeText(LoginActivity.this, "로그인 실패..", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }


    //위치정보 권한 요청
    public void accessGps(Activity activity){
       int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
       if(permissionCheck!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }
    }

    //위치정보 권한 요청 응답
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "위치정보 제공 승인을 허가하였습니다.", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "위치정보 제공을 승인 받지않았습니다.", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
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

    @SuppressLint("MissingPermission")
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("LoginActivity","onRestart Called");
    }


}