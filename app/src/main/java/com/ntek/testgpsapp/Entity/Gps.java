package com.ntek.testgpsapp.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.util.Date;

@Entity(tableName = "gps")
public class Gps {

    //순번
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "gps_seq")
    public int gps_seq;

    //접속자 아이디
    @ColumnInfo(name = "gps_id")
    @NonNull
    public String GPS_uid;

    //경도
    @ColumnInfo(name = "gps_lat")
    public double lat;

    //위도
    @ColumnInfo(name = "gps_lon")
    public double lon;

    //고도
    @ColumnInfo(name = "gps_alt")
    public double alt;

    //등록일시
    @ColumnInfo(name = "gps_reg_date")
    public String reg_date;

    public Gps(int gps_seq, String GPS_uid, double lat, double lon, double alt, String reg_date) {
        this.gps_seq = gps_seq;
        this.GPS_uid = GPS_uid;
        this.lat = lat;
        this.lon = lon;
        this.alt = alt;
        this.reg_date = reg_date;
    }

    public int getGps_seq() {
        return gps_seq;
    }

    public void setGps_seq(int gps_seq) {
        this.gps_seq = gps_seq;
    }

    public String getGPS_uid() {
        return GPS_uid;
    }

    public void setGPS_uid(String GPS_uid) {
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

    public String getReg_date() {
        return reg_date;
    }

    public void setReg_date(String reg_date) {
        this.reg_date = reg_date;
    }
}
