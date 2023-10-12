package com.ntek.testgpsapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ntek.testgpsapp.Entity.Gps;

import java.util.ArrayList;


public class FirstFragment extends Fragment {
    public RecyclerView firstRecycler;  //리사이클러 뷰 생성
    public RecyclerView.Adapter firstAdapter;   //어댑터 생성
    public ArrayList<Gps>gps_items = new ArrayList<>(); //gps 데이터 생성

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("로그: ","onCreateView");
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        firstRecycler = (RecyclerView) view.findViewById(R.id.firstRecycler);
        firstRecycler.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("로그: ","onStart");

        /*
         * arrayList 데이터 추가 예시
         * */
        for(int i=0; i<5; i++){
            gps_items.add(new Gps("아이디",0,0,0));
        }

        /*
         * recyclerView에 데이터 전송
         * */
        firstAdapter = new AppDBAdapter(gps_items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        firstRecycler.setLayoutManager(layoutManager);
        firstRecycler.setAdapter(firstAdapter);
    }
}