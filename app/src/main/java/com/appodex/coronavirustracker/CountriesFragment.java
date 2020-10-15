package com.appodex.coronavirustracker;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class CountriesFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<CountryStats>> {


    private CountryAdapter mAdapter;
    private ListView listView;
    private LinearLayout linearLayout1;
//    private AdView countryPopupAd;
//    private ArrayList<CountryStats> country;


    public CountriesFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_countries_fragment, container, false);

        listView = view.findViewById(R.id.list_view);
        linearLayout1 = view.findViewById(R.id.linear_layout_1);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, CountriesFragment.this);

        final ArrayList<CountryStats> countries = new ArrayList<CountryStats>();

        mAdapter = new CountryAdapter(getContext(), countries);
        listView.setAdapter(mAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CountryStats currentCountryStats = countries.get(position);


                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.country_popup, null);


                TextView countryNameTextView = customView.findViewById(R.id.country_name_text_view);
                TextView countryCasesTextView = customView.findViewById(R.id.country_cases_text_view);
                TextView countryDeathsTextView = customView.findViewById(R.id.country_death_text_view);
                TextView countryRecoveredTextView = customView.findViewById(R.id.country_recovered_text_view);
                TextView countryActiveTextView = customView.findViewById(R.id.country_active_text_view);
                TextView countryCriticalTextView = customView.findViewById(R.id.country_critical_text_view);
                TextView countryTodayCases = customView.findViewById(R.id.country_today_cases);
                TextView countryTodayDeaths = customView.findViewById(R.id.country_today_deaths);
                final ImageView popupDismiss = customView.findViewById(R.id.popup_dismiss);

                countryNameTextView.setText(currentCountryStats.getCountryName());
                countryCasesTextView.setText(currentCountryStats.getCases());
                countryDeathsTextView.setText(currentCountryStats.getDeaths());
                countryRecoveredTextView.setText(currentCountryStats.getRecovered());
                countryActiveTextView.setText(currentCountryStats.getActive());
                countryCriticalTextView.setText(currentCountryStats.getCritical());
                countryTodayCases.setText(currentCountryStats.getTodayCases());
                countryTodayDeaths.setText(currentCountryStats.getTodayDeaths());


//                countryPopupAd = customView.findViewById(R.id.country_popup_ad);
//                AdRequest adRequest = new AdRequest.Builder().build();
//                countryPopupAd.loadAd(adRequest);



                final PopupWindow popupWindow = new PopupWindow(
                        customView,
                        ViewGroup.LayoutParams.FILL_PARENT,
                        ViewGroup.LayoutParams.FILL_PARENT);
                popupWindow.setOutsideTouchable(false);
                popupWindow.setAnimationStyle(R.style.popup_window_animation);
                popupWindow.showAtLocation(linearLayout1, Gravity.CENTER, 0, 0);


                popupDismiss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        popupWindow.dismiss();

                    }
                });



            }
        });

        return view;

    }

    @NonNull
    @Override
    public Loader<ArrayList<CountryStats>> onCreateLoader(int id, @Nullable Bundle args) {
        return new BackgroundTask(getContext(), "https://coronavirus-19-api.herokuapp.com/countries", 1);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<CountryStats>> loader, ArrayList<CountryStats> country) {

        mAdapter.clear();

        for(int i = 0; i < country.size(); i++) {
            Log.i("testing", country.get(i).getCountryName());
        }

        if(country != null && !country.isEmpty()) {
            Log.i("testing", "country != null");
            mAdapter.addAll(country);
        }

//        mAdapter = new CountryAdapter(getContext(), new ArrayList<CountryStats>());
//        listView.setAdapter(mAdapter);



    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<CountryStats>> loader) {

        mAdapter.clear();

    }
}
