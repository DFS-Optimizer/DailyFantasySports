package com.example.dfoptimizerapp;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ArrayList<ArrayList<String>> SendRequestAndPrintResponse(String url, int num, Spinner spinner){

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
            String[] lineupArray = lineups.get(0).toArray(new String[lineups.size()]);
            ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(m_context, android.R.layout.simple_spinner_dropdown_item, lineupArray) {
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                    ((TextView) v).setTextColor(Color.parseColor("#ffffff"));
                    return v;
                }
            };
            spinner.setAdapter(dropdownAdapter);



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
                    singleLineup.add("Lineup " + (j+1));
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
