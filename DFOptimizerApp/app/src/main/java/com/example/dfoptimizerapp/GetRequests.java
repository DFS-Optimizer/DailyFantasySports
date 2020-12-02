package com.example.dfoptimizerapp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class GetRequests{

    private Context m_context;

    public GetRequests(Context context) {
        m_context = context;
    }

    public void SendRequestAndPrintResponse(String url, int num, TextView txtView){

        String TAG=DisplayLineups.class.getName();
        RequestQueue mRequestQueue;
        StringRequest stringRequest;
        url=url+num;

        mRequestQueue= Volley.newRequestQueue(m_context);

        stringRequest=new StringRequest(Request.Method.GET,url, response -> {
            Log.i(TAG,"Response: "+ response);
            Toast.makeText(m_context,"Generating Optimized Lineup... ",Toast.LENGTH_SHORT).show();
            ParseReceive(response,txtView, num);

        }, error -> Log.i(TAG,"Response: "+error.toString()));
        mRequestQueue.add(stringRequest);
    }

    public void ParseReceive(String jsonStr,TextView txtView, int num){

        if(jsonStr.contains( "slate unavailable"))
        {
            txtView.append("This sport is out of season");
        }
        else {
            //ELSE
            txtView.setText("");
            JSONArray jsonarray = null;
            try {
                jsonarray = new JSONArray(jsonStr);
                for(int j = 1; j <= num; j++) {
                    for (int i = 0; i < jsonarray.length(); i++) {
                        System.out.println(jsonarray.length());
                        System.out.println("Current:" + i);
                        if (i == jsonarray.length() - 1) {
                            JSONObject jsonObjectTotal = jsonarray.getJSONObject(i);
                            String total = jsonObjectTotal.getString("Total");
                            System.out.println("Total: " + total);
                            txtView.append("Total: " + total);
                        } else {
                            JSONObject jsonobject = jsonarray.getJSONObject(i);
                            String player = jsonobject.getString("player");
                            String score = jsonobject.getString("score");
                            System.out.println(player + " " + score);
                            txtView.append(player + " " + score);
                            txtView.append("\n");
                        }


                    }
                    txtView.append("\n\n");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
