package com.example.dfoptimizerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListPlayers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players);
        final Button continueBtn = (Button) findViewById(R.id.continueBtn);
        final Spinner positionFilter = (Spinner) findViewById(R.id.positionFilter);
        final int site = getIntent().getIntExtra("siteChoice",1);
        String[] positions = new String[] {"All Positions","PG", "SF", "SG", "PF", "C"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, positions);
        positionFilter.setAdapter(adapter);


        continueBtn.setOnClickListener((v) -> {
            //if site == 1, that means that FanDuel was chosen in the beginning, else DraftKings was chosen
            if (site == 1) {
                Intent fd_selectNBALineup = new Intent(v.getContext(), FD_SelectLineup_NBA.class);
                startActivity(fd_selectNBALineup);
            }
            else{
                Intent dk_selectNBALineup = new Intent (v.getContext(), DK_SelectLineup_NBA.class);
                startActivity(dk_selectNBALineup);
            }
        });

    }

}
