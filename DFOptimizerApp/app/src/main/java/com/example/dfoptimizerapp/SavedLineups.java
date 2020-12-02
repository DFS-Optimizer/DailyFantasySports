package com.example.dfoptimizerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SavedLineups extends AppCompatActivity {
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    Spinner savedSport;
    Button btnSubmit;
    public final static String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_lineups);
        saveLineup();
        addItemsOnSpinner();
        addListenerOnButton();



    }

    public void saveLineup() {
        fAuth = FirebaseAuth.getInstance();


        String generatedLineup = getIntent().getStringExtra("generatedLineup");

        Toast.makeText(SavedLineups.this, "Saving Lineup", Toast.LENGTH_SHORT).show();
        //Test
        System.out.println(generatedLineup);

        String userID = fAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        Map<String, Object> user = new HashMap<>();
        user.put("NFL lineup", generatedLineup);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: User Profile created " + userID);
            }


        });
    }

    public void addItemsOnSpinner() {
        savedSport = (Spinner) findViewById(R.id.spinnerSavedLineups);
        List<String> list = new ArrayList<String>();
        list.add("NBA Fanduel");
        list.add("NFL Fanduel");
        list.add("MLB Fanduel");
        list.add("NBA Draftkings");
        list.add("NFL Draftkings");
        list.add("MLB Draftkings");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        savedSport.setAdapter(dataAdapter);
    }

    public void addListenerOnButton() {
     btnSubmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                final TextView savedDisplay = (TextView) findViewById(R.id.displaySavedLineups);
                savedSport = (Spinner) findViewById(R.id.spinnerSavedLineups);
                btnSubmit = (Button) findViewById(R.id.btnToSave);

                if (savedSport.toString() == "NFL Draftkings") {

                    System.out.println("NFL DRAFT KINGS DISPLAY"); //Test Code
                        /*String userID = fAuth.getCurrentUser().getUid();

                        DocumentReference documentReference = fStore.collection("users").document(userID);
                        documentReference.addSnapshotListener(SavedLineups.this, new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                                savedDisplay.setText(documentSnapshot.getString("NFL lineup"));

                            }
                        });
*/                    }

            } catch (NumberFormatException error) {
                Toast.makeText(SavedLineups.this, "Error grabbing data from Firestore", Toast.LENGTH_SHORT).show();
            }

        }
    });


   }


}

