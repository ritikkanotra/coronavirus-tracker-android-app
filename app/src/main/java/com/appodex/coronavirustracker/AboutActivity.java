package com.appodex.coronavirustracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    CardView mApiLinkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        getSupportActionBar().hide();

        mApiLinkView = findViewById(R.id.tv_api_link);

        mApiLinkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/javieraviles/covidAPI"));
//                intent.setData();
                startActivity(intent);
            }
        });

    }

    public void websiteClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ritikkanotra.me"));
        startActivity(intent);
    }

    public void githubClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.github.com/ritikkanotra"));
        startActivity(intent);
    }

    public void closeActivity(View view) {
//        finish();
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}