package com.example.dfoptimizerapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GetSlate {


    private static final String TAG = DisplayLineups.class.getName();
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
        String slateURL = "";

        //NEW SLATE URL FOR NBA DRAFT KINGS

        if (m_sportChoice == 1) {
            if(m_siteChoice == 2) {
                slateURL = "http://ec2-18-188-133-48.us-east-2.compute.amazonaws.com/dk/nba/getslate";
            }
            else
            {
                slateURL = "http://ec2-18-188-133-48.us-east-2.compute.amazonaws.com/fd/nba/getslate";
            }

        }
        /* NEW SLATE URL FOR NFL DRAFT KINGS*/
        else if(m_sportChoice == 2){
            if(m_siteChoice == 2) {
                slateURL = "http://ec2-18-188-133-48.us-east-2.compute.amazonaws.com/dk/nfl/getslate";
            }
            else
            {
                slateURL = "http://ec2-18-188-133-48.us-east-2.compute.amazonaws.com/fd/nfl/getslate";
            }

        }
        //NEW SLATE URL FOR MLB DRAFT KINGS
        else
        {
            if(m_siteChoice == 2) {
                slateURL = "http://ec2-18-188-133-48.us-east-2.compute.amazonaws.com/dk/mlb/getslate";
            }
            else
            {
                slateURL = "http://ec2-18-188-133-48.us-east-2.compute.amazonaws.com/dk/nba/getslate";
            }
        }
        //DEBUG CODE
        System.out.println(slateURL);

        mRequestQueue = Volley.newRequestQueue(m_context);

        stringRequest = new StringRequest(Request.Method.GET, slateURL, response -> {
            Log.i(TAG, "Response: " + response);
            if(response.contains( "slate unavailable"))
            {
                Toast.makeText(m_context,"This sport is out of season", Toast.LENGTH_SHORT).show();
            }
            else{
                //Toast.makeText(m_context, "Generating New Slate... ", Toast.LENGTH_SHORT).show();

                ArrayList<ArrayList<String>> data = ParseReceive(response);

                Intent listPlayers;

                listPlayers = new Intent(m_view.getContext(), ListPlayers.class);
                listPlayers.putExtra("siteChoice", m_siteChoice);
                listPlayers.putExtra("sportChoice", m_sportChoice);
                listPlayers.putExtra("playerList", data.get(0));
                listPlayers.putExtra("positionList", data.get(1));
                listPlayers.putExtra("teamsList", data.get(2));
                listPlayers.putExtra("salariesList", data.get(3));
                listPlayers.putExtra("opponentsList", data.get(4));
                listPlayers.putExtra("projectionList", data.get(5));
                m_context.startActivity(listPlayers);
            }


        }, error -> Log.i(TAG, "Response: " + error.toString()));
        mRequestQueue.add(stringRequest);
    }


    public ArrayList<ArrayList<String>> ParseReceive(String jsonStr) {
        //Write line to display in list view

        ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
        String formattedLine;
        ArrayList<String> players = new ArrayList<String>();

        ArrayList<String> salaries = new ArrayList<String>();
        ArrayList<String> positions = new ArrayList<String>();
        ArrayList<String> teams = new ArrayList<String>();
        ArrayList<String> opponents = new ArrayList<String>();
        ArrayList<String> projections = new ArrayList<String>();


        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(jsonStr);


            for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    players.add(jsonobject.getString("player"));
                    String salary = jsonobject.getString("Salary");
                    salary = "$" + salary;
                    salaries.add(salary);
                    positions.add(jsonobject.getString("Position"));
                    teams.add(jsonobject.getString("Team"));
                    opponents.add(jsonobject.getString("Opponent"));

                    DecimalFormat projFormat = new DecimalFormat("#.##");
                    double proj = Double.parseDouble(jsonobject.getString("Projection"));

                    projections.add(projFormat.format(proj));

                    /*formattedLine = players.get(i) + "(" + positions.get(i) + ", " + teams.get(i) + ") -- Proj. FP: " +
                            projFormat.format(proj) + "\n$" + salaries.get(i) +
                            ", Opponent: " + opponents.get(i);*/

            }

            data.add(players);
            data.add(positions);
            data.add(teams);
            data.add(salaries);
            data.add(opponents);
            data.add(projections);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;

    }



}







