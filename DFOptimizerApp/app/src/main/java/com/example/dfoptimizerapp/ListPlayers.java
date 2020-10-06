package com.example.dfoptimizerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListPlayers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players);
        final Button fanDuelBtn = (Button) findViewById(R.id.fanDuelBtn);
        final Button continueBtn = (Button) findViewById(R.id.continueBtn);
        final int site = getIntent().getIntExtra("siteChoice",1);
//        final ListView playerListView = (ListView) findViewById(R.id.playerListView);
////        String[] names = new String[]{"Lebron James", "James Harden","Kevin Durant","Steph Curry"};
////        final List<String> players = new ArrayList<>(Arrays.asList(names));
////        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.activity_list_players, players);
////        playerListView.setAdapter(adapter);

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
