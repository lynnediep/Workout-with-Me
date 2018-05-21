package com.example.zion.workoutwithme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class auth_user extends AppCompatActivity implements View.OnClickListener {

    private Button authButton;
    private TextView authText;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_user);

        authButton = (Button) findViewById(R.id.authButton);
        authText = (TextView) findViewById(R.id.authText);

        findViewById(R.id.authButton).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.authButton:
                    verify_user();
                    //startActivity(new Intent(auth_user.this, Profile_Edit.class));

                    break;
            }

        }

    // **************************** Authentication *****************************************

    private void verify_user() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user.isEmailVerified()){
            authText.setText("Email is verified");

        }else {

            authText.setText("Email Not Verified (Click to Verify)");
            authText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){

                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(auth_user.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                                finish();

                            }

                        }
                    });


                }
            });

            //****************************************************************************************
        }

    }


}
