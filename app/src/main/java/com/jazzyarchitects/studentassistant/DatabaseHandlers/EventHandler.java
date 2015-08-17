package com.jazzyarchitects.studentassistant.DatabaseHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jazzyarchitects.studentassistant.Models.Event;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Aanisha on 16-Aug-15.
 */
public class EventHandler extends SQLiteOpenHelper {

    String TAG = "EventDatabase";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Events";

    // Event table name
    private static final String TABLE_EVENTS = "EventDetails";

    // Event Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_EVENT_NAME = "event";
    private static final String KEY_SUBJECT_NAME = "subject";
    private static final String KEY_DATE = "date";
    private static final String KEY_TIME = "time";
    private static final String KEY_NOTES = "notes";

    String CREATE_EXPENSES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENTS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_EVENT_NAME + " TEXT," + KEY_SUBJECT_NAME + " TEXT,"
            + KEY_DATE + " DATE," + KEY_TIME + " TIME," + KEY_NOTES + " TEXT" + ");";

    SQLiteDatabase db;

    public EventHandler(Context context) {
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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);

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

    // Adding new Event
    public void addEvent(Event event) {

//        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_EXPENSES_TABLE);

        ContentValues values = new ContentValues();
        values.put(KEY_ID, getEventId());
        values.put(KEY_EVENT_NAME, event.getEvent());
        values.put(KEY_SUBJECT_NAME, event.getSubject());
        values.put(KEY_DATE, event.getDate());
        values.put(KEY_TIME, event.getTime());
        values.put(KEY_NOTES, event.getNotes());

        // Inserting Row
        db.insert(TABLE_EVENTS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All events
    public ArrayList<Event> getAllEvents() {
        ArrayList<Event> eventList = new ArrayList<Event>();

        String selectQuery = "SELECT  * FROM " + TABLE_EVENTS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    Event event = new Event();
                    event.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                    event.setSubject(cursor.getString(cursor.getColumnIndex(KEY_SUBJECT_NAME)));
                    event.setEvent(cursor.getString(cursor.getColumnIndex(KEY_EVENT_NAME)));
                    event.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
                    event.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));
                    event.setNotes(cursor.getString(cursor.getColumnIndex(KEY_NOTES)));

                    eventList.add(event);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        // return event list
        return eventList;
    }

    // Updating single event
    public int updateEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_EVENT_NAME, event.getEvent());
        values.put(KEY_SUBJECT_NAME, event.getSubject());
        values.put(KEY_DATE, event.getDate());
        values.put(KEY_TIME, event.getTime());
        values.put(KEY_NOTES, event.getNotes());

        // updating row
        return db.update(TABLE_EVENTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(event.getId())});
    }

    // Deleting single event
    public void deleteEvent(Event event) {
//        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EVENTS, KEY_ID + " = ?",
                new String[]{String.valueOf(event.getId())});
        db.close();
    }

    public Event findEventById(String eventId) {
        Event event = null;
        if (eventId.equalsIgnoreCase("0"))
            return null;
        Cursor c = db.query(TABLE_EVENTS, new String[]{KEY_ID, KEY_EVENT_NAME, KEY_SUBJECT_NAME, KEY_DATE,
                KEY_TIME, KEY_NOTES}, KEY_ID + "= ?", new String[]{eventId}, null, null, null);
        if (c.moveToFirst()) {
            event = new Event();
            event.setId(c.getString(0));
            event.setEvent(c.getString(1));
            event.setSubject(c.getString(2));
            event.setDate(c.getString(3));
            event.setTime(c.getString(4));
            event.setNotes(c.getString(5));
        }
        c.close();
        return event;
    }


    // Getting event Id
    private int getEventId() {
        Random random = new Random();
        int id;
        Cursor c;
        do {
            id = random.nextInt();
            if (id == 0)
                id++;
            c = db.rawQuery("SELECT * FROM " + TABLE_EVENTS + " WHERE " + KEY_ID + "=" + id, null);
        } while (c.moveToFirst());
        c.close();
        Log.e(TAG, "Event id: " + id);
        return id;
    }

    // Delete Database
    public void deleteDatabase() {
        this.getWritableDatabase().delete(TABLE_EVENTS,null,null);
    }


}

