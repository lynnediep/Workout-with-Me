package com.example.zion.workoutwithme;

import android.provider.Settings;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.ArrayList;
import java.util.List;

public class Event {

    private String Title;
    private String Description;
    private String Time;
    private String Date;
    private String Location;
    private String Host;
    private ArrayList<String> Users = new ArrayList<String>();
    private int User_Count;
    private int Max_Count;

    public Event() {
    }

    public Event(String title, String description, String time, String date, String location, String host, int maxCount) {
        Title = title;
        Description = description;
        Time = time;
        Date = date;
        Location = location;
        Host = host;
        Users.add(host);
        User_Count = 1;
        Max_Count = maxCount;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String host) {
        Host = host;
    }

    public ArrayList<String> getUsers() {
        return Users;
    }

    public void setUsers(ArrayList<String> users) {
        Users = users;
    }

    public int getUser_Count() {
        return User_Count;
    }

    public void setUser_Count(int user_Count) {
        User_Count = user_Count;
    }

    public int getMax_Count() {
        return Max_Count;
    }

    public void setMax_Count(int max_Count) {
        Max_Count = max_Count;
    }
}
