package com.jazzyarchitects.studentassistant.Activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.DatabaseHandlers.SubjectDatabase;
import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.R;
import com.rey.material.widget.Spinner;

import java.util.ArrayList;

public class AddEvent extends AppCompatActivity {

    Spinner eventType, subject;
    EditText notes;
    TextView save, cancel;
    SubjectDatabase subjectDatabase;
    String[] events;
    Toolbar toolbar;
    ArrayList<String> subjects;
    ArrayList<Subject> subjectList;
    SpinnerAdapter spinnerAdapterEvents, spinnerAdapterSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Event");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        eventType = (Spinner) findViewById(R.id.eventType);
        subject = (Spinner) findViewById(R.id.subject);
        notes = (EditText) findViewById(R.id.notes);
        save = (TextView) findViewById(R.id.save);
        cancel = (TextView) findViewById(R.id.cancel);
        events = new String[]{"Assignment", "Exam", "Class Text", "Extra Class"};

        //get Subject List from database
        subjectDatabase = new SubjectDatabase(this);
        subjectList = new ArrayList<>();
        subjectList = subjectDatabase.getAllSubject();
        subjects=new ArrayList<>();
        if (!subjectList.isEmpty()) {
            for (int i = 0; i < subjectList.size(); i++)
                subjects.add(subjectList.get(i).getSubject());
        } else {
            subjects.add("No Subjects Added");
        }

        spinnerAdapterEvents = Constants.spinnerAdapter(this, events);
        spinnerAdapterSubject = Constants.spinnerAdapter(this, subjects);

        eventType.setAdapter(spinnerAdapterEvents);
        eventType.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner spinner, View view, int position, long l) {
                TextView label = (TextView) view.findViewById(android.R.id.text1);
                label.setText(events[position]);
            }
        });
        eventType.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner spinner, View view, int i, long l) {
                TextView label = (TextView) view.findViewById(android.R.id.text1);
                label.setText(events[i]);
                return false;
            }
        });
        subject.setAdapter(spinnerAdapterSubject);
        subject.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner spinner, View view, int position, long l) {
                TextView label = (TextView) view.findViewById(android.R.id.text1);
                label.setText(subjects.get(position));
            }
        });
        subject.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner spinner, View view, int i, long l) {
                TextView label = (TextView) view.findViewById(android.R.id.text1);
                label.setText(subjects.get(i));
                return false;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //store to database
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

        if(id==android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

//        if (id == R.id.save) {
//            savePreferences();
//            startHomeScreenActivity();
//        }

        //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.slide_right_show, R.anim.slide_right_show);
    }
}
