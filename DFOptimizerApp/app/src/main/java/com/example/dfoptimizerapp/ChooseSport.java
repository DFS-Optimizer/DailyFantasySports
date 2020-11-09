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
                //new GetSlate(v,site,1);
                Intent listPlayers = new Intent(v.getContext(), ListPlayers.class);
                listPlayers.putExtra("siteChoice", site);
                listPlayers.putExtra("sportChoice", 1);
                startActivity(listPlayers);
            }
            else if(nflSwitch.isChecked()){
                new GetSlate(v,site,2, this);
            }

        });
    }


}

