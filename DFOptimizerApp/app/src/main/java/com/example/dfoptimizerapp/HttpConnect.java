package com.example.dfoptimizerapp;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import android.app.DownloadManager;
import java.util.regex.Pattern;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;






public class HttpConnect extends AppCompatActivity {

    private static final String TAG= HttpConnect.class.getName();
    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_connect);
        final int site = getIntent().getIntExtra("siteChoice",1);
        final int sport = getIntent().getIntExtra("sportChoice", 1);
        final TextView numberOfLineups = findViewById(R.id.numberOfLineups);


        String url = "http://ec2-3-15-46-189.us-east-2.compute.amazonaws.com/";

        if(site == 1)
        {
            url = url.concat("fd/");
        }
        else
        {
            url = url.concat("dk/");
        }

        if(sport == 1)
        {
            url = url.concat("nba/");
        }
        else if(sport == 2)
        {
            url=url.concat("nfl/");
        }
        else
        {
            url = url.concat("mlb/");
        }

        //Connect button
        Button btn = (Button) findViewById(R.id.httpBut);
        String finalUrl = url;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int num = Integer.parseInt(numberOfLineups.getText().toString());
                    if(num > 0) {
                        SendRequestAndPrintResponse(finalUrl, num);
                    }
                    else
                    {
                        Toast.makeText(HttpConnect.this, "You must enter a number of lineups greater than 0", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (NumberFormatException e){
                    Toast.makeText(HttpConnect.this, "You must enter a number of lineups greater than 0", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void SendRequestAndPrintResponse(String url, int num) {

        int i;
        int j;
        ArrayList<String> initialPlayerList = (ArrayList<String>) getIntent().getSerializableExtra("selectedPlayers");
        ArrayList<String> formattedPlayerList = new ArrayList<String>();
        String name;
        String formattedName = "";

        System.out.println("Input: " + initialPlayerList);

        for(i = 0; i < initialPlayerList.size(); i++){
            name = initialPlayerList.get(i);
            for(j = 0; j < name.length(); j++){
                //append each letter until a space
                if(name.charAt(j) == ' '){
                    //append %20
                    formattedName = formattedName + "%20";
                }
                else{
                    formattedName = formattedName + name.charAt(j);
                }
            }
            formattedPlayerList.add(formattedName);
            formattedName = "";
        }
        System.out.println(formattedPlayerList);
        int k;
        String finalURL = url;
        for(k = 0; k < formattedPlayerList.size(); k++){

            finalURL = finalURL + formattedPlayerList.get(k);
            finalURL = finalURL + "/";
        }
        finalURL = finalURL + num;
        System.out.println(finalURL);


        mRequestQueue = Volley.newRequestQueue(this);

        stringRequest = new StringRequest(Request.Method.GET, finalURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response: " + response.toString());
                Toast.makeText(getApplicationContext(), "Generating Optimized Lineup... " , Toast.LENGTH_SHORT).show();
                ParseReceive(response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Response: " + error.toString());
            }

        });
        mRequestQueue.add(stringRequest);
    }

    public void ParseReceive(String jsonStr) {
        TextView txtView = (TextView) findViewById(R.id.textView3);
        txtView.setText(" ");
        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(jsonStr);

            for(int i = 0; i < jsonarray.length(); i++) {
                System.out.println(jsonarray.length());
                System.out.println("Current:" + i);
                if(i == jsonarray.length()-1){
                    JSONObject jsonObjectTotal = jsonarray.getJSONObject(i);
                    String total = jsonObjectTotal.getString("Total");
                    System.out.println("Total: "  + total);
                    txtView.append("Total: " + total);
                }
                else {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String player = jsonobject.getString("player");
                    String score = jsonobject.getString("score");
                    System.out.println(player + " " + score);
                    txtView.append(player + " " + score);
                    txtView.append("\n");
                }


            }

        } catch (JSONException e) {
                e.printStackTrace();
            }
    }


}







