package com.jazzyarchitects.studentassistant.DatabaseHandlers;

/**
 * Created by Aanisha on 25-Mar-15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jazzyarchitects.studentassistant.Models.Subject;

import java.util.ArrayList;
import java.util.Random;

public class SubjectDatabase extends SQLiteOpenHelper {

    String TAG = "SubjectDatabase";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Subjects";

    // subject table name
    private static final String TABLE_SUBJECTS = "SubjectDetails";

    // subject Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_SUBJECT_NAME = "subject";
    private static final String KEY_SHORT_NAME = "shortname";
    private static final String KEY_SUBJECT_COLOR = "color";
    private static final String KEY_TEACHER_NAME = "teacher";
    private static final String KEY_DAYS = "days";
    private static final String KEY_PLACE = "place";
    private static final String KEY_NOTES = "notes";

    String CREATE_EXPENSES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_SUBJECTS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SUBJECT_NAME + " TEXT," + KEY_SHORT_NAME + " TEXT,"
            + KEY_SUBJECT_COLOR + " INTEGER," + KEY_DAYS + " TEXT," + KEY_TEACHER_NAME + " TEXT," + KEY_PLACE + " TEXT,"
            + KEY_NOTES + " TEXT" + ");";

    SQLiteDatabase db;

    public SubjectDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("SQL", CREATE_EXPENSES_TABLE);
        db.execSQL(CREATE_EXPENSES_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);

        // Create tables again
        onCreate(db);
    }

    // Downgrading Database
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new subject
    public void addSubject(Subject subject) {

//        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_EXPENSES_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_ID, getSubjectId());
        values.put(KEY_SUBJECT_NAME, subject.getSubject());
        values.put(KEY_SHORT_NAME, subject.getShortSubject());
        values.put(KEY_SUBJECT_COLOR, subject.getColor());
        values.put(KEY_DAYS, subject.getDays());
        values.put(KEY_TEACHER_NAME, subject.getTeacher());
        values.put(KEY_PLACE, subject.getPlace());
        values.put(KEY_NOTES, subject.getNotes());

        // Inserting Row
        db.insert(TABLE_SUBJECTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All subject
    public ArrayList<Subject> getAllSubject() {
        ArrayList<Subject> subjectList = new ArrayList<Subject>();

        String selectQuery = "SELECT  * FROM " + TABLE_SUBJECTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Subject subject = new Subject();
                    subject.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    subject.setSubject(cursor.getString(cursor.getColumnIndex(KEY_SUBJECT_NAME)));
                    subject.setShortSubject(cursor.getString(cursor.getColumnIndex(KEY_SHORT_NAME)));
                    subject.setColor(cursor.getInt(cursor.getColumnIndex(KEY_SUBJECT_COLOR)));
                    subject.setDays(cursor.getString(cursor.getColumnIndex(KEY_DAYS)));
                    subject.setTeacher(cursor.getString(cursor.getColumnIndex(KEY_TEACHER_NAME)));
                    subject.setPlace(cursor.getString(cursor.getColumnIndex(KEY_PLACE)));
                    subject.setNotes(cursor.getString(cursor.getColumnIndex(KEY_NOTES)));

                    subjectList.add(subject);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        // return subject list
        return subjectList;
    }

    // Updating single subject
    public int updateSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT_NAME, subject.getSubject());
        values.put(KEY_SHORT_NAME, subject.getShortSubject());
        values.put(KEY_SUBJECT_COLOR, subject.getColor());
        values.put(KEY_TEACHER_NAME, subject.getTeacher());
        values.put(KEY_DAYS, subject.getDays());
        values.put(KEY_PLACE, subject.getPlace());
        values.put(KEY_NOTES, subject.getNotes());

        // updating row
        return db.update(TABLE_SUBJECTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(subject.getId())});
    }

    // Deleting single subject
    public void deleteSubject(Subject subject) {
//        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUBJECTS, KEY_ID + " = ?",
                new String[]{String.valueOf(subject.getId())});
        db.close();
    }

    public Subject findSubjectById(String subjectId) {
//        Log.e(TAG,"Requested Subject has id="+subjectId);
        Subject subject = null;
        if (subjectId.equalsIgnoreCase("0"))
            return null;
        Cursor c = db.query(TABLE_SUBJECTS, new String[]{KEY_ID, KEY_SUBJECT_NAME, KEY_SHORT_NAME, KEY_SUBJECT_COLOR,
                KEY_DAYS, KEY_TEACHER_NAME, KEY_PLACE, KEY_NOTES}, KEY_ID + "= ?", new String[]{subjectId}, null, null, null);
        if (c.moveToFirst()) {
            subject = new Subject();
            subject.setId(c.getString(0));
            subject.setSubject(c.getString(1));
            subject.setShortSubject(c.getString(2));
            subject.setColor(Integer.parseInt(c.getString(3)));
            subject.setDays(c.getString(4));
            subject.setTeacher(c.getString(5));
            subject.setPlace(c.getString(6));
            subject.setNotes(c.getString(7));
        }
        c.close();
        return subject;
    }


    // Getting subject Count
    private int getSubjectId() {
        Random random = new Random();
        int id;
        Cursor c;
        do {
            id = random.nextInt();
            if (id == 0)
                id++;
            c = db.rawQuery("SELECT * FROM " + TABLE_SUBJECTS + " WHERE " + KEY_ID + "=" + id, null);
        } while (c.moveToFirst());
        c.close();
        Log.e(TAG, "Subject id: " + id);
        return id;
    }

    // Delete Database
    public void deleteDatabase() {
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
    }


}
