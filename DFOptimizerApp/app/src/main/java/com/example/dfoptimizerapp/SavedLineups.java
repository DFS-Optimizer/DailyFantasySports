package com.example.dfoptimizerapp;

        import androidx.annotation.RequiresApi;
        import androidx.appcompat.app.AppCompatActivity;

        import android.graphics.Color;
        import android.os.Build;
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
        import com.google.firebase.firestore.FirebaseFirestore;

        import org.w3c.dom.Text;

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
    @RequiresApi(api = Build.VERSION_CODES.M)
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
    @RequiresApi(api = Build.VERSION_CODES.M)
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
                android.R.layout.simple_spinner_item, list){
          public View getView(int position, View convertView, ViewGroup parent) {
              View v = super.getView(position, convertView, parent);
              ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
              ((TextView) v).setTextColor(Color.parseColor("#ffffff"));
              return v;
          }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        savedSport.setAdapter(dataAdapter);
        savedSport.setForegroundTintList(getColorStateList(R.color.white));
        savedSport.setBackgroundTintList(getColorStateList(R.color.white));
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

