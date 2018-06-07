package com.example.zion.workoutwithme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class comment_section extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference comments;
    String eventCount;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_section);

        database = FirebaseDatabase.getInstance();
        comments = database.getReference("Event/" + eventCount + "/comments");

        listView = findViewById(R.id.listView);
        FirebaseListAdapter adapter = new FirebaseListAdapter<Comment>(this, Comment.class, R.layout.comment, comments) {
            @Override
            protected void populateView(View v, Comment model, int position) {
                TextView comment = v.findViewById(R.id.comment);

                comment.setText(model.getMessage());
            }
        };


    }
}
