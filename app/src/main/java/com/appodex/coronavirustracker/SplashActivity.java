package com.appodex.coronavirustracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIMEOUT = 600;
    private TextView currentVersionTextView;
    private String currentVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        try {
            PackageInfo packageInfo = SplashActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        currentVersionTextView = findViewById(R.id.current_version_text_view);
        currentVersionTextView.setText("v" + currentVersion);


        isInternetConnected();


    }

    public void isInternetConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo != null && networkInfo.isConnected()) {

            startMainActivity();

        }
        else {

            createNetworkAlertDialog();

        }

    }

    public void startMainActivity() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, SPLASH_SCREEN_TIMEOUT);

    }

    public void createNetworkAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("No Internet Connection!").setMessage("Please turn on the internet and try again.");

        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                isInternetConnected();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

            }
        });

        AlertDialog notConnectedAlertDialog = builder.create();
        notConnectedAlertDialog.setCancelable(false);
        notConnectedAlertDialog.setCanceledOnTouchOutside(false);
        notConnectedAlertDialog.show();

    }
}
