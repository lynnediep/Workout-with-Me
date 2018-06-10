package com.example.zion.workoutwithme;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.Calendar;

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
    int x;
    ArrayList<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        checkAutomaticTime();

        // Current User
        Intent userInfo = getIntent();
        cruzID = userInfo.getStringExtra(Sign_In.CURRENT_USER_ID);
        if(cruzID == "" || cruzID == null){
            cruzID = userInfo.getStringExtra(Profile_Edit.CURRENT_USER_ID);
            if(cruzID == "" || cruzID == null) {
                cruzID = userInfo.getStringExtra("CURRENT_USER_ID");
            }
        }

        // User Database and Event Database loaded
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference current_user = database.getReference("User");
        final DatabaseReference event_table = database.getReference("Event");

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

        final Query query = event_table.orderByChild("date");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        adapter = new FirebaseListAdapter<Event>(this, Event.class, R.layout.event, query) {
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
                
                final String theHost = adapter.getItem(position).getHost();

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        FirebaseDatabase anuthaDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference userRef = anuthaDatabase.getReference("User").child(theHost).child("profilePic");

                        userRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                try {
                                    if (dataSnapshot.getValue() != null) {
                                        String profilePicUUID = dataSnapshot.getValue(String.class);

                                        storageReference.child("images/"+ theHost+"/"+profilePicUUID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

        event_table.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the current time to compare to timestamp
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);
                if(currentHour == 0) {
                    String cH = "0" + Integer.toString(currentHour);
                }

                String secondPart = Integer.toString(currentHour) + Integer.toString(currentMinute);

                // Find the first part of the current time
                int month = calendar.get(Calendar.MONTH) + 1;
                String m = "";
                if(month < 10) {
                    m = "0" + Integer.toString(month);
                } else {
                    m = Integer.toString(month);
                }
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String d = "";
                if(day < 10) {
                     d = "0" + Integer.toString(day);
                } else {
                    d = Integer.toString(day);
                }
                String firstPart = m + d;

                // Check Delete is time + .02
                String checkDeleteTime = firstPart + "." + secondPart;

                for(DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {
                    String eventTimeStamp = (String) eventSnapshot.child("timestamp").getValue();
                    double check = Double.parseDouble(checkDeleteTime) - Double.parseDouble(eventTimeStamp);
                    if(check >= .02) {
                        String key = eventSnapshot.getKey();
                        event_table.child(key).removeValue();
                    }

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
