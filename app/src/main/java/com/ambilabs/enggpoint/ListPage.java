package com.ambilabs.enggpoint;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
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
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListPage extends AppCompatActivity {

    ListView lview;
    ArrayList<Pdf> pdfArrayList =new ArrayList<>();
    String subject;
    InterstitialAd mInterstitialAd;
    AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_page);

        mInterstitialAd = new InterstitialAd(this);

        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen2));

        AdRequest adRequest = new AdRequest.Builder()
                .build();

        // Load ads into Interstitial Ads
        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

//Banner Ad Related things
        MobileAds.initialize(this, "ca-app-pub-1194349429791925~1927920961");

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest2 = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest2);
//till here

        Intent listPage = this.getIntent();
        String type = listPage.getExtras().getString("Type");
        subject = listPage.getExtras().getString("SUBJECT");
        String did = listPage.getExtras().getString("DID");
        lview = (ListView) findViewById(R.id.lvid);
        setTitle(type);

        getPdfs(type,did);
    }


    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void getPdfs(String type, String did) {

        String PDF_FETCH_URL;
        final Boolean typeCheck;

        if(type.equals("Notes")) {
         typeCheck = true;
            PDF_FETCH_URL = "http://engineerspoint.tech/getPdfs.php?param1="+did;

            //000webhost
        }
        else
        {
            typeCheck = false;
            PDF_FETCH_URL = "http://engineerspoint.tech/getPprSol.php?param1="+did;
            //PDF_FETCH_URL = "http://enggpoint.000webhostapp.com/getPprSol.php?param1="+did;
            //000webhost
        }
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching Notes... Please Wait");
        progressDialog.show();
        RequestQueue request = Volley.newRequestQueue(this);
        Log.e("Position","above SR");

        StringRequest stringRequest = new StringRequest(Request.Method.GET, PDF_FETCH_URL,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        progressDialog.dismiss();
                        try {
                            Log.e("Position","above json");
                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(ListPage.this,obj.getString("message"), Toast.LENGTH_SHORT).show();

                            JSONArray jsonArray = obj.getJSONArray("pdfs");

                            for(int i=0;i<jsonArray.length();i++){

                                //Declaring a json object corresponding to every pdf object in our json Array
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                //Declaring a Pdf object to add it to the ArrayList  pdfList
                                Pdf pdf  = new Pdf();
                                //String pdftopic = jsonObject.getString("topic");
                                //Log.e("Position",pdftopic);

                                String pdfUrl = jsonObject.getString("url");
                                String pdfAuthor = jsonObject.getString("author");

                                if(typeCheck==true) {
                                    String pdfTopicType = jsonObject.getString("topic");
                                    pdf.setTopic(pdfTopicType);

                                }
                                else
                                {
                                    String pstype = jsonObject.getString("type");
                                    String mon = jsonObject.getString("month");
                                    String year = jsonObject.getString("year");
                                    pdf.setType(pstype);
                                    pdf.setMonth(mon);
                                    pdf.setYear(year);
                                }
                                String org = jsonObject.getString("organisation");
                                String post = jsonObject.getString("post");
                                String datetime = jsonObject.getString("datetime");
                                pdf.setUrl(pdfUrl);
                                pdf.setAuthor(pdfAuthor);
                                pdf.setDatetime(datetime);
                                pdf.setOrg(org);
                                pdf.setPost(post);
                                //pdf.set
                                pdfArrayList.add(pdf);

                            }
                            lview.setAdapter(new CustomAdapter(ListPage.this, pdfArrayList,typeCheck,subject));
                            //lview.setAdapter(cAdap);
                            //pdfAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("this oer: ",error.toString());
                        //Toast.makeText(ListPage.this, "No Internet Connection",Toast.LENGTH_LONG);


                    }
                }
        );
        /*{

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("param1", "pitpdf");
                //params.put("param2", num2);
                return params;
            }
        };*/
        // lview = (ListView) findViewById(R.id.lvid);
        //CustomAdapter cAdap =
        //lview.setAdapter(new CustomAdapter(MainActivity.this, pdfArrayList));

        request.add(stringRequest);
        Log.e("Position","below SR");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        if (mAdView != null) {
            mAdView.resume();
        }
    }



    @Override
    public void onPause() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();}

        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();}
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }



}
