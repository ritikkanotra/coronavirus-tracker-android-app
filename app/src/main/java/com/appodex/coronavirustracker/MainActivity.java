package com.appodex.coronavirustracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {

    private String CURRENT_VERSION;
    private String realVersion;
    private String updateChanges;
    private String realVersionDownloadUrl;
    private DatabaseReference mDatabaseRef;
    private AdView mAdView;
    private String editedChanges;
    private ProgressDialog updateProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CURRENT_VERSION = getCurrentVersion();
        checkPermissions();

        realVersion = null;

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mDatabaseRef.child("app_version").child("version_no").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                realVersion = dataSnapshot.getValue().toString();
                Log.i("testing", realVersion);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final String[][] newChanges = new String[1][1];

        mDatabaseRef.child("app_version").child("version_change").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                updateChanges = dataSnapshot.getValue().toString();
                Log.i("testing", updateChanges);

                newChanges[0] = updateChanges.split("///");
                editedChanges = "";
                for(int i = 0; i < newChanges[0].length; i++) {
                    editedChanges += (i + 1) + ". " +  newChanges[0][i] + "\n";
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        realVersionDownloadUrl = getRealVersionDownloadUrl();



        getSupportActionBar().setElevation(0);

        ViewPager viewPager = findViewById(R.id.viewpager);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(categoryAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                checkVersion();

            }
        }, 5000);

    }

    private void checkVersion() {

        if(realVersion != null && !realVersion.equals(CURRENT_VERSION)) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Update Available").setMessage(editedChanges);

            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    downloadAndUpdateApp();

                }
            });

            builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        }

    }

    private void downloadAndUpdateApp() {


        downloadUpdate();
        updateApp();



    }

    private void downloadUpdate() {

        updateProgressDialog = new ProgressDialog(MainActivity.this);
        updateProgressDialog.setMessage("Downloading Update");
        updateProgressDialog.setCancelable(false);
        updateProgressDialog.setCanceledOnTouchOutside(false);
        updateProgressDialog.show();

        String destination = Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName() + "/.updates/";
        String fileName = "CoronavirusTracker.apk";
        destination += fileName;

        //Delete update file if exists
        File file = new File(destination);
        if (file.exists())
            //file.delete() - test this, I think sometimes it doesnt work
            file.delete();

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(realVersionDownloadUrl));
        request.setDestinationInExternalPublicDir("/Android/data/" + getPackageName() + "/.updates", "CoronavirusTracker.apk");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);

    }

    private void updateApp() {

        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {

                updateProgressDialog.dismiss();

                Intent install = new Intent(Intent.ACTION_VIEW);
                File file = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getPackageName() + "/.updates/CoronavirusTracker.apk");
                Uri data = FileProvider.getUriForFile(MainActivity.this, BuildConfig.APPLICATION_ID +".provider",file);
                Log.i("testing", data.getPath());
                install.setDataAndType(data,"application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(install);

                unregisterReceiver(this);
                finish();
            }
        };
        //register receiver for when .apk download is compete
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    private String getRealVersionDownloadUrl() {

        mDatabaseRef.child("app_version").child("version_url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                realVersionDownloadUrl = dataSnapshot.getValue().toString();
                Log.i("testing", realVersionDownloadUrl);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return realVersionDownloadUrl;

    }

    private void checkPermissions() {

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                            },
                    1);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private String getCurrentVersion() {

        String currentVersion = null;

        try {
            PackageInfo packageInfo = MainActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            currentVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return currentVersion;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i("testing", "BACK pressed");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_about:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
