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
                Intent chooseSite = new Intent(v.getContext(), ChooseSite.class);
                startActivity(chooseSite);
            }
            else if(nflSwitch.isChecked()){
                if(nbaSwitch.isChecked()) {
                    nbaSwitch.setChecked(false);
                }
                //add functionality later
                //Intent selectNFLLineup = new Intent(v.getContext(), SelectLineup_NFL.class);
                //startActivity(selectNFLLineup);
            }
        });
    }

}