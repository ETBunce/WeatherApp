package com.ethanbunce.weatherapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethanbunce.weatherapp.db.Location;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class LocationsFragment extends Fragment {

    private View view;
    private RecyclerView locationRecyclerView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_locations, container, false);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setTitle("Locations");
        }

        locationRecyclerView = view.findViewById(R.id.rv_locations);

        LocationRecyclerViewAdapter adapter = new LocationRecyclerViewAdapter(new ArrayList<Location>());
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        locationRecyclerView.setAdapter(adapter);
        locationRecyclerView.setHasFixedSize(false);

        new ViewModelProvider(this)
                .get(AllLocationsViewModel.class)
                .getLocationList(getContext())
                .observe(getViewLifecycleOwner(), new Observer<List<Location>>() {
                    @Override
                    public void onChanged(List<Location> courses) {
                        if (courses != null) {
                            adapter.setItems(courses);
                        }
                    }
                });

        view.findViewById(R.id.fabNewLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() != null) {
//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.container,new NewLocationDialog(),"new_location_dialog")
//                            .addToBackStack(null)
//                            .commit();
                    new NewLocationDialog().show(getActivity().getSupportFragmentManager(),null);
                }
            }
        });

        return view;
    }
}