package com.jazzyarchitects.studentassistant.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.Models.SubjectCell;
import com.jazzyarchitects.studentassistant.Models.TimeTableIds;
import com.jazzyarchitects.studentassistant.R;


public class TimeTable extends AppCompatActivity {

    Toolbar toolbar;
    TextView notes;
    static int selectedDayIndex = -1, selectedPeriodIndex = -1;
    protected static View activityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLayout = View.inflate(this, R.layout.activity_time_table, null);
        setContentView(activityLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        notes = (TextView) findViewById(R.id.notes);

        setSupportActionBar(toolbar);
        TimeTableOperations.setUpLabels();

        for (int i = 0; i < TimeTableIds.day.length; i++) {
            for (int j = 0; j < TimeTableIds.period.length; j++) {
                SubjectCell subjectCell = new SubjectCell("0", i + " -- " + i * j);
                TimeTableOperations.setSubject(i, j, subjectCell);
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


    public static class TimeTableOperations {

        static String[] day = {"Mon", "Tue", "Wed", "Thu", "Fri"};
        static String[] timings = {"8:00 - 8:50", "8:50 - 9:40", "9:40 - 10:30", "10:30 - 11:20", "11:20 - 12:10",
                "13:30 - 14:20", "14:20 - 15:10", "15:10 - 16:00", "16:00 - 16:50", "16:50 - 17:40"};
        static TableLayout tableLayout;
        static TableRow tableRow;
        static TextView textView;

        public static void setUpLabels(){
            tableLayout = (TableLayout) activityLayout.findViewById(TimeTableIds.table);
            TableRow days=(TableRow)activityLayout.findViewById(R.id.days);
            for(int i=1;i<days.getChildCount();i++){
                if(days.getChildAt(i) instanceof TextView) {
                    TextView v = (TextView) days.getChildAt(i);
                    v.setText(day[i-1]);
                }
            }

            for(int i=0,j=0;i<tableLayout.getChildCount();i++){
                if(tableLayout.getChildAt(i) instanceof TableRow){
                    TextView time=(TextView)((TableRow) tableLayout.getChildAt(i)).getChildAt(0);
                    time.setText(timings[j]);
                    j++;
                }
            }
        }

        private static void getCellDetails(int dayIndex, int periodIndex) {
            tableLayout = (TableLayout) activityLayout.findViewById(TimeTableIds.table);
            tableRow = (TableRow) tableLayout.findViewById(TimeTableIds.period[periodIndex]);
            textView = (TextView) tableRow.findViewById(TimeTableIds.day[dayIndex]);
        }

        public static SubjectCell getSubject(int dayIndex, int periodIndex) {
            getCellDetails(dayIndex, periodIndex);
            return new SubjectCell(textView.getTag().toString(), textView.getText().toString());
        }

        public static TextView getSubjectView(int dayIndex, int periodIndex) {
            getCellDetails(dayIndex, periodIndex);
            return textView;
        }

        public static void setSubject(int dayIndex, int periodIndex, SubjectCell subject) {
            getCellDetails(dayIndex, periodIndex);
            textView.setText(subject.getSubject());
            textView.setTag(subject.getId());
        }

        public static void changeBackground(int dayIndex, int periodIndex, int color) {
            getCellDetails(dayIndex, periodIndex);
            textView.setBackgroundColor(color);
        }
    }

    public void subjectClick(View v) {
        if (v instanceof TextView) {
            int dayIndex = TimeTableIds.getDay(v.getId());
            int periodIndex = TimeTableIds.getPeriod(((View) v.getParent()).getId());
//            Log.e("Ids: ",dayIndex+" "+periodIndex);

            SubjectCell cell = TimeTableOperations.getSubject(dayIndex, periodIndex);

            applyClickBackground(dayIndex, periodIndex);
            refreshDetails(cell.getSubject());

            selectedDayIndex = dayIndex;
            selectedPeriodIndex = periodIndex;
        }
    }

    public void applyClickBackground(int dayIndex, int periodIndex) {
        if (selectedDayIndex >= 0) {
            TimeTableOperations.changeBackground(selectedDayIndex, selectedPeriodIndex, getResources().getColor(R.color.unSelectedCellColor));
        }
        TimeTableOperations.changeBackground(dayIndex, periodIndex, getResources().getColor(R.color.selectedCellColor));
    }

    public void refreshDetails(String content) {
        notes.setText(content);
    }
}
