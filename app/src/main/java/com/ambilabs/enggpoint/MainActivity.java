package com.ambilabs.enggpoint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {

    Button nBtn;
    Button vBtn;
    String answer;
    String subject;
    Button psBtn;
    String did;
    Boolean chk;
    ArrayMap <String,String> aMap;

    //ArrayList<Pdf> pdfArrayList;
    ArrayList<String> sub;
    Spinner subSpinner;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chk=true;
        //boolean flag1 =false;
        //boolean flag2 =false;

        FloatingActionButton contactIcon = (FloatingActionButton) findViewById(R.id.contactIconId);
        contactIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toContact = new Intent(MainActivity.this, ContactPage.class);
                startActivity(toContact);
            }
        });


        nBtn = (Button)findViewById(R.id.notesBtn);
        //vBtn = (Button)findViewById(R.id.vidBtn);
        psBtn = (Button)findViewById(R.id.pprBtn);

        sub = new ArrayList<>();
        aMap = new ArrayMap<>();

       /* final Spinner semSpinner = (Spinner)findViewById(R.id.sem_spinner);

        final Spinner branchSpinner = (Spinner)findViewById(R.id.branch_spinner);

        //subSpinner.setOnItemSelectedListener();
        ArrayAdapter<String> adapBranch = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,getResources()
                .getStringArray(R.array.Branch));

        adapBranch.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        branchSpinner.setAdapter(adapBranch);

        String branch = branchSpinner.getSelectedItem().toString();


        ArrayAdapter<String> adapSem = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,getResources()
                .getStringArray(R.array.Semester));

        adapSem.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        semSpinner.setAdapter(adapSem);
        String semester = semSpinner.getSelectedItem().toString();
        flag2=true;


        if(flag1&&flag2)
        setSpinner(semester,branch);

        */

//Banner Ad Related things
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-1194349429791925~1927920961");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
//till here



        subSpinner = (Spinner)findViewById(R.id.sub_spinner);
        setSubSpinner();
        subSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // if(chk==true)
                 //   chk=false;
                //else
                subject = subSpinner.getSelectedItem().toString();
                did = aMap.get(subject);//getDid(subSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //To check network connectivity!
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null == activeNetwork) {
            answer = "No Internet Connectivity";
            Toast.makeText(getApplicationContext(), answer, Toast.LENGTH_LONG).show();
        }

        nBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent listPage = new Intent(MainActivity.this, ListPage.class);
                    // getPdfs();
                listPage.putExtra("DID",did);
                listPage.putExtra("SUBJECT",subject);
                    listPage.putExtra("Type", "Notes");
                    startActivity(listPage);
            }
        });

        //setting on click listner on video button
        /*vBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
*/
        //Setting on clicklistner on paper soltuin buton
        psBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listPage = new Intent (MainActivity.this,ListPage.class);
                listPage.putExtra("DID",did);
                listPage.putExtra("SUBJECT",subject);
                listPage.putExtra("Type","Papers & Solution");
                startActivity(listPage);
            }});
    }





    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null == activeNetwork) {
            answer = "No internet Connectivity";
            Toast.makeText(getApplicationContext(), answer, Toast.LENGTH_LONG).show();
        }

        if (mAdView != null) {
            mAdView.resume();
        }
    }


    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    private void setSubSpinner()
    {
        String PDF_FETCH_URL;
        Boolean chk2 = true;
        PDF_FETCH_URL = "http://engineerspoint.tech/getSub.php";
        //PDF_FETCH_URL = "http://enggpoint.000webhostapp.com/getSub.php";
        //000webhost


            RequestQueue request = Volley.newRequestQueue(this);
            Log.e("Position", "above SR sub");

            StringRequest stringRequest = new StringRequest(Request.Method.GET, PDF_FETCH_URL,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            // progressDialog.dismiss();
                            try {
                                Log.e("Position", "above json sub");
                                JSONObject obj = new JSONObject(response);
                                //Toast.makeText(MainActivity.this,obj.getString("message"), Toast.LENGTH_SHORT).show();

                                JSONArray jsonArray = obj.getJSONArray("details");

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    sub.add(jsonObject.getString("sub"));
                                    aMap.put(jsonObject.getString("sub"),String.valueOf(jsonObject.getInt("did")));
                                    //sub.add(String.valueOf(jsonObject.getInt("did")));
                                }
                                subSpinner.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, sub));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("this oer: ", error.toString());
                        }
                    }
            );
            request.add(stringRequest);
        }

/*
    private void getDid(String subject)
    {


        if(sem.equalsIgnoreCase("Sem 1"))
            sem="1";
        else if(sem.equalsIgnoreCase("Sem 2"))
            sem="2";
        else if(sem.equalsIgnoreCase("Sem 3"))
            sem="3";
        else if(sem.equalsIgnoreCase("Sem 4"))
            sem="4";
        else if(sem.equalsIgnoreCase("Sem 5"))
            sem="5";
        else if(sem.equalsIgnoreCase("Sem 6"))
            sem="6";
        else if(sem.equalsIgnoreCase("Sem 7"))
            sem="7";
        else if(sem.equalsIgnoreCase("Sem 8"))
            sem="8";


        String PDF_FETCH_URL;
        PDF_FETCH_URL = "http://engineerspoint.tech/getDid.php?param1="+subject;

        RequestQueue request = Volley.newRequestQueue(this);
        Log.e("Position","above SR did");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, PDF_FETCH_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                       // progressDialog.dismiss();
                        try {
                            Log.e("Position","above json did");
                            JSONObject obj = new JSONObject(response);
                            //Toast.makeText(MainActivity.this,obj.getString("message"), Toast.LENGTH_SHORT).show();

                            JSONArray jsonArray = obj.getJSONArray("details");

                            for(int i=0;i<jsonArray.length();i++){
                                //Declaring a json object corresponding to every pdf object in our json Array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                did = jsonObject.getString("did");
                                Log.e("Position",did);

                            }
                            subSpinner.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, sub));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("this oer: ",error.toString());
                    }
                }
        );
        request.add(stringRequest);
    }*/
}
