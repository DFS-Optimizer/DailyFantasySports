package com.example.dfoptimizerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SavedLineups extends AppCompatActivity {
FirebaseFirestore fStore = FirebaseFirestore.getInstance();
FirebaseAuth fAuth = FirebaseAuth.getInstance();
    public final static String TAG = "TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_lineups);


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


}