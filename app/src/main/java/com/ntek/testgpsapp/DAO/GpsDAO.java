package com.ntek.testgpsapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.ntek.testgpsapp.Entity.Gps;

import java.util.List;

@Dao
public interface GpsDAO {
    // 좌표 목록 조회
    @Query("SELECT * FROM gps")
    List<Gps> getGpsList();

    // 좌표 아이디 오름차순 조회
    @Query("SELECT * FROM gps ORDER BY gps_id ASC")
    List<Gps> listOrderByAsc();

    // 좌표 아이디 내림차순 조회
    @Query("SELECT * FROM gps ORDER BY gps_id DESC")
    List<Gps> listOrderByDesc();

    // 좌표 저장
    @Insert
    void insertAll(Gps... gps);

    // 데이터 갯수 조회
    @Query("SELECT COUNT(*) FROM gps")
    int gpsDataNumber();

    // 순번으로 조회
    @Query("SELECT * FROM gps where gps_seq = :gpsSeq")
    List<Gps> findByGpsSeq(int gpsSeq);


}
