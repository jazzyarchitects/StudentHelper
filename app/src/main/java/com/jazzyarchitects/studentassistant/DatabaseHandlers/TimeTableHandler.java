package com.jazzyarchitects.studentassistant.DatabaseHandlers;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.Models.ClassTime;

import java.util.ArrayList;

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
    private static int COLUMNS = 2;
    String CREATE_STRUCTURE = "CREATE TABLE IF NOT EXISTS " + TIMETABLE_STRUCTURE_TABLE;

    public TimeTableHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        db = this.getWritableDatabase();
        if(tableExists(TIMETABLE_STRUCTURE_TABLE))
            displayTable();

        boolean tableRestructured=context.getSharedPreferences(Constants.TimeTablePreferences.Preference,Context.MODE_PRIVATE).getBoolean(Constants.TimeTablePreferences.Restructured,false);
        if (!tableExists(TIMETABLE_STRUCTURE_TABLE) || tableRestructured) {
//            Log.e(TAG,"Table Restructuring Required: "+tableRestructured);
            db.execSQL("DROP TABLE IF EXISTS " + TIMETABLE_STRUCTURE_TABLE);
            context.getSharedPreferences(Constants.TimeTablePreferences.Preference,Context.MODE_PRIVATE).edit().putBoolean(Constants.TimeTablePreferences.Restructured,false).apply();
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

    void retrieveTimeTableColumns() {
        prefs = context.getSharedPreferences(Constants.TimeTablePreferences.Preference, Context.MODE_PRIVATE);
        COLUMNS = prefs.getInt(Constants.TimeTablePreferences.PeriodCount, 2);
    }

    void insertDaysOfWeek(){
        int days=prefs.getInt(Constants.TimeTablePreferences.WorkingDaysInWeek, 0);
        for (int j=0;j<days;j++){
            ContentValues values=new ContentValues();
            values.put(DAY_INDEX_COLUMN_NAME,j);
            for(int d=0;d<COLUMNS;d++){
                values.put(getColumnName(d),"0");
            }
            db.insertOrThrow(TIMETABLE_STRUCTURE_TABLE,null,values);
        }
        ContentValues contentValues=new ContentValues();
        contentValues.put(DAY_INDEX_COLUMN_NAME,"Timings");
        for(int d=0;d<COLUMNS;d++){
            contentValues.put(getColumnName(d),"0");
        }
        db.insertOrThrow(TIMETABLE_STRUCTURE_TABLE,null,contentValues);
    }

    public String getSubjectId(int dayIndex, int periodIndex){
        Cursor c=db.query(TIMETABLE_STRUCTURE_TABLE, new String[]{getColumnName(periodIndex)}, DAY_INDEX_COLUMN_NAME + "= ?", new String[]{String.valueOf(dayIndex)}, null, null, null);
        String s="0";
        if(c.moveToFirst())
            s=c.getString(0);
        c.close();
//        Log.e(TAG, "Requested Id: (" + dayIndex + "," + periodIndex + "): "+s);
        return s;
    }

    public void insertSubject(int dayIndex, int periodIndex, String subjectId){
        Log.e(TAG,"Updating subject: "+subjectId+" in ("+String.valueOf(dayIndex)+","+getColumnName(periodIndex)+")");
        ContentValues contentValues=new ContentValues();
        contentValues.put(getColumnName(periodIndex), subjectId);
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

    public void addPeriodTime(int periodIndex,ClassTime classTime){
        ContentValues contentValues=new ContentValues();
        contentValues.put(getColumnName(periodIndex-1),classTime.getHour()+","+classTime.getMinute());
        db.update(TIMETABLE_STRUCTURE_TABLE,contentValues,DAY_INDEX_COLUMN_NAME+"=?",new String[]{"Timings"});
    }

    public ClassTime getFirstClassTime(){
        Cursor c=db.query(TIMETABLE_STRUCTURE_TABLE, null, DAY_INDEX_COLUMN_NAME + "=?", new String[]{"Timings"}, null, null, null);
        c.moveToFirst();
        String s=c.getString(1);
        String[] times=s.split(",");
        c.close();
        return new ClassTime(Integer.parseInt(times[0]),Integer.parseInt(times[1]));
    }

    public ArrayList<ClassTime> getClassTimes(){
        ArrayList<ClassTime> classTimes=new ArrayList<>();
        Cursor c=db.query(TIMETABLE_STRUCTURE_TABLE,null,DAY_INDEX_COLUMN_NAME+"=?",new String[]{"Timings"},null,null,null);
        c.moveToFirst();
        for(int i=1;i<c.getColumnCount();i++){
            try {
                String s = c.getString(i);
                String[] times = s.split(",");
                try {
                    classTimes.add(new ClassTime(Integer.parseInt(times[0]), Integer.parseInt(times[1])));
                }catch (ArrayIndexOutOfBoundsException e){
                    classTimes.add(new ClassTime(0,0));
                }
            }catch (CursorIndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }
        c.close();
        return classTimes;
    }
}
