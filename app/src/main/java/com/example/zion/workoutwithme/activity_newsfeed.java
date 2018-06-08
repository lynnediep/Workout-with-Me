package com.example.zion.workoutwithme;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class activity_newsfeed extends AppCompatActivity {

    Button changeActivityButton;
    Button changeActivityButton2;
    String cruzID;
    FirebaseListAdapter<Event> adapter;
    FirebaseStorage storage;
    StorageReference storageReference;
    private static final String TAG = "";
    public static final String CURRENT_USER_ID = "";
    User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        // Current User
        Intent userInfo = getIntent();
        cruzID = userInfo.getStringExtra(Sign_In.CURRENT_USER_ID);
        if(cruzID == "" || cruzID == null){
            cruzID = userInfo.getStringExtra(Profile_Edit.CURRENT_USER_ID);
            if(cruzID == "" || cruzID == null) {
                cruzID = userInfo.getStringExtra(event_detail.CURRENT_USER_ID);
            }
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


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        adapter = new FirebaseListAdapter<Event>(this, Event.class, R.layout.event, event_table) {
            @Override
            protected void populateView(View v, Event model, int position) {
                TextView eventName = v.findViewById(R.id.event_name);
                TextView eventDescription = v.findViewById(R.id.event_description);
                TextView eventDate = v.findViewById(R.id.event_date);
                TextView eventTime = v.findViewById(R.id.event_time);
                TextView eventLocation = v.findViewById(R.id.event_location);
                TextView eventUsers = v.findViewById(R.id.event_users);
                final ImageView eventHostProfilePic = v.findViewById(R.id.host_profile);

                eventName.setText(model.getTitle());
                eventDescription.setText(model.getDescription());
                eventDate.setText(model.getDate());
                eventTime.setText(model.getTime());
                eventLocation.setText(model.getLocation());
                eventUsers.setText(Integer.toString(model.getUsers().size()) + " / " + Integer.toString(model.getMax_Count()));

                FirebaseDatabase anuthaDatabase = FirebaseDatabase.getInstance();
                // NEED TO UPDATE TO CURRENT HOST
                DatabaseReference userRef = anuthaDatabase.getReference("User").child(model.getHost());

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
                                                .with(activity_newsfeed.this)
                                                .load(uri)
                                                .asBitmap()
                                                .into(eventHostProfilePic);

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
            }
        };
        ListView newsfeed = (ListView) findViewById(R.id.newsfeed);
        newsfeed.setAdapter(adapter);
        newsfeed.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent details = new Intent(activity_newsfeed.this, event_detail.class);

                Event clickedEvent = adapter.getItem(i);
                // Put all information of the event that was just clicked into next page
                Bundle extras = new Bundle();
                extras.putString("EVENT_TITLE", clickedEvent.getTitle());
                extras.putString("EVENT_DESCRIPTION", clickedEvent.getDescription());
                extras.putString("EVENT_DATE", clickedEvent.getDate());
                extras.putString("EVENT_TIME", clickedEvent.getTime());
                extras.putString("EVENT_LOCATION", clickedEvent.getLocation());
                extras.putString("EVENT_HOST", clickedEvent.getHost());
                extras.putString("CURRENT_USER", cruzID);
                extras.putInt("EVENT_MAX_USERS", clickedEvent.getMax_Count());
                extras.putInt("EVENT_USER_COUNT", clickedEvent.getUser_Count());
                extras.putStringArrayList("EVENT_USERS", clickedEvent.getUsers());
                extras.putString("EVENT_COUNT", clickedEvent.getCount());
                details.putExtras(extras);

                startActivity(details);
            }
        });



    }


}
