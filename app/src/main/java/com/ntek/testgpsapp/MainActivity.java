package com.ntek.testgpsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    // 전역변수
    TabLayout tabLayout;
    ViewPager2 viewPager;
    String[] str = new String[] { "아이디 내림차순", "아이디 오름차순"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("위치정보 목록");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xF7F2FA));

        // xml 연결
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


}