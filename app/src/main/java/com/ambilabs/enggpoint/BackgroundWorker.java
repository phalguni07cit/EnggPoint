package com.ambilabs.enggpoint;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class BackgroundWorker extends AsyncTask<String,Void,String> {

    Context context;
    ProgressDialog dialog;
    public BackgroundWorker()
    {}
    public BackgroundWorker(Context ctx)
    {
        context = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
                dialog = ProgressDialog.show(context, "Please Wait", null, true, true);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
    }

    @Override
    protected String doInBackground(String... params) {

        String sem = (String)params[0];
        String branch = (String)params[1];
        String link = "";
        BufferedReader bufferReader=null;


        try{
        URL url = new URL(link);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection() ;
            bufferReader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String result;
            result=bufferReader.readLine();
            return  result;

        } catch(Exception e) {
            return new String("Exception: " + e.getMessage());
        }

    }
}
