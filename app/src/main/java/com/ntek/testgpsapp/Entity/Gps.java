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
    public double lat;

    //위도
    public double lon;

    //고도
    public double alt;

    public Gps(@NonNull String GPS_uid, double lat, double lon, double alt) {
        this.GPS_uid = GPS_uid;
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
    }

    @NonNull
    public String getGPS_uid() {
        return GPS_uid;
    }

    public void setGPS_uid(@NonNull String GPS_uid) {
        this.GPS_uid = GPS_uid;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getAlt() {
        return alt;
    }

    public void setAlt(double alt) {
        this.alt = alt;
    }
}
