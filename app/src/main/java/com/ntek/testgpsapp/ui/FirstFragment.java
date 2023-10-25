package com.ntek.testgpsapp.ui;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntek.testgpsapp.R;
import com.ntek.testgpsapp.persistance.AppDBAdapter;
import com.ntek.testgpsapp.persistance.AppDatabase;
import com.ntek.testgpsapp.persistance.Entity.Gps;

import java.util.List;


public class FirstFragment extends Fragment {
    private AppDatabase db;
    public RecyclerView firstRecycler;  //리사이클러 뷰 생성
    public AppDBAdapter firstAdapter;   //db어댑터 생성

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


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("FirstFragment","onStart Called");

        init();
        getData();
    }

    /*
    * 위치정보데이터 adapter에 추가
    * */
    @SuppressLint("LongLogTag")
    private void getData(){
        List<Gps> totalList = null;

        try {
            totalList = db.gpsDao().listOrderByDesc();
        } catch (SQLiteConstraintException e) {
            Log.e("SQLiteConstraintException: %s", e.getMessage());
        }

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