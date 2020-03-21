package com.appodex.coronavirustracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;

public class MyCountryFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<CountryStats>> {

    private TextView countryNameTextView;
    private TextView countryCasesTextView;
    private TextView countryDeathsTextView;
    private TextView countryRecoveredTextView;
    private TextView countryActiveTextView;
    private TextView countryCriticalTextView;
    private TextView countryTodayCases;
    private TextView countryTodayDeaths;


    public MyCountryFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_my_country_fragment, container, false);

        countryNameTextView = view.findViewById(R.id.country_name_text_view);
        countryCasesTextView = view.findViewById(R.id.country_cases_text_view);
        countryDeathsTextView = view.findViewById(R.id.country_death_text_view);
        countryRecoveredTextView = view.findViewById(R.id.country_recovered_text_view);
        countryActiveTextView = view.findViewById(R.id.country_active_text_view);
        countryCriticalTextView = view.findViewById(R.id.country_critical_text_view);
        countryTodayCases = view.findViewById(R.id.country_today_cases);
        countryTodayDeaths = view.findViewById(R.id.country_today_deaths);


        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(2, null, MyCountryFragment.this);


        return view;

    }

    @NonNull
    @Override
    public Loader<ArrayList<CountryStats>> onCreateLoader(int id, @Nullable Bundle args) {
        return new BackgroundTask(getContext(), "https://coronavirus-19-api.herokuapp.com/countries", 2);
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

        countryNameTextView.setText(countryName);
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
}
