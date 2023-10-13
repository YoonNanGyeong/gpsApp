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
    private ArrayList<Gps> gpsList;
    public AppDBAdapter(ArrayList<Gps>gpsData){
        this.gpsList=gpsData;
    }

    @NonNull
    @Override
    public AppDBAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AppDBAdapter.ViewHolder holder, int position) {
        holder.itemView.findViewById(R.id.listItem_id);
        holder.itemView.findViewById(R.id.listItem_lon);
        holder.itemView.findViewById(R.id.listItem_lat);
        holder.itemView.findViewById(R.id.listItem_alt);
    }

    @Override
    public int getItemCount() {
        return gpsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView listItem_id;
        private TextView listItem_lon;
        private TextView listItem_lat;
        private TextView listItem_alt;

        public ViewHolder(@NonNull View view) {
            super(view);
            listItem_id = (TextView)view.findViewById(R.id.listItem_id);
            listItem_lon = (TextView)view.findViewById(R.id.listItem_lon);
            listItem_lat = (TextView)view.findViewById(R.id.listItem_lat);
            listItem_alt = (TextView)view.findViewById(R.id.listItem_alt);
        }
    }
}
