package com.example.zion.workoutwithme;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPw;
    private TextView textViewSignUp;

    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            // send the user to the profile activity
            finish();
            startActivity(new Intent(getApplicationContext(), Profile_Edit.class));
        }

        progressBar = new ProgressBar(this);
        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        EditText editTextPw = (EditText) findViewById(R.id.editTextPw);
        TextView textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);


        //Create listener
        findViewById(R.id.buttonLogin).setOnClickListener(this);
        findViewById(R.id.textViewSignUp).setOnClickListener(this);


    }

    // This is the method that will login the user
    private void logInUser(){
        String email = editTextEmail.getText().toString();
        String password = editTextPw.getText().toString();

        // Makes sure an email is present
        if(email.isEmpty()){
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;

        }

        // Makes sure a Pw is entered
        if(password.isEmpty()){
            //password is empty
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            return;

        }

        //If email and Pw are entered we will have a progress dialog showing
        //progressBar.draw();


        // Need to set the firebase auth to sign in user and move to profile page.
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    finish();
                    startActivity(new Intent(MainActivity.this, Profile_Edit.class));
                }else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //Sets the OnClick for button and textviews
        @Override
        public void onClick(View view) {

            switch(view.getId()){
                case R.id.textViewSignUp:
                    finish();
                    startActivity(new Intent(this, login_activity.class));
                    break;

                case R.id.buttonLogin:
                    logInUser();
                    break;

            }


        }

    }

    // CMPS 121 PROJECT
    // ZION CALVO
    // LYNNE DIEP
    // DANIEL GUTIERREZ
    // CHUONG V TRUONG



