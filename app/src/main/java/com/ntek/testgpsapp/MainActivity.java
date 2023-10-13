package com.ntek.testgpsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

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

        // adapter 준비 및 연결
        TabPagerAdapter adapter = new TabPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // TabLayout, ViewPager 연결
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(str[position])
        ).attach();


    }

    //appbar 메뉴 옵션 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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