package com.appodex.coronavirustracker;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class GlobalFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<CountryStats>> {

    private TextView GlobalCasesTextView;
    private TextView GlobalDeathTextView;
    private TextView GlobalRecoveredTextView;

    public GlobalFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_global_fragment, container, false);

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, GlobalFragment.this);

        GlobalCasesTextView = view.findViewById(R.id.global_cases_text_view);
        GlobalDeathTextView = view.findViewById(R.id.global_death_text_view);
        GlobalRecoveredTextView = view.findViewById(R.id.global_recovered_text_view);

        return view;
    }

    @NonNull
    @Override
    public Loader<ArrayList<CountryStats>> onCreateLoader(int id, @Nullable Bundle args) {
        Log.i("testing", "loader start");
        return new BackgroundTask(getContext(), "https://coronavirus-19-api.herokuapp.com/all", 0);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<CountryStats>> loader, ArrayList<CountryStats> data) {

        CountryStats globalStats = data.get(0);

        String cases = globalStats.getCases();
        String deaths = globalStats.getDeaths();
        String recovered = globalStats.getRecovered();

        GlobalCasesTextView.setText(cases);
        GlobalDeathTextView.setText(deaths);
        GlobalRecoveredTextView.setText(recovered);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<CountryStats>> loader) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("testing", "Global Destroyed");
    }
}
