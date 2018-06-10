package com.example.zion.workoutwithme;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import java.util.Arrays;

public class display_profile extends AppCompatActivity {

    TextView name, year, bio, major;
    ImageView profilePic;
    ListView interests;
    FirebaseStorage storage;
    StorageReference storageReference;
    String hostID;
    User currentUser;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_profile);

        Intent info = getIntent();
        hostID = info.getStringExtra("HOST_ID");

        name = findViewById(R.id.name_tv);
        year = findViewById(R.id.year_tv);
        major = findViewById(R.id.major_tv);
        bio = findViewById(R.id.bio_tv);
        profilePic = findViewById(R.id.profile_img);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("User").child(hostID);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            currentUser = dataSnapshot.getValue(User.class);

                            interests = (ListView) findViewById(R.id.interests_lv);

                            storageReference.child("images/"+hostID+"/"+currentUser.getProfilePic()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Got the download URL

                                    Glide
                                            .with(display_profile.this)
                                            .load(uri)
                                            .asBitmap()
                                            .into(profilePic);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });

                            name.setText(currentUser.getName());
                            year.setText(currentUser.getYear());
                            major.setText(currentUser.getMajor());
                            bio.setText(currentUser.getBio());

                            String[] interests = currentUser.getInterests().toString().split(" ");
                            ArrayList<String> currentInterests = new ArrayList<String>
                                    (Arrays.asList(interests));
                            adapter = new ArrayAdapter<String>
                                    (display_profile.this, android.R.layout.simple_list_item_1,
                                            currentInterests);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        interests.setAdapter(adapter);
                    } else {
                        Log.e("TAG", "It's null.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("TAG", "onCancelled", databaseError.toException());
            }
        });


    }
}
