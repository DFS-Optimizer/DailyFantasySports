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
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static android.R.layout.simple_list_item_multiple_choice;

public class ListPlayers extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players);
        final Button continueBtn = (Button) findViewById(R.id.continueBtn);
        final Button clearBtn = (Button) findViewById(R.id.clearPlayers);
        final Spinner positionFilter = (Spinner) findViewById(R.id.positionFilter);
        final int site = getIntent().getIntExtra("siteChoice",1);
        final int sport = getIntent().getIntExtra("sportChoice", 1);
        final ArrayList<String> players = getIntent().getStringArrayListExtra("playerList");
        final ListView playerListView = (ListView) findViewById(R.id.playerListView);
        TextView remainingSalaryTxt = (TextView) findViewById(R.id.remainingSalaryTxt);
        //"Rem. Salary: $"
        String remainingSalaryString = remainingSalaryTxt.getText().toString().substring(0, 14);

        //60000
        final int[] remainingSalary = {Integer.parseInt(remainingSalaryTxt.getText().toString().substring(14))};
        final String[] filterText = {"All Positions"};

        //List of positions that can be used as a filter
        String[] pos;
        if(sport == 1) {
            pos = new String[]{"All Positions", "PG", "SF", "SG", "PF", "C"};
        }
        else{
            pos = new String[]{"All Positions","QB","WR","RB","TE","DF","K"};
        }
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pos);
        positionFilter.setAdapter(dropdownAdapter);
        

        final ArrayAdapter<String>[] playerListAdapter = new ArrayAdapter[]{null};

        //round projection numbers

        //populate list view
        playerListView.setSaveEnabled(true);
        playerListView.setMinimumWidth(50);
        playerListAdapter[0] = new ArrayAdapter<String>(this, simple_list_item_multiple_choice, (String[]) players.toArray(new String[0]));;
        playerListView.setAdapter(playerListAdapter[0]);



        positionFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterText[0] = (String) positionFilter.getItemAtPosition(i);
                if (filterText[0] == "All Positions") {
                    playerListAdapter[0].getFilter().filter("");
                    playerListAdapter[0].notifyDataSetChanged();
                } else {
                    playerListAdapter[0].getFilter().filter(filterText[0]);
                    playerListAdapter[0].notifyDataSetChanged();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                playerListAdapter[0].getFilter().filter("");
                playerListAdapter[0].notifyDataSetChanged();
            }

        });

        /*ArrayAdapter<String>[] finalPlayerListAdapter = playerListAdapter;
        positionFilter.setOnItemSelectedListener((adapterView, view, i, l) -> {

            filterText[0] = (String) positionFilter.getItemAtPosition();
            if(filterText[0] == "All Positions")
            {
                finalPlayerListAdapter[0] = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, (String[]) players.toArray(new String[0]));
            }
            else {
                List<String> filteredPlayers = new ArrayList<String>();
                for (int j = 0; j < players.size(); j++) {
                    String temp = players.get(j);
                    temp = temp.substring(temp.indexOf("(") + 1, temp.indexOf(","));
                    if(temp.contains(filterText[0]))
                    {
                        filteredPlayers.add(temp);
                    }
                }
                finalPlayerListAdapter[0] = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, (String[]) filteredPlayers.toArray(new String[0]));
            } playerListView.setAdapter(finalPlayerListAdapter[0]);
        });*/

        final int[] id = {0};
        playerListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        playerListView.setSaveEnabled(false);
        List<String> selectedPlayers = new ArrayList<String>();
        playerListView.setOnItemClickListener((adapterView, view, i, l) -> {
            CheckedTextView v = (CheckedTextView) view;
            boolean isChecked = v.isChecked();
            String player = (String) playerListView.getItemAtPosition(i);
            int index = player.indexOf("(");
            v.setId(++id[0]);
            String name = player.substring(0, index);
            int startIndex = player.indexOf("\n$") + 2;
            int endIndex = player.indexOf(", Opponent");
            int salary = Integer.parseInt(player.substring(startIndex, endIndex));
            int maxChecked = 4; //max of 4 players can be selected at a time

            //user cannot select more than 4 players or a player whose salary is greater than the
            //remaining salary cap


            if (selectedPlayers.size() >= maxChecked && isChecked)
            {

                //playerListView.clearChoices();
                v.setChecked(false);
                Toast.makeText(getApplicationContext(), "You cannot select more that 4 players", Toast.LENGTH_SHORT).show();
//                for(int j = 0; j < players.size();j++) {
//                    if(selectedPlayers.size() >= maxChecked && isChecked){

//
//                }

            }
            else if ((remainingSalary[0] - salary) < 0)
            {
                v.setChecked(false);
                Toast.makeText(getApplicationContext(), "This player's salary is more than your remaining salary", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                if (isChecked) {
                    selectedPlayers.add(name);

                    //DEBUG CODE
                    System.out.println(selectedPlayers.size());
                    remainingSalary[0] -= salary;
                } else {
                    remainingSalary[0] += salary;
                    selectedPlayers.remove(name);
                }
                remainingSalaryTxt.setText((remainingSalaryString).concat(Integer.toString(remainingSalary[0])));
            }
        });
        continueBtn.setOnClickListener((v) -> {
            //if site == 1, that means that FanDuel was chosen in the beginning, else DraftKings was chosen

            Intent httpConnect = new Intent(v.getContext(), HttpConnect.class);
            httpConnect.putExtra("selectedPlayers", (Serializable) selectedPlayers);
            httpConnect.putExtra("siteChoice", site);
            httpConnect.putExtra("sportChoice", sport);
            startActivity(httpConnect);
        });

        clearBtn.setOnClickListener((v) -> {
            //A bit laggy, why?
            playerListView.clearChoices();
            Toast.makeText(getApplicationContext(), "All selections cleared", Toast.LENGTH_SHORT).show();



        });


//Search Bar Implementation

        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                playerListAdapter[0].getFilter().filter(query);
                playerListAdapter[0].notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                playerListAdapter[0].getFilter().filter(newText);
                playerListAdapter[0].notifyDataSetChanged();

                return true;


            }
        });



    }

}
