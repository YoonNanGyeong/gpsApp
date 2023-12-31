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


public class SecondFragment extends Fragment {
    private AppDatabase db;
    public RecyclerView secondRecycler;  //리사이클러 뷰
    public AppDBAdapter secondAdapter;   //db어댑터
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = AppDatabase.getInstance(getContext());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("SecondFragment","onCreateView Called");
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        secondRecycler = (RecyclerView) view.findViewById(R.id.secondRecycler);
        secondRecycler.setHasFixedSize(true); //목록을 구성하는 아이템의 크기 변동 없음

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("SecondFragment","onStart Called");
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
            totalList = db.gpsDao().listOrderByAsc();
        } catch (SQLiteConstraintException e) {
            Log.e("SQLiteConstraintException: %s", e.getMessage());
        }

        for(int i=0; i < totalList.size(); i++){
            Gps resultGps = totalList.get(i);
            Gps gps = new Gps(resultGps.getGps_seq(),resultGps.getGps_uid(),resultGps.getLat(),
                    resultGps.getLon(),resultGps.getAlt(), resultGps.getReg_date());

            // 각 값이 들어간 data를 adapter에 추가
            secondAdapter.addItem(gps);
        }

        // adapter 값 업데이트 명시
        secondAdapter.notifyDataSetChanged();
    }

    private void init(){
        /*
         * recyclerView에 데이터 전송
         * */
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        secondRecycler.setLayoutManager(layoutManager);

        secondAdapter = new AppDBAdapter();
        secondRecycler.setAdapter(secondAdapter);
    }

}