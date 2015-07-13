package com.jazzyarchitects.studentassistant.Activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.CustomViews.ColorPickerDialog;
import com.jazzyarchitects.studentassistant.R;

import org.w3c.dom.Text;

public class AddSubject extends AppCompatActivity {

    EditText subjectName, teacherName;
    View colorPicker;
    TextView txtSubjectName,txtTeacherName, discard, save;
    int color;
    CheckBox mon, tue, wed, thrus, fri, sat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);

        subjectName=(EditText)findViewById(R.id.editSubjectName);
        teacherName=(EditText)findViewById(R.id.editTeacherName);
        txtSubjectName=(TextView)findViewById(R.id.textSubjectName);
        txtTeacherName=(TextView)findViewById(R.id.textTeacherName);
        discard=(TextView)findViewById(R.id.discard);
        save=(TextView)findViewById(R.id.save);
        colorPicker=findViewById(R.id.colorPicker);

        colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(AddSubject.this, getResources().getColor(R.color.white),
                        new ColorPickerDialog.OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int color) {
                                colorPicker.setBackgroundColor(color);
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
                finish();
            }
        });

    }

    public void textViewVisibility(final EditText editText, final TextView textView, final String s){
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    textView.setVisibility(View.VISIBLE);
                    editText.setHint("");
                }
                else{
                    if(editText.getText().toString().isEmpty()){
                        textView.setVisibility(View.GONE);
                        editText.setHint(s);
                    }
                    else{
                        textView.setVisibility(View.VISIBLE);
                    }
                }
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
