package com.ntek.testgpsapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ntek.testgpsapp.Entity.Gps;

import java.util.List;

@Dao
public interface GpsDAO {
    // 좌표 목록 조회
    @Query("SELECT * FROM Gps")
    List<Gps> getGpsList();

    // 좌표 아이디 오름차순 조회
    @Query("SELECT * FROM Gps ORDER BY GPS_uid ASC")
    List<Gps> listOrderByAsc();

    // 좌표 아이디 내림차순 조회
    @Query("SELECT * FROM Gps ORDER BY GPS_uid DESC")
    List<Gps> listOrderByDesc();

    // 좌표 저장
    @Insert
    void insertAll(Gps... gps);


}
