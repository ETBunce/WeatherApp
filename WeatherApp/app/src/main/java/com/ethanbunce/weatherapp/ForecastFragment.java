package com.ethanbunce.weatherapp;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ethanbunce.weatherapp.db.AppDatabase;
import com.ethanbunce.weatherapp.db.Location;
import com.ethanbunce.weatherapp.db.LocationDAO;

import org.w3c.dom.Text;

import java.util.List;

public class ForecastFragment extends Fragment {

    private final String TAG = "ForecastFragment";

    private View view;
    private WeatherService.ForecastResponseListener forecastResponseListener;
    private LocationDAO dao;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_forecast, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forecast, container, false);

        // Set the toolbar title
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("Forecast");
        }

        // Get the DAO
        dao = AppDatabase.getInstance(getContext()).locationDAO();

        //Set up the response listener
        forecastResponseListener = new WeatherService.ForecastResponseListener() {
            @Override
            public void onResponse(List<ForecastListItem> newForecast) {
                // PUT IT ON THE SCREEN
                listForecast(newForecast);
            }

            @Override
            public void onError(String message) {

            }
        };


        // Load the forecast
        if (savedInstanceState == null) { // First time
            loadForecast();
        } else { // Refreshing
            listForecast(new WeatherService().getForecast());
        }
        String locationName = WeatherService.getName();
        String locationZip = WeatherService.getZip();
        TextView locationNameText = view.findViewById(R.id.tv_forecast_fragment_location_name);
        if (locationName != null && locationZip != null) {
            locationNameText.setText(String.format("%s\t%s", locationName, locationZip));
        } else {
            locationNameText.setText("");
        }

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, item.toString());
        if (item.getItemId() == R.id.menuActionRefreshForecast) {
            loadForecast();
        } else if (item.getItemId() == R.id.menuActionDeleteCity) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Delete Location")
                    .setMessage("Are you sure you want to delete this location?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Location location = dao.getByZip(WeatherService.getZip());
                            if (location != null) {
                                WeatherService.setZipAndName(null, null);
                                dao.delete(location);
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        getActivity().findViewById(R.id.nav_menu_locations).performClick();
                                    }
                                });
                            }
                        }
                    }).start();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //Close the dialog
                }
            });

            AlertDialog alert = builder.create();
            alert.show();


        }
        return true;
    }

    private void loadForecast() {
        new WeatherService().loadForecast(getContext(), forecastResponseListener);
    }

    private void listForecast(List<ForecastListItem> forecast) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, forecast);
        ((ListView)view.findViewById(R.id.lv_forecast)).setAdapter(arrayAdapter);
    }
}