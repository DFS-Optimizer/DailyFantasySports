package com.example.dfoptimizerapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import org.w3c.dom.Text;


public class SelectLineup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lineup);

        final Button generateBtn = (Button) findViewById(R.id.generateBtn);
        final EditText namesTxtBx = (EditText) findViewById(R.id.namesTxtBx);

        generateBtn.setOnClickListener((v) -> {
            if(! Python.isStarted())
            {
                Python.start(new AndroidPlatform(v.getContext()));
            }
                Python py = Python.getInstance();
                PyObject file = py.getModule("test");
                PyObject array = PyObject.fromJava(new String[] {"Charity", "Andy", "Elizabeth", "Gage"});
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