package com.example.dfoptimizerapp;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetRequests{

    private Context m_context;
    static private ArrayList<ArrayList<String>> lineups;

    public GetRequests(Context context) {
        m_context = context;
    }

    public ArrayList<ArrayList<String>> SendRequestAndPrintResponse(String url, int num){

        String TAG=DisplayLineups.class.getName();
        RequestQueue mRequestQueue;
        StringRequest stringRequest;
        url=url+num;
        lineups = new ArrayList<>(num);
        mRequestQueue= Volley.newRequestQueue(m_context);

        stringRequest=new StringRequest(Request.Method.GET,url, response -> {
            Log.i(TAG,"Response: "+ response);
            Toast.makeText(m_context,"Generating Optimized Lineup... ",Toast.LENGTH_SHORT).show();
            lineups = ParseReceive(response, num);


        }, error -> Log.i(TAG,"Response: "+error.toString()));
        mRequestQueue.add(stringRequest);
        return lineups;

    }

    public ArrayList<ArrayList<String>> ParseReceive(String jsonStr, int num){
         ArrayList<ArrayList<String>> allLineups  = new ArrayList<>(num);
        ArrayList<String> singleLineup;

        if(jsonStr.contains( "slate unavailable"))
        {
            singleLineup = new ArrayList<>();
            singleLineup.add("This sport is out of season");
            allLineups.add(singleLineup);
        }
        else {
            JSONArray jsonarray;
            try {
                jsonarray = new JSONArray(jsonStr);
                for(int j = 0; j < num; j++) {
                    singleLineup = new ArrayList<>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        if (i == jsonarray.length() - 1) {
                            JSONObject jsonObjectTotal = jsonarray.getJSONObject(i);
                            String total = jsonObjectTotal.getString("Total");
                            singleLineup.add("Total: " + total);
                            System.out.println("Total: " + total);
                        } else {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String player = jsonobject.getString("player");
                            String score = jsonobject.getString("score");
                            System.out.println(player + " " + score);
                            singleLineup.add(player + " " + score);
                        }
                    }
                    allLineups.add(singleLineup);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return allLineups;
    }
    public void Display(ArrayList<ArrayList<String>> lineups, TextView txtView)
    {
        txtView.setText("");
        for(int i = 0; i < lineups.size(); i++)
        {
            for(int j = 0; j < lineups.get(i).size(); j++)
            {
                txtView.append(lineups.get(i).get(j));
                txtView.append("\n");
            }
            txtView.append("\n\n");
        }
    }

}
