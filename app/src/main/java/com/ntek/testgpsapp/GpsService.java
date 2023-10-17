package com.ntek.testgpsapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.ntek.testgpsapp.persistance.AppDatabase;
import com.ntek.testgpsapp.persistance.Entity.Gps;
import com.ntek.testgpsapp.ui.MainActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class GpsService extends Service {
    private static final String CHANNEL_ID = "gpsService";
    private AppDatabase db; //데이터베이스
    String uId;    //로그인 아이디

    LocationManager locationMng;
    Location loc_current;
    double lon, lat, alt; //위도, 경도, 고도
    LocalDateTime today;    //현재 연월일시
    String formatedNow; //현재 연월일시 포맷팅
    int gpsSeq; //위치정보 순번
    int totalNum;   //위치정보 데이터개수


    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("GpsService","onCreate Called");

        // 데이터베이스 인스턴스 생성
        db = AppDatabase.getInstance(this);

        locationMng = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        loc_current = locationMng.getLastKnownLocation(LocationManager.GPS_PROVIDER);   //현재위치정보

        //가상에뮬레이터에서 좌표 null 오류나서 수정
        loc_current = locationMng.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        //위도,경도,고도 초기값
        lon = 0.0;
        lat = 0.0;
        alt = 0.0;

        totalNum = db.gpsDao().gpsDataNumber(); //위치정보데이터 개수
        gpsSeq = totalNum + 1;  //위치정보데이터 순번
        today = LocalDateTime.now();    //현재 시간
        formatedNow = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));   //시간 포맷
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("GpsService","onStartCommand Called");
        if(intent == null){
            return START_STICKY;
        }else{
            uId = intent.getStringExtra("id");
            Log.e("GpsService","intent: "+uId);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel =
                    new NotificationChannel(CHANNEL_ID, "testGpsApp 서비스 알림 설정", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(serviceChannel);
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("testGpsApp service 알림")
                .setContentText("foreground service가 실행중 입니다.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();


        startForeground(1, notification);

        Handler mHandler = new Handler(Looper.getMainLooper());

        mHandler.post(new Runnable() { //비동기적으로 실행
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                //10초 마다 업데이트
                locationMng.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,0,gpsLocationListener);
                locationMng.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,10000,0,gpsLocationListener);
            }
        });


        return super.onStartCommand(intent, flags, startId);
    }


    //위치정보 업데이트
    final LocationListener gpsLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            // 위도, 경도, 고도
            lon = loc_current.getLongitude();
            lat = loc_current.getLatitude();
            alt = loc_current.getAltitude();

            List<Gps> findBySeq = db.gpsDao().findByGpsSeq(gpsSeq); //같은 순번의 데이터가 있는지 확인
            if(findBySeq.size()>0){
                totalNum = db.gpsDao().gpsDataNumber(); //위치정보데이터 개수
                gpsSeq = totalNum + 1;  //위치정보데이터 순번 업데이트
            }

            // 데이터 객체
            Gps gps = new Gps(gpsSeq,uId,lat,lon,alt,formatedNow);
            db.gpsDao().insertAll(gps); // db에 로그인 유저 아이디, 위치정보 저장

            Log.e("GpsService","위도: "+lon+" 경도: "+lat+" 고도: "+alt);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("GpsService","onBind Called");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("GpsService","onDestroy Called");
        locationMng.removeUpdates(gpsLocationListener);
    }


}