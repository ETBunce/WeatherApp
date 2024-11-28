package com.ethanbunce.weatherapp;

public class ForecastListTitle extends ForecastListItem {

    private String title;

    public ForecastListTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }
}
