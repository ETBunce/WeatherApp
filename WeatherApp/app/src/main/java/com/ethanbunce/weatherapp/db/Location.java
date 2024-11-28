package com.ethanbunce.weatherapp.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Location {

    // forecast call: api.openweathermap.org/data/2.5/forecast?lat={lat}&lon={lon}&appid={API key}
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private String zip;
    private double lat;
    private double lon;

    public Location(String name, String zip, double lat, double lon) {
        this.name = name;
        this.zip = zip;
        this.lat = lat;
        this.lon = lon;
    };

    public String getName() { return name; }
    public int getId() {
        return id;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
    public String getZip() {
        return zip;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String toString() {
        return "zip: " + zip + " lat: " + lat + " lon: " + lon;
    }

}
