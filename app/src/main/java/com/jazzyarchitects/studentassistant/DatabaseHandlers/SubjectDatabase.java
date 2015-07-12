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
import java.util.List;

public class SubjectDatabase extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "Subjects";

    // subject table name
    private static final String TABLE_SUBJECTS = "SubjectDetails";

    // subject Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_SUBJECT_NAME = "category";
    private static final String KEY_SUBJECT_COLOR = "date";
    private static final String KEY_TEACHER_NAME = "amount";

    public SubjectDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_SUBJECTS + " IF NOT EXISTS("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SUBJECT_NAME + " TEXT,"
                + KEY_SUBJECT_COLOR + " TEXT," + KEY_TEACHER_NAME + " TEXT" + ")";
        Log.i("SQL", CREATE_EXPENSES_TABLE);
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

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new subject
    void addSubject(Subject subject) {

        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_SUBJECTS + " IF NOT EXISTS("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SUBJECT_NAME + " TEXT,"
                + KEY_SUBJECT_COLOR + " TEXT," + KEY_TEACHER_NAME + " TEXT" + ")";

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(CREATE_EXPENSES_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT_NAME, subject.getSubject());
        values.put(KEY_SUBJECT_COLOR, subject.getColor());
        values.put(KEY_TEACHER_NAME, subject.getTeacher());

        Log.e("Data", subject.getSubject() + subject.getSubject());
        // Inserting Row
        db.insert(TABLE_SUBJECTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All subject
    public List<Subject> getAllSubject() {
        List<Subject> subjectList = new ArrayList<Subject>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SUBJECTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Subject subject = new Subject();
                subject.setSubject(cursor.getString(0));
                subject.setColor(Integer.parseInt(cursor.getString(1)));
                subject.setTeacher(cursor.getString(2));
                // Adding contact to list
                subjectList.add(subject);
            } while (cursor.moveToNext());
        }

        // return subject list
        return subjectList;
    }

    // Updating single subject
    public int updateSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SUBJECT_NAME, subject.getSubject());
        values.put(KEY_SUBJECT_COLOR, subject.getColor());
        values.put(KEY_TEACHER_NAME, subject.getTeacher());

        // updating row
        return db.update(TABLE_SUBJECTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(subject.getId())});
    }

    // Deleting single subject
    public void deleteSubject(Subject subject) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SUBJECTS, KEY_ID + " = ?",
                new String[]{String.valueOf(subject.getId())});
        db.close();
    }


    // Getting subject Count
    public int getSubjectCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SUBJECTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    // Delete Database
    public void deleteDatabase() {
        this.getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
    }


}
