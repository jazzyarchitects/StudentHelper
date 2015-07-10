package com.jazzyarchitects.studentassistant.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.CustomViews.SubjectDetailDialog;
import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.Models.TimeTableIds;
import com.jazzyarchitects.studentassistant.R;

import java.util.Random;


public class TimeTable extends AppCompatActivity {

    Toolbar toolbar;
    static int selectedDayIndex = -1, selectedPeriodIndex = -1;
    boolean selected = false;
    protected static View activityLayout;


    int col(int id){
        return getResources().getColor(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLayout = View.inflate(this, R.layout.activity_time_table, null);
        setContentView(activityLayout);

        //View setup
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Setting toolbar
        setSupportActionBar(toolbar);

        //Setting up time table
        TimeTableOperations.setUpLabels();

        int[] colors={col(R.color.color1),col(R.color.color2),col(R.color.color3),col(R.color.color4),
                col(R.color.color5),col(R.color.color6),col(R.color.color7),Color.RED, Color.MAGENTA, Color.GREEN, Color.YELLOW, Color.CYAN};


        //Writing some junk Values
        for (int i = 0; i < TimeTableIds.day.length; i++) {
            for (int j = 0; j < TimeTableIds.period.length; j++) {
                Subject subject = new Subject("0", "Subject (" + j + "," + i + ")");
                if(i*j+j+i*2%3==0){
                    subject.incrementAssignmentCount();
                }
                subject.setColor(colors[(new Random()).nextInt(colors.length)]);
                TimeTableOperations.setSubject(i, j, subject);

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_table, menu);
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


    /**
     * Invoked when the cell is clicked
     *
     * @param v clicked view
     */
    public void subjectClick(View v) {
        if (v instanceof RelativeLayout) {
            int dayIndex = TimeTableIds.getDay(v.getId());
            int periodIndex = TimeTableIds.getPeriod(((View) v.getParent()).getId());

            //TODO: uncomment this if bottom details pane to be shown
            if (dayIndex == selectedDayIndex && periodIndex == selectedPeriodIndex && selected) {
//                bottomLinearLayout.setVisibility(View.GONE);
                selected = false;
            } else {
//                bottomLinearLayout.setVisibility(View.VISIBLE);
                selected = true;
            }

            Subject subject = TimeTableOperations.getSubject(dayIndex, periodIndex);

            applyClickBackground(dayIndex, periodIndex);
            refreshDetails(subject);

            selectedDayIndex = dayIndex;
            selectedPeriodIndex = periodIndex;
        }
    }

    /**
     * Changing the background of cell when clicked
     *
     * @param dayIndex    which day
     * @param periodIndex which period
     */
    public void applyClickBackground(int dayIndex, int periodIndex) {
        if (selectedDayIndex >= 0) {
            int color = getResources().getColor(R.color.unSelectedCellColor);
            TimeTableOperations.changeBackground(selectedDayIndex, selectedPeriodIndex, color);
        }
        if (selected) {
            TimeTableOperations.changeBackground(dayIndex, periodIndex, getResources().getColor(R.color.selectedCellColor));
        } else {
            int color = getResources().getColor(R.color.unSelectedCellColor);
            TimeTableOperations.changeBackground(dayIndex, periodIndex, color);
        }
    }

    /**
     * Update subject details
     *
     * @param subject selected subject
     */
    public void refreshDetails(Subject subject) {
        SubjectDetailDialog detailDialog=new SubjectDetailDialog(this, subject);
        detailDialog.show();

    }


    /**
     * All operations on time table
     */
    public static class TimeTableOperations {

        static String[] day = {"Mon", "Tue", "Wed", "Thu", "Fri"};
        static String[] timings = {"8:00 - 8:50", "8:50 - 9:40", "9:40 - 10:30", "10:30 - 11:20", "11:20 - 12:10",
                "13:30 - 14:20", "14:20 - 15:10", "15:10 - 16:00", "16:00 - 16:50", "16:50 - 17:40"};
        static TableLayout tableLayout;
        static TableRow tableRow;
        static RelativeLayout cell;
        static ImageView assignmentIcon;
        static TextView textView;
        static View colorIndicator;

        public static void setUpLabels() {
            tableLayout = (TableLayout) activityLayout.findViewById(TimeTableIds.table);
            TableRow days = (TableRow) activityLayout.findViewById(R.id.days);
            for (int i = 1; i < days.getChildCount(); i++) {
                if (days.getChildAt(i) instanceof TextView) {
                    TextView v = (TextView) days.getChildAt(i);
                    v.setText(day[i - 1]);
                }
            }

            for (int i = 0, j = 0; i < tableLayout.getChildCount(); i++) {
                if (tableLayout.getChildAt(i) instanceof TableRow) {
                    TextView time = (TextView) ((TableRow) tableLayout.getChildAt(i)).getChildAt(0);
                    time.setText(timings[j]);
                    j++;
                }
            }
        }

        private static void getCellDetails(int dayIndex, int periodIndex) {
            tableLayout = (TableLayout) activityLayout.findViewById(TimeTableIds.table);
            tableRow = (TableRow) tableLayout.findViewById(TimeTableIds.period[periodIndex]);
            cell = (RelativeLayout) tableRow.findViewById(TimeTableIds.day[dayIndex]);
            assignmentIcon = (ImageView) cell.findViewById(TimeTableIds.assignmentIcon);
            textView = (TextView) cell.findViewById(TimeTableIds.subjectName);
            colorIndicator=cell.findViewById(R.id.colorIndicator);
        }

        public static Subject getSubject(int dayIndex, int periodIndex) {
            getCellDetails(dayIndex, periodIndex);
            return (Subject) cell.getTag();
        }

        public static TextView getSubjectView(int dayIndex, int periodIndex) {
            getCellDetails(dayIndex, periodIndex);
            return textView;
        }

        public static void setSubject(int dayIndex, int periodIndex, Subject subject) {
            getCellDetails(dayIndex, periodIndex);

            if (subject == null) {
                textView.setText("---");
                cell.setBackgroundColor(Color.parseColor("#EEEEEE"));
                cell.setTag(null);
            } else {
                textView.setText(subject.getSubject());
                cell.setTag(subject);
                colorIndicator.setBackgroundColor(subject.getColor());

//                cell.setBackgroundColor(getLightenColor(subject.getColor()));

                //Setting text color to white for darker colors
//                float[] hsv=new float[3];
//                Color.colorToHSV(subject.getColor(),hsv);
//                if(hsv[0]>345.0 || hsv[0]<15.0 || (hsv[0]>225.0 && hsv[0]<265.0) || hsv[2]<0.4){
//                    textView.setTextColor(Color.WHITE);
//                }
//                Log.e("HSV Values", subject.getSubject() + " " + hsv[0] + " " + hsv[1] + " " + hsv[2]);
                if (subject.hasAssignment()) {
                    assignmentIcon.setVisibility(View.VISIBLE);
                }
            }
        }

        public static void changeBackground(int dayIndex, int periodIndex, int color) {
            getCellDetails(dayIndex, periodIndex);
            cell.setBackgroundColor(color);
//            cell.setBackgroundColor(getLightenColor(color));
        }

        public static int getLightenColor(int color){
            float[] hsv=new float[3];
            Color.colorToHSV(color, hsv);
            hsv[1]=0.1f;
            return Color.HSVToColor(hsv);
        }
    }
}
