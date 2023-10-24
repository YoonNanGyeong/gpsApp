package com.ntek.testgpsapp.persistance.DAO;

import android.content.Context;
import android.util.Log;

import com.ntek.testgpsapp.persistance.AppDatabase;
import com.ntek.testgpsapp.persistance.Entity.Gps;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class GpsDAOTest extends TestCase {

    private GpsDAO gpsDao;
    private AppDatabase db;

    LocalDateTime today;    //현재 연월일시
    String formatedNow; //현재 연월일시 포맷팅

    @Before
    public void create(){
        gpsDao = db.gpsDao();
    }
    @Test
    public void testListOrderByAsc() {
        List<Gps> ListOrderByAsc = gpsDao.listOrderByAsc();
        for(Gps ele:ListOrderByAsc){
            Log.i("testListOrderByAsc",ele.toString());
        }
    }

    @Test
    public void testListOrderByDesc() {
        List<Gps> ListOrderByDesc = gpsDao.listOrderByDesc();
        for(Gps ele:ListOrderByDesc){
            Log.i("testListOrderByDesc",ele.toString());
        }
    }

    @After
    public void testInsertAll() {
        Integer totalNum = gpsDao.gpsDataNumber();
        int gpsSeq = totalNum + 1 ;
        today = LocalDateTime.now();    //현재 시간
        formatedNow = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));   //시간 포맷

        // 테스트 gps 객체
        Gps testGps = new Gps(gpsSeq, "ntek00", 129.2527398, 35.5389653, 76.80000305175781, formatedNow);
        gpsDao.insertAll(testGps);  // gps 저장

    }
}