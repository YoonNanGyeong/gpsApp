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


public class SecondFragment extends Fragment {
    public RecyclerView secondRecycler;  //리사이클러 뷰 생성
    public RecyclerView.Adapter secondAdapter;   //어댑터 생성
    public ArrayList<Gps> gps_items = new ArrayList<>(); //gps 데이터 생성
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("SecondFragment","onCreateView Called");
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        secondRecycler = (RecyclerView) view.findViewById(R.id.secondRecycler);
        secondRecycler.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("SecondFragment","onStart Called");

        /*
        * arrayList 데이터 추가 예시
        * */
        for(int i=0; i<5; i++){
            gps_items.add(new Gps("아이디",0,0,0));
        }

        /*
        * recyclerView에 데이터 전송
        * */
        secondAdapter = new AppDBAdapter(gps_items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        secondRecycler.setLayoutManager(layoutManager);
        secondRecycler.setAdapter(secondAdapter);

    }
}