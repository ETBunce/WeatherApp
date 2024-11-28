package com.ethanbunce.weatherapp.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocationDAO {

    @Query("SELECT * FROM Location")
    LiveData<List<Location>> getAll();

    @Query("SELECT * FROM Location WHERE zip = :zip")
    Location getByZip(String zip);

    @Delete
    void delete(Location location);

    @Insert
    void insertLocation(Location location);

}
