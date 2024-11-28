package com.ethanbunce.weatherapp;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ethanbunce.weatherapp.db.Location;

import java.util.List;

public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<LocationRecyclerViewAdapter.ViewHolder> {

    private final List<Location> locationList;

    public LocationRecyclerViewAdapter(List<Location> locationList) {
        this.locationList = locationList;
    }

    public void setItems(List<Location> courses) {
        this.locationList.clear();
        this.locationList.addAll(courses);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public Location location;
        public TextView tvLocationName, tvLocationZip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvLocationName = itemView.findViewById(R.id.tv_location_item_name);
            tvLocationZip = itemView.findViewById(R.id.tv_location_item_zip);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location location = locationList.get(position);
        String name = location.getName();
        String zip = location.getZip();
        holder.tvLocationName.setText(name);
        holder.tvLocationZip.setText(zip);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                WeatherService.setZipAndName(zip,name);
                activity.findViewById(R.id.nav_menu_forecast).performClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }


}
