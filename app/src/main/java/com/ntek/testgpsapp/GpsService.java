package com.ntek.testgpsapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
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

    LocationManager locationMng; //위치정보매니저
    Location loc_current; //현재위치를 담을 location 객체
    double lon, lat, alt; //위도, 경도, 고도
    LocalDateTime today;    //현재 연월일시
    String formatedNow; //현재 연월일시 포맷팅
    int gpsSeq; //위치정보 순번
    int totalNum;   //위치정보 데이터개수
    long gps_seconds; //위치정보 업데이트 시간

    @SuppressLint({"MissingPermission", "LongLogTag"})
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("GpsService","onCreate Called");

        // 데이터베이스 인스턴스 생성
        db = AppDatabase.getInstance(this);

        locationMng = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //현재위치정보
        loc_current = locationMng.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        //위도,경도,고도 초기값
        lon = 0.0;
        lat = 0.0;
        alt = 0.0;

        try {
            totalNum = db.gpsDao().gpsDataNumber(); //위치정보데이터 개수
        } catch (SQLiteConstraintException e) {
            Log.e("SQLiteConstraintException: %s", e.getMessage());
        }
        gpsSeq = totalNum + 1;  //위치정보데이터 순번

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            today = LocalDateTime.now();    //현재 시간
            formatedNow = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));   //시간 포맷
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("GpsService","onStartCommand Called");
        if(intent == null){
            return START_STICKY;
        }else{
            uId = intent.getStringExtra("id");
            gps_seconds = intent.getLongExtra("gpsSeconds",10000);
            Log.d("GpsService","gps_seconds: "+gps_seconds);
            notification(); // 알림 상태바 생성 메소드
        }


        Handler mHandler = new Handler(Looper.getMainLooper());

        mHandler.post(new Runnable() { //비동기적으로 실행
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                //위치정보 업데이트
                // 이동통신 기지국 또는 와이파이 접속 장소 기준으로 위치정보 측정
                locationMng.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,gps_seconds,0,gpsLocationListener);
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    //알림 상태바 생성
    private void notification(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel =
                    new NotificationChannel(CHANNEL_ID, "testGpsApp 서비스 알림 설정", NotificationManager.IMPORTANCE_LOW);    //벨소리, 진동 x
            serviceChannel.setDescription("testGpsApp 위치정보저장 서비스");

            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null; //null 체크
            manager.createNotificationChannel(serviceChannel);
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        // notification은 notification 매니저가 다른 프로세스에서 수행하기 때문에 pendingIntent 사용 필수
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("위치정보 저장 알림")
                .setContentText("현재 위치정보가 저장되고 있습니다.")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }


    //위치정보 업데이트
    final LocationListener gpsLocationListener = new LocationListener() {
        @SuppressLint("LongLogTag")
        @Override
        public void onLocationChanged(@NonNull Location location) {
            // 위도, 경도, 고도
            lon = loc_current.getLongitude();
            lat = loc_current.getLatitude();
            alt = loc_current.getAltitude();

            List<Gps> findBySeq = null; //같은 순번의 데이터가 있는지 확인
            try {
                findBySeq = db.gpsDao().findByGpsSeq(gpsSeq);
            } catch (SQLiteConstraintException e) {
                Log.e("SQLiteConstraintException: %s", e.getMessage());
            }

            if(findBySeq.size()>0){
                try {
                    totalNum = db.gpsDao().gpsDataNumber(); //위치정보데이터 개수
                } catch (SQLiteConstraintException e) {
                    Log.e("SQLiteConstraintException: %s", e.getMessage());
                }
                gpsSeq = totalNum + 1;  //위치정보데이터 순번 업데이트
            }

            // 데이터 객체
            Gps gps = new Gps(gpsSeq,uId,lat,lon,alt,formatedNow);

            try {
                db.gpsDao().insertAll(gps); // db에 로그인 유저 아이디, 위치정보 저장
            } catch (SQLiteConstraintException e) {
                Log.e("SQLiteConstraintException: %s", e.getMessage());
            }

            Log.d("GpsService","위도: "+lon+" 경도: "+lat+" 고도: "+alt);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("GpsService","onBind Called");
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("GpsService","onDestroy Called");
        locationMng.removeUpdates(gpsLocationListener); //위치정보 업데이트 중지
    }


}