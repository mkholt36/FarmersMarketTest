package com.example.mholt2587.farmersmarkettest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    private ListView mListView;

    GPSTracker gps;

    Context mContext;

    String marketAddress;
    ArrayList<String> marketAddressArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mListView = (ListView) findViewById(R.id.list_view);

        double latitude = 45.496481;
        double longitude = -122.573462;

        gps = new GPSTracker(mContext, MainActivity.this);

        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }
        else {
            gps.showSettingsAlert();
        }


        final String marketUrl = "https://search.ams.usda.gov/farmersmarkets/v1/data.svc/locSearch?lat=" + latitude + "&lng=" + longitude;

        Log.d(TAG, String.valueOf(latitude));
        Log.d(TAG, String.valueOf(longitude));


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(marketUrl)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {


            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "failure");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    final String jsonData = response.body().string();
                    Log.v(TAG, "THIS IS MY JSONDATA " + jsonData);

                    if (response.isSuccessful()) {
                        Log.d(TAG, marketUrl);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getCurrentDetails(jsonData);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                        Log.v(TAG, jsonData);

                    }
                }
                catch (IOException e) {
                    Log.e(TAG, "Exception caught: ", e);
                }

            }
        });

        Log.d(TAG, "Main UI code is running!");

    }

    private void getCurrentDetails(String jsonData) throws JSONException {

        JSONObject usdaJSON = new JSONObject(jsonData);
        JSONArray resultsJSON = usdaJSON.getJSONArray("results");
        Market[] markets = new Market[resultsJSON.length()];
        for(int i = 0; i < resultsJSON.length(); i++){
            final JSONObject marketJSON = resultsJSON.getJSONObject(i);

            String marketname = marketJSON.getString("marketname");
            String id = marketJSON.getString("id");

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://search.ams.usda.gov/farmersmarkets/v1/data.svc/mktDetail?id=" + id)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {


                @Override
                public void onFailure(Request request, IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "failure");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        final String marketDetailsJsonData = response.body().string();
                        Log.v(TAG, "THIS IS MY JSONDATA " + marketDetailsJsonData);

                        if (response.isSuccessful()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        JSONObject detailsJSON = new JSONObject(marketDetailsJsonData);
                                        JSONObject marketDetailsJSON = detailsJSON.getJSONObject("marketdetails");
                                        marketAddress = marketDetailsJSON.getString("Address");
                                        marketAddressArrayList.add(marketAddress);
                                        //marketAddressArrayList.get(0);


                                        //updateMarketAddress();   call this method for each market found - it will run 20 times if there are 20 markets
                                        Log.d(TAG, "this is marketadress"+ marketAddress);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d(TAG, "broken");
                                    }
                                }
                            });


                        }
                    }
                    catch (IOException e) {
                        Log.e(TAG, "Exception caught: ", e);
                    }

                }


            });



            Log.d(TAG, "outside of the loop"+ marketname);

            Market market = new Market(marketname, id, marketAddress);
            markets[i] = market;
            //markets[i].setAddress(marketAddressArrayList.get(i));
        }

        MarketAdapter adapter = new MarketAdapter(this, markets);
        mListView.setAdapter(adapter);

        for(int i = 0; i < resultsJSON.length(); i++) {
            Log.d(TAG, markets[i].getMarketname());
            Log.d(TAG, markets[i].getId());
           // Log.d(TAG, markets[i].getMarketAddress());
        }

    }
}
