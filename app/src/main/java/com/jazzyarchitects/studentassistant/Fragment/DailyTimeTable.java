package com.jazzyarchitects.studentassistant.Fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.Activities.HomeScreen;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.SubjectDatabase;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.TimeTableHandler;
import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.HelperClasses.TimingClass;
import com.jazzyarchitects.studentassistant.R;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class DailyTimeTable extends Fragment {

    static View activityView;
    int dayCount, periodCount;
    static Context context;
    Calendar calendar;
    HorizontalScrollView horizontalScrollView;


    public DailyTimeTable() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        activityView = inflater.inflate(R.layout.fragment_daily_time_table, container, false);
        context = getActivity();
        SharedPreferences prefs = context.getSharedPreferences(Constants.TimeTablePreferences.Preference, Context.MODE_PRIVATE);
        dayCount = prefs.getInt(Constants.TimeTablePreferences.WorkingDaysInWeek, 5);
        periodCount = prefs.getInt(Constants.TimeTablePreferences.PeriodCount, 10);
        horizontalScrollView=(HorizontalScrollView)activityView.findViewById(R.id.horizontalScrollView);
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Today's Schedule");
        } catch (Exception e) {
            e.printStackTrace();
        }
        calendar = Calendar.getInstance();

        if(calendar.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
            onDetach();
            ((HomeScreen)getActivity()).replaceViewWithFragment(new SundayView());
        }else {
            setupDayAndTime();
            createTable();
        }
        return activityView;
    }

    public void setupDayAndTime() {
        TextView dayIndicator = (TextView) activityView.findViewById(R.id.todayView);
        final TextView timeIndicator = (TextView) activityView.findViewById(R.id.timeView);
        dayIndicator.setText(TimingClass.getFullDayName(calendar.get(Calendar.DAY_OF_WEEK)) + "   " +
                "  " + calendar.get(Calendar.DAY_OF_MONTH) + " " +
                "" + TimingClass.getEquivalentMonth((calendar.get(Calendar.MONTH)) + 1) + " " + calendar.get(Calendar.YEAR));
        timeIndicator.setText(TimingClass.getTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false));
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeIndicator.post(new Runnable() {
                    @Override
                    public void run() {
                        Calendar calendar1 = Calendar.getInstance();
                        timeIndicator.setText(TimingClass.getTime(calendar1.get(Calendar.HOUR_OF_DAY), calendar1.get(Calendar.MINUTE), false));
                    }
                });
            }
        }, 0, 1000);
    }

    public void createTable() {
        TableLayout tableLayout = (TableLayout) activityView.findViewById(R.id.timeTable);
        tableLayout.removeAllViews();
        int dayToday=calendar.get(Calendar.DAY_OF_WEEK)-2;
        TableRow tableRow = new TableRow(context),
                tableRow1 = new TableRow(context);
        tableRow.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (int) context.getResources().getDimension(R.dimen.cellMinHeight)));
        tableRow1.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        TimeTableHandler timeTableHandler=new TimeTableHandler(context);
        SubjectDatabase subjectDatabase=new SubjectDatabase(context);
        for (int i = 0; i < periodCount; i++) {
            tableRow.addView(Constants.DailyTimeTable.getTimeView(context, i));
            View cell=Constants.DailyTimeTable.getSubjectView(context,subjectDatabase.findSubjectById(timeTableHandler.getSubjectId(dayToday,i)),i);
            tableRow1.addView(cell);
        }

        subjectDatabase.close();
        timeTableHandler.close();
        tableLayout.addView(tableRow);
        tableLayout.addView(tableRow1);
    }
}
