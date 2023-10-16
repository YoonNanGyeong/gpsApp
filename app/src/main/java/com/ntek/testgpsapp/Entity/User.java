package com.ntek.testgpsapp.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    //아이디
    @PrimaryKey
    @NonNull
    @ColumnInfo(name="userId")
    public String uid;

    //비밀번호
    @ColumnInfo(name = "userPw")
    public String pw;

    //이메일
    @ColumnInfo(name = "userEmail")
    public String email;

    public User(@NonNull String uid, String pw, String email) {
        this.uid = uid;
        this.pw = pw;
        this.email = email;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public void setUid(@NonNull String uid) {
        this.uid = uid;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
