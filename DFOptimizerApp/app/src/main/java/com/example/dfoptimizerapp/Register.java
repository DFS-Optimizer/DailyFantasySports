package com.example.dfoptimizerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

   final EditText mFullName = findViewById(R.id.fullName_register);
   final EditText mEmail = findViewById(R.id.email_register);
   final EditText mPassword = findViewById(R.id.password_register);
   final Button mRegisterBtn = findViewById(R.id.registerBtn);
   final TextView mLoginBtn = findViewById(R.id.existingUser);

   final FirebaseAuth fAuth = FirebaseAuth.getInstance();
  //final ProgressBar progressBar = findViewById(R.id.progressBar);

   if(fAuth.getCurrentUser() != null){
       startActivity(new Intent(getApplicationContext(), ChooseSite.class));
       finish();
   }
    mRegisterBtn.setOnClickListener(v -> {
        String email = mEmail.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            mEmail.setError("Email is required.");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            mPassword.setError("Password is Required.");
            return;
        }
        if (password.length() < 6) {
            mPassword.setError("Password must be greater than or equal to 6 characters");
            return;
        }
        //progressBar.setVisibility(View.VISIBLE);


        //register the user to the firebase
        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), ChooseSite.class));

            } else {
                Toast.makeText(Register.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                //progressBar.setVisibility(View.GONE);
            }
        });
    });

            mLoginBtn.setOnClickListener(v -> {
                startActivity(new Intent(getApplicationContext(), Login.class));
            });
    }
   }

