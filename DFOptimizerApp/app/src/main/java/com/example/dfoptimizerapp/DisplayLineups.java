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
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;



public class DisplayLineups extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_lineups);
        final int site = getIntent().getIntExtra("siteChoice",1);
        final int sport = getIntent().getIntExtra("sportChoice", 1);
        final Spinner numberOfLineups = findViewById(R.id.numberOfLineups);
        final TextView lineupTxtView = (TextView) findViewById(R.id.textView3);
        final Button saveButton = (Button) findViewById(R.id.save);

        final String [] pos = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, pos) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                ((TextView) v).setTextColor(Color.parseColor("#ffffff"));
                //((TextView) v).setTypeface(vrFont);
                return v;
            }
        };
        numberOfLineups.setAdapter(dropdownAdapter);
        numberOfLineups.setBackgroundTintList(getColorStateList(R.color.white));
        numberOfLineups.setForegroundTintList(getColorStateList(R.color.white));
        final String url = formatURL(site, sport);
        GetRequests getRequests = new GetRequests(getApplicationContext());

        saveButton.setClickable(false);
        //Connect button
        Button httpButton = (Button) findViewById(R.id.httpBut);

        httpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveButton.setClickable(true);
                try {
                    int num = Integer.parseInt(numberOfLineups.getSelectedItem().toString());
                    if(num > 0) {
                        getRequests.SendRequestAndPrintResponse(url, num, lineupTxtView);
                    }
                    else
                    {
                        Toast.makeText(DisplayLineups.this, "You must enter a number of lineups greater than 0", Toast.LENGTH_SHORT).show();
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
             Intent saveLineup = new Intent(view.getContext(), SavedLineups.class);
             saveLineup.putExtra("generatedLineup", lineupTxtView.getText().toString());
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







