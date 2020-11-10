package com.example.dfoptimizerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText mFullName = findViewById(R.id.fullName_register);
        final EditText mEmail = findViewById(R.id.email_register);
        final EditText mPassword = findViewById(R.id.password_register);
        final EditText mConfirmPassword = findViewById(R.id.confirmPass_register);
        final Button mRegisterBtn = findViewById(R.id.registerBtn);
        final TextView mLoginBtn = findViewById(R.id.existingUser);

        //Labels for name,email,password,and confirm password fields.
        final TextView fullNameLabel = findViewById(R.id.fullNameText);
        final TextView emailLabel = findViewById(R.id.emailAddressTextView);
        final TextView passwordLabel = findViewById(R.id.passwordTextView);
        final TextView confirmPasswordLabel = findViewById(R.id.confirmPasswordTextView);

        final FirebaseAuth fAuth = FirebaseAuth.getInstance();
        //final ProgressBar progressBar = findViewById(R.id.progressBar);

        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), ChooseSite.class));
            finish();
        }

        //OnKeyListeners for fields. if a field is not empty, its label is set to invisible
        mFullName.setOnKeyListener((v, keyCode, event) -> {
            String name = mFullName.getText().toString();
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            String confirmedPassword = mConfirmPassword.getText().toString();

            //makes labels visible if they are empty
            if(email.isEmpty())
            {
                emailLabel.setVisibility(View.VISIBLE);
            }
            if(password.isEmpty())
            {
                passwordLabel.setVisibility(View.VISIBLE);
            }
            if(confirmedPassword.isEmpty())
            {
                confirmPasswordLabel.setVisibility(View.VISIBLE);
            }

            if (!name.isEmpty()) {
                fullNameLabel.setVisibility(View.INVISIBLE);
            } else {
                fullNameLabel.setVisibility(View.VISIBLE);
            }
            return false;
        });

        mEmail.setOnKeyListener((v, keyCode, event) -> {
            String name = mFullName.getText().toString();
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            String confirmedPassword = mConfirmPassword.getText().toString();

            //makes labels visible if they are empty
            if(name.isEmpty())
            {
                fullNameLabel.setVisibility(View.VISIBLE);
            }
            if(password.isEmpty())
            {
                passwordLabel.setVisibility(View.VISIBLE);
            }
            if(confirmedPassword.isEmpty())
            {
                confirmPasswordLabel.setVisibility(View.VISIBLE);
            }
            if (!email.isEmpty()) {
                emailLabel.setVisibility(View.INVISIBLE);
            } else {
                emailLabel.setVisibility(View.VISIBLE);
            }
            return false;
        });





        mPassword.setOnKeyListener((v, keyCode, event) -> {
            String name = mFullName.getText().toString();
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            String confirmedPassword = mConfirmPassword.getText().toString();
            //makes  labels visible if they are empty
            if(name.isEmpty())
            {
                fullNameLabel.setVisibility(View.VISIBLE);
            }
            if(email.isEmpty())
            {
                emailLabel.setVisibility(View.VISIBLE);
            }
            if(confirmedPassword.isEmpty())
            {
                confirmPasswordLabel.setVisibility(View.VISIBLE);
            }
            if (!password.isEmpty()) {
                passwordLabel.setVisibility(View.INVISIBLE);
            } else {
                passwordLabel.setVisibility(View.VISIBLE);
            }
            return false;
        });


        mConfirmPassword.setOnKeyListener((v, keyCode, event) -> {
            String name = mFullName.getText().toString();
            String email = mEmail.getText().toString();
            String password = mPassword.getText().toString();
            String confirmedPassword = mConfirmPassword.getText().toString();

            //makes labels visible if they are empty
            if(name.isEmpty())
            {
                fullNameLabel.setVisibility(View.VISIBLE);
            }
            if(password.isEmpty())
            {
                passwordLabel.setVisibility(View.VISIBLE);
            }
            if(email.isEmpty())
            {
                emailLabel.setVisibility(View.VISIBLE);
            }
            if (!confirmedPassword.isEmpty()) {
                confirmPasswordLabel.setVisibility(View.INVISIBLE);
            } else {
                confirmPasswordLabel.setVisibility(View.VISIBLE);
            }
            return false;
        });

        //OnClickListener for Register Button
        mRegisterBtn.setOnClickListener(v -> {
            String name = mFullName.getText().toString().trim();
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();
            String confirmedPassword = mConfirmPassword.getText().toString().trim();

            //Check Name:
            if (TextUtils.isEmpty(name)) {
                mFullName.setError("Enter your name");
            }

            //Check Email:
            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is required.");
                return;
            }

            //Check Password:
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is required.");
                return;
            }

            boolean greaterSix = passMoreEqualThanSix(password);

            if (greaterSix == false) {
               //throw new IllegalArgumentException("Password must be greater than or equal to 6 characters");
                mPassword.setError("Password must be greater than or equal to 6 characters");

            }

            //Check Confirm Password
            if (TextUtils.isEmpty(confirmedPassword)) {
                mConfirmPassword.setError("Confirm your password.");
            }

            if (!password.equals(confirmedPassword)) {
                mConfirmPassword.setError("Does not match password entered above.");
            }
            //progressBar.setVisibility(View.VISIBLE);


            //register the user to the firebase
            fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ChooseSite.class));

                } else {
                    Toast.makeText(Register.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        mLoginBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Login.class));
        });







        }
    //MOSTLY FOR TEST CASES
    public static boolean passMoreEqualThanSix(String password) {
        if (password.length() < 6) {
            // throw new IllegalArgumentException("Password must be greater than or equal to 6 characters");

            //mPassword.setError("Password must be greater than or equal to 6 characters");
            return false;
        }
        else{
            return true;
        }
    }




    }



