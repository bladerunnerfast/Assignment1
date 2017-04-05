package com.week1.tae.realitivelayoutassignment1.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.ByteBuffer;

/**
 * Created by jamessmith on 24/03/2016.
 */
public class Handler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";

    // Contacts table name
    private static final String profile= "profile";

    // Contacts Table Columns names
    private static final String id = "id";
    private static final String KEY_NAME = "forename";
    private static final String lastname = "surname";
    private static final String nationality = "nationality";
    private static final String dob = "dob";
    private static final String gender = "gender";
    private static final String image = "image";


    public Handler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + profile + "("
                + id + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + lastname + " TEXT," + nationality + " TEXT," + dob + " TEXT," + gender +" TEXT,"+ image + " BLOB"+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + profile);
        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void Add_Contact(Profile userProfile) {
        //Convert image into bytes before adding to database.
        int bytes = userProfile.getImg().getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        userProfile.getImg().copyPixelsToBuffer(buffer);
        byte[] array = buffer.array();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //Build data block for storage.
        values.put(KEY_NAME, userProfile.getForename());
        values.put(lastname, userProfile.getSurname());
        values.put(nationality, userProfile.getNationality());
        values.put(dob, userProfile.getDob());
        values.put(gender, userProfile.getGender());
        values.put(image, array);
        //LONG -1 for error, 0 or greater if a row has been added.
       userProfile.setStatus(db.insert(profile, null, values));
        db.close(); // Closing database connection
    }
}