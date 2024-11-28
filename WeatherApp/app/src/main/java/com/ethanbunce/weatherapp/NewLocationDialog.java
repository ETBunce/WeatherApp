package com.ethanbunce.weatherapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ethanbunce.weatherapp.db.AppDatabase;
import com.ethanbunce.weatherapp.db.Location;
import com.ethanbunce.weatherapp.db.LocationDAO;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class NewLocationDialog extends DialogFragment {

    private View view;
    private LocationDAO locationDao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFragmentStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_location_dialog, container, false);
        TextInputEditText inputZip = view.findViewById(R.id.inputZip);
        TextView errorHint = view.findViewById(R.id.tvBadZipHint);
        locationDao = AppDatabase.getInstance(getActivity()).locationDAO();

        if (getDialog() != null) {
            getDialog().setTitle("Add New City");
        }

        Button okayButton = view.findViewById(R.id.btnAddLocation);
        okayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = getActivity();
                if (context == null) return;

                errorHint.setTextColor(getResources().getColor(R.color.black));
                errorHint.setText("Loading...");

                // Instantiate the RequestQueue.
                String zip = String.valueOf(inputZip.getText());
                String url = String.format("https://api.openweathermap.org/geo/1.0/zip?zip=%s,US&appid=%s",
                        zip,
                        getString(R.string.weather_api_key));

                //GET DATA FROM ZIP
                String name = "";
                double lat;
                double lon;
                // Request a string response from the provided URL.
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    Log.d("NewLocationDialog", "trying to insert new location");

                                    String name = response.getString("name");
                                    String zip = response.getString("zip");
                                    double lat = response.getDouble("lat");
                                    double lon = response.getDouble("lon");

                                    //Load the contents into the database!
                                    Location location = new Location(
                                            name,
                                            zip,
                                            lat,
                                            lon
                                    );

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {

                                            if (locationDao.getByZip(zip) == null) {
                                                locationDao.insertLocation(location);
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        WeatherService.setZipAndName(zip,name);
                                                        getActivity().findViewById(R.id.nav_menu_forecast).performClick();
                                                        dismiss();
                                                    }
                                                });
                                            } else {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        errorHint.setTextColor(getResources().getColor(R.color.red));
                                                        errorHint.setText(R.string.location_exists_error_hint);
                                                    }
                                                });
                                            }

                                        }


                                    }).start();


                                } catch (Exception e) {
                                    //Show error hint
                                    Log.e("NewLocationDialog",e.toString());


                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                errorHint.setTextColor(getResources().getColor(R.color.red));
                                errorHint.setText(R.string.zip_error_hint);
                            }
                        });
                VolleyRequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);

            }
        });

        return view;
    }
}