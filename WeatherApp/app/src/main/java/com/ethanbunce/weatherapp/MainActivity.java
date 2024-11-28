package com.ethanbunce.weatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FragmentManager fm;

    LocationsFragment locationsFragment;
    ForecastFragment forecastFragment;
    AboutFragment aboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fm = getSupportFragmentManager();

        setSupportActionBar(findViewById(R.id.toolbar));

        //Start at the locations screen
        if (savedInstanceState == null) {
            fm.beginTransaction()
                    .replace(R.id.container, new LocationsFragment(), "locations_fragment")
                    .commit();
            getSupportActionBar().setTitle("Locations");
        }


        //Set up bottom menu
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_menu_locations) {
                    fm.beginTransaction()
                            .replace(R.id.container, new LocationsFragment(), "locations_fragment")
                            .commit();
                    return true;
                } else if (id == R.id.nav_menu_forecast) {
                    fm.beginTransaction()
                            .replace(R.id.container, new ForecastFragment(), "forecast_fragment")
                            .commit();
                    return true;
                } else if (id == R.id.nav_menu_about) {
                    fm.beginTransaction()
                            .replace(R.id.container, new AboutFragment(), "about_fragment")
                            .commit();
                    return true;
                }
                return false;
            }
        });



    }

}