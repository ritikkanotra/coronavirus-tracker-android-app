package com.appodex.coronavirustracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<CountryStats> {

    public CountryAdapter(@NonNull Context context, ArrayList<CountryStats> countries) {
        super(context, 0, countries);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listView = convertView;
        if(listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        CountryStats currentCountry = getItem(position);

        TextView countryNameTextView = listView.findViewById(R.id.current_country_name);
        countryNameTextView.setText(currentCountry.getCountryName());

        TextView countryCasesTextView = listView.findViewById(R.id.current_country_cases);
        countryCasesTextView.setText(currentCountry.getCases() + " C");

        TextView countryDeathsTextView = listView.findViewById(R.id.current_country_deaths);
        countryDeathsTextView.setText(currentCountry.getDeaths() + " D");

        TextView countryRecoveredTextView = listView.findViewById(R.id.current_country_recovered);
        countryRecoveredTextView.setText(currentCountry.getRecovered() + " R");



        return listView;
    }
}
