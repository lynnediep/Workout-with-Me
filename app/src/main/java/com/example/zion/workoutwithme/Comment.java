package com.example.zion.workoutwithme;

public class Comment {

    public String Message, Time, Date;

    public Comment(){
    }

    public Comment(String comment, String time, String date){
        Message = comment;
        Time = time;
        Date = date;
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
