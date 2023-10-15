package com.ntek.testgpsapp;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;

import com.ntek.testgpsapp.DAO.GpsDAO;
import com.ntek.testgpsapp.DAO.UserDAO;
import com.ntek.testgpsapp.Entity.Gps;
import com.ntek.testgpsapp.Entity.User;

@Database(entities = {User.class, Gps.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase database;
    private static String DATABASE_NAME = "appDB";
    public synchronized static AppDatabase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract UserDAO userDao();
    public abstract GpsDAO gpsDao();


}
