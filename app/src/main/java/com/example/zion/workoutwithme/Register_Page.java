package com.example.zion.workoutwithme;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Page extends AppCompatActivity {

    private TextInputEditText email, password, passwordConfirm, firstName, lastName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Databases

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        passwordConfirm = findViewById(R.id.etPassword);
        firstName = findViewById(R.id.etFirstName);
        lastName = findViewById(R.id.etLastName);


        Button registerButton = findViewById(R.id.bRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Check Passwords Match
                if(password.getText().toString().matches(passwordConfirm.getText().toString())) {

                    // Check if Email is UCSC Email
                    if(email.getText().toString().contains("@ucsc.edu")) {

                        // Creates new User
                        User user = new User(email.getText().toString(), password.getText().toString());
                        table_user.child(firstName.getText().toString() + "_" + lastName.getText().toString()).setValue(user);

                    } else {
                        Toast.makeText(Register_Page.this, "Must be UCSC email", Toast.LENGTH_SHORT).show();
                        email.setError("Must be UCSC email");
                    }

                } else {
                    Toast.makeText(Register_Page.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    password.setError("Password does not match");
                    passwordConfirm.setError("Password does not match");
                }
            }
        });
    }

    public void onRegisterClick(View v) {

       /* String email = findViewById(R.id.emailRegister).toString();
        if(email.equals(null)) {
            Toast.makeText(this, "Email is invalid", Toast.LENGTH_SHORT);

        } else {
            Toast.makeText(this, "Email registered", Toast.LENGTH_SHORT);
        }
*/
    }
}