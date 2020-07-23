package com.example.fire;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.Constants;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;

import javax.security.auth.callback.Callback;


public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private InterstitialAd interstitialAd;
    private AdView adView;
    ProgressDialog pDialog;
    private PDFView pdfView;
    private String url;
    final InputStream[] input = new InputStream[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isConnected(MainActivity.this)) buildDialog(MainActivity.this).show();
        else {
            setContentView(R.layout.activity_main);

            AudienceNetworkAds.initialize(this);
            adView = new AdView(this, "IMG_16_9_APP_INSTALL#331085661108287_331432567740263", AdSize.BANNER_HEIGHT_50);
            LinearLayout adContainer = (LinearLayout) findViewById(R.id.fb_banner);
            adContainer.addView(adView);
            adView.setAdListener(new AdListener() {
                @Override
                public void onError(Ad ad, AdError adError) {
                    // Ad error callback
                    Toast.makeText(MainActivity.this, "Error: " + adError.getErrorMessage(),
                            Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Ad loaded callback
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback
                }
            });


            // Request an ad
            // adView.loadAd();


            interstitialAd = new InterstitialAd(this, "VID_HD_16_9_15S_APP_INSTALL#331085661108287_331449584405228");
            interstitialAd.setAdListener(new InterstitialAdListener() {

                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    // Interstitial ad displayed callback
                    Log.e(TAG, "Interstitial ad displayed.");
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    // Interstitial dismissed callback
                    Log.e(TAG, "Interstitial ad dismissed.");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    // Ad error callback
                    Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    // Interstitial ad is loaded and ready to be displayed
                    Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                    // Show the ad
                    interstitialAd.show();
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                    Log.d(TAG, "Interstitial ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback
                    Log.d(TAG, "Interstitial ad impression logged!");
                }


            });

            // interstitialAd.loadAd();


            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setTitle("PDF");
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            final WebView webView = findViewById(R.id.webView);
            final ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);


            url = "https://firebasestorage.googleapis.com/v0/b/fire-75564.appspot.com/o/Chapter%202%20%20Federalism.pdf?alt=media&token=0e6cc371-a080-4e0d-bd4e-ed772022299b";
            downloadPdf(url);
        }

    }
    private void downloadPdf(final String url){

       new AsyncTask<Void, Void, Void>()

    {
        @SuppressLint({"WrongThread", "StaticFieldLeak"})
        @Override
        protected Void doInBackground (Void...voids){
        try {
            getSupportActionBar().setTitle("Loading PDF....");
            input[0] = new URL(url).openStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

        @Override
        protected void onPostExecute (Void aVoid){
        super.onPostExecute(aVoid);
        openPdf();
    }
    }.execute();

}
    private void openPdf() {
        try {
            getSupportActionBar().setTitle("Showing PDF 👍");
            pdfView = findViewById(R.id.pdfView);
            pdfView.setVisibility(View.VISIBLE);
            pdfView.fromStream(input[0])
                    .enableAntialiasing(true)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .spacing(0)
                    .load();

        } catch (Exception e) {
            e.printStackTrace();

        }



    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.super_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                try {
                    getSupportActionBar().setTitle("Showing PDF 👍");
                    pdfView = findViewById(R.id.pdfView);
                    pdfView.setVisibility(View.VISIBLE);
                    pdfView.fromStream(input[0])
                            .enableAntialiasing(true)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .scrollHandle(new DefaultScrollHandle(this))
                            .spacing(0)
                            .load();

                } catch (Exception e) {
                    e.printStackTrace();

                }
                Toast.makeText(getApplicationContext(), "Page is Refreshing Please Wait", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
        else return false;
        } else
        return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }


    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        super.onDestroy();
    }


}
