package com.example.dfoptimizerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
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
        final TextView remainingSalary = (TextView) findViewById(R.id.remainingSalaryTxt);


        //List of positions that can be used as a filter
        String[] pos = new String[] {"All Positions","PG", "SF", "SG", "PF", "C"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pos);
        positionFilter.setAdapter(adapter);

        //Get list of players from the slate
        InputStream slate = null;
        try {
            slate = getApplicationContext().getAssets().open("slate.csv");

            BufferedReader slateReader = null;
            if (slate != null) {
                slateReader = new BufferedReader(new InputStreamReader(slate, "UTF8"));
            }
            if(slateReader != null)
            {
                String line = "";
                line = slateReader.readLine();
                List<String> names = new ArrayList<String>();
                List<String> salaries = new ArrayList<String>();
                List<String> positions = new ArrayList<String>();
                List<String> teams = new ArrayList<String>();
                List<String> opponents = new ArrayList<String>();
                List<String> projections = new ArrayList<String>();
                while(line != null)
                {
                    String[] array = line.split(",");
                    names.add(array[0]);
                    salaries.add(array[1]);
                    positions.add(array[2]);
                    teams.add(array[3]);
                    opponents.add(array[4]);
                    projections.add(array[5]);
                }



            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


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
