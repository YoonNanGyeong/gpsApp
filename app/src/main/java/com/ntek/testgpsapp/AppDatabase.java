package com.ntek.testgpsapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.ntek.testgpsapp.DAO.GpsDAO;
import com.ntek.testgpsapp.DAO.UserDAO;
import com.ntek.testgpsapp.Entity.Gps;
import com.ntek.testgpsapp.Entity.User;

@Database(entities = {User.class, Gps.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDAO userDao();
    public abstract GpsDAO gpsDao();
}
