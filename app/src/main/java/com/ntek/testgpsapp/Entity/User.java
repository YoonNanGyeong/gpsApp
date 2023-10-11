package com.ntek.testgpsapp.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    //아이디
    @PrimaryKey
    @NonNull
    public String uid;

    //비밀번호
    @ColumnInfo(name = "pw")
    public String pw;

    //이메일
    @ColumnInfo(name = "email")
    public String email;

}
