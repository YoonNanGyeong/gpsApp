package com.ntek.testgpsapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ntek.testgpsapp.Entity.Gps;
import com.ntek.testgpsapp.Entity.User;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private AppDatabase db;
    TextView sign;
    TextInputEditText id, pw;
    AppCompatButton loginBtn;
    LinearLayout outSide;

    String uId, uPw;

    LocationManager locationMng;
    Location loc_current;
    double lon, lat, alt; //위도, 경도, 고도
    LocalDateTime today;
    String formatedNow;
    int gpsSeq; //위치정보 순번
    int totalNum;   //위치정보 데이터개수

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

        locationMng = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        loc_current = locationMng.getLastKnownLocation(LocationManager.GPS_PROVIDER);   //현재위치정보

        //위도,경도,고도 초기값
        lon = 0.0;
        lat = 0.0;
        alt = 0.0;

        totalNum = db.gpsDao().gpsDataNumber(); //위치정보데이터 개수
        gpsSeq = totalNum + 1;  //위치정보데이터 순번
        today = LocalDateTime.now();    //현재 시간
        formatedNow = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));   //시간 포맷
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
        * 다른영역 터치(포커스해제 키보드 내림)
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
                        // 위도, 경도, 고도
                        lon = loc_current.getLongitude();
                        lat = loc_current.getLatitude();
                        alt = loc_current.getAltitude();

                        // 위치정보 db에 저장
                        gpsData(uId,lat,lon,alt);

                    Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("id",uId);
                    intent.putExtra("pw",uPw);

                    startActivity(intent);  //메인화면으로 이동
                }else{
                    Toast.makeText(LoginActivity.this, "로그인 실패..", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    //위치정보 데이터 순번 얻기
    public void gpsData(String uId, double lat, double lon, double alt){
        // 데이터 객체
        Gps gps = new Gps(gpsSeq,uId,lat,lon,alt,formatedNow);
        db.gpsDao().insertAll(gps); // db에 로그인 유저 아이디, 위치정보 저장

        Log.i("gpsData",gps.toString()); //위치정보 데이터 확인 로그
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
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
    //위치정보 업데이트
    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            // 위도, 경도, 고도
            lon = loc_current.getLongitude();
            lat = loc_current.getLatitude();
            alt = loc_current.getAltitude();

            // 데이터 객체
            Gps gps = new Gps(gpsSeq,uId,lat,lon,alt,formatedNow);
            db.gpsDao().insertAll(gps); // db에 로그인 유저 아이디, 위치정보 저장
        }
    };


    @SuppressLint("MissingPermission")
    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LoginActivity","onPause Called");
        locationMng.requestLocationUpdates(LocationManager.GPS_PROVIDER,100000,10.0f,gpsLocationListener);
        locationMng.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100000,10.0f,gpsLocationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LoginActivity","onStop Called");
        locationMng.removeUpdates(gpsLocationListener);
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