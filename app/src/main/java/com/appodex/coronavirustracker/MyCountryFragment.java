package com.appodex.coronavirustracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class MyCountryFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<CountryStats>>, LocationListener {

//    private TextView countryNameTextView;
    private TextView countryCasesTextView;
    private TextView countryDeathsTextView;
    private TextView countryRecoveredTextView;
    private TextView countryActiveTextView;
    private TextView countryCriticalTextView;
    private TextView countryTodayCases;
    private TextView countryTodayDeaths;

    private Location gpsLoc = null, networkLoc = null, finalLoc = null;

    private Double latitude = null, longitude = null;

    private String myCountry = null;

    public MyCountryFragment() {

    }

    @SuppressLint("MissingPermission")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_my_country_fragment, container, false);

//        countryNameTextView = view.findViewById(R.id.country_name_text_view);
        countryCasesTextView = view.findViewById(R.id.country_cases_text_view);
        countryDeathsTextView = view.findViewById(R.id.country_death_text_view);
        countryRecoveredTextView = view.findViewById(R.id.country_recovered_text_view);
        countryActiveTextView = view.findViewById(R.id.country_active_text_view);
        countryCriticalTextView = view.findViewById(R.id.country_critical_text_view);
        countryTodayCases = view.findViewById(R.id.country_today_cases);
        countryTodayDeaths = view.findViewById(R.id.country_today_deaths);



        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        try {
            gpsLoc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            networkLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (gpsLoc != null) {
            finalLoc = gpsLoc;
        }
        else if (networkLoc != null) {
            finalLoc = networkLoc;
        }

        if(finalLoc != null) {
            onLocationChanged(finalLoc);
            myCountry = getCountryName(finalLoc);
        }


        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(2, null, MyCountryFragment.this);


        return view;

    }

    @NonNull
    @Override
    public Loader<ArrayList<CountryStats>> onCreateLoader(int id, @Nullable Bundle args) {
        return new BackgroundTask(getContext(), "https://coronavirus-19-api.herokuapp.com/countries", myCountry, 2);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<CountryStats>> loader, ArrayList<CountryStats> data) {

        CountryStats myCountryStats = data.get(0);
        String countryName = myCountryStats.getCountryName();
        String cases = myCountryStats.getCases();
        String todayCases = myCountryStats.getTodayCases();
        String deaths = myCountryStats.getDeaths();
        String todayDeaths = myCountryStats.getTodayDeaths();
        String recovered = myCountryStats.getRecovered();
        String active = myCountryStats.getActive();
        String critical = myCountryStats.getCritical();

//        countryNameTextView.setText(countryName);
        countryCasesTextView.setText(cases);
        countryDeathsTextView.setText(deaths);
        countryRecoveredTextView.setText(recovered);
        countryActiveTextView.setText(active);
        countryCriticalTextView.setText(critical);
        countryTodayCases.setText(todayCases);
        countryTodayDeaths.setText(todayDeaths);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<CountryStats>> loader) {

    }

    private String getCountryName(Location location) {

        String country = null;

        try {
            Geocoder geocoder = new Geocoder(getContext());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            country = addresses.get(0).getCountryName();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return country;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

}
