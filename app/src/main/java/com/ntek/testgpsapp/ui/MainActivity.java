package com.ntek.testgpsapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ntek.testgpsapp.GpsService;
import com.ntek.testgpsapp.R;
import com.ntek.testgpsapp.persistance.AppDatabase;


public class MainActivity extends AppCompatActivity{
    private AppDatabase db;
    TabLayout tabLayout;
    ViewPager2 viewPager;

    String[] str;

    Intent gpsInt;
    Intent mainInt;
    long gps_seconds;    //위치정보 업데이트 시간
    String loginId;
    FloatingActionButton mapButton; //지도화면 바로가기 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity","onCreate Called");
        setContentView(R.layout.activity_main);

        // 탭 타이틀
        str = new String[] { "아이디 내림차순", "아이디 오름차순"};

        // appbar생성
        getSupportActionBar().show();

        // 데이터베이스 인스턴스 생성
        db = AppDatabase.getInstance(this);

        // xml객체 뷰 바인딩
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewPager.setNestedScrollingEnabled(true);
        }
        mapButton = findViewById(R.id.map_button);

        gpsInt = new Intent(MainActivity.this, GpsService.class);
        mainInt = getIntent();   //현재 액티비티 인텐트
        loginId = mainInt.getStringExtra("id");  //로그인 아이디
        gpsInt.putExtra("id",loginId);
//        gpsInt.putExtra("gpsSeconds",gps_seconds); //위치정보 업데이트 시간
//        startForegroundService(gpsInt); //위치정보 실시간 저장 서비스 시작

    }

    //appbar 메뉴 옵션 추가(모양 변경)
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    //appbar 로그아웃 메뉴 클릭 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Log.i("MainActivity","onOptionsItemSelected called");

        if(item.getItemId() == R.id.action_logout){
            logOutAction(); //로그아웃 확인 다이얼로그 생성
            return true;
        }else if(item.getItemId() == R.id.action_10seconds){
            stopService(gpsInt);
            gps_seconds = 10000;
            gpsInt.putExtra("gps_seconds",gps_seconds);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(gpsInt);
            }
            return true;
        }else if(item.getItemId() == R.id.action_30seconds){
            stopService(gpsInt);
            gps_seconds = 30000;
            gpsInt.putExtra("gps_seconds",gps_seconds);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(gpsInt);
            }
            return true;
        }else if(item.getItemId() == R.id.action_60seconds){
            stopService(gpsInt);
            gps_seconds = 60000;
            gpsInt.putExtra("gps_seconds",gps_seconds);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(gpsInt);
            }
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    // 로그아웃 확인 다이얼로그
    private void logOutAction(){
        //alert dialog 생성
        new AlertDialog.Builder(this)
                .setTitle("알림")
                .setMessage("로그아웃 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() { //예 버튼 클릭 이벤트
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);  //로그인 화면으로 전환
                    }
                })
                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "로그아웃 취소", Toast.LENGTH_SHORT).show();
                    }
                }).show();

        }




    @Override
    protected void onStart() {
        super.onStart();
        Log.i("MainActivity","onStart Called");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("MainActivity","onResume Called");

        // adapter 준비 및 연결(Viewpager2에 프래그먼트 연결)
        TabPagerAdapter adapter = new TabPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // TabLayout과 ViewPager2 연결
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(str[position])
                //해당 배열의 값으로 탭구성 값 설정
        ).attach();

        //지도 버튼 클릭 시 지도화면으로 이동
        mapButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("MainActivity","onPause Called");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("MainActivity","onStop Called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("MainActivity","onDestroy Called");
        stopService(gpsInt);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MainActivity","onRestart Called");
    }

}