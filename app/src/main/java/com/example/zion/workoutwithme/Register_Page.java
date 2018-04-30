package com.example.zion.workoutwithme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Page extends AppCompatActivity {

    private TextInputEditText email, password, passwordConfirm, firstName, lastName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Databases
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        mAuth = FirebaseAuth.getInstance();
// MAKING AUTHENTICATION


        final ProgressDialog mDialog = new ProgressDialog(Register_Page.this);
        mDialog.setMessage("Please Wait...");


        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        passwordConfirm = findViewById(R.id.etConfirmPassword);
        firstName = findViewById(R.id.etFirstName);
        lastName = findViewById(R.id.etLastName);


        Button registerButton = findViewById(R.id.bRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Progress Bar
                mDialog.show();


                if(checkNullInput()) {
                    mDialog.dismiss();
                    return;
                }

                // Check if Passwords match
                if(password.getText().toString().matches(passwordConfirm.getText().toString())) {

                    // Check if Email is UCSC Email
                    if(email.getText().toString().contains("@ucsc.edu")) {

                        // Creates new User
                        User user = new User(firstName.getText().toString() +" " + lastName.getText().toString()
                                , password.getText().toString()
                                , email.getText().toString());
                        table_user.child(email.getText().toString().replaceAll("@ucsc.edu", "")).setValue(user);
                        mDialog.dismiss();
                        // Goes to Profile page on successful register
                        Intent profile = new Intent(Register_Page.this, Profile_Edit.class);
                        startActivity(profile);

                    } else {
                        mDialog.dismiss();
                        Toast.makeText(Register_Page.this, "Must be UCSC email", Toast.LENGTH_SHORT).show();
                        email.setError("Must be UCSC email");
                    }

                } else {
                    mDialog.dismiss();
                    Toast.makeText(Register_Page.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    password.setError("Password does not match");
                    passwordConfirm.setError("Password does not match");
                }
            }
        });

        Button cancel = findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancel = new Intent(Register_Page.this, Log_In.class);
                cancel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(cancel);
            }
        });
    }

    public boolean checkNullInput() {
        // Checks if we make the Toast Message
        int error = 0;

        // Detect if first name is empty
        if(firstName.getText().toString().matches("")) {
            firstName.setError("Please enter your First Name");
            error++;
        }
        // Detect if last name is empty
        if(lastName.getText().toString().matches("")) {
            lastName.setError("Please enter your Last Name");
            error++;
        }
        // Detect if password is empty
        if(password.getText().toString().matches("")) {
            password.setError("Please make a password");
            error++;
        }
        // Detect if password confirmation is empty
        if(passwordConfirm.getText().toString().matches("")) {
            passwordConfirm.setError("Please confirm your password");
            error++;
        }

        // Check if there was any null errors
        if(error > 0) {
            Toast.makeText(Register_Page.this, "Please fill out require fields", Toast.LENGTH_SHORT).show();
            return true;
        }
            return false;
    }

}
