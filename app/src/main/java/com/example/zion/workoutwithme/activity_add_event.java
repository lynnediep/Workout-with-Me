package com.example.zion.workoutwithme;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;
import java.util.Locale;

import static java.util.logging.Logger.global;

public class activity_add_event extends AppCompatActivity {

    Button changeActivityButton, btnCancel;
    EditText etTitle, etLocation, etDescription, etMax;
    String count;
    TextView etDate, etTime;

    private static final String TAG = "activity_add_event";
    private TextView mEnter_Date, mEnter_Time;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    TimePickerDialog timePickerDialog;
        Calendar calendar;
        int currentHour;
        int currentMinute;
        String amPm;

    int x;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        checkAutomaticTime();

        // Current User
        Intent userInfo = getIntent();
        final String cruzID = userInfo.getStringExtra(activity_newsfeed.CURRENT_USER_ID);


        // Database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("Event");
        final DatabaseReference eventCount = database.getReference("event_count");


        etTitle = findViewById(R.id.enter_title);
        etLocation = findViewById(R.id.enter_location);
        etTime = findViewById(R.id.enter_time);
        etDate = findViewById(R.id.enter_date);
        etDescription = findViewById(R.id.enter_description);
        etMax = findViewById(R.id.enter_max);

        mEnter_Date = (TextView) findViewById(R.id.enter_date);
        mEnter_Time = (TextView) findViewById(R.id.enter_time);

        mEnter_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        activity_add_event.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = "";
                if(day < 10 && month < 10) {
                    date = "0" + month + "/0" + day + "/" + year;
                } else if(day < 10){
                    date = month + "/0" + day + "/" + year;
                } else if(month < 10) {
                    date = "0" + month + "/" + day + "/" + year;
                }

                Log.d(TAG, "onDateset: mm/dd/yyyy: " + month + "/" + day + "/" + year);
                mEnter_Date.setText(date);
            }
        };

        mEnter_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(activity_add_event.this,
                        android.R.style.Theme_DeviceDefault_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourofDay, int minutes) {
                        if (hourofDay >=12){
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        if(hourofDay > 12 && hourofDay < 24 && amPm.matches("PM")) {
                            hourofDay -= 12;
                        } else if(hourofDay == 0) {
                            hourofDay += 12;
                        }
                        mEnter_Time.setText(String.format("%02d:%02d", hourofDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

            }
        });


      //  btnCancel = (Button)findViewById(R.id.btncancel);
      //  btnCancel.setOnClickListener(new View.OnClickListener() {
      //      @Override
      //      public void onClick(View view) {
      //          Intent intent = new Intent(activity_add_event.this, activity_newsfeed.class);
      //          startActivity(intent);
      //      }
      //  });



        changeActivityButton = (Button)findViewById(R.id.add);
        changeActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (nullCheck()) {
                    return;
                }

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Makes the new event in the database and updates the event count
                        eventCount.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Make a timestamp (for deletion)
                                String time = etTime.getText().toString();
                                String hour = time.substring(0, 2);
                                String minute = time.substring(3,5);
                                String date = etDate.getText().toString();
                                String month = date.substring(0,2);
                                String day = date.substring(3,5);
                                String timestamp = month + day + "." + hour + minute;

                                count = dataSnapshot.child("count").getValue(String.class);

                                final Event event = new Event(
                                        etTitle.getText().toString(),
                                        etDescription.getText().toString(),
                                        etTime.getText().toString(),
                                        etDate.getText().toString(),
                                        etLocation.getText().toString(),
                                        cruzID,
                                        Integer.parseInt(etMax.getText().toString()),
                                        count,
                                        new Comment("Welcome to the comment section. Please be nice to others!",
                                                "00:00", "0/0/0000", "sammyslug"),
                                        timestamp
                                );

                                eventCount.child("count").setValue(Integer.toString(Integer.parseInt(count) + 1));
                                table_user.child("event" + count).setValue(event);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        //count = dataSnapshot.child("event_count").getValue(String.class);


                        //table_user.child("event_count").setValue(Integer.toString(Integer.parseInt(count) + 1));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(activity_add_event.this, activity_newsfeed.class);
                intent.putExtra("CURRENT_USER_ID", cruzID);
                startActivity(intent);
            }
        });
    }

    public boolean nullCheck() {
        int error = 0;
        // Detect if title is empty
        if (etTitle.getText().toString().matches("")) {
            etTitle.setError("Please make your title");
            error++;
        }
        // Detect if location is empty
        if (etLocation.getText().toString().matches("")) {
            etLocation.setError("Please make a location");
            error++;
        }
        // Detect if time is empty
        if (etTime.getText().toString().matches("")) {
            etLocation.setError("Please make a time");
            error++;
        }
        // Detect if date is empty
        if (etDate.getText().toString().matches("")) {
            etDate.setError("Please make a date");
            error++;
        }
        // Detect if description is empty
        if (etDescription.getText().toString().matches("")) {
            etDescription.setError("Please make a description");
            error++;
        }
        // Detect if max is empty
        if (etMax.getText().toString().matches("")) {
            etMax.setError("Please make a max search");
            error++;
        }
        if (error > 0) {
            Toast.makeText(activity_add_event.this, "Please fill out require fields", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
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
