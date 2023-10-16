package com.ntek.testgpsapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ntek.testgpsapp.Entity.User;

import java.util.List;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private AppDatabase db; //데이터베이스
    EditText id, pw, pwChk, email;  //입력필드(아이디, 비밀번호, 비밀번호확인, 이메일)
    AppCompatButton joinBtn, pwChkBtn;  //회원가입버튼, 비밀번호확인버튼
    ScrollView outSide; // 로그인 전체 레이아웃 뷰
    String uId, uPw, uEmail, uPwChk;    //입력id, 비밀번호, 이메일, 비밀번호확인

    /* 에러확인 메세지(아이디,비밀번호,비밀번호확인,이메일)
    * */
    TextView tv_errorMsg_id;
    TextView tv_errorMsg_pw;
    TextView tv_errorMsg_pw2;
    TextView tv_errorMsg_email;

    final String ALLOW_ID_PATTERN = "^[a-zA-Z0-9]{5,12}+$"; // 영문 숫자 조합 6~12자리
    final String ALLOW_PW_PATTERN = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{6,16}+$"; // 영문 대소문자 + 숫자 + 특수문자 조합으로 6 ~ 16자리


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.i("SignupActivity","onPostCreate Called");
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
        pwChk = findViewById(R.id.signPW2);
        joinBtn = findViewById(R.id.signUpButton);
        pwChkBtn = findViewById(R.id.pwcheckbutton);
         // 에러메세지
        tv_errorMsg_id = findViewById(R.id.errorMsg_signId);
        tv_errorMsg_pw = findViewById(R.id.errorMsg_signPW);
        tv_errorMsg_pw2 = findViewById(R.id.errorMsg_signPW2);
        tv_errorMsg_email = findViewById(R.id.errorMsg_signEmail);
         // 필드 외 영역
        outSide = findViewById(R.id.layout_signUp_outSide);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("SignupActivity","onStart Called");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("SignupActivity","onResume Called");

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


        // 입력필드 값 검증
        verifyOfInput();

        /*
         * 회원가입 버튼 클릭
         * */
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uId = id.getText().toString();
                uPw = pw.getText().toString();
                uPwChk = pwChk.getText().toString();
                uEmail = email.getText().toString();

                // data 객체
                User user = new User(uId,uPw,uEmail);

                // 필수값을 입력 안했을 경우
                if(uId.length() == 0 ||uPw.length() == 0 ||uEmail.length() == 0){
                    Toast.makeText(SignupActivity.this, "입력 값이 없습니다.", Toast.LENGTH_SHORT).show();
                    id.requestFocus();
                }else if(uPw.length() != uPwChk.length() || uPw.length() < 6){
                    Toast.makeText(SignupActivity.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    pw.requestFocus();
                }else if(!Patterns.EMAIL_ADDRESS.matcher(uEmail.toString()).matches()){
                    Toast.makeText(SignupActivity.this, "이메일형식을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    email.requestFocus();
                }
                else{
                    //abstract interface 구현 -> UserDAO를 사용하여 db 저장
                    db.userDao().insertAll(user);
                    Toast.makeText(SignupActivity.this, "회원가입 완료!", Toast.LENGTH_SHORT).show();

                    //로그인 화면으로 전환
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }

            }
        });

        /*
         * 비밀번호 확인 버튼 클릭
         * */
        pwChkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uPw = pw.getText().toString();
                uPwChk = pwChk.getText().toString();

                if(uPw.length() != uPwChk.length()){
                    tv_errorMsg_pw2.setText("비밀번호가 일치하지 않습니다.");    // 경고메세지
                    pwChk.setBackgroundResource(R.drawable.error_input);   //테두리 변경
                    tv_errorMsg_pw2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error)); //메세지 색상변경

                }else{
                    tv_errorMsg_pw2.setText("비밀번호가 일치합니다.");   // 확인메세지
                    tv_errorMsg_pw2.setTextColor( Color.parseColor("#34C759")); //메세지 색상변경
                    pwChk.setBackgroundResource(R.drawable.success_input);   //테두리 변경
                }
            }
        });
    }

    // 입력값 검증
    public void verifyOfInput(){
        id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 아이디중복 확인
                List<User> findByUserId =  db.userDao().findByUserId(charSequence.toString());
                if(findByUserId.size()>0){
                    tv_errorMsg_id.setText("이미 존재하는 아이디입니다.");    //경고메세지
                    id.setBackgroundResource(R.drawable.error_input);   //테두리 변경
                    tv_errorMsg_id.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error)); //메세지 색상변경

                }else if(charSequence.equals("")){  //공백 입력
                    tv_errorMsg_id.setText("공백은 입력할 수 없습니다.");    //경고메세지
                    id.setBackgroundResource(R.drawable.error_input);   //테두리 변경
                    tv_errorMsg_id.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error)); //메세지 색상변경
                }
                else{
                    tv_errorMsg_id.setText("");    //경고메세지 제거
                    id.setBackgroundResource(R.drawable.success_input);   //테두리 변경

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                List<User> findByUserId =  db.userDao().findByUserId(editable.toString());
                if(nullVerify(editable.toString())==false){ // 입력값 없을 경우
                    tv_errorMsg_id.setText("아이디는 필수입력입니다.");    //경고메세지
                    id.setBackgroundResource(R.drawable.error_input);   //테두리 변경
                    tv_errorMsg_id.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error)); //메세지 색상변경

                }else if(findByUserId.size()>0){
                    tv_errorMsg_id.setText("이미 존재하는 아이디입니다.");    //경고메세지
                    id.setBackgroundResource(R.drawable.error_input);   //테두리 변경
                    tv_errorMsg_id.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error)); //메세지 색상변경

                }else if(!Pattern.compile(ALLOW_ID_PATTERN).matcher(editable.toString()).matches()){
                    //입력값이 영문 숫자 조합이 아닌 경우
                    tv_errorMsg_id.setText("아이디는 영문 숫자 조합 6~12자리만 가능합니다.");    //경고메세지
                    id.setBackgroundResource(R.drawable.error_input);   //테두리 변경
                    tv_errorMsg_id.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error)); //메세지 색상변경
                }
                else{
                    tv_errorMsg_id.setText("");    //경고메세지 제거
                    id.setBackgroundResource(R.drawable.success_input);   //테두리 변경

                }
            }
        });

        pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(nullVerify(charSequence.toString())==false){ // 입력값 없을 경우
                    tv_errorMsg_pw.setText("비밀번호는 필수입력입니다.");    //경고메세지
                    pw.setBackgroundResource(R.drawable.error_input);   //테두리 변경
                    tv_errorMsg_pw.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error)); //메세지 색상변경

                }
                else{
                    tv_errorMsg_pw.setText("");    //경고메세지 제거
                    pw.setBackgroundResource(R.drawable.success_input);   //테두리 변경

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(nullVerify(editable.toString())==false){
                    tv_errorMsg_pw.setText("비밀번호는 필수입력입니다.");    //경고메세지
                    pw.setBackgroundResource(R.drawable.error_input);   //테두리 변경
                    tv_errorMsg_pw.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error)); //메세지 색상변경

                }else if(!Pattern.compile(ALLOW_PW_PATTERN).matcher(editable.toString()).matches()){
                    //입력값이 형식에 맞지 않는 경우
                    tv_errorMsg_pw.setText("비밀번호는 대소문자, 숫자, 특수문자 조합 6~16자리만 가능합니다.");    //경고메세지
                    pw.setBackgroundResource(R.drawable.error_input);   //테두리 변경
                    tv_errorMsg_pw.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error)); //메세지 색상변경
                }
                else{
                    tv_errorMsg_pw.setText("");    //경고메세지 제거
                    pw.setBackgroundResource(R.drawable.success_input);   //테두리 변경

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

            @Override
            public void afterTextChanged(Editable e) {
                if(!Patterns.EMAIL_ADDRESS.matcher(e.toString()).matches()){
                    tv_errorMsg_email.setText("이메일 형식으로 입력해주세요");
                    email.setBackgroundResource(R.drawable.error_input);
                    tv_errorMsg_email.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.error)); //메세지 색상변경

                }else{
                    tv_errorMsg_email.setText("");
                    email.setBackgroundResource(R.drawable.success_input);

                }
            }
        });
    }

    /*
     * appbar에서 뒤로가기 눌렀을 때 로그인 화면으로 이동
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Log.i("SignupActivity","onOptionsItemSelected Called");
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

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("SignupActivity","onStop Called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("SignupActivity","onDestroy Called");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("SignupActivity","onPause Called");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("SignupActivity","onRestart Called");
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



}