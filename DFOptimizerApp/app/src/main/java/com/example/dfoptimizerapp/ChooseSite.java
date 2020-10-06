package com.example.dfoptimizerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class ChooseSite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_site);

        final Button fanDuelBtn = (Button) findViewById(R.id.fanDuelBtn);
        final Button draftKingsBtn = (Button) findViewById(R.id.draftKingsBtn);

        fanDuelBtn.setOnClickListener((v) -> {
            Intent listPlayers = new Intent(v.getContext(), ListPlayers.class);
            listPlayers.putExtra("siteChoice", 1); //value 1 to indicate fanduel site choice
            startActivity(listPlayers);
        });

        draftKingsBtn.setOnClickListener((v) -> {
            Intent listPlayers = new Intent(v.getContext(), ListPlayers.class);
            listPlayers.putExtra("siteChoice", 2); //value 2 to indicate draftkings site choice
            startActivity(listPlayers);
        });




    }
}