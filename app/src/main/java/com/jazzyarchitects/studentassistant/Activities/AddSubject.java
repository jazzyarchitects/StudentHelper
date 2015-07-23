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

    EditText subjectName, teacherName;
    View colorPicker;
    TextView txtSubjectName, txtTeacherName, discard, save;
    int subColor;
    SubjectDatabase subjectDatabase;
    CheckBox mon, tue, wed, thrus, fri, sat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        subjectName = (EditText) findViewById(R.id.editSubjectName);
        teacherName = (EditText) findViewById(R.id.editTeacherName);
        txtSubjectName = (TextView) findViewById(R.id.textSubjectName);
        txtTeacherName = (TextView) findViewById(R.id.textTeacherName);
        mon=(CheckBox)findViewById(R.id.checkMon);
        tue=(CheckBox)findViewById(R.id.checkTues);
        wed=(CheckBox)findViewById(R.id.checkWed);
        thrus=(CheckBox)findViewById(R.id.checkThrus);
        fri=(CheckBox)findViewById(R.id.checkFri);
        sat=(CheckBox)findViewById(R.id.checkSat);
        discard = (TextView) findViewById(R.id.discard);
        save = (TextView) findViewById(R.id.save);
        colorPicker = findViewById(R.id.colorPicker);
        subColor = getResources().getColor(R.color.white);

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

        textViewVisibility(subjectName, txtSubjectName, "Subject Name");
        textViewVisibility(teacherName, txtTeacherName, "Teacher Name");

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
                if (subject.isEmpty()) {
                    subjectName.setError("Enter Subject Name");
                    return;
                }
                if (teacher.isEmpty()) {
                    teacher = "";
                }
                subjectDatabase.addSubject(new Subject(subject, teacher, subColor));

                ArrayList<Subject> subjectList = subjectDatabase.getAllSubject();
                for (int i = 0; i < subjectList.size(); i++) {
                    Log.e("database", subjectList.get(i).getSubject());
                }
                finish();
            }
        });

    }

    public void textViewVisibility(final EditText editText, final TextView textView, final String s) {
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    textView.setVisibility(View.VISIBLE);
                    editText.setHint("");
                } else {
                    if (editText.getText().toString().isEmpty()) {
                        textView.setVisibility(View.GONE);
                        editText.setHint(s);
                    } else {
                        textView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void checkIfChecked(CheckBox checkBox){
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
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
