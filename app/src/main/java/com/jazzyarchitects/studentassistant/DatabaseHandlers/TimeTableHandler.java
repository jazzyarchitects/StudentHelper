package com.jazzyarchitects.studentassistant.DatabaseHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.Listeners.NoDayInAWeekException;

/**
 * Created by Jibin_ism on 19-Jul-15.
 */
public class TimeTableHandler extends SQLiteOpenHelper {

    String TAG = "TimeTableHandler";

    Context context;
    private static final String DATABASE_NAME = "TimeTableDatabase";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;

    SharedPreferences prefs;

    private static final String TIMETABLE_STRUCTURE_TABLE = "timeTableStructure";
    private static final String DAY_INDEX_COLUMN_NAME="dayIndex";
    private static int COLUMNS = 0;
    String CREATE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " + TIMETABLE_STRUCTURE_TABLE;


    public TimeTableHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public TimeTableHandler(Context context, Boolean restructureTimeTable) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        db = this.getWritableDatabase();
        if(tableExists(TIMETABLE_STRUCTURE_TABLE))
            displayTable();
        if (!tableExists(TIMETABLE_STRUCTURE_TABLE)) {
            retrieveTimeTableColumns();
            if (COLUMNS == 0) {
                throw new IndexOutOfBoundsException("Period Count not saved in database");
            }
            CREATE_STRUCTURE += " ("+DAY_INDEX_COLUMN_NAME+" TEXT, ";
            for (int i = 0; i < COLUMNS; i++) {
                CREATE_STRUCTURE+=getColumnName(i)+" TEXT";
                if(i!=COLUMNS-1){
                    CREATE_STRUCTURE+=", ";
                }
            }
            CREATE_STRUCTURE+=" );";
            Log.e(TAG,"Create table query: "+CREATE_STRUCTURE);
            db.execSQL(CREATE_STRUCTURE);
            insertDaysOfWeek();
        }
    }

    public TimeTableHandler(Context context) {
        this(context, false);
    }

    void retrieveTimeTableColumns() {
        prefs = context.getSharedPreferences(Constants.TimeTablePreferences.Preference, Context.MODE_PRIVATE);
        COLUMNS = prefs.getInt(Constants.TimeTablePreferences.PeriodCount, 0);
    }

    void insertDaysOfWeek(){
        int days=prefs.getInt(Constants.TimeTablePreferences.WorkingDaysInWeek, 0);
        if(days==0){
            throw new NoDayInAWeekException();
        }
        for (int j=0;j<days;j++){
            ContentValues values=new ContentValues();
            values.put(DAY_INDEX_COLUMN_NAME,j);
            for(int d=0;d<COLUMNS;d++){
                values.put(getColumnName(d),"0");
            }
            db.insertOrThrow(TIMETABLE_STRUCTURE_TABLE,null,values);
        }
    }

    public String getSubjectId(int dayIndex, int periodIndex){
        Cursor c=db.query(TIMETABLE_STRUCTURE_TABLE, new String[]{getColumnName(periodIndex)}, DAY_INDEX_COLUMN_NAME + "= ?", new String[]{String.valueOf(dayIndex)}, null, null, null);
        c.moveToFirst();
        String s=c.getString(0);
        c.close();
        return s;
    }

    public void insertSubject(int dayIndex, int periodIndex, String subjectId){
        Log.e(TAG,"Updating subject: "+subjectId+" in ("+String.valueOf(dayIndex)+","+getColumnName(periodIndex)+")");
        ContentValues contentValues=new ContentValues();
        contentValues.put(getColumnName(periodIndex),subjectId);
        db.update(TIMETABLE_STRUCTURE_TABLE, contentValues, DAY_INDEX_COLUMN_NAME + " = ?", new String[]{String.valueOf(dayIndex)});
    }

    public void removeSubject(int dayIndex, int periodIndex){
        insertSubject(dayIndex, periodIndex, "0");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO: update Database Code
    }

    private boolean tableExists(String TABLE) {
        try {
            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE name='"+TABLE+"'", null);
            Log.e(TAG, "Table exists? " + cursor.moveToFirst());
            Boolean b = cursor.moveToFirst();
            cursor.close();
            return b;
        }catch (SQLiteException e){
            e.printStackTrace();
            return false;
        }
    }

    private void displayTable(){
        Cursor c=db.query(TIMETABLE_STRUCTURE_TABLE,null,null,null,null,null,null);
        if(c.moveToFirst()){
            String s="";
            for(int i=0;i<c.getColumnCount();i++){
                s+=c.getColumnName(i)+" ";
            }
            Log.e(TAG,s);
            s="";
            do{
                for(int j=0;j<c.getColumnCount();j++){
                    s+=c.getString(j)+" ";
                }
                Log.e(TAG,s);
                s="";
            }while (c.moveToNext());
        }
        c.close();
    }

    private String getColumnName(int index){
        return "__"+index;
    }
}
