package com.ethanbunce.weatherapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ethanbunce.weatherapp.db.AppDatabase;
import com.ethanbunce.weatherapp.db.Location;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeatherService {

    private static final String DATE_INPUT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String TAG = "WeatherService";
    public static final String QUERY_FOR_WEATHER = "REDACTED";

    private static String zip;
    private static String name;
    private static List<ForecastListItem> forecast;

    public interface ForecastResponseListener {
        void onResponse(List<ForecastListItem> newForecast);
        void onError(String message);
    }

    public void loadForecast(Context context, ForecastResponseListener listener) {

        if (zip == null) {
            Log.d(TAG, "Could not load forecast, zip is null");
            return;
        } else {
            Log.d(TAG,"Loading forecast for " + zip);
        }

        if (forecast == null) {
            forecast = new ArrayList<>();
        }
        clearForecast();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Location location = AppDatabase.getInstance(context).locationDAO().getByZip(zip);
                    if (location == null) {
                        Log.d(TAG, "Could not get location from zip: " + zip);
                        return;
                    }
                    String url = String.format(QUERY_FOR_WEATHER, location.getLat(), location.getLon());

                    //get the JSON object from API
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //Got the object, now create a list of ForecastPartModels
                            try {
                                JSONArray list = response.getJSONArray("list");
                                String lastDate = "";
                                for (int i = 0; i < list.length(); i++) {
                                    JSONObject forecastPart = (JSONObject) list.get(i);
                                    String dt_txt = forecastPart.getString("dt_txt");

                                    ForecastListItem item = new ForecastPartModel(
                                            forecastPart.getString("dt_txt"),
                                            forecastPart.getJSONObject("main").getDouble("temp"),
                                            forecastPart.getJSONObject("main").getDouble("feels_like"),
                                            ((JSONObject)(forecastPart.getJSONArray("weather").get(0))).getString("description")
                                    );
                                    //Whenever a new date is found, add a title to the date
                                    if (!lastDate.equals(getFormattedDate(dt_txt, "MM-dd-yyyy"))) {
                                        Log.d("WeatherService","lastDate:" + lastDate + "  other date: " + getFormattedDate(dt_txt, "MM-dd-yyyy"));
                                        lastDate = getFormattedDate(dt_txt, "MM-dd-yyyy");
                                        forecast.add(new ForecastListTitle(getDayTitle(dt_txt)));
                                    }
                                    forecast.add(item);
                                }
//                                Log.d(TAG, "Fetched forecast: " + forecast.toString());
                                //At this point the forecast variable is filled with a list of ForecastPartModels.
                                //return the forecast to the listener
                                listener.onResponse(forecast);

                            } catch (Exception e) {
                                Log.d(TAG, e.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    VolleyRequestQueue.getInstance(context).addToRequestQueue(request);

                    //get list property called "list"

                    //
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
        }).start();

    }

    public List<ForecastListItem> getForecast() {
        return forecast;
    }

    public static void setZipAndName(String newZip, String newName) {
        zip = newZip;
        name = newName;
        clearForecast();
    }

    public static String getZip() {
        return zip;
    }
    public static String getName() { return name; }
    public static String getDayTitle(String dt_txt) {
        return getFormattedDate(dt_txt, "EEEE MMM dd");
    }

    public static String getFormattedDate(String input, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_INPUT_FORMAT, Locale.ENGLISH);
        DateFormat outFormatter = new SimpleDateFormat(format,Locale.ENGLISH);
        String output = "";
        try {
            Date dt = formatter.parse(input);
            //Get the day of the week
            output = outFormatter.format(dt);
        } catch (Exception e) {
            Log.d("WeatherService",e.toString());
        }
        return output;
    }

    private static void clearForecast() {
        if(forecast != null) forecast.clear();
    }

}
