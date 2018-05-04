package com.example.zion.workoutwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class activity_add_event extends AppCompatActivity {

    Button changeActivityButton;
    EditText etTitle, etLocation, etTime, etDate, etDescription, etMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        // Current User
        Intent userInfo = getIntent();
        final String cruzID = userInfo.getStringExtra(activity_newsfeed.CURRENT_USER_ID);


        // Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Event");


        etTitle = findViewById(R.id.enter_title);
        etLocation = findViewById(R.id.enter_location);
        etTime = findViewById(R.id.enter_time);
        etDate = findViewById(R.id.enter_date);
        etDescription = findViewById(R.id.enter_description);
        etMax = findViewById(R.id.enter_max);


        changeActivityButton = (Button)findViewById(R.id.add);
        changeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nullCheck()) {
                    return;
                }

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Make the new event
                        Event event = new Event(
                                etTitle.getText().toString(),
                                etDescription.getText().toString(),
                                etTime.getText().toString(),
                                etDate.getText().toString(),
                                etLocation.getText().toString(),
                                cruzID,
                                Integer.parseInt(etMax.getText().toString())

                        );
                        // Makes the new event in the database and updates the event count
                        String count = dataSnapshot.child("event_count").getValue(String.class);
                        table_user.child("event" + count).setValue(event);
                        table_user.child("event_count").setValue(Integer.toString(Integer.parseInt(count) + 1));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(activity_add_event.this, activity_newsfeed.class);
                startActivity(intent);
            }
        });
    }

    public boolean nullCheck() {
        int error = 0;
        // Detect if title is empty
        if (etTitle.getText().toString().matches("")) {
            etTitle.setError("Please make your title");
            error++;
        }
        // Detect if location is empty
        if (etLocation.getText().toString().matches("")) {
            etLocation.setError("Please make a location");
            error++;
        }
        // Detect if time is empty
        if (etTime.getText().toString().matches("")) {
            etLocation.setError("Please make a time");
            error++;
        }
        // Detect if date is empty
        if (etDate.getText().toString().matches("")) {
            etDate.setError("Please make a date");
            error++;
        }
        // Detect if description is empty
        if (etDescription.getText().toString().matches("")) {
            etDescription.setError("Please make a description");
            error++;
        }
        // Detect if max is empty
        if (etMax.getText().toString().matches("")) {
            etMax.setError("Please make a max search");
            error++;
        }
        if (error > 0) {
            Toast.makeText(activity_add_event.this, "Please fill out require fields", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }
}
