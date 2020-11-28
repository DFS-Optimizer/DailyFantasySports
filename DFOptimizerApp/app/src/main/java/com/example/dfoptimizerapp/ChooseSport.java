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

//        final Switch nbaSwitch = (Switch) findViewById(R.id.nbaSwitch);
//        final Switch nflSwitch = (Switch) findViewById(R.id.nflSwitch);
//        final Switch mlbSwitch = (Switch) findViewById(R.id.mlbSwitch);
        //final Button continueBtn = (Button) findViewById(R.id.continueBtn);
        final Button nbaButton = (Button) findViewById(R.id.nbaButton);
        final Button nflButton = (Button) findViewById(R.id.nflButton);
        final Button mlbButton = (Button) findViewById(R.id.mlbButton);
        final int site = getIntent().getIntExtra("siteChoice",1);


        nflButton.setOnClickListener((v) -> {
            new GetSlate(v, site, 2, this);
        });

        nbaButton.setOnClickListener((v) -> {
            new GetSlate(v, site, 1, this);
        });

        mlbButton.setOnClickListener((v) -> {
            new GetSlate(v, site, 3, this);
        });
//        nbaSwitch.setOnCheckedChangeListener((v, b) ->{
//            nflSwitch.setChecked(false);
//            mlbSwitch.setChecked(false);
//            nbaSwitch.setChecked(b);
//        });
//        nflSwitch.setOnCheckedChangeListener((v, b) ->{
//            nbaSwitch.setChecked(false);
//            mlbSwitch.setChecked(false);
//            nflSwitch.setChecked(b);
//        });
//        mlbSwitch.setOnCheckedChangeListener((v,b) ->{
//            nbaSwitch.setChecked(false);
//            nflSwitch.setChecked(false);
//            mlbSwitch.setChecked(b);
//        });
//        continueBtn.setOnClickListener((v) -> {
//            if(nbaSwitch.isChecked()) {
//                new GetSlate(v,site,1,this);
//            }
//            else if(nflSwitch.isChecked()){
//                new GetSlate(v,site,2, this);
//            }
//            else if(mlbSwitch.isChecked())
//            {
//                new GetSlate(v,site,3,this);
//            }
//
//        });
    }


}

