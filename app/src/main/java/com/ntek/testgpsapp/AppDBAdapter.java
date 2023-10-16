package com.ntek.testgpsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ntek.testgpsapp.Entity.Gps;

import java.util.ArrayList;

public class AppDBAdapter extends RecyclerView.Adapter<AppDBAdapter.ViewHolder> {
    private ArrayList<Gps> gpsList = new ArrayList<>();

    @NonNull
    @Override
    public AppDBAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(gpsList.get(position));
    }

    @Override
    public int getItemCount() {
        return gpsList.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    // 외부에서 item 추가
    public void addItem(Gps gpsData){
        gpsList.add(gpsData);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView listItem_id;
        private TextView listItem_lon;
        private TextView listItem_lat;
        private TextView listItem_alt;

        public ViewHolder(@NonNull View view) {
            super(view);
            listItem_id = (TextView) view.findViewById(R.id.listItem_id);
            listItem_lon = (TextView)view.findViewById(R.id.listItem_lon_text);
            listItem_lat = (TextView)view.findViewById(R.id.listItem_lat_text);
            listItem_alt = (TextView)view.findViewById(R.id.listItem_alt_text);
        }
        void onBind(Gps gpsData){
            String str_lon = Double.toString(gpsData.getLon());
            String str_lat = Double.toString(gpsData.getLat());
            String str_alt = Double.toString(gpsData.getAlt());

            listItem_id.setText(gpsData.getGps_uid());
            listItem_lon.setText(str_lon);
            listItem_lat.setText(str_lat);
            listItem_alt.setText(str_alt);
        }
    }
}
