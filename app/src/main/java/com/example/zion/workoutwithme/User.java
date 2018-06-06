package com.example.zion.workoutwithme;

public class User {

    private String Name;
    private String Password;
    private String Bio;
    private String Year;
    private String Email;
    private String Major;
    private String Interests;
    private String ProfilePic;


    public User() {
    }

    public User(String name, String password, String email) {
        this.Name = name;
        this.Password = password;
        this.Email = email;
    }

    public User(String name, String password, String bio, String year, String email, String major, String interests) {
        this.Name = name;
        this.Password = password;
        this.Bio = bio;
        this.Year = year;
        this.Email = email;
        this.Major = major;
        this.Interests = interests;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMajor() {
        return Major;
    }

    public void setMajor(String major) {
        Major = major;
    }

    public String getInterests() {
        return Interests;
    }

    public void setInterests(String interests) {
        this.Interests = interests;
    }

    public String getProfilePic() { return ProfilePic; }

    public void setProfilePic(String profilePic) { this.ProfilePic = profilePic; }
}
