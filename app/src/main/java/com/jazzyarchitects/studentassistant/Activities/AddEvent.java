package com.jazzyarchitects.studentassistant.Activities;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    Spinner eventType,subject;
    EditText notes;
    TextView save, cancel;
    SubjectDatabase subjectDatabase;
    String[] events,subjects;
    ArrayList<Subject> subjectList;
    SpinnerAdapter spinnerAdapterEvents,spinnerAdapterSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        eventType = (Spinner) findViewById(R.id.eventType);
        subject=(Spinner)findViewById(R.id.subject);
        notes = (EditText) findViewById(R.id.notes);
        save=(TextView)findViewById(R.id.save);
        cancel=(TextView)findViewById(R.id.cancel);
        events = new String[]{"Assignment", "Exam", "Class Text", "Extra Class"};

        //get Subject List from database
        subjectDatabase = new SubjectDatabase(this);
        subjectList = new ArrayList<>();
        subjectList = subjectDatabase.getAllSubject();
        if(!subjectList.isEmpty())
        {
            subjects=new String[]{};
            for(int i=0;i<subjectList.size();i++)
                subjects[i]=subjectList.get(i).getSubject();
        }
        else {
            subjects[0]="No Subjects Added";
        }

        spinnerAdapterEvents= Constants.spinnerAdapter(this,events);
        spinnerAdapterSubject=Constants.spinnerAdapter(this,subjects);

        eventType.setAdapter(spinnerAdapterEvents);
        subject.setAdapter(spinnerAdapterSubject);

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_add_event, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
