package com.example.zion.workoutwithme;

// Profile Edit Java
// Lynne Diep
// Chuong Truong

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Profile_Edit extends AppCompatActivity {
    EditText nameText;
    EditText bioText;
    EditText interestText;
    Button changeActivityButton;
    String[] fnLnEmail;
    int flag = 0;
    public static final String EXTRA_PROFILE2NEWSFEED_FN_LN_EMAIL = "";
    public static final String EXTRA_PROFILE2NEWSFEED_BIO = "";
    String theBio = "Biography";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__edit);

        nameText = (EditText) findViewById(R.id.Name_editText);
        bioText = (EditText) findViewById(R.id.Bio_editText);
        interestText = (EditText) findViewById(R.id.Interest_editText);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        // Get the Intent that started this activity and extract the strings

        if(flag == 0){
            Intent intent = getIntent();
            fnLnEmail = intent.getStringArrayExtra(Register_Page.EXTRA_REG2PROFILE_FN_LN_EMAIL);
            flag = 1;
        }

        if(flag == 0){
            Bundle recieveExtras = getIntent().getExtras();
            fnLnEmail = recieveExtras.getStringArray(activity_newsfeed.EXTRA_NEWSFEED2PROFILE_FN_LN_EMAIL);
            theBio = recieveExtras.getString(activity_newsfeed.EXTRA_NEWSFEED2PROFILE_BIO);
            bioText.setText(theBio);
        }

        // Capture the layout's TextView & set the string as its text
        if(fnLnEmail[0] != null && fnLnEmail[1] != null){
            nameText.setText(fnLnEmail[0] + " " + fnLnEmail[1]);
        }

        final String cruzID = fnLnEmail[2].replaceAll("@ucsc.edu","");

        changeActivityButton = (Button)findViewById(R.id.save);
        changeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bioDB = "bio";
                String nameDB = "name";
                table_user.child(cruzID).child(nameDB).setValue(nameText.getText().toString());
                table_user.child(cruzID).child(bioDB).setValue(bioText.getText().toString());

                if(table_user.child(cruzID).child(bioDB).getKey() != null){
                    theBio = table_user.child(cruzID).child(bioDB).getKey();
                }

                Intent intent = new Intent(Profile_Edit.this, activity_newsfeed.class);
                Bundle sendExtras = new Bundle();
                sendExtras.putStringArray(EXTRA_PROFILE2NEWSFEED_FN_LN_EMAIL, new String[] {fnLnEmail[0],
                        fnLnEmail[1], fnLnEmail[2]});
                sendExtras.putString(EXTRA_PROFILE2NEWSFEED_BIO, theBio);
                intent.putExtras(sendExtras);
                startActivity(intent);
            }

        });
    }
}
