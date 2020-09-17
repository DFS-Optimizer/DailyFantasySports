package com.example.dfoptimizerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button fanDuelBtn = (Button) findViewById(R.id.fanDuelBtn);
        final Button draftKingsBtn = (Button) findViewById(R.id.draftKingsBtn);

        fanDuelBtn.setOnClickListener((v) -> {
            Intent chooseSport = new Intent(v.getContext(), ChooseSport.class);
            startActivity(chooseSport);
        });

        draftKingsBtn.setOnClickListener((v) -> {
            Intent chooseSport = new Intent(v.getContext(), ChooseSport.class);
            startActivity(chooseSport);
        });




    }
}