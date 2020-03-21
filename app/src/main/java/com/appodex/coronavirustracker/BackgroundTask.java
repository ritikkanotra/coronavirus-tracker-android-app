package com.appodex.coronavirustracker;

import android.content.Context;
import android.util.Log;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class BackgroundTask extends AsyncTaskLoader<ArrayList<CountryStats>> {

//    private String stringUrl = "https://coronavirus-19-api.herokuapp.com/all";

    String mStringUrl;
    int mTask;


    public BackgroundTask(@NonNull Context context, String url, int task) {
        super(context);
        mStringUrl = url;
        mTask = task;
    }

    @Nullable
    @Override
    public ArrayList<CountryStats> loadInBackground() {

        Log.i("testing", "loadinback");

        String jsonResponse = null;
        ArrayList<CountryStats> values = new ArrayList<>();

        try {
            URL url = new URL(mStringUrl);
            jsonResponse = makeHttpRequest(url);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if(mTask == 0) {
                JSONObject rootJsonObject = new JSONObject(jsonResponse);

                values.add(new CountryStats(Integer.toString(rootJsonObject.getInt("cases")),
                        Integer.toString(rootJsonObject.getInt("deaths")),
                        Integer.toString(rootJsonObject.getInt("recovered"))));

            }
            else if(mTask == 1) {

                Log.i("testing", "task1");

                JSONArray rootJsonArray = new JSONArray(jsonResponse);
                for(int i = 0; i < rootJsonArray.length(); i++) {

                    JSONObject countryJsonObject = rootJsonArray.getJSONObject(i);
                    values.add(new CountryStats(countryJsonObject.getString("country"),
                            Integer.toString(countryJsonObject.getInt("cases")),
                            Integer.toString(countryJsonObject.getInt("todayCases")),
                            Integer.toString(countryJsonObject.getInt("deaths")),
                            Integer.toString(countryJsonObject.getInt("todayDeaths")),
                            Integer.toString(countryJsonObject.getInt("recovered")),
                            Integer.toString(countryJsonObject.getInt("active")),
                            Integer.toString(countryJsonObject.getInt("critical"))));
                }

            }
            else {
                JSONArray rootJsonArray = new JSONArray(jsonResponse);
                for(int i = 0; i < rootJsonArray.length(); i++) {

                    JSONObject countryJsonObject = rootJsonArray.getJSONObject(i);
                    String country = countryJsonObject.getString("country");
                    if(country.equals("India")) {

                        values.add(new CountryStats(countryJsonObject.getString("country"),
                                Integer.toString(countryJsonObject.getInt("cases")),
                                Integer.toString(countryJsonObject.getInt("todayCases")),
                                Integer.toString(countryJsonObject.getInt("deaths")),
                                Integer.toString(countryJsonObject.getInt("todayDeaths")),
                                Integer.toString(countryJsonObject.getInt("recovered")),
                                Integer.toString(countryJsonObject.getInt("active")),
                                Integer.toString(countryJsonObject.getInt("critical"))));

                        break;
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return values;


    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    private String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            inputStream.close();
//            urlConnection.disconnect();
        }

        Log.i("testing", jsonResponse);

        return jsonResponse;

    }

    private String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if(inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            if(line != null) {
                output.append(line);
                line = reader.readLine();
            }

        }

        return output.toString();

    }
}
