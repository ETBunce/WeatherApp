package com.ethanbunce.weatherapp;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ethanbunce.weatherapp.db.AppDatabase;
import com.ethanbunce.weatherapp.db.Location;

import java.util.List;

public class AllLocationsViewModel extends ViewModel {

    private LiveData<List<Location>> locationList;

    public LiveData<List<Location>> getLocationList(Context c) {
        if (locationList != null)
            return locationList;
        else
            return locationList = AppDatabase.getInstance(c).locationDAO().getAll();
    }
}
