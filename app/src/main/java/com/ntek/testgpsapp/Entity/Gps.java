package com.ntek.testgpsapp.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Gps {
    //접속 아이디
    @PrimaryKey
    @NonNull
    public String GPS_uid;

    //경도
    @ColumnInfo(name = "lat")
    public int lat;

    //위도
    @ColumnInfo(name = "lon")
    public int lon;

    //고도
    @ColumnInfo(name = "alt")
    public int alt;


}
