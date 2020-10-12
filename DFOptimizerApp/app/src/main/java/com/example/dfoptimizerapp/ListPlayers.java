package com.example.dfoptimizerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
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
        final ListView playerListView = (ListView) findViewById(R.id.playerListView);
        TextView remainingSalaryTxt = (TextView) findViewById(R.id.remainingSalaryTxt);
        //"Rem. Salary: $"
        String remainingSalaryString = remainingSalaryTxt.getText().toString().substring(0, 14);

        //60000
        final int[] remainingSalary = {Integer.parseInt(remainingSalaryTxt.getText().toString().substring(14))};




        //List of positions that can be used as a filter
        String[] pos = new String[] {"All Positions","PG", "SF", "SG", "PF", "C"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pos);
        positionFilter.setAdapter(adapter1);

        //Get list of players from the slate
        InputStream slate = null;
        try {
            slate = getResources().openRawResource(getResources().getIdentifier("slate", "raw", getPackageName()));

            BufferedReader slateReader = null;
            if (slate != null) {
                slateReader = new BufferedReader(new InputStreamReader(slate, "UTF8"));
            }
            if(slateReader != null) {
                String line = "";
                line = slateReader.readLine();
                line = slateReader.readLine();
                List<String> players = new ArrayList<String>();
                /*List<String> names = new ArrayList<String>();
                List<String> salaries = new ArrayList<String>();
                List<String> positions = new ArrayList<String>();
                List<String> teams = new ArrayList<String>();
                List<String> opponents = new ArrayList<String>();
                List<String> projections = new ArrayList<String>();*/
                while (line != null) {
                    //String[] playerInfo = line.split(",");
                    /*names.add(playerInfo[0]);
                    salaries.add(playerInfo[1]);
                    positions.add(playerInfo[2]);
                    teams.add(playerInfo[3]);
                    opponents.add(playerInfo[4]);
                    projections.add(playerInfo[5]);*/
                    players.add(line);
                    line = slateReader.readLine();

                }
                ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, (String[]) players.toArray(new String[0]));
                playerListView.setAdapter(adapter2);


            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        List<String> selectedPlayers = new ArrayList<String>();
        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CheckedTextView v = (CheckedTextView) view;
                boolean isChecked = v.isChecked();
                String player = (String) playerListView.getItemAtPosition(i);
                String[] splitInfo = player.split(",");
                String name = splitInfo[0];
                int salary = Integer.parseInt(splitInfo[1]);
                if (selectedPlayers.size() > 4)
                {
                    v.setChecked(false);
                    Toast.makeText(getApplicationContext(), "You cannot select more that 4 players", Toast.LENGTH_SHORT).show();
                } else if ((remainingSalary[0] - salary) < 0)
                {
                    v.setChecked(false);
                    Toast.makeText(getApplicationContext(), "This player's salary is more than your remaining salary", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    if (isChecked) {
                        selectedPlayers.add(name);
                        remainingSalary[0] -= salary;
                    } else {
                        remainingSalary[0] += salary;
                        selectedPlayers.remove(name);
                    }
                    remainingSalaryTxt.setText((CharSequence) (remainingSalaryString).concat(Integer.toString(remainingSalary[0])));
                }
            }
        });
        continueBtn.setOnClickListener((v) -> {
            //if site == 1, that means that FanDuel was chosen in the beginning, else DraftKings was chosen
            if (site == 1) {

                Intent httpConnect = new Intent(v.getContext(), HttpConnect.class);
                httpConnect.putExtra("selectedPlayers", (Serializable) selectedPlayers);
                startActivity(httpConnect);

            }
            else{
                //Intent dk_selectNBALineup = new Intent (v.getContext(), DK_SelectLineup_NBA.class);
                //startActivity(dk_selectNBALineup);
            }
        });

    }

}
