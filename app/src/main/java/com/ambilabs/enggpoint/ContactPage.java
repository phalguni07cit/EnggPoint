package com.ambilabs.enggpoint;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class ContactPage extends Activity {
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_page);

        webview = (WebView) findViewById(R.id.webview1);
        webview.setWebViewClient(new MyWebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);

        openURL();
    }

    private void openURL() {
        webview.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLScBq37mKdXtjf1wrlh_duSRT2ud6fRuQ00pa26h54f8x6Xl9A/viewform?usp=sf_link");

        webview.requestFocus();

    }
}