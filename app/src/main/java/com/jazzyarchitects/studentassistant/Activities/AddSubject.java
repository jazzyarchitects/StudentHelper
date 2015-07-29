package com.jazzyarchitects.studentassistant.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jazzyarchitects.studentassistant.CustomViews.ColorPickerDialog;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.SubjectDatabase;
import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;

public class AddSubject extends AppCompatActivity {

    EditText subjectName, teacherName, shortName, notes, place;
    View colorPicker;
    TextView discard, save;
    int subColor;
    String days="0000000";
    SubjectDatabase subjectDatabase;
    CheckBox mon, tue, wed, thurs, fri, sat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        subjectName = (EditText) findViewById(R.id.editSubjectName);
        teacherName = (EditText) findViewById(R.id.editTeacherName);
        shortName=(EditText)findViewById(R.id.editShortName);
        place=(EditText)findViewById(R.id.place);
        notes=(EditText)findViewById(R.id.notes);
        mon=(CheckBox)findViewById(R.id.checkMon);
        tue=(CheckBox)findViewById(R.id.checkTues);
        wed=(CheckBox)findViewById(R.id.checkWed);
        thurs=(CheckBox)findViewById(R.id.checkThurs);
        fri=(CheckBox)findViewById(R.id.checkFri);
        sat=(CheckBox)findViewById(R.id.checkSat);
        discard = (TextView) findViewById(R.id.discard);
        save = (TextView) findViewById(R.id.save);
        colorPicker = findViewById(R.id.colorPicker);
        subColor = getResources().getColor(R.color.white);

        checkIfChecked(mon,"monday");
        checkIfChecked(tue,"tuesday");
        checkIfChecked(wed,"wednesday");
        checkIfChecked(thurs,"thursday");
        checkIfChecked(fri,"friday");
        checkIfChecked(sat,"saturday");

        subjectDatabase = new SubjectDatabase(this);

        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(AddSubject.this, getResources().getColor(R.color.white),
                        new ColorPickerDialog.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int color) {
                                Toast.makeText(getBaseContext(), String.valueOf(color), Toast.LENGTH_LONG).show();
                                colorPicker.setBackgroundColor(color);
                                subColor = color;
                            }
                        });
                colorPickerDialog.show();
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String subject = subjectName.getText().toString();
                String teacher = teacherName.getText().toString();
                String noteSubject=notes.getText().toString();
                String shortSubject=shortName.getText().toString();
                String subjectPlace=place.getText().toString();
                if (subject.isEmpty()) {
                    subjectName.setError("Enter Subject Name");
                    return;
                }
                if (teacher.isEmpty()) {
                    teacher = "";
                }
                if (noteSubject.isEmpty()) {
                    noteSubject= "";
                }
                subjectDatabase.addSubject(new Subject(subject,shortSubject, teacher, subColor,days,noteSubject,subjectPlace));
                Log.e("days",days);

                ArrayList<Subject> subjectList = subjectDatabase.getAllSubject();
                for (int i = 0; i < subjectList.size(); i++) {
                    Log.e("database", subjectList.get(i).getDays());
                }
                finish();
            }
        });

    }

    public void checkIfChecked(CheckBox checkBox, final String name){
        final int[] x = {0};
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (name){
                    case "sunday": x[0] =0;
                        break;
                    case "monday": x[0]=1;
                        break;
                    case "tuesday": x[0] =2;
                        break;
                    case "wednesday": x[0]=3;
                        break;
                    case "thursday": x[0] =4;
                        break;
                    case "friday": x[0]=5;
                        break;
                    case "saturday":  x[0]=6;
                        break;
                }
                if (isChecked){
                    days=replace(days,x[0],'1');
                }
                else{
                    days=replace(days,x[0],'0');
                }
            }
        });
    }

    public String replace(String str, int index, char replace){
        char[] chars = str.toCharArray();
        chars[index] = replace;
        return String.valueOf(chars);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_subject, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
