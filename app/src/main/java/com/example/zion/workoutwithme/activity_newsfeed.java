package com.example.zion.workoutwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activity_newsfeed extends AppCompatActivity {

    Button changeActivityButton;
    Button changeActivityButton2;
    public static final String CURRENT_USER_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        // Current User
        Intent userInfo = getIntent();
        final String cruzID = userInfo.getStringExtra(Sign_In.CURRENT_USER_ID);

        // User Database and Event Database loaded
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference current_user = database.getReference("User");
        DatabaseReference event_table = database.getReference("Event");


        changeActivityButton = (Button)findViewById(R.id.edit_profile);
        changeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_newsfeed.this, Profile_Edit.class);
                startActivity(intent);
            }
        });

        changeActivityButton2 = (Button)findViewById(R.id.add_event);
        changeActivityButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_newsfeed.this, activity_add_event.class);

                intent.putExtra(CURRENT_USER_ID, cruzID);

                startActivity(intent);
            }
        });
    }


}
