package com.example.zion.workoutwithme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_activity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPw;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        mAuth = FirebaseAuth.getInstance();

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPw = (EditText) findViewById(R.id.editTextPw);

        findViewById(R.id.buttonRegister).setOnClickListener(this);

    }




    // This is the method that will Register the user
    private void registerUser() {
        String email = editTextEmail.getText().toString();
        String password = editTextPw.getText().toString();

        // Makes sure an email is present
        if (email.isEmpty()) {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;

        }

        // Makes sure a Pw is entered
        if (password.isEmpty()) {
            //password is empty
            Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            return;

        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //user is successfully registered
                            // we then go to the profile activity
                            finish();
                            startActivity(new Intent(login_activity.this, MainActivity.class));


                        }
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, Profile_Edit.class));
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonRegister:
                registerUser();
                break;
        }



    }
}
