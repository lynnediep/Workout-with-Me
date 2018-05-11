package com.example.zion.workoutwithme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class activity_newsfeed extends AppCompatActivity {

    Button changeActivityButton;
    Button changeActivityButton2;
    String cruzID;

    public static final String CURRENT_USER_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        // Current User
        Intent userInfo = getIntent();
        cruzID = userInfo.getStringExtra(Sign_In.CURRENT_USER_ID);
        if(cruzID == "" || cruzID == null){
            cruzID = userInfo.getStringExtra(Profile_Edit.CURRENT_USER_ID);
        }

        // User Database and Event Database loaded
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference current_user = database.getReference("User");
        DatabaseReference event_table = database.getReference("Event");

        changeActivityButton = (Button)findViewById(R.id.edit_profile);
        changeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_newsfeed.this, Profile_Edit.class);
                intent.putExtra(CURRENT_USER_ID, cruzID);
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

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Event");


        FirebaseListAdapter<Event> adapter = new FirebaseListAdapter<Event>(this, Event.class, android.R.layout.simple_list_item_1, event_table) {
            @Override
            protected void populateView(View v, Event model, int position) {
                TextView eventName = v.findViewById(android.R.id.text1);
                eventName.setText(model.getTitle());
            }
        };
        ListView newsfeed = (ListView) findViewById(R.id.newsfeed);
        newsfeed.setAdapter(adapter);


    }


}
