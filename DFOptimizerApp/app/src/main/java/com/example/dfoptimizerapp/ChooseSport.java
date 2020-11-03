package com.example.dfoptimizerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

public class ChooseSport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sport);

        final Switch nbaSwitch = (Switch) findViewById(R.id.nbaSwitch);
        final Switch nflSwitch = (Switch) findViewById(R.id.nflSwitch);
        final Button continueBtn = (Button) findViewById(R.id.continueBtn);
        final int site = getIntent().getIntExtra("siteChoice",1);

        nbaSwitch.setOnCheckedChangeListener((v, b) ->{
            nflSwitch.setChecked(false);
            nbaSwitch.setChecked(b);
        });
        nflSwitch.setOnCheckedChangeListener((v, b) ->{
            nbaSwitch.setChecked(false);
            nflSwitch.setChecked(b);
        });
        continueBtn.setOnClickListener((v) -> {
            if(nbaSwitch.isChecked()) {
                Intent listPlayers_nba = new Intent(v.getContext(), ListPlayers.class);
                listPlayers_nba.putExtra("siteChoice", site);
                listPlayers_nba.putExtra("sportChoice", 1);
                startActivity(listPlayers_nba);
            }
            else if(nflSwitch.isChecked()){
                //add functionality later
                Intent listPlayers_nfl = new Intent(v.getContext(), ListPlayers.class);
                listPlayers_nfl.putExtra("siteChoice", site);
                listPlayers_nfl.putExtra("sportChoice",2);
                startActivity(listPlayers_nfl);
            }

        });
    }


}