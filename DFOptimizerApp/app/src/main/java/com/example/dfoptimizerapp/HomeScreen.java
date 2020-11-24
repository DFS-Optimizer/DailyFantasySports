package com.example.dfoptimizerapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        final Button customizeBtn = findViewById(R.id.customizeBtn);

        customizeBtn.setOnClickListener((v) ->{
            startActivity(new Intent(getApplicationContext(), ChooseSite.class));
        });
    }
}
