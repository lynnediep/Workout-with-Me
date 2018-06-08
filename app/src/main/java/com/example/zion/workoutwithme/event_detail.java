package com.example.zion.workoutwithme;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class event_detail extends AppCompatActivity {

    TextView title, description, date, time, location, comments;
    ImageView hostPicture;
    Button joinButton, leaveButton, btnCancel;
    ArrayList<String> users;
    ArrayList<Comment> commentList;
    User currentUser;
    FirebaseDatabase database;
    DatabaseReference event;
    FirebaseStorage storage;
    StorageReference storageReference;
    private static final String TAG = "";
    String host;

    public static String CURRENT_USER_ID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);


        title = findViewById(R.id.eventTitle);
        description = findViewById(R.id.eventDescription);
        date = findViewById(R.id.eventDate);
        time = findViewById(R.id.eventTime);
        location = findViewById(R.id.eventLocation);
        hostPicture = findViewById(R.id.hostPicture);
        comments = findViewById(R.id.viewComments);

        Intent info = getIntent();
        Bundle extras = info.getExtras();

        String event_title = extras.getString("EVENT_TITLE");
        String event_desc = extras.getString("EVENT_DESCRIPTION");
        final String event_date = extras.getString("EVENT_DATE");
        String event_time = extras.getString("EVENT_TIME");
        String event_location = extras.getString("EVENT_LOCATION");
        int event_max_users = extras.getInt("EVENT_MAX_USERS");
        int event_user_count = extras.getInt("EVENT_USER_COUNT");
        final String cruzID = extras.getString("CURRENT_USER");
        String eventHost = extras.getString("EVENT_HOST");
        users = extras.getStringArrayList("EVENT_USERS");

        String count = extras.getString("EVENT_COUNT");
        final String eventCount = "event" + count;

        database = FirebaseDatabase.getInstance();
        event = database.getReference("Event/" + eventCount);

        title.setText(event_title);
        description.setText(event_desc);
        date.setText(event_date);
        time.setText(event_time);
        location.setText(event_location);

        event.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int commentSize = ((int) dataSnapshot.child("comments").getChildrenCount()) - 1;
                if(commentSize > 0) {
                    comments.setText("Post/View Comments(" + Integer.toString(commentSize) + ")");
                } else {
                    comments.setText("Post/View Comments(0)");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       // btnCancel = (Button)findViewById(R.id.btncancel);
       // btnCancel.setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View view) {
       //         Intent intent = new Intent(event_detail.this, activity_newsfeed.class);
       //         startActivity(intent);
       //     }
       // });

        FirebaseDatabase anuthaDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = anuthaDatabase.getReference("User").child(eventHost);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        currentUser = dataSnapshot.getValue(User.class);

                        storageReference.child("images/"+currentUser.getProfilePic()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL
                                Glide
                                        .with(event_detail.this)
                                        .load(uri)
                                        .asBitmap()
                                        .into(hostPicture);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                    }
                    else {
                        Log.e("TAG", "It's null.");
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });

        joinButton = findViewById(R.id.joinButton);
        if(users.contains(cruzID)) {
            joinButton.setText("JOINED");
        } else if (event_user_count == event_max_users){
            joinButton.setText("ACTIVITY FULL");
        } else {
            joinButton.setText("Join");
        }
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(joinButton.getText().toString().matches("JOINED")) {
                    Toast.makeText(event_detail.this, "Already joined event", Toast.LENGTH_LONG).show();
                } else if (joinButton.getText().toString().matches("ACTIVITY FULL")) {
                    Toast.makeText(event_detail.this, "Activity is full!", Toast.LENGTH_LONG).show();
                } else {

                    event.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            users.add(cruzID);
                            event.child("users").setValue(users);
                            event.child("user_Count").setValue(users.size());
                            Toast.makeText(event_detail.this, "Successfully joined event", Toast.LENGTH_SHORT).show();
                            joinButton.setText("JOINED");
                            Intent intent = new Intent(event_detail.this, activity_newsfeed.class);
                            intent.putExtra(CURRENT_USER_ID, cruzID);
                            startActivity(intent);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }

            }
        });

        leaveButton = findViewById(R.id.leaveActivity);
        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(joinButton.getText().toString().matches("JOINED")) {

                    event.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            users.remove(cruzID);
                            event.child("users").setValue(users);
                            event.child("user_Count").setValue(users.size());
                            Toast.makeText(event_detail.this, "Successfully left event", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(event_detail.this, activity_newsfeed.class);
                            intent.putExtra(CURRENT_USER_ID, cruzID);
                            startActivity(intent);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {

                    Toast.makeText(event_detail.this, "You are not in the event", Toast.LENGTH_SHORT).show();

                }

            }
        });

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent commentSection = new Intent(event_detail.this, comment_section.class);
                commentSection.putExtra("EVENT_COUNT", eventCount);
                commentSection.putExtra("CURRENT_USER", cruzID);
                startActivity(commentSection);
            }
        });

        hostPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(event_detail.this, display_profile.class);
                startActivity(i);
            }
        });
    }

}
