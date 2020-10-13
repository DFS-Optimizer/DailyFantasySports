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
        String url;
        if(myList.size() != 0) {
             url = "http://73.82.159.9:5000/selectplayer/";
        }
        else{
             url = "http://73.82.159.9:5000/selectplayer";
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







    //Textview to Display optimized lineup
        TextView txtView = (TextView) findViewById(R.id.textView3);

        mRequestQueue = Volley.newRequestQueue(this);

        stringRequest = new StringRequest(Request.Method.GET, finalURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response: " + response.toString());
                Toast.makeText(getApplicationContext(), "Response: " + response.toString(), Toast.LENGTH_SHORT).show();
                txtView.setText(response.toString());
                //String testResponse = "{output: [{players: Lebron James}, {score: 42}]}";
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

        JSONArray jsonarray = new JSONArray(jsonStr);
        for(int i = 0; i < jsonarray.length(); i++){

            JSONObject jsonobject = jsonarray.getJSONObject(i);
            String player = jsonobject.getString("player");
            String score = jsonobject.getString("score");
            System.out.println(player);
            System.out.println(score);
        }
    }


}







// In other words : print("{output : [{players:" + player(0) + ", score:" + player(1) + "}]}")
    /*
        String input = "{output : [{players:LebronJames, score:20}]}";
    String input2 = "{output : [{players:LebronJames, score:20}]}";

try {
    JSONObject obj = new JSONObject(input);

    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> scoreList = new ArrayList<String>();
    JSONArray array = obj.getJSONArray("output");
    for (int i = 0; i < array.length(); i++) {
        list.add(array.getJSONObject(i).getString("players"));
        scoreList.add(array.getJSONObject(i).getString("score"));

    }
for(int j = 0; j < array.length(); j++){
    System.out.println(list.get(j) + " " + scoreList.get(j));
}

}
catch(JSONException e) {
    Log.e(TAG, "unexpected JSON exception", e);
    // Do something to recover ... or kill the app.

        }

*/

    /*
        try {
            JSONObject obj = new JSONObject(received);
            String playerName = obj.getJSONObject("output").getString("players");
            System.out.println(playerName);
            //JSONArray arr = obj.getJSONArray("posts");
            /*for (int i = 0; i < arr.length(); i++)
            {
                String post_id = arr.getJSONObject(i).getString("post_id");

            }
            */

/*
        } catch (JSONException e) {
            Log.e(TAG, "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }
*/