package com.example.dfoptimizerapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class ListPlayers extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint({"ResourceAsColor", "ResourceType"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_players);

        //GUI components
        final Button continueBtn = (Button) findViewById(R.id.continueBtn);
        final Button clearBtn = (Button) findViewById(R.id.clearPlayers);
        final Spinner positionFilter = (Spinner) findViewById(R.id.positionFilter);
        final int site = getIntent().getIntExtra("siteChoice", 1);
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


        final String[] pos;
        //List of positions that can be used as a filter and determining the salary cap
        if (sport == 1) {
            pos = new String[]{"All Positions", "PG", "SF", "SG", "PF", "C"};
            if(site == 1)
            {
                remainingSalaryTxt.setText("Rem. Salary: $60000");
            }
            else
            {
                remainingSalaryTxt.setText("Rem. Salary: $55000");
            }
        } else if (sport == 2) {
            pos = new String[]{"All Positions", "QB", "WR", "RB", "TE", "D"};
            if(site == 1)
            {
                remainingSalaryTxt.setText("Rem. Salary: $60000");
            }
            else
            {
                remainingSalaryTxt.setText("Rem. Salary: $50000");
            }
        } else {
            pos = new String[]{"All Positions", "P", "C/1B", "2B", "3B", "SS", "OF", "UTIL"};
            if(site == 1)
            {
                   remainingSalaryTxt.setText("Rem. Salary: $35000");
            }
            else
            {
                remainingSalaryTxt.setText("Rem. Salary: $50000");
            }
        }
        int[] remainingSalary = new int[]{Integer.parseInt(remainingSalaryTxt.getText().toString().substring(14))};

        //adapter to fill the dropdown menu
        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pos) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                ((TextView) v).setTextColor(Color.parseColor("#ffffff"));
                //((TextView) v).setTypeface(vrFont);
                return v;
            }
        };
        positionFilter.setAdapter(dropdownAdapter);
        positionFilter.setBackgroundTintList(getColorStateList(R.color.white));
        positionFilter.setForegroundTintList(getColorStateList(R.color.white));
        //positionFilter.setBackgroundColor(Color.WHITE);





        /****INITIALIZE THE TABLE*****/
        //header row
        TableRow header = new TableRow(this);
        TableRow.LayoutParams p = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        header.setLayoutParams(p);
        TextView playerCol = new TextView(this);
        playerCol.setText("Player:");
        playerCol.setTextColor(Color.parseColor("#ffffff"));
        TextView positionCol = new TextView(this);
        positionCol.setTextColor(Color.parseColor("#ffffff"));
        positionCol.setText("Pos.:");
        TextView teamCol = new TextView(this);
        teamCol.setTextColor(Color.parseColor("#ffffff"));
        teamCol.setText("Team:");
        TextView salaryCol = new TextView(this);
        salaryCol.setTextColor(Color.parseColor("#ffffff"));
        salaryCol.setText("Salary:");
        TextView opponentCol = new TextView(this);
        opponentCol.setTextColor(Color.parseColor("#ffffff"));
        opponentCol.setText("Opp.:");
        TextView projCol = new TextView(this);
        projCol.setTextColor(Color.parseColor("#ffffff"));
        projCol.setText("Proj.");
        header.addView(playerCol);
        header.addView(positionCol);
        header.addView(teamCol);
        header.addView(salaryCol);
        header.addView(opponentCol);
        header.addView(projCol);
        playerListView.addView(header);
        //all the other rows
        for (int i = 0; i < players.size(); i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(layoutParams);
            CheckBox chk = new CheckBox(this);
            chk.setButtonTintList(getColorStateList(R.color.white));
            playerCol = new TextView(this);
            playerCol.setTextColor(Color.parseColor("#ffffff"));
            playerCol.setText(players.get(i));
            positionCol = new TextView(this);
            positionCol.setTextColor(Color.parseColor("#ffffff"));
            positionCol.setText(positions.get(i));
            teamCol = new TextView(this);
            teamCol.setTextColor(Color.parseColor("#ffffff"));
            teamCol.setText(teams.get(i));
            salaryCol = new TextView(this);
            salaryCol.setTextColor(Color.parseColor("#ffffff"));
            salaryCol.setText(salaries.get(i));
            opponentCol = new TextView(this);
            opponentCol.setTextColor(Color.parseColor("#ffffff"));
            opponentCol.setText(opponents.get(i));
            projCol = new TextView(this);
            projCol.setTextColor(Color.parseColor("#ffffff"));
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
                row.setBackgroundColor(Color.parseColor("#404040"));
            } else {
                row.setBackgroundColor(Color.parseColor("#333333"));
            }

            /***CHECKED CHANGED LISTENER FOR EACH CHECKBOX***/
            chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton v, boolean b) {
                    boolean isChecked = b;
                    //get the player name (column 0)
                    String player = ((TextView) row.getChildAt(0)).getText().toString();

                    //get the salary (column 1)
                    String selectedSalary = ((TextView) row.getChildAt(3)).getText().toString().substring(1);
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
            selectedPlayers.clear();
            for(int i=1; i<players.size(); i++){
                TableRow row = (TableRow)playerListView.getChildAt(i);
                //System.out.println(i+" "+(CheckBox)row.getChildAt(6));
                CheckBox chk = (CheckBox)row.getChildAt(6);
                chk.setChecked(false);
            }
            //Toast.makeText(getApplicationContext(), "All selections cleared", Toast.LENGTH_SHORT).show();


        });
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                TableRow row;
                for (int i = 1; i <= players.size(); i++) {
                    row = (TableRow) playerListView.getChildAt(i);
                    if (!((TextView) row.getChildAt(0)).getText().toString().toLowerCase().contains(query.toLowerCase())) {
                        row.setVisibility(View.GONE);
                    } else {
                        row.setVisibility(View.VISIBLE);
                    }

                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TableRow row;
                for (int i = 1; i <= players.size(); i++) {
                    row = (TableRow) playerListView.getChildAt(i);
                    if (!((TextView) row.getChildAt(0)).getText().toString().toLowerCase().contains(newText.toLowerCase())) {
                        row.setVisibility(View.GONE);
                    } else {
                        row.setVisibility(View.VISIBLE);
                    }

                }

                return false;


            }
        });

        /***FILTER DROPDOWN***/
        final String[] filterText = {"All Positions"};
        positionFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterText[0] = (String) positionFilter.getItemAtPosition(i);
                TableRow row;
                for (int j = 1; j <= players.size(); j++) {
                    row = (TableRow) playerListView.getChildAt(j);
                    row.setVisibility(View.VISIBLE);
                    if (filterText[0] != "All Positions") {
                        if (!((TextView) row.getChildAt(1)).getText().toString().toLowerCase().contains(filterText[0].toLowerCase())) {
                            row.setVisibility(View.GONE);
                        }
                    } else {
                        row.setVisibility(View.VISIBLE);
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                for (int i = 0; i < players.size(); i++) {
                    TableRow row = (TableRow) playerListView.getChildAt(i);
                    row.setVisibility(View.VISIBLE);
                }
            }

        });


    }
}