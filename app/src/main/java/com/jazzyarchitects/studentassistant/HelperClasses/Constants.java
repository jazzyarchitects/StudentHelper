package com.jazzyarchitects.studentassistant.HelperClasses;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.Models.ViewTag;
import com.jazzyarchitects.studentassistant.R;

/**
 * Created by Jibin_ism on 08-Jul-15.
 */
public class Constants {

    public static Spanned getBunkedClassText(int bunkedClasses) {
        return Html.fromHtml("Bunked Classes: <b>" + bunkedClasses + "</b>");
    }

    public static Spanned getAttendancePercentageText(double percentage) {
        return Html.fromHtml("Attendance: <b>" + percentage + "%</b>");
    }

    public static Spanned getProfessorNameText(String name) {
        return Html.fromHtml("Professor: <b>" + name + "</b>");
    }

    public static Spanned getAssignmentCountText(int assignmentCount) {
        return Html.fromHtml("Assignments Pending: <b>" + assignmentCount + "</b>");
    }

    public static class TimeTablePreferences {
        public static final String Preference = "timeTablePreferences";
        public static final String PeriodCount = "columns";
        public static final String WorkingDaysInWeek = "workingDays";
    }


    public static boolean isColorDark(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return (hsv[0] > 345.0) ||
                (hsv[0] < 15.0) ||
                (hsv[0] > 215.0 && hsv[0] < 275.0) ||
                (hsv[2] < 0.5);
    }

    public static View getSubjectView(Context context, Subject subject,int dayIndex, int periodIndex) {
        View v = View.inflate(context, R.layout.table_cell_subject, null);
        v.setTag(subject);

        TextView textView;
        RelativeLayout cell, innerCell;
        ImageView assignmentIcon;

        textView = (TextView) v.findViewById(R.id.subjectName);
        cell = (RelativeLayout) v;
        assignmentIcon = (ImageView) v.findViewById(R.id.assignmentIcon);
        innerCell = (RelativeLayout) v.findViewById(R.id.cell);

        innerCell.setTag(new ViewTag(subject,dayIndex,periodIndex));

        if (subject == null) {
            textView.setText("____");
            assignmentIcon.setVisibility(View.GONE);
            innerCell.setBackgroundColor(Color.WHITE);
            textView.setTextColor(Color.BLACK);
//                Log.v(TAG,"Null subject in ("+dayIndex+","+periodIndex+")");
        } else {
//                Log.v(TAG, "Setting subject: " + subject.getSubject() + ",id: " + subject.getId() + " in (" + dayIndex + "," + periodIndex + ")");
            textView.setText(subject.getSubject());
            cell.setTag(subject);
            cell.setBackgroundColor(subject.getColor());
            if (Constants.isColorDark(subject.getColor())) {
                textView.setTextColor(Color.parseColor("#fefefe"));
            }
            if (subject.hasAssignment()) {
                assignmentIcon.setVisibility(View.VISIBLE);
            }
        }
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        params.setMargins(1, 1, 0, 0);
        v.setLayoutParams(params);
        return v;
    }

    public static View getTimeView(Context context, String time) {
        TextView v = (TextView) View.inflate(context, R.layout.table_cell_time, null);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        params.setMargins(0, 1, 1, 0);
        v.setText(time);
        v.setLayoutParams(params);
        return v;
    }

    public static View getDayView(Context context, String day) {
        TextView v = (TextView) View.inflate(context, R.layout.table_cell_day, null);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        params.setMargins(1, 0, 0, 1);
        v.setLayoutParams(params);
        v.setText(day);
        return v;
    }

}
