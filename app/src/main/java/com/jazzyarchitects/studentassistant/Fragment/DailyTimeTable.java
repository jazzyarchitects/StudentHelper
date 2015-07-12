package com.jazzyarchitects.studentassistant.Fragment;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyTimeTable#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyTimeTable extends Fragment {

    static View activityView;

    public static DailyTimeTable newInstance() {
        DailyTimeTable fragment = new DailyTimeTable();
        return fragment;
    }

    public DailyTimeTable() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activityView= inflater.inflate(R.layout.fragment_daily_time_table, container, false);

        DailyTimeTableOperations.setLabels();

        return activityView;
    }

    private static class DailyTimeTableIds{
        public static int table=R.id.table;
        public static int timeRow=R.id.time;
        public static int[] time={R.id.time1, R.id.time2, R.id.time3, R.id.time4, R.id.time5,
                R.id.time6, R.id.time7, R.id.time8, R.id.time9, R.id.time10};
        public static int periodRow=R.id.subjects;
        public static int[] period={R.id.p1, R.id.p2, R.id.p3,R.id.p4, R.id.p5, R.id.p6,R.id.p7, R.id.p8, R.id.p9,R.id.p10};

        public static int getDay(int id){
            return getPosition(id, time);
        }

        public static int getPeriod(int id){
            return getPosition(id,period);
        }

        private static int getPosition(int searchItem, int[] searchArray){
            for(int i=0;i<searchArray.length;i++){
                if(searchItem==searchArray[i]){
                    return i;
                }
            }
            return -1;
        }
    }

    private static class DailyTimeTableOperations{
        static TableLayout tableLayout;
        static TableRow tableRow;
        static RelativeLayout cell;
        static TextView subjectName, notes, bunkedClasses, attendancePercentage, assignments;

        static String[] day = {"Mon", "Tue", "Wed", "Thu", "Fri"};
        static String[] timings = {"8:00 - 8:50", "8:50 - 9:40", "9:40 - 10:30", "10:30 - 11:20", "11:20 - 12:10",
                "13:30 - 14:20", "14:20 - 15:10", "15:10 - 16:00", "16:00 - 16:50", "16:50 - 17:40"};

        private static void getCellDetails(int periodIndex){
            tableLayout=(TableLayout)activityView.findViewById(DailyTimeTableIds.table);
            tableRow=(TableRow)tableLayout.findViewById(DailyTimeTableIds.periodRow);
            cell=(RelativeLayout)tableRow.findViewById(DailyTimeTableIds.period[periodIndex]);
            subjectName=(TextView)cell.findViewById(R.id.subjectName);
            notes=(TextView)cell.findViewById(R.id.notes);
            bunkedClasses=(TextView)cell.findViewById(R.id.bunkedClasses);
            attendancePercentage=(TextView)cell.findViewById(R.id.attendancePercentage);
            assignments=(TextView)cell.findViewById(R.id.assignmentCount);
        }

        public static void setLabels(){
            tableLayout=(TableLayout)activityView.findViewById(DailyTimeTableIds.table);
            tableRow=(TableRow)tableLayout.findViewById(DailyTimeTableIds.timeRow);
            int j=0;
            for(int i: DailyTimeTableIds.time){
                TextView view=(TextView)tableRow.findViewById(i);
                view.setText(timings[j]);
                j++;
            }
        }

        public static void setSubject(int periodIndex, Subject subject){
            getCellDetails(periodIndex);
            subjectName.setText(subject.getSubject());
            subjectName.setBackgroundColor(subject.getColor());
            if(Constants.isColorDark(subject.getColor())){
                subjectName.setTextColor(Color.WHITE);
            }
            notes.setText(subject.getNotes());
            bunkedClasses.setText(Constants.getBunkedClassText(subject.getBunkedClasses()));
            attendancePercentage.setText(Constants.getAttendancePercentageText(subject.getAttendancePercentage()));
            assignments.setText(Constants.getAssignmentCountText(subject.getAssignmentCount()));
        }
    }

}
