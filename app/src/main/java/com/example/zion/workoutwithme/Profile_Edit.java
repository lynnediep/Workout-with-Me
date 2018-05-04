package com.example.zion.workoutwithme;

// Profile Edit Java
// Lynne Diep
// Chuong Truong

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile_Edit extends AppCompatActivity {
    EditText nameText;
    EditText bioText;
    EditText interestText;
    Button changeActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__edit);

        nameText = (EditText) findViewById(R.id.Name_editText);
        bioText = (EditText) findViewById(R.id.Bio_editText);
        interestText = (EditText) findViewById(R.id.Interest_editText);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth mAuth;
        final DatabaseReference table_user = database.getReference("User");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        changeActivityButton = (Button)findViewById(R.id.save);
        changeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bioDB = "bio";
                String nameDB = "name";

                Intent intent = new Intent(Profile_Edit.this, activity_newsfeed.class);
                startActivity(intent);
            }

        });
    }
}
