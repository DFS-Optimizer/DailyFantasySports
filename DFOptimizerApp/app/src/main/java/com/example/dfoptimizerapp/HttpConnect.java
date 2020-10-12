package com.example.dfoptimizerapp;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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


public class HttpConnect extends AppCompatActivity {

    private static final String TAG= HttpConnect.class.getName();
    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;
    //private String url="http://73.82.159.9:5000/";
    //ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("selectedPlayers");
//private String url= "https://run.mocky.io/v3/9c07d6d8-1ca2-4ca9-a2d2-fe104731e71c";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_connect);


        //Connect button
        Button btn = (Button) findViewById(R.id.httpBut);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Sending http request", Toast.LENGTH_SHORT).show();
                SendRequestAndPrintResponse();
            }
        });


    }
    private void SendRequestAndPrintResponse() {

        int i;
        int j;
        ArrayList<String> myList = (ArrayList<String>) getIntent().getSerializableExtra("selectedPlayers");
        //ArrayList<String> myList = new ArrayList<String>();
        //myList.add("Lebron James");
        //myList.add("Kawhi Leonard");
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


        String url = "http://73.82.159.9:5000/selectplayer/";
        int k;
        String finalURL = url;
        for(k = 0; k < newList.size(); k++){

            finalURL = finalURL + newList.get(k);

            finalURL = finalURL + "/";

        }

        System.out.println(finalURL);








        mRequestQueue = Volley.newRequestQueue(this);

        stringRequest = new StringRequest(Request.Method.GET, finalURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response: " + response.toString());
                Toast.makeText(getApplicationContext(), "Response: " + response.toString(), Toast.LENGTH_SHORT).show();
                ParseString(response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Response: " + error.toString());
            }

        });
        mRequestQueue.add(stringRequest);
    }


    public void ParseString(String line) {
        //using String split function
        String[] words = line.split(" ");
        System.out.println(Arrays.toString(words));
        //using java.util.regex Pattern
        //Pattern pattern = Pattern.compile(" ");
        //words = pattern.split(line);
        //System.out.println(Arrays.toString(words));
    }


}



