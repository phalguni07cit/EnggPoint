package com.ambilabs.enggpoint;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PdfOpen extends AppCompatActivity {

    PDFView mpdfView;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_open);
        MobileAds.initialize(this, "ca-app-pub-1194349429791925~1927920961");

        mpdfView = (PDFView) findViewById(R.id.pdfviewId);
        //DefaultScrollHandle msbar = (DefaultScrollHandle) findViewById(R.id.sbarId);

        //mpdfView.setScrollbarFadingEnabled(true);

        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });


        Intent next = this.getIntent();
        String path = next.getExtras().getString("PATH");
        String title = next.getExtras().getString("TITLE");
        //getActionBar().hide();
        getSupportActionBar().hide();

        //setTitle(title);

        new RetrivePdf().execute(path);

    }


    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    class RetrivePdf extends AsyncTask<String,Void,InputStream>
    {
        @Override
        protected InputStream doInBackground(String...strings) {
            InputStream minputStream = null;
            try{
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                if(urlConnection.getResponseCode() == 200)
                {
                    minputStream = new BufferedInputStream(urlConnection.getInputStream());
                }
            }catch (IOException e){
                return null;
            }

            return minputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            mpdfView.fromStream(inputStream).load();
        }
    }


}
