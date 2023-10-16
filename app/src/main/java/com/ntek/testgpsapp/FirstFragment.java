package com.ntek.testgpsapp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntek.testgpsapp.Entity.Gps;

import java.util.List;


public class FirstFragment extends Fragment {
    private AppDatabase db;
    public RecyclerView firstRecycler;  //리사이클러 뷰 생성
    public AppDBAdapter firstAdapter;   //어댑터 생성

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("FirstFragment","onCreateView Called");

        View view = inflater.inflate(R.layout.fragment_first, container, false);
        firstRecycler = (RecyclerView)view.findViewById(R.id.firstRecycler);
        firstRecycler.setHasFixedSize(true);

        init();
        getData();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("FirstFragment","onStart Called");
    }

    /*
    * 위치정보데이터 adapter에 추가
    * */
    private void getData(){
        List<Gps>totalList = db.gpsDao().listOrderByDesc();

        for(int i=0; i < totalList.size(); i++){
            Gps resultGps = totalList.get(i);
            Gps gps = new Gps(resultGps.getGps_seq(),resultGps.getGps_uid(),resultGps.getLat(),
                    resultGps.getLon(),resultGps.getAlt(), resultGps.getReg_date());
            
            // 각 값이 들어간 data를 adapter에 추가
            firstAdapter.addItem(gps);
        }

        // adapter 값 업데이트 명시
        firstAdapter.notifyDataSetChanged();
    }

    private void init(){
        /*
         * recyclerView에 데이터 전송
         * */
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        firstRecycler.setLayoutManager(layoutManager);

        firstAdapter = new AppDBAdapter();
        firstRecycler.setAdapter(firstAdapter);
    }
}