package com.example.zion.workoutwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Log_In extends AppCompatActivity {

    private Button registerButton, signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log__in);

        registerButton = findViewById(R.id.bRegister);
        signInButton = findViewById(R.id.bLogIn);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Register Page
                Intent register = new Intent(Log_In.this, Register_Page.class);
                startActivity(register);
            }
        });

        signInButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go to Sign In Page
                Intent signIn = new Intent(Log_In.this, Sign_In.class);
                startActivity(signIn);
            }
        }));

    }
}
