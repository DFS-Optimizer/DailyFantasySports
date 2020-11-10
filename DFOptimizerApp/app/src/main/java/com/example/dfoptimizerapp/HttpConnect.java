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
            url = url.concat("nba");
        }
        else
        {
            url=url.concat("nfl");
        }

        //Connect button
        Button btn = (Button) findViewById(R.id.httpBut);
        String finalUrl = url;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendRequestAndPrintResponse(finalUrl);
            }
        });






    }
    private void SendRequestAndPrintResponse(String url) {

        int i;
        int j;
        ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("selectedPlayers");
        ArrayList<String> newList = new ArrayList<String>();
        String word;
        String newWord = "";

        System.out.println("Input: " + myList);

        for(i = 0; i < myList.size(); i++){
            word = myList.get(i);
            for(j = 0; j < word.length(); j++){
                //append each letter until a space
                if(word.charAt(j) == ' '){
                    //append %20
                    newWord = newWord + "%20";
                }
                else{
                    newWord = newWord + word.charAt(j);
                }
            }
            newList.add(newWord);
            newWord = "";
        }
        System.out.println(newList);
        if(myList.size() != 0) {
             url = url.concat("/");
        }
        int k;
        String finalURL = url;
        for(k = 0; k < newList.size(); k++){

            finalURL = finalURL + newList.get(k);
            if(k != newList.size()-1) {
                finalURL = finalURL + "/";
            }

        }

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







