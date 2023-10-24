package com.ntek.testgpsapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ntek.testgpsapp.R;
import com.ntek.testgpsapp.persistance.AppDatabase;
import com.ntek.testgpsapp.persistance.Entity.Gps;

import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    private GoogleMap mMap;
    private AppDatabase db; //데이터베이스
    List<Gps> savedGpsList; //저장된 위치정보 목록
    double lat, lng;    //위도, 경도
    Button currentBtn, savedGpsBtn; //현재위치, 저장된위치 버튼


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // 데이터베이스 인스턴스 생성
        db = AppDatabase.getInstance(this);

        // xml 객체 뷰 바인딩
        currentBtn = findViewById(R.id.currentGps_button);
        savedGpsBtn = findViewById(R.id.savedGps_button);

        // 맵 객체와 뷰 연결
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        savedGpsList = db.gpsDao().listOrderByDesc();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {


        mMap = googleMap;

        // 현재위치버튼 클릭이벤트
        currentBtn.setOnClickListener(v -> {

        });

        // 위치정보가져오기버튼 클릭이벤트
        savedGpsBtn.setOnClickListener(v -> {
            LatLng savedGps = null;

            for(Gps ele : savedGpsList){
                savedGps = new LatLng(ele.getLat(),ele.getLon());
                // 1. 마커 옵션 설정
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions
                        .position(new LatLng(ele.getLat(),ele.getLon()))
                        .title(ele.getGps_uid())    // 타이틀
                        .snippet("순번"+ele.getGps_seq());

                // 2. 마커 생성
                mMap.addMarker(markerOptions);
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(savedGps, 10));
        });



//        LatLng SEOUL = new LatLng(37.556, 126.97);
//
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(SEOUL);
//        markerOptions.title("서울");
//        markerOptions.snippet("한국 수도");
//
//        mMap.addMarker(markerOptions);
//
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10));

    }
}