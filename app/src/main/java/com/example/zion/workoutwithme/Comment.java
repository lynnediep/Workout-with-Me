package com.example.zion.workoutwithme;

import android.os.Parcelable;

public class Comment{

    private String Message, Time, Date, Name;

    public Comment(){
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Comment(String comment, String time, String date, String host){
        Message = comment;
        Time = time;
        Date = date;
        Name = host;

    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

}
