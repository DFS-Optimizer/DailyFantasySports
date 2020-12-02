package com.example.dfoptimizerapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomeScreen extends AppCompatActivity {

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
        String url_DK = "http://ec2-3-15-46-189.us-east-2.compute.amazonaws.com/dk/";
        String url_FD = "http://ec2-3-15-46-189.us-east-2.compute.amazonaws.com/fd/";


        GetRequests getRequests = new GetRequests(getApplicationContext());

        nbaDKLineup = getRequests.SendRequestAndPrintResponse(url_DK + "nba/", 1, nbaDK);
        nflDKLineup = getRequests.SendRequestAndPrintResponse(url_DK + "nfl/", 1, nflDK);
        mlbDKLineup = getRequests.SendRequestAndPrintResponse(url_DK + "mlb/", 1,mlbDK);

        nbaFDLineup = getRequests.SendRequestAndPrintResponse(url_DK + "nba/", 1, nbaFD);
        nflFDLineup = getRequests.SendRequestAndPrintResponse(url_DK + "nfl/", 1, nflFD);
        mlbFDLineup = getRequests.SendRequestAndPrintResponse(url_DK + "mlb/", 1,mlbFD);



        customizeBtn.setOnClickListener((v) ->{
            startActivity(new Intent(getApplicationContext(), ChooseSite.class));
        });
    }

}

