package com.jazzyarchitects.studentassistant.CustomViews;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.R;

/**
 * Created by Jibin_ism on 10-Jul-15.
 */
public class SubjectDetailDialog extends AppCompatDialog {

    Subject subject = null;
    TextView subjectName, professor, note, bunkedClasses, attendancePercentage, assignmentCount;
    View colorStrip;

    public SubjectDetailDialog(Context context) {
        super(context);
    }

    public SubjectDetailDialog(Context context, Subject subject) {
        super(context);
        this.subject = subject;
    }

    public SubjectDetailDialog(Context context, int theme) {
        super(context, theme);
    }

    protected SubjectDetailDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        setContentView(R.layout.subject_detail_layout);

        subjectName = (TextView) findViewById(R.id.subjectName);
        professor = (TextView) findViewById(R.id.professorName);
        note = (TextView) findViewById(R.id.notes);
        bunkedClasses = (TextView) findViewById(R.id.bunkedClasses);
        attendancePercentage = (TextView) findViewById(R.id.attendancePercentage);
        assignmentCount = (TextView) findViewById(R.id.assignmentCount);
        colorStrip = findViewById(R.id.subjectColor);


        setColor(subject.getColor());
        subjectName.setText(subject.getSubject().toUpperCase());
        professor.setText(Constants.getProfessorNameText(subject.getProfessor()));
        bunkedClasses.setText(Constants.getBunkedClassText(subject.getBunkedClasses()));
        attendancePercentage.setText(Constants.getAttendancePercentageText(subject.getAttendancePercentage()));
        if(subject.getNotes().isEmpty()){
            note.setVisibility(View.GONE);
        }else {
            note.setText(subject.getNotes());
        }
        assignmentCount.setText(Constants.getAssignmentCountText(subject.getAssignmentCount()));

    }

    private void setColor(int color) {
        colorStrip.setBackgroundColor(color);
        subjectName.setBackgroundColor(color);

        if (Constants.isColorDark(color)) {
            subjectName.setTextColor(Color.WHITE);
        }
    }
}
