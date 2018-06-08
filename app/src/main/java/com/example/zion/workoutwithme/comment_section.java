package com.example.zion.workoutwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class comment_section extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference comments;
    ListView listView;
    ArrayList<Comment> commentList;
    Button commentButton;
    EditText userComment;
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_section);

        Intent extras = getIntent();
        String eventComments = extras.getStringExtra("EVENT_COUNT");
        final String cruzID = extras.getStringExtra("CURRENT_USER");

        database = FirebaseDatabase.getInstance();
        comments = database.getReference("Event/" + eventComments + "/comments/");

        listView = findViewById(R.id.listView);
        FirebaseListAdapter adapter = new FirebaseListAdapter<Comment>(this, Comment.class, R.layout.comment, comments) {
            @Override
            protected void populateView(View v, Comment model, int position) {

                TextView comment = v.findViewById(R.id.comment);
                comment.setText(model.getMessage());
                TextView name = v.findViewById(R.id.comment_host_name);
                name.setText(model.getName());
                TextView time = v.findViewById(R.id.comment_time);
                time.setText(model.getTime());

            }
        };

        listView.setAdapter(adapter);

        userComment = findViewById(R.id.userComment);

        commentButton = findViewById(R.id.commentButton);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Message
                String commentString = userComment.getText().toString();
                // Date
                calendar.setTime(date);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int year = calendar.get(Calendar.YEAR);
                final String date = Integer.toString(month) + "/" + Integer.toString(day) + "/" + Integer.toString(year);
                // Time
                DateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
                final String time = timeFormat.format(calendar.getTime());

                final Comment currentComment = new Comment(commentString, time, date, cruzID);

                comments.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        commentList = new ArrayList<Comment>();
                        for(DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            String message = (String) childSnapshot.child("message").getValue();
                            String time = (String) childSnapshot.child("time").getValue();
                            String date = (String) childSnapshot.child("date").getValue();
                            String user = (String) childSnapshot.child("name").getValue();
                            commentList.add(new Comment(message, time, date, user));
                        }
                        commentList.add(currentComment);

                        comments.setValue(commentList);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                userComment.setText("");

            }
        });

    }
}
