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
import android.widget.Button;

public class Log_In extends AppCompatActivity {

    private Button registerButton, signInButton;
    int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__in);

        checkAutomaticTime();

        registerButton = findViewById(R.id.bRegister);
        signInButton = findViewById(R.id.bLogIn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Register Page
                checkAutomaticTime();
                Intent register = new Intent(Log_In.this, Register_Page.class);
                startActivity(register);

            }
        });

        signInButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Sign In Page
                checkAutomaticTime();
                Intent signIn = new Intent(Log_In.this, Sign_In.class);
                startActivity(signIn);
            }
        }));

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
