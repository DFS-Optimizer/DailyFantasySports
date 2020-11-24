package com.example.dfoptimizerapp;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        final Button customizeBtn = findViewById(R.id.customizeBtn);
        final TextView nbaTextView = findViewById(R.id.nbaLineupTxtView);
        final TextView nflTextView = findViewById(R.id.nflLineupTxtView);
        final TextView mlbTextView = findViewById(R.id.mlbLineupTxtView);
        String url = "http://ec2-3-15-46-189.us-east-2.compute.amazonaws.com/dk/";

        GetRequests getRequests = new GetRequests(getApplicationContext());
        getRequests.SendRequestAndPrintResponse(url + "nba/", 1, nbaTextView);
        getRequests.SendRequestAndPrintResponse(url + "nfl/", 1, nflTextView);
        getRequests.SendRequestAndPrintResponse(url + "mlb/", 1, mlbTextView);

        customizeBtn.setOnClickListener((v) ->{
            startActivity(new Intent(getApplicationContext(), ChooseSite.class));
        });
    }
}
