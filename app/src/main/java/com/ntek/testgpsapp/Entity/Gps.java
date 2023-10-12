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
    public int lat;

    //위도
    public int lon;

    //고도
    public int alt;

    @NonNull
    public String getGPS_uid() {
        return GPS_uid;
    }

    public void setGPS_uid(@NonNull String GPS_uid) {
        this.GPS_uid = GPS_uid;
    }

    public int getLat() {
        return lat;
    }

    public void setLat(int lat) {
        this.lat = lat;
    }

    public int getLon() {
        return lon;
    }

    public void setLon(int lon) {
        this.lon = lon;
    }

    public int getAlt() {
        return alt;
    }

    public void setAlt(int alt) {
        this.alt = alt;
    }
}
