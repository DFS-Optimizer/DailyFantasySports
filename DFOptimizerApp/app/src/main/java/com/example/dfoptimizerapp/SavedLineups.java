package com.example.dfoptimizerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavedLineups extends AppCompatActivity {


    /*Variable Declaration*/
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    Spinner savedSport;
    public final static String TAG = "TAG";
    Button btnSubmit;
    String siteField;
    String sportField;

    /**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_lineups);

        savedSport = (Spinner) findViewById(R.id.spinnerSavedLineups);
        btnSubmit = (Button) findViewById(R.id.btnToDisplay);
        /*Passed in Parameters*/
        final int site = getIntent().getIntExtra("siteChoice", 1);
        final int sport = getIntent().getIntExtra("sportChoice", 1);
        if (site == 1) {
            siteField = "Fanduel";
        }
        else {
            siteField = "Draftkings";
        }


        if (sport == 1) {
            sportField = "NBA";
        } else if (sport == 2) {
            sportField = "NFL";
        } else {
            sportField = "MLB";
        }

        /**/
        System.out.println(site + sport);
        saveLineup(site, sport);
        addItemsOnSpinner();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    final TextView savedDisplay = (TextView) findViewById(R.id.displaySavedLineups);
                    final TextView displaySavedSport = (TextView) findViewById(R.id.displaySavedSport);



                        displaySavedSport.setText(" ");
                        displaySavedSport.setText(savedSport.getSelectedItem().toString());
                        String userID = fAuth.getCurrentUser().getUid();

                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        documentReference.addSnapshotListener(SavedLineups.this, new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                                savedDisplay.setText(documentSnapshot.getString(savedSport.getSelectedItem().toString()+ " "+ "lineup"));
                                System.out.println("TEST" + savedDisplay.getText().toString());
                            }
                        });


                } catch (NumberFormatException error) {
                    Toast.makeText(SavedLineups.this, "Error grabbing data from Firestore", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button goHome = (Button) findViewById(R.id.goToHome);
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(v.getContext(), HomeScreen.class);
                startActivity(home);
            }
        });


    }


    public void saveLineup(int site, int sport) {

System.out.println("SITE FIELD: " + siteField);
        fAuth = FirebaseAuth.getInstance();


        String generatedLineup = getIntent().getStringExtra("generatedLineup");

        Toast.makeText(SavedLineups.this, "Saving Lineup", Toast.LENGTH_SHORT).show();
        //Test
        System.out.println(generatedLineup);

        String userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        Map<String, Object> user = new HashMap<>();

        user.put(sportField + " " + siteField + " " + "lineup", generatedLineup);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: User Profile created " + userID);
            }


        });
    }

    public void addItemsOnSpinner() {
        List<String> list = new ArrayList<String>();
        list.add("NBA Fanduel");
        list.add("NFL Fanduel");
        list.add("MLB Fanduel");
        list.add("NBA Draftkings");
        list.add("NFL Draftkings");
        list.add("MLB Draftkings");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, list) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
                ((TextView) v).setTextColor(Color.parseColor("#ffffff"));
                //((TextView) v).setTypeface(vrFont);
                return v;
            }
        };
        savedSport.setAdapter(dataAdapter);
    }




}

