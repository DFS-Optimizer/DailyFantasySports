package com.example.dfoptimizerapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;


public class FD_SelectLineup_NBA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fd_select_lineup_nba);

        final Button generateBtn = (Button) findViewById(R.id.generateBtn);
        final EditText namesTxtBx = (EditText) findViewById(R.id.namesTxtBx);

        generateBtn.setOnClickListener((v) -> {


        });
    }
}