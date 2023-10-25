package com.ntek.testgpsapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteConstraintException;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ntek.testgpsapp.GpsService;
import com.ntek.testgpsapp.R;
import com.ntek.testgpsapp.persistance.AppDatabase;
import com.ntek.testgpsapp.persistance.Entity.Gps;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;

    //google play 서비스 클라이언트(기기 위치정보 가져오는 메서드 제공)
    private FusedLocationProviderClient mFusedLocationClient;
    LatLng defaultLocation; // 기본 좌표값
    private AppDatabase db; //데이터베이스
    List<Gps> savedGpsList; //저장된 위치정보 목록
    Button savedGpsBtn; //저장된위치
    Location loc_current;   //현재위치를 담을 location 객체
    Intent gpsInt;  //gpsService 인텐트
    MarkerOptions markerOptions;    //지도 마커

    int permissionCheck; //위치권한여부 체크
    boolean locationPermissionGranted;  //위치정보권한 허용여부
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION=1001;    //권한허용코드


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //tool bar
        Toolbar toolbar = (Toolbar)findViewById(R.id.map_toolbar);
        this.setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 추가

        // 데이터베이스 인스턴스 생성
        db = AppDatabase.getInstance(this);

        // xml 객체 뷰 바인딩
        savedGpsBtn = findViewById(R.id.savedGps_button);

        // 지도 마커 인스턴스 생성
        markerOptions = new MarkerOptions();

        // 맵 객체와 뷰 연결
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);


        //위치 정보를 제공하는 클라이언트 객체
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //회원위치정보저장 서비스 인텐트 생성
        gpsInt = new Intent(MapActivity.this, GpsService.class);

        // 디폴트 좌표값을 서울로 설정
        defaultLocation = new LatLng(37.556, 126.97);

        // 위치권한여부 체크
        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            locationPermissionGranted = true;
        }else{
            locationPermissionGranted = false;
        }

    }

    // 뒤로가기 선택 시 이전화면으로 이동
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.back_button) {
            Log.e("MapActivity","뒤로가기 선택됨");
            Intent mainInt = new Intent(MapActivity.this, MainActivity.class);
            startActivity(mainInt);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        stopService(gpsInt);    // 사용자 위치정보저장 서비스 종료
        Log.d("MapActivity","stop Service Success");
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onResume() {
        super.onResume();
        try {
            savedGpsList = db.gpsDao().listOrderByDesc();
        } catch (SQLiteConstraintException e) {
            Log.e("SQLiteConstraintException: %s", e.getMessage());
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;

        // 위치권한 여부에 따라 지도 현재위치 관련 지도ui 업데이트
        updateLocationUI();

        // 기기 위치정보 가져오고 지도 포지션 설정
        getDeivceLocation();

        // 위치정보가져오기버튼 클릭이벤트
        savedGpsBtn.setOnClickListener(v -> {
            LatLng savedGps = null;

            try {
                for(Gps ele : savedGpsList){    //db에 저장된 회원위치정보 지도에 마커생성
                    savedGps = new LatLng(ele.getLat(),ele.getLon());
                    // 1. 마커 옵션 설정
                    markerOptions
                            .position(new LatLng(ele.getLat(),ele.getLon()))
                            .title(ele.getGps_uid())    // 타이틀
                            .snippet("순번"+ele.getGps_seq());

                    // 2. 마커 생성
                    mMap.addMarker(markerOptions);
                }
                // 마지막 위치정보 위도
                Log.d("MapActivity","last savedGps lat: " + savedGps.latitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(savedGps, 10));
            } catch (Exception e) {
                Log.e("Exception: %s", e.getMessage());
                getDeivceLocation();
            }

        });


    }

    //위치정보 권한 요청
    public void accessGps(Activity activity){
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);

            locationPermissionGranted = true;
        }else{
            locationPermissionGranted = false;
        }
    }

    
    //위치정보 권한요청 응답
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        locationPermissionGranted = false;

        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                } else {
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }

        updateLocationUI();
    }
    
    // 위치권한 여부에 따라 지도에 현재위치 관련 ui 업데이트
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {    //위치권한이 허용인 경우 지도에서 현재위치 ui 활성화
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {    //위치권한이 거부인 경우 지도에서 현재위치 ui 비활성화, 권한 요청
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                loc_current = null;
                accessGps(this);    // 위치정보권한요청
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    // 현재위치정보 여부에 따라 지도 포지션 설정
    @SuppressLint("MissingPermission")
    private void getDeivceLocation(){
        try {
            if(locationPermissionGranted){
                //가장 최근 위치 정보를 가져옴
                Task<Location> locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if(task.isSuccessful()){    // 최근 위치정보 가져오는데 성공시
                            loc_current = task.getResult(); // task에서 현재위치정보 가져오기
                            LatLng resultLatLng = new LatLng(loc_current.getLatitude(),loc_current.getLongitude());

                            if(loc_current != null){    //현재위치정보 값이 있으면 해당 위치에 포커스
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        resultLatLng, 10));
                                // 1. 마커 옵션 설정
                                markerOptions
                                        .position(resultLatLng)
                                        .title("현재위치");    // 타이틀

                                // 2. 마커 생성
                                mMap.addMarker(markerOptions);
                                // 기본 위치로 포커스
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(resultLatLng, 10));
                            }else{  //현재위치정보 값이 없으면 기본위치인 서울에 포커스
                                // 1. 마커 옵션 설정
                                markerOptions
                                        .position(defaultLocation)
                                        .title("서울");    // 타이틀

                                // 2. 마커 생성
                                mMap.addMarker(markerOptions);
                                // 기본 위치로 포커스
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
                            }
                        }else{ // 최근 위치정보 가져오는데 실패시
                            Log.d("MapActivity","Current location is null. Using defaults.");
                            Log.e("MapActivity","Exception: %s",task.getException());
                            mMap.getUiSettings().setMyLocationButtonEnabled(false); //현재위치가져오기 버튼 비활성화

                            // 1. 마커 옵션 설정
                            markerOptions
                                    .position(defaultLocation)
                                    .title("서울");    // 타이틀

                            // 2. 마커 생성
                            mMap.addMarker(markerOptions);
                            // 기본 위치로 포커스
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
                            // 기본 위치로 포커스
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
                        }
                    }
                });

            }
        } catch (SecurityException e) {
            Log.e("Exception: %s",e.getMessage(), e);
        }
    }


}