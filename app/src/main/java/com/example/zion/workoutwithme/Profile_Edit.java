package com.example.zion.workoutwithme;

// Profile Edit Java
// Lynne Diep
// Chuong Truong

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Profile_Edit extends AppCompatActivity {
    EditText nameText,yearText, majorText, interestText, bioText;
    Button addInterestsBtn;
    Button changeActivityButton;
    Button refreshInterests;
    ListView interestsListView;
    User currentUser;
    ImageView profilePic;
    ArrayList<String> interestsArray = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    String finalInterests;
    public static final String CURRENT_USER_ID = "";
    private static final String TAG = "";
    public static final int GET_FROM_GALLERY = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__edit);

        /*
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        */

        // References for each view
        nameText = (EditText) findViewById(R.id.Name_editText);
        yearText = (EditText) findViewById(R.id.Year_editText);
        majorText = (EditText) findViewById(R.id.Major_editText);
        interestText = (EditText) findViewById(R.id.Interests_editText);
        bioText = (EditText) findViewById(R.id.Bio_editText);
        addInterestsBtn = (Button) findViewById(R.id.addInterests_Button);
        interestsListView = (ListView) findViewById(R.id.Interests_ListView);
        refreshInterests = (Button) findViewById(R.id.Refresh_Button);
        profilePic = (ImageView) findViewById(R.id.Profile_img);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Detects request codes
        if(requestCode==GET_FROM_GALLERY && resultCode == Profile_Edit.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Setting profile pic
        profilePic.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI),
                        GET_FROM_GALLERY);
            }
        });

        // Current User
        Intent userInfo = getIntent();
        final String cruzID = userInfo.getStringExtra(Sign_In.CURRENT_USER_ID);

        // Instantiate database & set up user reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference userRef = database.getReference("User").child(cruzID);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.getValue() != null) {
                        try {
                            currentUser = dataSnapshot.getValue(User.class);

                            // Set EditText's to current profile info if it exists
                            if(currentUser.getName() != null){
                                nameText.setText(currentUser.getName());
                            }

                            if(currentUser.getYear() != null) {
                                yearText.setText(currentUser.getYear());
                            }

                            if(currentUser.getMajor() != null) {
                                majorText.setText(currentUser.getMajor());
                            }

                            if(currentUser.getInterests() != null) {
                                String[] interests = currentUser.getInterests().toString().split(" ");
                                ArrayList<String> currentInterests = new ArrayList<String>
                                        (Arrays.asList(interests));
                                adapter = new ArrayAdapter<String>
                                        (Profile_Edit.this, android.R.layout.simple_list_item_1,
                                                currentInterests);
                                interestsListView.setAdapter(adapter);
                            }

                            if(currentUser.getBio() != null) {
                                bioText.setText(currentUser.getBio());
                            }

                            // Add interests one by one
                            addInterestsBtn.setOnClickListener(new View.OnClickListener(){
                                String getInput;
                                @Override
                                public void onClick(View view) {
                                    getInput = interestText.getText().toString();
                                    if(interestsArray.contains(getInput)){
                                        Toast.makeText(getBaseContext(), "\""+getInput+"\" has already been added",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else if(getInput == null || getInput.trim().equals("")){
                                        Toast.makeText(getBaseContext(), "Input field is empty",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else{
                                        interestsArray.add(getInput);
                                        adapter = new ArrayAdapter<String>
                                                (Profile_Edit.this, android.R.layout.simple_list_item_1,
                                                        interestsArray);
                                        interestsListView.setAdapter(adapter);
                                        interestText.setText("");
                                    }
                                }
                            });

                            // Refresh button, maybe remove
                            refreshInterests.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View view) {
                                    adapter.notifyDataSetChanged();
                                    interestsListView.setAdapter(adapter);
                                }
                            });

                            // Remove an interest by clicking on it in the ListView
                            interestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    AlertDialog.Builder adb = new AlertDialog.Builder(Profile_Edit.this);
                                    adb.setTitle("Delete?");
                                    adb.setMessage("Are you sure you want to delete \""+adapter.getItem(i)+"\"");
                                    final int positionToRemove = i;
                                    adb.setNegativeButton("Cancel", null);
                                    adb.setPositiveButton("Ok", new AlertDialog.OnClickListener(){
                                        public void onClick(DialogInterface dialog, int which){
                                            adapter.remove(adapter.getItem(positionToRemove));
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                    adb.show();
                                }
                            });

                            // Save profile with inputted info
                            changeActivityButton = (Button) findViewById(R.id.save);
                            changeActivityButton.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Profile_Edit.this, activity_newsfeed.class);

                                    String currentName = nameText.getText().toString();
                                    String currentYear = yearText.getText().toString();
                                    String currentMajor = majorText.getText().toString();
                                    String currentBio = bioText.getText().toString();

                                    if(currentName == null || currentName.trim().equals("")){
                                        Toast.makeText(getBaseContext(), "Name field is empty",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else if(currentYear == null || currentYear.trim().equals("")){
                                        Toast.makeText(getBaseContext(), "Year field is empty",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else if(currentMajor == null || currentMajor.trim().equals("")){
                                        Toast.makeText(getBaseContext(), "Major field is empty",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else if(currentBio == null || currentBio.trim().equals("")){
                                        Toast.makeText(getBaseContext(), "Bio field is empty",
                                                Toast.LENGTH_LONG).show();
                                    }
                                    else {
                                        StringBuilder sb = new StringBuilder();
                                        for(String s : interestsArray){
                                            sb.append(s);
                                            if(s != null){
                                                sb.append(" ");
                                            }
                                        }

                                        currentUser.setName(currentName);
                                        currentUser.setYear(currentYear);
                                        currentUser.setMajor(currentMajor);
                                        currentUser.setBio(currentBio);
                                        currentUser.setInterests(sb.toString());

                                        userRef.setValue(currentUser);

                                        intent.putExtra(CURRENT_USER_ID, cruzID);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.e("TAG", "It's null.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "onCancelled", databaseError.toException());
            }
        });
    }
}
