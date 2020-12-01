package com.example.dfoptimizerapp;

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
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.FirebaseFirestore;

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
    public void addItemsOnSpinner(){
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

        savedSport = (Spinner) findViewById(R.id.spinnerSavedLineups);
        btnSubmit = (Button) findViewById(R.id.btnToSave);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


            }
        });
    }


}

