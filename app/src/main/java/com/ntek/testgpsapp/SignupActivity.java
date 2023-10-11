package com.ntek.testgpsapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ntek.testgpsapp.DAO.UserDAO;
import com.ntek.testgpsapp.Entity.User;

public class SignupActivity extends AppCompatActivity {

    EditText id, pw, email;
    String uId, uPw, uEmail;
    User user;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        id = (EditText)findViewById(R.id.signID);
        pw = (EditText)findViewById(R.id.signPW);
        email = (EditText)findViewById(R.id.signEmail);

        // appbar 설정
        getSupportActionBar().setTitle("회원가입");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 데이터베이스 인스턴스 생성
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "AppDatabase").build();
        UserDAO userDAO = db.userDao(); //DAO 인스턴스

        AppCompatButton joinBtn = findViewById(R.id.signUpButton);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uId = id.getText().toString();
                uPw = pw.getText().toString();
                uEmail = email.getText().toString();

                user.uid = uId;
                user.pw = uPw;
                user.email = uEmail;

                userDAO.insertAll(user);    //회원정보 저장

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:{
                //액티비티 이동
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}