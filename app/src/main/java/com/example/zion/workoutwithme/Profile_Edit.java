package com.example.zion.workoutwithme;

// Profile Edit Java
// Lynne Diep
// Chuong Truong

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;

public class Profile_Edit extends AppCompatActivity implements OnClickListener {

    private Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__edit);
        button2 = (Button) findViewById(R.id.button2);


    }


    private void edit_profile() {


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonRegister:
                edit_profile();
                break;
        }


    }

}