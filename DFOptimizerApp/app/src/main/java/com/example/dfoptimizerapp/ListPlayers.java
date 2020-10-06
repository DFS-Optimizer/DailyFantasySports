package com.example.dfoptimizerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

public class ListPlayers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players);
        final Button fanDuelBtn = (Button) findViewById(R.id.fanDuelBtn);
        final Button continueBtn = (Button) findViewById(R.id.continueBtn);
        final int site = getIntent().getIntExtra("siteChoice",1);

        continueBtn.setOnClickListener((v) -> {
            //if site == 1, that means that FanDuel was chosen in the beginning, else DraftKings was chosen
            if (site == 1) {
                Intent fd_selectNBALineup = new Intent(v.getContext(), FD_SelectLineup_NBA.class);
                startActivity(fd_selectNBALineup);
            }
            else{
                //Intent dk_selectNBALineup = new Intent (v.getContext(), DK_SelectLineup_NBA.class);
                //startActivity(dk_selectNBALineup);
            }
        });

    }

}
