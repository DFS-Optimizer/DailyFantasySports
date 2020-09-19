package com.example.dfoptimizerapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;


public class SelectLineup_NBA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lineup_nba);

        final Button generateBtn = (Button) findViewById(R.id.generateBtn);
        final EditText namesTxtBx = (EditText) findViewById(R.id.namesTxtBx);

        generateBtn.setOnClickListener((v) -> {
            //check to see if an instance of Python has already started
            if(! Python.isStarted())
            {
                Python.start(new AndroidPlatform(v.getContext()));
            }

            Python py = Python.getInstance();
            //open the file (test.py)
            PyObject file = py.getModule("test");
            //creating a java string array and casting it to a python object so it can be passed as
            //an argument
            PyObject array = PyObject.fromJava(new String[] {"Charity", "Andy", "Elizabeth", "Gage"});

            //call the python function (testFunction) which returns a string array
            String[] names = file.callAttr("testFunction", array).toJava(String[].class);
            String nameString = "";
            for(int i = 0; i < names.length; i++)
            {
                nameString += names[i];
                if(i != names.length - 1)
                {
                    nameString += ", ";
                }
            }
            namesTxtBx.setText(nameString);
        });
    }
}