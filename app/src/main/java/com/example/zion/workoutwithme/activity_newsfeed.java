package com.example.zion.workoutwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activity_newsfeed extends AppCompatActivity {

    Button changeActivityButton;
    Button changeActivityButton2;
    String[] fnLnEmail;
    String theBio;

    public static final String EXTRA_NEWSFEED2PROFILE_FN_LN_EMAIL = "";
    public static final String EXTRA_NEWSFEED2PROFILE_BIO = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsfeed);

        Bundle recieveExtras = getIntent().getExtras();
        fnLnEmail = recieveExtras.getStringArray(Profile_Edit.EXTRA_PROFILE2NEWSFEED_FN_LN_EMAIL);
        theBio = recieveExtras.getString(theBio);


        changeActivityButton = (Button)findViewById(R.id.edit_profile);
        changeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_newsfeed.this, Profile_Edit.class);
                Bundle sendExtras = new Bundle();
                sendExtras.putStringArray(EXTRA_NEWSFEED2PROFILE_FN_LN_EMAIL, new String[] {fnLnEmail[0],
                        fnLnEmail[1], fnLnEmail[2]});
                sendExtras.putString(EXTRA_NEWSFEED2PROFILE_BIO, theBio);
                startActivity(intent);
            }
        });

        changeActivityButton2 = (Button)findViewById(R.id.add_event);
        changeActivityButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity_newsfeed.this, activity_add_event.class);
                startActivity(intent);
            }
        });
    }


}
