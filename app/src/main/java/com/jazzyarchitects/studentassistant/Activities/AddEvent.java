package com.jazzyarchitects.studentassistant.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.EventHandler;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.SubjectDatabase;
import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.HelperClasses.TimingClass;
import com.jazzyarchitects.studentassistant.Models.Event;
import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.R;
import com.rey.material.widget.Button;
import com.rey.material.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;

public class AddEvent extends AppCompatActivity {

    Spinner eventType, subjectSpinner;
    EditText notes;
    TextView save, cancel, setDate, setTime;
    SubjectDatabase subjectDatabase;
    String[] events;
    String date, month, year, hour, minute;
    ArrayList<String> subjects;
    ArrayList<Subject> subjectList;
    Button dateButton, timeButton;
    EventHandler eventHandler;
    SpinnerAdapter spinnerAdapterEvents, spinnerAdapterSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        eventType = (Spinner) findViewById(R.id.eventType);
        subjectSpinner = (Spinner) findViewById(R.id.subject);
        notes = (EditText) findViewById(R.id.notes);
        save = (TextView) findViewById(R.id.save);
        dateButton = (Button) findViewById(R.id.setDate);
        timeButton = (Button) findViewById(R.id.setTime);
        setDate = (TextView) findViewById(R.id.date);
        setTime = (TextView) findViewById(R.id.time);
        cancel = (TextView) findViewById(R.id.cancel);
        events = new String[]{"Assignment", "Exam", "Class Text", "Extra Class"};

        //get Subject List from database
        subjectDatabase = new SubjectDatabase(this);
        subjectList = new ArrayList<>();
        subjectList = subjectDatabase.getAllSubject();
        subjects = new ArrayList<>();
        if (!subjectList.isEmpty()) {
            for (int i = 0; i < subjectList.size(); i++)
                subjects.add(subjectList.get(i).getSubject());
        } else {
            subjects.add("No Subjects Added");
        }

        spinnerAdapterEvents = Constants.spinnerAdapter(this, events);
        spinnerAdapterSubject = Constants.spinnerAdapter(this, subjects);

        eventType.setAdapter(spinnerAdapterEvents);
        subjectSpinner.setAdapter(spinnerAdapterSubject);

        //set Time
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int h, int m) {
                        hour= String.valueOf(h);
                        minute=String.valueOf(m);
                        setTime.setText(TimingClass.getTime(h, m, true));
                    }
                };
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog.newInstance(timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                        .show(getFragmentManager(), "TIME PICKER DIALOG");

            }
        });
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    //month returned here is zero indexed
                    public void onDateSet(DatePickerDialog datePickerDialog, int y, int m, int d) {
                        m=m+1;
                        date=String.valueOf(d);
                        month=String.valueOf(m);
                        year=String.valueOf(y);
                        setDate.setText(d+"/"+m+"/"+y);
                    }
                };
                Calendar calendar=Calendar.getInstance();
                DatePickerDialog.newInstance(dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)
                        ,calendar.get(Calendar.DATE))
                        .show(getFragmentManager(),"DATE PICKER DIALOG");
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //store to database
                String event=events[eventType.getSelectedItemPosition()];
                String subject=subjects.get(subjectSpinner.getSelectedItemPosition());
                String noteString=notes.getText().toString();
                if(noteString.isEmpty())
                    noteString="";
                Log.e("check", "save clicked");
                eventHandler = new EventHandler(getBaseContext());
                eventHandler.addEvent(new Event(event,subject,noteString,date,month,year,minute,noteString));
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("check","cancel clicked");
                finish();
            }
        });
    }

}
