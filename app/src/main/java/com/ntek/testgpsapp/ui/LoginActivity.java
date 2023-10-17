package com.ntek.testgpsapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    private AppDatabase db; //데이터베이스
    TextView sign;  //회원가입 버튼
    TextInputEditText id, pw;   //아이디, 비밀번호 입력필드
    AppCompatButton loginBtn;   //로그인 버튼
    LinearLayout outSide;   //입력필드 외 영역

    String uId, uPw;    //아이디, 비밀번호 입력값

    Intent gpsInt;  // gpsService intent
    GpsService gpsService;  //gpsService 객체
//    ServiceConnection conn = new ServiceConnection() {  //서비스 연결 여부
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            GpsService.GpsBinder gb = (GpsService.GpsBinder)iBinder;
//            gpsService = gb.getService();
//            isService = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            isService = false;
//        }
//    };


//    LocationManager locationMng;
//    Location loc_current;
//    double lon, lat, alt; //위도, 경도, 고도
//    LocalDateTime today;    //현재 연월일시
//    String formatedNow; //현재 연월일시 포맷팅
    int gpsSeq; //위치정보 순번
//    int totalNum;   //위치정보 데이터개수

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

//        locationMng = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        loc_current = locationMng.getLastKnownLocation(LocationManager.GPS_PROVIDER);   //현재위치정보
//
//        //위도,경도,고도 초기값
//        lon = 0.0;
//        lat = 0.0;
//        alt = 0.0;
//
//        totalNum = db.gpsDao().gpsDataNumber(); //위치정보데이터 개수
//        gpsSeq = totalNum + 1;  //위치정보데이터 순번
//        today = LocalDateTime.now();    //현재 시간
//        formatedNow = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));   //시간 포맷
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
                        List<Gps> findBySeq = db.gpsDao().findByGpsSeq(gpsSeq);
                        if(findBySeq.size()>0){
                            Log.i("LoginActivity","중복 순번 발견");
                        }else{
                            // 위치정보 서비스 시작
                            gpsInt = new Intent(LoginActivity.this, GpsService.class);
                            startForegroundService(gpsInt);
                            gpsInt.putExtra("id",uId);
                            gpsService.onBind(gpsInt);
                        }

                        Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();

                        Intent logInt = new Intent(LoginActivity.this,MainActivity.class);
                        logInt.putExtra("id",uId);
                        logInt.putExtra("pw",uPw);

                        startActivity(logInt);  //메인화면으로 이동
                }else{
                    Toast.makeText(LoginActivity.this, "로그인 실패..", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    //위치정보 데이터 저장
//    public void gpsData(String uId){
//        // 위도, 경도, 고도
//        lon = loc_current.getLongitude();
//        lat = loc_current.getLatitude();
//        alt = loc_current.getAltitude();
//
//        // 데이터 객체
//        Gps gps =  new Gps(gpsSeq,uId,lat,lon,alt,formatedNow);
//        db.gpsDao().insertAll(gps); // db에 로그인 유저 아이디, 위치정보 저장
//    }


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
//    final LocationListener gpsLocationListener = new LocationListener() {
//        @Override
//        public void onLocationChanged(@NonNull Location location) {
//            // 위도, 경도, 고도
//            lon = loc_current.getLongitude();
//            lat = loc_current.getLatitude();
//            alt = loc_current.getAltitude();
//
//            List<Gps> findBySeq = db.gpsDao().findByGpsSeq(gpsSeq);
//            if(findBySeq.size()>0){
//                totalNum = db.gpsDao().gpsDataNumber(); //위치정보데이터 개수
//                gpsSeq = totalNum + 1;  //위치정보데이터 순번
//            }
//
//            // 데이터 객체
//            Gps gps = new Gps(gpsSeq,uId,lat,lon,alt,formatedNow);
//            db.gpsDao().insertAll(gps); // db에 로그인 유저 아이디, 위치정보 저장
//        }
//    };


    @Override
    protected void onPause() {
        super.onPause();
        Log.i("LoginActivity","onPause Called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("LoginActivity","onStop Called");
//        locationMng.removeUpdates(gpsLocationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("LoginActivity","onDestroy Called");
        stopService(gpsInt);    //서비스 종료
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("LoginActivity","onRestart Called");

        // 입력한 아이디, 비밀번호
//        uId = id.getText().toString();
//        uPw = pw.getText().toString();

//        List<User> findByUserAssign = db.userDao().findByUserAssign(uId, uPw);  // db에서 해당 계정 찾기
//
//        if(findByUserAssign.size() > 0){ // 로그인 계정이 맞으면 업데이트
//            totalNum = db.gpsDao().gpsDataNumber(); //위치정보데이터 개수
//            gpsSeq = totalNum + 1;  //위치정보데이터 순번
//            locationMng.requestLocationUpdates(LocationManager.GPS_PROVIDER,100000,10.0f,gpsLocationListener);
//            locationMng.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100000,10.0f,gpsLocationListener);
//        }

    }


}