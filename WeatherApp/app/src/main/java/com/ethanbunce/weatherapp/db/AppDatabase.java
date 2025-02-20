package com.ethanbunce.weatherapp.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {com.ethanbunce.weatherapp.db.Location.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


    public abstract  LocationDAO locationDAO();
    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance != null) {
            return instance;
        } else {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "user-database")
                    .build();
            return instance;
        }
    }

}
