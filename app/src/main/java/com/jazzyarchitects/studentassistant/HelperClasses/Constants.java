package com.jazzyarchitects.studentassistant.HelperClasses;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.Models.ClassTime;
import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.Models.ViewTag;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;

/**
 * Created by Jibin_ism on 08-Jul-15.
 */
public class Constants {

    public static Spanned getBunkedClassText(int bunkedClasses) {
//        return Html.fromHtml("Bunked Classes: <b>" + bunkedClasses + "</b>");
        return Html.fromHtml("");
    }

    public static Spanned getAttendancePercentageText(double percentage) {
//        return Html.fromHtml("Attendance: <b>" + percentage + "%</b>");
        return Html.fromHtml("");
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
        public static final String PreferencesSet="haveSetPreferences";
        public static final String ClassDuration="classDuration";
        public static final String Restructured="tableRestructured";
    }

    public static final String EVENT_KEY="keyEvent";

    public static boolean isColorDark(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return !(hsv[1] < 0.5 && hsv[2] > 0.5) &&
                ((hsv[0] > 345.0) ||
                (hsv[0] < 15.0) ||
                (hsv[0] > 215.0 && hsv[0] < 275.0) ||
                (hsv[2] < 0.5));
    }


    public static int getLightenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[1] = 0.05f;
        hsv[2] = 0.9f;
        return Color.HSVToColor(hsv);
    }

    public static View getSubjectView(Context context, Subject subject, int dayIndex, int periodIndex) {
        View v = View.inflate(context, R.layout.table_cell_subject, null);
        v.setTag(subject);

        TextView textView = (TextView) v.findViewById(R.id.subjectName);
        ;
        RelativeLayout cell = (RelativeLayout) v, innerCell = (RelativeLayout) v.findViewById(R.id.cell);
        ImageView assignmentIcon = (ImageView) v.findViewById(R.id.assignmentIcon);

        innerCell.setTag(new ViewTag(subject, dayIndex, periodIndex));

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

    public static class DailyTimeTable {
        public static View getSubjectView(Context context, Subject subject, int periodIndex) {
            RelativeLayout view = (RelativeLayout) View.inflate(context, R.layout.table_daily_cell_subject, null);
            view.setTag(new ViewTag(subject, 0, periodIndex));


            TextView subjectName = (TextView) view.findViewById(R.id.subjectName);
            TextView notes = (TextView) view.findViewById(R.id.notes);
            TextView bunkedClasses = (TextView) view.findViewById(R.id.bunkedClasses);
            TextView attendance = (TextView) view.findViewById(R.id.attendancePercentage);
            TextView assignments = (TextView) view.findViewById(R.id.assignmentCount);
            TextView professorName=(TextView)view.findViewById(R.id.professorName);

            if (subject == null) {
                subjectName.setVisibility(View.GONE);
                professorName.setVisibility(View.GONE);
                notes.setText("There is nothing here... Enjoy your day...");
                notes.setGravity(Gravity.CENTER);
                bunkedClasses.setVisibility(View.GONE);
                assignments.setVisibility(View.GONE);
                attendance.setVisibility(View.GONE);
            } else {
                view.setBackgroundColor(Constants.getLightenColor(subject.getColor()));
                subjectName.setText(subject.getSubject());
                subjectName.setBackgroundColor(subject.getColor());
                subjectName.setTextColor(Constants.isColorDark(subject.getColor()) ? Color.WHITE : Color.BLACK);

                if (subject.getNotes().isEmpty())
                    notes.setVisibility(View.GONE);
                else {
                    notes.setVisibility(View.VISIBLE);
                    notes.setText(subject.getNotes());
                }

                bunkedClasses.setText(Constants.getBunkedClassText(subject.getBunkedClasses()));
                professorName.setText(Constants.getProfessorNameText(subject.getTeacher()));
                attendance.setText(Constants.getAssignmentCountText(subject.getAssignmentCount()));

                assignments.setText(Constants.getAssignmentCountText(subject.getAssignmentCount()));
            }

            TableRow.LayoutParams params = new TableRow.LayoutParams((int) context.getResources().getDimension(R.dimen.dailyTimeTableSubjectWidth), ViewGroup.LayoutParams.MATCH_PARENT);
            params.setMargins(0, 0, 1, 0);
            view.setLayoutParams(params);
            return view;
        }

        public static View getTimeView(Context context, int periodIndex, ClassTime startTime) {
            TextView view = (TextView) View.inflate(context, R.layout.table_cell_day, null);
            ClassTime finishTime=TimingClass.getFinishTime(startTime,context.getSharedPreferences(TimeTablePreferences.Preference,Context.MODE_PRIVATE).getInt(TimeTablePreferences.ClassDuration,50));
            view.setText("Period " + (periodIndex + 1)+"\n"+TimingClass.getTime(startTime,true)+" - "+TimingClass.getTime(finishTime,true));
            TableRow.LayoutParams params = new TableRow.LayoutParams((int) context.getResources().getDimension(R.dimen.dailyTimeTableSubjectWidth), (int) context.getResources().getDimension(R.dimen.cellMinHeight));
            params.setMargins(0, 1, 1, 0);
            view.setLayoutParams(params);
            return view;
        }
    }

    //function to return spinner Adapter for Material Design Spinners
    public static SpinnerAdapter spinnerAdapter(final Context context, final String[] list){
        return new SpinnerAdapter() {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View row = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
                TextView label = (TextView) row.findViewById(android.R.id.text1);
                label.setText(list[position]);
                return row;
            }

            @Override
            public void registerDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public void unregisterDataSetObserver(DataSetObserver observer) {

            }

            @Override
            public int getCount() {
                return list.length;
            }

            @Override
            public Object getItem(int position) {
                return list[position];
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return getDropDownView(position,convertView,parent);
            }

            @Override
            public int getItemViewType(int position) {
                return 0;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }
        };
    }

    public static SpinnerAdapter spinnerAdapter(final Context context, ArrayList<String > strings){
        String[] s=new String[strings.size()];
        for(int i=0;i<strings.size();i++){
            s[i]=strings.get(i);
        }
        return spinnerAdapter(context,s);
    }

}
