package com.ntek.testgpsapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ntek.testgpsapp.DAO.UserDAO;
import com.ntek.testgpsapp.Entity.User;

import java.util.List;

public class SignupActivity extends AppCompatActivity {
    private AppDatabase db;
    EditText id, pw, pwChk, email;
    AppCompatButton joinBtn;
    String uId, uPw, uEmail, uPwChk;

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


        // 에러메세지
        TextView tv_errorMsg_id = findViewById(R.id.errorMsg_signId);
        TextView tv_errorMsg_pw = findViewById(R.id.errorMsg_signPW);
        TextView tv_errorMsg_email = findViewById(R.id.errorMsg_signEmail);

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

        // 입력필드 값 변경 이벤트
        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable editable) {
                uId = id.getText().toString();

                if(nullVerify(uId)==false){ // 입력값 없을 경우
                    tv_errorMsg_id.setText("아이디는 필수입력입니다.");    //경고메세지
                    tv_errorMsg_id.setTextColor(R.color.error);
                    id.setBackgroundResource(R.drawable.error_input);   //테두리 변경
                }else{
                    tv_errorMsg_id.setText("");    //경고메세지 제거
                    id.setBackgroundResource(R.drawable.unfocus_input_text);   //테두리 변경
                }
            }
        });

        pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable editable) {
                uPw = pw.getText().toString();

                if(nullVerify(uPw)==false){ // 입력값 없을 경우
                    tv_errorMsg_pw.setText("비밀번호는 필수입력입니다.");    //경고메세지
                    tv_errorMsg_pw.setTextColor(R.color.error);
                    pw.setBackgroundResource(R.drawable.error_input);   //테두리 변경
                }else{
                    tv_errorMsg_pw.setText("");    //경고메세지 제거
                    pw.setBackgroundResource(R.drawable.unfocus_input_text);   //테두리 변경
                }
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void afterTextChanged(Editable s) {
                if(!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()){
                    tv_errorMsg_email.setText("이메일 형식으로 입력해주세요");
                    tv_errorMsg_email.setTextColor(R.color.error);
                    email.setBackgroundResource(R.drawable.error_input);
                }else{
                    tv_errorMsg_email.setText("");
                    email.setBackgroundResource(R.drawable.unfocus_input_text);
                }
            }
        });


        /*
        * 회원가입 버튼 클릭
        * */
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uId = id.getText().toString();
                uPw = pw.getText().toString();
                uEmail = email.getText().toString();

                // data 객체
                User user = new User(uId,uPw,uEmail);
                
                // 필수값을 입력 안했을 경우
                if(uId.length() == 0 ||uPw.length() == 0 ||uEmail.length() == 0){
                    Toast.makeText(SignupActivity.this, "입력 값이 없습니다.", Toast.LENGTH_SHORT).show();
                    id.requestFocus();
                }else{
                    //abstract interface 구현 -> UserDAO를 사용하여 db 저장
                    db.userDao().insertAll(user);
                    Toast.makeText(SignupActivity.this, "회원가입 완료!", Toast.LENGTH_SHORT).show();

                    //로그인 화면으로 전환
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
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