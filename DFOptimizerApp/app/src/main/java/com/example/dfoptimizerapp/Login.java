package com.example.dfoptimizerapp;

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

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //fields
        final EditText mEmail = findViewById(R.id.email_login);
        final EditText mPassword = findViewById(R.id.password_login);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final FirebaseAuth fAuth = FirebaseAuth.getInstance();
        final Button mLoginBtn = findViewById(R.id.login);
        final TextView mCreateBtn = findViewById(R.id.newUser);

        //Labels for fields
        final TextView emailLabel = findViewById(R.id.loginEmailLabel);
        final TextView passwordLabel = findViewById(R.id.loginPasswordLabel);

        //OnKeyListeners for fields. If length > 0, the field's label is set to invisible
        mEmail.setOnKeyListener((v, keyCode, event) -> {
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            if(password.length() == 0)
            {
                passwordLabel.setVisibility(View.VISIBLE);
            }
            if (!email.isEmpty()) {
                emailLabel.setVisibility(View.INVISIBLE);
            } else {
                emailLabel.setVisibility(View.VISIBLE);
            }
            return false;
        });

        mPassword.setOnKeyListener((v, keyCode, event) -> {
            String password = mPassword.getText().toString();
            String email = mPassword.getText().toString();
            if(email.length() == 0)
            {
                emailLabel.setVisibility(View.VISIBLE);
            }
            if (!password.isEmpty()) {
                passwordLabel.setVisibility(View.INVISIBLE);
            } else {
                passwordLabel.setVisibility(View.VISIBLE);
            }
            return false;
        });

        //OnClickListener for Login Button
        mLoginBtn.setOnClickListener((v) -> {

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
            progressBar.setVisibility(View.VISIBLE);

            //authenticate the user
            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ChooseSite.class));
                } else {
                    Toast.makeText(Login.this, "Error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        });


        mCreateBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Register.class));
        });

    }
}

