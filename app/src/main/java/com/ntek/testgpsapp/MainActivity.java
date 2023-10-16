package com.ntek.testgpsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;
    TabLayout tabLayout;
    ViewPager2 viewPager;

    String[] str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity","onCreate Called");
        setContentView(R.layout.activity_main);

        str = new String[] { "아이디 내림차순", "아이디 오름차순"};

        // appbar 타이틀 및 배경색 변경
        getSupportActionBar().setTitle("위치정보 목록");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F7F2FA")));

        // 데이터베이스 인스턴스 생성
        db = AppDatabase.getInstance(this);

        // xml객체 뷰 바인딩
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);
        viewPager.setNestedScrollingEnabled(true);

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

        if(item.getItemId() == R.id.logOut_action){
            logOutAction(); //로그아웃 확인 다이얼로그 생성
            return true;
        }else{
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

        // adapter 준비 및 연결
        TabPagerAdapter adapter = new TabPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // TabLayout, ViewPager 연결
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(str[position])
        ).attach();
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
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("MainActivity","onRestart Called");
    }

}