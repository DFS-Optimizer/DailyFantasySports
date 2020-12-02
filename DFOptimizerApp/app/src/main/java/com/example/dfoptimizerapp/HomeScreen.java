package com.example.dfoptimizerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        final Button customizeBtn = findViewById(R.id.customizeBtn);
        final TextView nbaTextView = findViewById(R.id.nbaLineupTxtView);
        final TextView nflTextView = findViewById(R.id.nflLineupTxtView);
        final TextView mlbTextView = findViewById(R.id.mlbLineupTxtView);
        ArrayList<ArrayList<String>> nbaLineup;
        ArrayList<ArrayList<String>> nflLineup;
        ArrayList<ArrayList<String>> mlbLineup;
        String url = "http://ec2-3-15-46-189.us-east-2.compute.amazonaws.com/dk/";

        GetRequests getRequests = new GetRequests(getApplicationContext());
        nbaLineup = getRequests.SendRequestAndPrintResponse(url + "nba/", 1);
        nflLineup = getRequests.SendRequestAndPrintResponse(url + "nfl/", 1);
        mlbLineup = getRequests.SendRequestAndPrintResponse(url + "mlb/", 1);

        getRequests.Display(nbaLineup, nbaTextView);
        getRequests.Display(nflLineup, nflTextView);
        getRequests.Display(mlbLineup, mlbTextView);

        customizeBtn.setOnClickListener((v) ->{
            startActivity(new Intent(getApplicationContext(), ChooseSite.class));
        });
    }

}

