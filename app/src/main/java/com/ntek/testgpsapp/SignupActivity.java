package com.ntek.testgpsapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.ntek.testgpsapp.DAO.UserDAO;
import com.ntek.testgpsapp.Entity.User;

import java.util.List;

public class SignupActivity extends AppCompatActivity {
    private AppDatabase db;
    EditText id, pw, email;
    AppCompatButton joinBtn;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        // appbar 설정
        getSupportActionBar().setTitle("회원가입");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 데이터베이스 인스턴스 생성
        db = AppDatabase.getInstance(this);

        // xml객체 뷰 바인딩
        id = findViewById(R.id.signID);
        pw = findViewById(R.id.signPW);
        email = findViewById(R.id.signEmail);
        joinBtn = findViewById(R.id.signUpButton);

        ScrollView outSide = findViewById(R.id.layout_signUp_outSide);
        final InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        // 필드 영역 외 터치
        outSide.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // 포커스 해제
                id.clearFocus();
                pw.clearFocus();
                email.clearFocus();

                // 키보드 내리기
                manager.hideSoftInputFromWindow(view.getWindowToken(),0);

                return false;
            }
        });



        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uId = id.getText().toString();
                String uPw = pw.getText().toString();
                String uEmail = email.getText().toString();

                // data 객체
                User user = new User(uId,uPw,uEmail);

                if(nullVerify(uId,uPw,uEmail)==false){  //입력 값이 없으면
                    Toast.makeText(SignupActivity.this, "입력 값이 없습니다.", Toast.LENGTH_SHORT).show();
                    id.requestFocus();
                }else{
                    //abstract interface 구현 -> UserDAO를 사용하여 db 저장
                    db.userDao().insertAll(user);
                }

            }
        });


    }


    /*
    * NOT NULL 확인 메소드
    * */
    private boolean nullVerify(String... params){
        for(String param : params){
            if(param == null || param.isEmpty()){
                return false;
            }
        }
        return true;
    }

    /*
    * 입력 값 검증 메소드
    * */
    private void inputVerify(String userId,String userEmail){
        List<User> findByIdList = db.userDao().findByUserId(userId);
        List<User> findByEmailList = db.userDao().findByEmail(userEmail);


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