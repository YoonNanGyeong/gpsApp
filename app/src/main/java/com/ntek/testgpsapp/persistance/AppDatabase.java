package com.ntek.testgpsapp.persistance;

import android.content.Context;

import androidx.room.AutoMigration;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ntek.testgpsapp.persistance.DAO.GpsDAO;
import com.ntek.testgpsapp.persistance.DAO.UserDAO;
import com.ntek.testgpsapp.persistance.Entity.Gps;
import com.ntek.testgpsapp.persistance.Entity.User;

@Database(
        entities = {User.class, Gps.class}, version = 7,
        autoMigrations = {
                @AutoMigration(from = 5, to = 7)    //버전변경 시 데이터 자동 이전
        },
        exportSchema = true
)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database;
    private static String DATABASE_NAME = "appDB";

    public synchronized static AppDatabase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()   // 메인 쓰레드에서 데이터베이스 접근 허용
                    .fallbackToDestructiveMigration()
                    //마이그레이션을 찾을 수 없는 경우 테이블 삭제. 현재 버전 가져올 시 마이그레이션 집합 없는 경우 데이터베이스 다시 생성
                    .build();
        }
        return database;
    }

    public abstract UserDAO userDao();
    public abstract GpsDAO gpsDao();


}
