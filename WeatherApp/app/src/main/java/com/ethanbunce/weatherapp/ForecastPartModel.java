package com.ethanbunce.weatherapp;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;

public class ForecastPartModel extends ForecastListItem {


    private String dt_txt; // the date and time in format "yyy-mm-dd hh:mm:ss"
    private double temp; // temperature
    private double feels_like;
    private String description; // e.g., clear sky, scattered clouds

    public ForecastPartModel(String dt_txt, double temp, double feels_like, String description) {
        this.dt_txt = dt_txt;
        this.temp = temp;
        this.feels_like = feels_like;
        this.description = description;
    }

    @Override
    public String toString() {
        //Create the date

        return "" +
                WeatherService.getFormattedDate(dt_txt, "h a") +
                "\ntemperature: " + temp + "\u00B0" +
                "\nfeels like: " + feels_like + "\u00B0" +
                "\n" + description;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
