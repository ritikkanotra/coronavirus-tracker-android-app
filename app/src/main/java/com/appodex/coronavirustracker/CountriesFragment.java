package com.appodex.coronavirustracker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;

public class CountriesFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<CountryStats>> {


    private CountryAdapter mAdapter;
    private ListView listView;
//    private ArrayList<CountryStats> country;


    public CountriesFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_countries_fragment, container, false);

        listView = view.findViewById(R.id.list_view);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, CountriesFragment.this);
//        ArrayList<String> items = new ArrayList<>();
//        items.add("ritik");
//        items.add("kartik");

//        String animalList[] = {"Lion","Tiger","Monkey","Elephant","Dog","Cat","Camel"};

//        TextView textView = view.findViewById(R.id.current_country_name);

//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.list_item, R.id.current_country_name, animalList);


//        country = new ArrayList<>();
//        country.add(new CountryStats("1234", "1234", "1245"));
//        country.add(new CountryStats("1234", "1234", "1245"));
//        country.add(new CountryStats("1234", "1234", "1245"));
//        country.add(new CountryStats("1234", "1234", "1245"));
//        country.add(new CountryStats("1234", "1234", "1245"));

        mAdapter = new CountryAdapter(getContext(), new ArrayList<CountryStats>());
        listView.setAdapter(mAdapter);

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
