package com.example.dfoptimizerapp;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class DisplayLineups extends AppCompatActivity {
    static ArrayList<ArrayList<String>> lineups = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_lineups);
        final int site = getIntent().getIntExtra("siteChoice",1);
        final int sport = getIntent().getIntExtra("sportChoice", 1);
        final Spinner numberOfLineups = findViewById(R.id.numberOfLineups);
        final Button httpButton = (Button) findViewById(R.id.httpBut);
        final Button saveButton = (Button) findViewById(R.id.save);
        final Spinner lineup1 = findViewById(R.id.lineup1);
        final Spinner lineup2 = findViewById(R.id.lineup2);
        final Spinner lineup3 = findViewById(R.id.lineup3);
        final Spinner lineup4 = findViewById(R.id.lineup4);
        final Spinner lineup5 = findViewById(R.id.lineup5);
        final Spinner lineup6 = findViewById(R.id.lineup6);
        final Spinner lineup7 = findViewById(R.id.lineup7);
        final Spinner lineup8 = findViewById(R.id.lineup8);
        final Spinner lineup9 = findViewById(R.id.lineup9);
        final Spinner lineup10 = findViewById(R.id.lineup10);


        saveButton.setClickable(false);

        Spinner[] spinners = new Spinner[]{lineup1,lineup2,lineup3, lineup4,lineup5,lineup6,lineup7,lineup8, lineup9, lineup10};

        final String [] pos = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pos) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                ((TextView) v).setTextColor(Color.parseColor("#ffffff"));
                return v;
            }
        };
        numberOfLineups.setAdapter(dropdownAdapter);
        numberOfLineups.setBackgroundTintList(getColorStateList(R.color.white));
        numberOfLineups.setForegroundTintList(getColorStateList(R.color.white));

        final String url = formatURL(site, sport);
        GetRequests getRequests = new GetRequests(getApplicationContext());



        httpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setClickable(true);
                try {
                    int num = Integer.parseInt(numberOfLineups.getSelectedItem().toString());

                        lineups = getRequests.SendRequestAndPrintResponse(url, num, spinners);

                        for(int i = 0; i < 10; i++)
                        {

                            if(i < num) {
                                spinners[i].setVisibility(View.VISIBLE);
                            }
                            else
                            {
                                spinners[i].setVisibility(View.GONE);
                            }
                        }

                }
                catch (NumberFormatException e){
                    Toast.makeText(DisplayLineups.this, "You must enter a number of lineups greater than 0", Toast.LENGTH_SHORT).show();
                }
            }
        });
/**SAVE BUTTON**/
     saveButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             String lineupString = "";
             for(int i = 0; i < 10; i++) {
                 if (i < Integer.parseInt((String) numberOfLineups.getSelectedItem())) {
                     int j = 0;
                     while ( j < spinners[i].getAdapter().getCount()) {
                         lineupString += spinners[i].getItemAtPosition(j);
                         lineupString += "\n";
                         j++;
                     }
                 }
             }
             Intent saveLineup = new Intent(view.getContext(), SavedLineups.class);
             saveLineup.putExtra("generatedLineup", lineupString);
             System.out.println(site + " " + sport);
             saveLineup.putExtra("siteChoice", site);
             saveLineup.putExtra("sportChoice", sport);
             startActivity(saveLineup);
         }

     });

    }


    private String formatURL(int site, int sport) {
        String url = "http://ec2-3-15-46-189.us-east-2.compute.amazonaws.com/";

        if(site == 1)
        {
            url = url.concat("fd/");
        }
        else
        {
            url = url.concat("dk/");
        }

        if(sport == 1)
        {
            url = url.concat("nba/");
        }
        else if(sport == 2)
        {
            url=url.concat("nfl/");
        }
        else
        {
            url = url.concat("mlb/");
        }
        int i;
        int j;
        ArrayList<String> initialPlayerList = (ArrayList<String>) getIntent().getSerializableExtra("selectedPlayers");
        ArrayList<String> formattedPlayerList = new ArrayList<String>();
        String name;
        String formattedName = "";

        System.out.println("Input: " + initialPlayerList);

        for(i = 0; i < initialPlayerList.size(); i++){
            name = initialPlayerList.get(i);
            for(j = 0; j < name.length(); j++){
                //append each letter until a space
                if(name.charAt(j) == ' '){
                    //append %20
                    formattedName = formattedName + "%20";
                }
                else{
                    formattedName = formattedName + name.charAt(j);
                }
            }
            formattedPlayerList.add(formattedName);
            formattedName = "";
        }
        System.out.println(formattedPlayerList);
        int k;
        for(k = 0; k < formattedPlayerList.size(); k++){

            url = url + formattedPlayerList.get(k);
            url = url + "/";
        }

        System.out.println(url);
        return url;
    }

}







