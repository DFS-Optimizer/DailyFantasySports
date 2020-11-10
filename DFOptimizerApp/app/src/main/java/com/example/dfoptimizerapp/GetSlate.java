package com.example.dfoptimizerapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class GetSlate {


    private static final String TAG = HttpConnect.class.getName();
    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;

    private int m_siteChoice;
    private int m_sportChoice;
    private View m_view;
    private Context m_context;

//Add overrides

    public GetSlate(View v, int siteChoice, int sportChoice, Context context) {
        m_siteChoice = siteChoice;
        m_sportChoice = sportChoice;
        m_view = v;
        m_context = context;
        System.out.println(m_siteChoice);
        System.out.println(m_sportChoice);
        System.out.println(m_view);
        SendRequestAndPrintResponse(sportChoice);
    }

    public static boolean checkValidURL(String slateURL){
        try {
            URL url = new URL(slateURL);
            URLConnection conn = url.openConnection();
            conn.connect();
        } catch (MalformedURLException e) {
            // the URL is not in a valid form
            return false;
        } catch (IOException e) {
            // the connection couldn't be established
            return false;
        }
    return true;
    }

    private void SendRequestAndPrintResponse(int slateChoice) {


        String slateURL;


        /* NEW SLATE URL FOR NFL DRAFT KINGS*/
        if (m_sportChoice == 2) {
            slateURL = "abcdedg";
                    //"http://ec2-3-15-46-189.us-east-2.compute.amazonaws.com/dk/nfl/getslate";
        }

    //NEW SLATE URL FOR NBA DRAFT KINGS
        else {
            slateURL = "http://ec2-3-15-46-189.us-east-2.compute.amazonaws.com/dk/nba/getslate";
        }

        System.out.println(slateURL);
        /*boolean checkURL = checkValidURL(slateURL);
        if(checkURL == false){
            throw new IllegalArgumentException("URL for slate is invalid");
        }*/
        //DEBUG CODE
        System.out.println(slateURL);



        mRequestQueue = Volley.newRequestQueue(m_context);

        stringRequest = new StringRequest(Request.Method.GET, slateURL, response -> {
            Log.i(TAG, "Response: " + response);
            if(response.contains( "slates not available, nba out of season"))
            {
                Toast.makeText(m_context,"This sport is out of season", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(m_context, "Generating New Slate... ", Toast.LENGTH_SHORT).show();
                ArrayList<String> data = ParseReceive(response);

                Intent listPlayers = new Intent(m_view.getContext(), ListPlayers.class);
                listPlayers.putExtra("siteChoice", m_siteChoice);
                listPlayers.putExtra("sportChoice", m_sportChoice);
                listPlayers.putExtra("playerList", data);
                m_context.startActivity(listPlayers);
            }


        }, error -> Log.i(TAG, "Response: " + error.toString()));
        mRequestQueue.add(stringRequest);
    }


    public ArrayList<String> ParseReceive(String jsonStr) {
        //Write line to display in list view

        ArrayList<String> data = new ArrayList<>();
        String formattedLine;
        List<String> players = new ArrayList<String>();

        List<String> salaries = new ArrayList<String>();
        List<String> positions = new ArrayList<String>();
        List<String> teams = new ArrayList<String>();
        List<String> opponents = new ArrayList<String>();
        List<String> projections = new ArrayList<String>();


        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(jsonStr);

            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                players.add(jsonobject.getString("player"));
                salaries.add(jsonobject.getString("Salary"));
                positions.add(jsonobject.getString("Position"));
                teams.add(jsonobject.getString("Team"));
                opponents.add(jsonobject.getString("Opponent"));
                projections.add(jsonobject.getString("Projection"));

                formattedLine = players.get(i) + "(" + positions.get(i) + ", " + teams.get(i) + ") -- Proj. FP: " +
                        projections.get(i) + "\n$" + salaries.get(i) +
                        ", Opponent: " + opponents.get(i);
                data.add(formattedLine);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;

    }



}







