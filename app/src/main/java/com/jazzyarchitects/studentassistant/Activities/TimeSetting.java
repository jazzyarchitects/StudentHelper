package com.jazzyarchitects.studentassistant.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.R;

public class TimeSetting extends AppCompatActivity {

    Toolbar toolbar;
    EditText dayCountIndicator, classCountIndicator, classDurationIndicator;
    SharedPreferences sharedPreferences;
    int initialDayCount, initialClassCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_setting);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(Constants.TimeTablePreferences.Preference, MODE_PRIVATE);
        initialDayCount = sharedPreferences.getInt(Constants.TimeTablePreferences.WorkingDaysInWeek, 5);
        initialClassCount = sharedPreferences.getInt(Constants.TimeTablePreferences.PeriodCount, 8);

        dayCountIndicator = (EditText) findViewById(R.id.dayCount);
        classCountIndicator = (EditText) findViewById(R.id.classCount);
        classDurationIndicator = (EditText) findViewById(R.id.classDuration);

        dayCountIndicator.setText(String.valueOf(sharedPreferences.getInt(Constants.TimeTablePreferences.WorkingDaysInWeek, 1)));
        classCountIndicator.setText(String.valueOf(sharedPreferences.getInt(Constants.TimeTablePreferences.PeriodCount, 1)));
        classDurationIndicator.setText(String.valueOf(sharedPreferences.getInt(Constants.TimeTablePreferences.ClassDuration, 50)));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.save) {
            savePreferences();
        }

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    public void savePreferences() {
        final int finalDayCount = Integer.parseInt(dayCountIndicator.getText().toString()),
                finalClassCount = Integer.parseInt(classCountIndicator.getText().toString());

        if(finalDayCount==0){
            dayCountIndicator.setError("Cannot be 0");
            return;
        }else if(finalDayCount>7){
            dayCountIndicator.setError("Cannot be greater than 7");
            return;
        }
        if(finalClassCount==0){
            classCountIndicator.setError("Cannot be 0");
            return;
        }

        if (sharedPreferences.getBoolean(Constants.TimeTablePreferences.PreferencesSet, false) &&
                (finalClassCount != initialClassCount || finalDayCount != initialDayCount)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Restructuring");
            builder.setMessage(getResources().getString(R.string.tableRestructuring_Alert));
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Constants.TimeTablePreferences.ClassDuration, Integer.parseInt(classDurationIndicator.getText().toString()));
                    editor.apply();
                }
            });
            builder.setPositiveButton("Yes, Delete Schedule", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(Constants.TimeTablePreferences.PeriodCount, finalClassCount);
                    editor.putInt(Constants.TimeTablePreferences.WorkingDaysInWeek, finalDayCount);
                    editor.putInt(Constants.TimeTablePreferences.ClassDuration, Integer.parseInt(classDurationIndicator.getText().toString()));
                    editor.putBoolean(Constants.TimeTablePreferences.Restructured,true);
                    editor.apply();
                    startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                    finish();
                }
            });
            builder.show();
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.TimeTablePreferences.PeriodCount, finalClassCount);
        editor.putInt(Constants.TimeTablePreferences.WorkingDaysInWeek, finalDayCount);
        editor.putInt(Constants.TimeTablePreferences.ClassDuration, Integer.parseInt(classDurationIndicator.getText().toString()));
        editor.putBoolean(Constants.TimeTablePreferences.PreferencesSet,true);
        editor.apply();

        startActivity(new Intent(this, HomeScreen.class));
        finish();
    }
}
