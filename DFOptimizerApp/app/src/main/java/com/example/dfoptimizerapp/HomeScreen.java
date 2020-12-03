package com.example.dfoptimizerapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    private static final String TAG = DisplayLineups.class.getName();
    private RequestQueue mRequestQueue;
    private StringRequest stringRequest;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        final Button customizeBtn = findViewById(R.id.customizeBtn);
        ArrayList<ArrayList<String>> nbaDKLineup;
        ArrayList<ArrayList<String>> nflDKLineup;
        ArrayList<ArrayList<String>> mlbDKLineup;
        ArrayList<ArrayList<String>> nbaFDLineup;
        ArrayList<ArrayList<String>> nflFDLineup;
        ArrayList<ArrayList<String>> mlbFDLineup;
        Spinner nbaDK = findViewById(R.id.nba_dk_lineup);

        Spinner nbaFD = findViewById(R.id.nba_fd_lineup);
        Spinner nflDK = findViewById(R.id.nfl_dk_lineup);
        nflDK.setClickable(false);
        Spinner nflFD = findViewById(R.id.nfl_fd_lineup);
        Spinner mlbDK = findViewById(R.id.mlb_dk_lineup);
        Spinner mlbFD = findViewById(R.id.mlb_fd_lineup);
        String url_DK = "http://ec2-18-188-133-48.us-east-2.compute.amazonaws.com/dk/";
        String url_FD = "http://ec2-18-188-133-48.us-east-2.compute.amazonaws.com/fd/";





        CheckGetState(url_DK, "nba/", nbaDK);
        CheckGetState(url_DK, "nfl/", nflDK);
        CheckGetState(url_DK, "mlb/", mlbDK);

        CheckGetState(url_FD, "nba/", nbaFD);
        CheckGetState(url_FD, "nfl/", nflFD);
        CheckGetState(url_FD, "mlb/", mlbFD);

        customizeBtn.setOnClickListener((v) ->{
            startActivity(new Intent(getApplicationContext(), ChooseSite.class));
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void CheckGetState(String url, String sport, Spinner spinner)
    {
        mRequestQueue = Volley.newRequestQueue(this);
        GetRequests getRequests = new GetRequests(getApplicationContext());
        String slateURL = url + sport + "getslate";
        stringRequest = new StringRequest(Request.Method.GET, slateURL, response -> {
            Log.i(TAG, "Response: " + response);
            if(!response.contains( "slate unavailable")) {
                getRequests.SendRequestAndPrintResponse(url + sport, 1, spinner);
            }
            else
            {
                String[] sportUnavailable = new String[] {"This sport is out of season"};
                ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sportUnavailable) {
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                        ((TextView) v).setTextColor(Color.parseColor("#ffffff"));
                        return v;
                    }
                };
                spinner.setAdapter(dropdownAdapter);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    spinner.setBackgroundTintList(getColorStateList(R.color.white));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    spinner.setForegroundTintList(getColorStateList(R.color.white));
                }
            }
        }, error -> Log.i(TAG, "Response: " + error.toString()));
        mRequestQueue.add(stringRequest);
    }


}

