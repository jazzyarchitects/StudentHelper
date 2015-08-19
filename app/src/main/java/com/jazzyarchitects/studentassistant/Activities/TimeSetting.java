package com.jazzyarchitects.studentassistant.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jazzyarchitects.studentassistant.Adapters.TimeSettingRowAdapter;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.TimeTableHandler;
import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.Models.ClassTime;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;

public class TimeSetting extends AppCompatActivity {

    Toolbar toolbar;
    EditText dayCountIndicator, classCountIndicator, classDurationIndicator;
    SharedPreferences sharedPreferences;
    RecyclerView timeSettingsView;
    Button saveSetting;
    int initialDayCount, initialClassCount;

    private Boolean DELETE_DATABASE_ON_COUNT_CHANGE = true;


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
        timeSettingsView = (RecyclerView) findViewById(R.id.classTimeSettingLayout);
        saveSetting = (Button) findViewById(R.id.saveButton);

        updateTimeViews(true);

        saveSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTimeViews(false);
            }
        });

        dayCountIndicator.setText(String.valueOf(sharedPreferences.getInt(Constants.TimeTablePreferences.WorkingDaysInWeek, 1)));
        classCountIndicator.setText(String.valueOf(sharedPreferences.getInt(Constants.TimeTablePreferences.PeriodCount, 1)));
        classDurationIndicator.setText(String.valueOf(sharedPreferences.getInt(Constants.TimeTablePreferences.ClassDuration, 1)));


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
            startHomeScreenActivity();
        }

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }


    void updateTimeViews(Boolean firstTime) {
        if (!firstTime)
            savePreferences();

        int finalClassCount = sharedPreferences.getInt(Constants.TimeTablePreferences.PeriodCount, 1);

        Log.e("TimeSetting", "Updating time views: " + finalClassCount);
        timeSettingsView.setLayoutManager(new LinearLayoutManager(this));
        TimeTableHandler handler = new TimeTableHandler(this);
        ArrayList<ClassTime> classTimes = handler.getClassTimes();
        TimeSettingRowAdapter adapter = new TimeSettingRowAdapter(this, finalClassCount, classTimes, getFragmentManager());
        handler.close();
        timeSettingsView.setAdapter(adapter);
    }




    public void savePreferences() {
        final int finalDayCount = Integer.parseInt(dayCountIndicator.getText().toString()),
                finalClassCount = Integer.parseInt(classCountIndicator.getText().toString());

        if (finalDayCount == 0) {
            dayCountIndicator.setError("Cannot be 0");
            return;
        } else if (finalDayCount > 7) {
            dayCountIndicator.setError("Cannot be greater than 7");
            return;
        }
        if (finalClassCount == 0) {
            classCountIndicator.setError("Cannot be 0");
            return;
        }

        if (sharedPreferences.getBoolean(Constants.TimeTablePreferences.PreferencesSet, false) &&
                (finalClassCount != initialClassCount || finalDayCount != initialDayCount) && DELETE_DATABASE_ON_COUNT_CHANGE) {
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
                    editor.putBoolean(Constants.TimeTablePreferences.Restructured, true);
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
        if (!sharedPreferences.getBoolean(Constants.TimeTablePreferences.PreferencesSet, false))
            editor.putBoolean(Constants.TimeTablePreferences.Restructured, true);
        editor.putInt(Constants.TimeTablePreferences.ClassDuration, Integer.parseInt(classDurationIndicator.getText().toString()));
        editor.putBoolean(Constants.TimeTablePreferences.PreferencesSet, true);
        editor.apply();
    }

    void startHomeScreenActivity() {
        startActivity(new Intent(this, HomeScreen.class));
        finish();
    }
}
