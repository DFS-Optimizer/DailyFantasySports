package com.example.dfoptimizerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ChooseSite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_site);

        final Button fanDuelBtn = (Button) findViewById(R.id.fanDuelBtn);
        final Button draftKingsBtn = (Button) findViewById(R.id.draftKingsBtn);
        final Button logoutBtn = (Button) findViewById(R.id.logout);

        fanDuelBtn.setOnClickListener((v) -> {
            Intent chooseSport = new Intent(v.getContext(), ChooseSport.class);
            chooseSport.putExtra("siteChoice", 1); //value 1 to indicate fanduel site choice
            startActivity(chooseSport);
        });

        draftKingsBtn.setOnClickListener((v) -> {
            Intent chooseSport = new Intent(v.getContext(), ChooseSport.class);
            chooseSport.putExtra("siteChoice", 2); //value 2 to indicate draftkings site choice
            startActivity(chooseSport);
        });

      logoutBtn.setOnClickListener((v)-> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        });

    }
}