package com.jazzyarchitects.studentassistant.Fragment;


import android.app.Activity;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.jazzyarchitects.studentassistant.Activities.HomeScreen;
import com.jazzyarchitects.studentassistant.CustomViews.SubjectDetailDialog;
import com.jazzyarchitects.studentassistant.CustomViews.SubjectSelection;
import com.jazzyarchitects.studentassistant.Listeners.DragEventListener;
import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.Models.TimeTableIds;
import com.jazzyarchitects.studentassistant.R;

import java.util.Calendar;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeTable extends Fragment {


    public TimeTable() {
        // Required empty public constructor
    }
    static int selectedDayIndex = -1, selectedPeriodIndex = -1;
    boolean selected = false;
    protected static View activityLayout;
    int dayToday=-1;
    int[] colors;

    LinearLayout subjectList;

    boolean TESTING=true;

    CardView card;


    int col(int id){
        return getResources().getColor(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activityLayout = View.inflate(getActivity(), R.layout.fragment_time_table, null);
        subjectList=(LinearLayout)activityLayout.findViewById(R.id.subjectList);
//        card=(CardView)activityLayout.findViewById(R.id.card);

//        card.setOnLongClickListener(new DragStartListener());

        try{
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Weekly Time Table");
        }catch (Exception e){
            e.printStackTrace();
        }
        Calendar calendar=Calendar.getInstance();
        if(TESTING) {
            dayToday=Calendar.WEDNESDAY;
        }else{
            dayToday = calendar.get(Calendar.DAY_OF_WEEK);
        }
        highlightToday();


        //Setting up time table
        TimeTableOperations.setUpLabels();

        colors = new int[]{col(R.color.color1),col(R.color.color2),col(R.color.color3),col(R.color.color4),
                col(R.color.color5),col(R.color.color6),col(R.color.color7), Color.RED, Color.MAGENTA,
                Color.GREEN, Color.YELLOW, Color.CYAN};


        populateSubjectList();
        //Writing some junk Values
//        for (int i = 0; i < TimeTableIds.day.length; i++) {
//            for (int j = 0; j < TimeTableIds.period.length; j++) {
//                Subject subject = new Subject("0", "Subject (" + j + "," + i + ")");
//                if(i*j+j+i*2%3==0){
//                    subject.incrementAssignmentCount();
//                }
//                subject.setColor(colors[(new Random()).nextInt(colors.length)]);
//                TimeTableOperations.setSubject(i, j, subject);
//
//            }
//        }
        TimeTableOperations.setDragListeners();
        return activityLayout;
    }

    public void populateSubjectList(){
        for(int i=0;i<5;i++) {
            Subject subject = new Subject(String.valueOf(i), "Subject " + (i + 1));
            subject.setColor(colors[((new Random()).nextInt(colors.length))]);
            subject.setAssignmentCount(i % 4);
            SubjectSelection subjectSelection=new SubjectSelection(getActivity());
            subjectSelection.setSubject(subject);
            subjectList.addView(subjectSelection);
        }

    }

    /**
     * Invoked when the cell is clicked
     *
     * @param v clicked view
     */
    public void subjectClick(View v) {
        if (v instanceof RelativeLayout) {
            int dayIndex = TimeTableIds.getDay(((View)v.getParent().getParent()).getId());
            int periodIndex = TimeTableIds.getPeriod(((View) v.getParent().getParent().getParent()).getId());

            if (dayIndex == selectedDayIndex && periodIndex == selectedPeriodIndex && selected) {
                selected = false;
            } else {
                selected = true;
            }

            Subject subject = TimeTableOperations.getSubject(dayIndex, periodIndex);

            if(selected) {
                refreshDetails(subject);
            }

            selectedDayIndex = dayIndex;
            selectedPeriodIndex = periodIndex;
        }
    }

    /**
     * Update subject details
     *
     * @param subject selected subject
     */
    public void refreshDetails(Subject subject) {
        SubjectDetailDialog detailDialog=new SubjectDetailDialog(getActivity(),subject);
        detailDialog.show();

    }

    public void highlightToday(){
        TimeTableOperations.markDay(dayToday);
    }


    /**
     * All operations on time table
     */
    private static class TimeTableOperations {

        static String[] day = {"Mon", "Tue", "Wed", "Thu", "Fri"};
        static String[] timings = {"8:00 - 8:50", "8:50 - 9:40", "9:40 - 10:30", "10:30 - 11:20", "11:20 - 12:10",
                "13:30 - 14:20", "14:20 - 15:10", "15:10 - 16:00", "16:00 - 16:50", "16:50 - 17:40"};
        static TableLayout tableLayout;
        static TableRow tableRow;
        static RelativeLayout cell;
        static ImageView assignmentIcon;
        static TextView textView;
        static View colorIndicator;
        static MaterialRippleLayout rippleLayout;

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
                if (subject.hasAssignment()) {
                    assignmentIcon.setVisibility(View.VISIBLE);
                }
            }
        }

        public static void changeBackground(int dayIndex, int periodIndex, int color) {
            getCellDetails(dayIndex, periodIndex);
            cell.setBackgroundColor(color);
        }

        public static int getLightenColor(int color){
            float[] hsv=new float[3];
            Color.colorToHSV(color, hsv);
            hsv[1]=0.1f;
            return Color.HSVToColor(hsv);
        }

        public static void markDay(int dayOfWeek){
            if(dayOfWeek==0 || dayOfWeek>=6){
                return;
            }
            tableLayout=(TableLayout)activityLayout.findViewById(R.id.table);
            for(int i=0;i<tableLayout.getChildCount();i++){
                View child=tableLayout.getChildAt(i);
                if(child instanceof TableRow){
                    RelativeLayout cell=(RelativeLayout)((TableRow) child).getChildAt(dayOfWeek-1);
                    cell.setBackgroundColor(Color.parseColor("#7AB317"));
                }
            }
        }

        public static void removeMaterialRipple(int dayIndex, int periodIndex){
            getCellDetails(dayIndex, periodIndex);
            rippleLayout=(MaterialRippleLayout)cell.findViewById(R.id.rippleLayout);
        }

        public static void setDragListeners(){
            for (int i = 0; i < TimeTableIds.day.length; i++) {
                for (int j = 0; j < TimeTableIds.period.length; j++) {
                    getCellDetails(i,j);
                    cell.setOnDragListener(new DragEventListener());
                }
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeScreen)activity).setActivityClickListener(new HomeScreen.ActivityClickListener() {
            @Override
            public void onSubjectClick(View view) {
                subjectClick(view);
            }
        });
    }
}
