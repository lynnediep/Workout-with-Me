package com.example.zion.workoutwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class comment_section extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference comments;
    ListView listView;
    ArrayList<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_section);



        Intent extras = getIntent();
        String eventComments = extras.getStringExtra("EVENT_COUNT");

        database = FirebaseDatabase.getInstance();
        comments = database.getReference("Event/" + eventComments + "/comments/");

        listView = findViewById(R.id.listView);
        FirebaseListAdapter adapter = new FirebaseListAdapter<Comment>(this, Comment.class, R.layout.comment, comments) {
            @Override
            protected void populateView(View v, Comment model, int position) {
                if(!model.getDate().matches("0/0/0000")) {
                    TextView comment = v.findViewById(R.id.comment);
                    comment.setText(model.getMessage());
                }
            }
        };

        listView.setAdapter(adapter);

    }
}
