package com.example.dfoptimizerapp;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetSlate extends AppCompatActivity {


    private static final String TAG= HttpConnect.class.getName();
    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;



//Add overrides




    private void SendRequestAndPrintResponse(int slateChoice) {


        String slateURL;


        /* NEW SLATE URL FOR NFL DRAFT KINGS*/
if(slateChoice == 1) {
    slateURL = "http://ec2-3-15-46-189.us-east-2.compute.amazonaws.com/dk/nfl/getslate";
}

//NEW SLATE URL FOR NBA DRAFT KINGS
else {
    slateURL = "http://ec2-3-15-46-189.us-east-2.compute.amazonaws.com/dk/nba/getslate";
}
        System.out.println(slateURL);

        mRequestQueue = Volley.newRequestQueue(this);

        stringRequest = new StringRequest(Request.Method.GET, slateURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response: " + response.toString());
                Toast.makeText(getApplicationContext(), "Generating New Slate... " , Toast.LENGTH_SHORT).show();
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
        //Write line to display in list view


        List<String> players = new ArrayList<String>();
        //List<String> names = new ArrayList<String>();
        List<String> salaries = new ArrayList<String>();
        List<String> positions = new ArrayList<String>();
        List<String> teams = new ArrayList<String>();
        List<String> opponents = new ArrayList<String>();
        List<String> projections = new ArrayList<String>();



        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(jsonStr);

            for(int i = 0; i < jsonarray.length(); i++) {
                System.out.println(jsonarray.length());
                System.out.println("Current:" + i);
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    players.add(jsonobject.getString("player"));
                    salaries.add(jsonobject.getString("Salary"));
                    positions.add(jsonobject.getString("Position"));
                    teams.add(jsonobject.getString("Team"));
                    opponents.add(jsonobject.getString("Opponent"));
                    projections.add(jsonobject.getString("Projection"));

                }
            System.out.println(players);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}







