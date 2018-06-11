package com.example.zion.workoutwithme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    DatabaseReference comments, users;
    ListView listView;
    ArrayList<Comment> commentList;
    ImageButton commentButton;
    EditText userComment;
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_section);

        checkAutomaticTime();

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
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView name = view.findViewById(R.id.comment_host_name);
                String user = name.getText().toString();
                final int y = i;
                if(user.matches(cruzID)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(comment_section.this, R.style.AlertBox);
                    builder.setTitle("Delete Comment")
                            .setMessage("Would you like to delete your comment?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    comments.child(Integer.toString(y)).removeValue();

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
                                            comments.setValue(commentList);
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                        }
                                    });

                                    Toast.makeText(comment_section.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    Intent intent = new Intent(comment_section.this, display_profile.class);
                    intent.putExtra("HOST_ID", user);
                    startActivity(intent);
                }
            }
        });

        userComment = findViewById(R.id.userComment);

        ImageButton commentButton = (ImageButton) findViewById(R.id.commentButton);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userComment.getText().toString().matches("")) {
                    return;
                }
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

    @Override
    protected void onResume() {
        super.onResume();

        checkAutomaticTime();
    }

    public void checkAutomaticTime() {
        // Check if user has correct time
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            x = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME, 0);
        } else {
            x = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0);
        }
        if(x != 1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Please turn on automatic time")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            while(x != 1) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                    x = Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME, 0);
                                } else {
                                    x = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0);
                                }
                            }
                        }
                    })
                    .show();
        }
    }
}
