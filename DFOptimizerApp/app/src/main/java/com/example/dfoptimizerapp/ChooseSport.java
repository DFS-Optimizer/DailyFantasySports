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
        final Button continueBtn = (Button) findViewById(R.id.continueBtn);

        continueBtn.setOnClickListener((v) -> {
            if(nbaSwitch.isChecked()) {
                Intent selectLineup = new Intent(v.getContext(), SelectLineup.class);
                startActivity(selectLineup);
            }
        });
    }

}