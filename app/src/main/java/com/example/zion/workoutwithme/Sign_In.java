package com.example.zion.workoutwithme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;

public class Sign_In extends AppCompatActivity {

    TextInputEditText email, password;
    Button bSignIn;
    public static final String CURRENT_USER_ID = "";
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__in);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        final DatabaseReference table_user = database.getReference("User");

        bSignIn = findViewById(R.id.signInButton);
        bSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(Sign_In.this);
                mDialog.setMessage("Please Wait...");
                mDialog.show();

                if(nullCheck()) {
                    return;
                }

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Check if user exists
                        final String cruzID = email.getText().toString().replaceAll("@ucsc.edu","");

                        if(dataSnapshot.child(cruzID).exists()) {
                            // Get User Info
                            mDialog.dismiss();
                            User user = dataSnapshot.child(cruzID).getValue(User.class);
                            if(user.getPassword().equals(password.getText().toString())) {


                                // ******************************** Sign in method for firebase ***********************

                                String etEmail = email.getText().toString();
                                String etPassword = password.getText().toString();

                                mAuth.signInWithEmailAndPassword(etEmail, etPassword)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    //user is successfully registered
                                                    // we then go to the profile activity
                                                    finish();

                                                }
                                            }
                                        });

                                // ***********************************************************************************

                                Toast.makeText(Sign_In.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                Intent home = new Intent(Sign_In.this, activity_newsfeed.class);

                                home.putExtra(CURRENT_USER_ID, cruzID);

                                startActivity(home);
                            } else {
                                Toast.makeText(Sign_In.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(Sign_In.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Sign_In.this, "Could not connect to server", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });


    }

    public boolean nullCheck() {
        int error = 0;
        // Detect if first name is empty
        if(email.getText().toString().matches("")) {
            email.setError("Please enter your UCSC email");
            error++;
        }
        // Detect if password is empty
        if(password.getText().toString().matches("")) {
            password.setError("Please enter your First Name");
            error++;
        }
        if(error > 0) {
            Toast.makeText(Sign_In.this, "Please fill out require fields", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
