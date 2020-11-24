package com.example.dfoptimizerapp;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;


public class ListPlayers_NBA<sport> extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players);
//GUI components
        final Button continueBtn = (Button) findViewById(R.id.continueBtn);
        final Button clearBtn = (Button) findViewById(R.id.clearPlayers);
        final Spinner positionFilter = (Spinner) findViewById(R.id.positionFilter);
        final int site = getIntent().getIntExtra("siteChoice",1);
        final int sport = getIntent().getIntExtra("sportChoice", 1);
        final ArrayList<String> players = getIntent().getStringArrayListExtra("playerList");
        ArrayList<String> positions = getIntent().getStringArrayListExtra("positionList");
        ArrayList<String> teams = getIntent().getStringArrayListExtra("teamsList");
        ArrayList<String> salaries = getIntent().getStringArrayListExtra("salariesList");
        ArrayList<String> opponents = getIntent().getStringArrayListExtra("opponentsList");
        ArrayList<String> projections = getIntent().getStringArrayListExtra("projectionList");
        final TableLayout playerListView = (TableLayout) findViewById(R.id.playerListView);
        TextView remainingSalaryTxt = (TextView) findViewById(R.id.remainingSalaryTxt);
        //"Rem. Salary: $"
        String remainingSalaryString = remainingSalaryTxt.getText().toString().substring(0, 14);
        ArrayList<String> selectedPlayers = new ArrayList<String>();
        int maxChecked = 4;

        //60000
        int []remainingSalary = new int[]{Integer.parseInt(remainingSalaryTxt.getText().toString().substring(14))};
        final String[] filterText = {"All Positions"};

        //List of positions that can be used as a filter
        String[] pos = new String[]{"All Positions", "PG", "SF", "SG", "PF", "C"};
        //adapter to fill the dropdown menu
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pos);
        positionFilter.setAdapter(dropdownAdapter);

        /****INITIALIZE THE TABLE*****/
        //header row
        TableRow header = new TableRow(this);
        TableRow.LayoutParams p = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(p);
        TextView playerCol = new TextView(this);
        playerCol.setText("Player:");
        TextView positionCol = new TextView(this);
        positionCol.setText("Pos.:");
        TextView teamCol = new TextView(this);
        teamCol.setText("Team:");
        TextView salaryCol = new TextView(this);
        salaryCol.setText("Salary:");
        TextView opponentCol = new TextView(this);
        opponentCol.setText("Opp.:");
        TextView projCol = new TextView(this);
        projCol.setText("Proj.");
        header.addView(playerCol);
        header.addView(positionCol);
        header.addView(teamCol);
        header.addView(salaryCol);
        header.addView(opponentCol);
        header.addView(projCol);
        playerListView.addView(header);

        //all the other rows
        for(int i = 0; i < players.size(); i++)
        {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(layoutParams);
            CheckBox chk = new CheckBox(this);
            playerCol=new TextView(this);
            playerCol.setText(players.get(i));
            positionCol=new TextView(this);
            positionCol.setText(positions.get(i));
            teamCol=new TextView(this);
            teamCol.setText(teams.get(i));
            salaryCol=new TextView(this);
            salaryCol.setText(salaries.get(i));
            opponentCol=new TextView(this);
            opponentCol.setText(opponents.get(i));
            projCol=new TextView(this);
            projCol.setText(projections.get(i));
            row.addView(playerCol);
            row.addView(positionCol);
            row.addView(teamCol);
            row.addView(salaryCol);
            row.addView(opponentCol);
            row.addView(projCol);
            row.setClickable(true);
            row.setId(i);
            row.addView(chk);
            if (i % 2 != 0) {
                row.setBackgroundColor(Color.LTGRAY);
            } else {
                row.setBackgroundColor(Color.WHITE);
            }

            /***CHECKED CHANGED LISTENER FOR EACH CHECKBOX***/
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton v, boolean b) {
                    boolean isChecked= b;
                    //get the player name (column 0)
                    String player = ((TextView)row.getChildAt(0)).getText().toString();

                    //get the salary (column 1)
                    String selectedSalary = ((TextView)row.getChildAt(3)).getText().toString().substring(1);
                    int salary = Integer.parseInt(selectedSalary);

                    //if the size of the selected players is >= 4, and the user is trying to check a 5th checkbox,
                    // set it to false and notify them
                    if (selectedPlayers.size() >= maxChecked && isChecked) {

                        v.setChecked(false);
                        Toast.makeText(getApplicationContext(), "You cannot select more that 4 players", Toast.LENGTH_SHORT).show();

                    }
                    //else if they are trying to check a player whose salary is greater than their remaining salary cap,
                    //set the checkbox to false and notify them
                    else if ((remainingSalary[0] - salary) < 0 && isChecked) {
                        v.setChecked(false);
                        Toast.makeText(getApplicationContext(), "This player's salary is more than your remaining salary", Toast.LENGTH_SHORT).show();

                    }
                    //else this is a legal check/uncheck -- add/remove the player and increment/decrement
                    else {
                        if (isChecked) {
                            selectedPlayers.add(player);

                            remainingSalary[0] -= salary;
                        } else {
                            remainingSalary[0] += salary;
                            selectedPlayers.remove(player);
                        }
                        remainingSalaryTxt.setText((remainingSalaryString).concat(Integer.toString(remainingSalary[0])));
                    }

                }
            });
            playerListView.addView(row);
        }


        /***CONTINUE BUTTON ON CLICK LISTENER***/
        continueBtn.setOnClickListener((v) -> {
            //if site == 1, that means that FanDuel was chosen in the beginning, else DraftKings was chosen

            Intent httpConnect = new Intent(v.getContext(), DisplayLineups.class);
            httpConnect.putExtra("selectedPlayers", (Serializable) selectedPlayers);
            httpConnect.putExtra("siteChoice", site);
            httpConnect.putExtra("sportChoice", sport);
            startActivity(httpConnect);
        });

        /***CLEAR BUTTON ON CLICK LISTENER***/
        clearBtn.setOnClickListener((v) -> {
            //A bit laggy, why?
            selectedPlayers.clear();
            //Toast.makeText(getApplicationContext(), "All selections cleared", Toast.LENGTH_SHORT).show();



        });
        //GUI components
        /*final Button continueBtn = (Button) findViewById(R.id.continueBtn);
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
        String[] pos = new String[]{"All Positions", "PG", "SF", "SG", "PF", "C"};

        //adapter to fill the dropdown menu
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pos);
        positionFilter.setAdapter(dropdownAdapter);


        //populate list view
        final ArrayAdapter<String>[] playerListAdapter = new ArrayAdapter[]{null};
        playerListView.setSaveEnabled(true);
        playerListView.setMinimumWidth(50);
        playerListAdapter[0] = new ArrayAdapter<String>(this, simple_list_item_multiple_choice, (String[]) players.toArray(new String[0]));;
        playerListView.setAdapter(playerListAdapter[0]);


        //onItemSelectedListener for the position filter
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


        //when player is selected from list:
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

            Intent httpConnect = new Intent(v.getContext(), DisplayLineups.class);
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


*/
    }

}

