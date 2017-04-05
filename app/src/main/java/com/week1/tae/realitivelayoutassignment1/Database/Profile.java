package com.week1.tae.realitivelayoutassignment1.Database;

import android.graphics.Bitmap;

/**
 * Created by jamessmith on 24/03/2016.
 */
public class Profile {

    // private variables
    private int id;
    private String forename;
    private String surname;
    private String nationality;
    private String dob;
    private String gender;
    private Bitmap img;
    private long status;

    public Profile() {
    }

    // constructor
    public Profile(String firstname, String lastname, String Nationality, String DOB, String Gender, Bitmap image) {
        this.forename = firstname;
        this.surname = lastname;
        this.nationality = Nationality;
        this.dob = DOB;
        this.gender = Gender;
        this.img = image;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}