package com.ntek.testgpsapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GpsService extends Service {

    int value = 0;
    public GpsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }
}