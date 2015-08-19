package com.jazzyarchitects.studentassistant.Fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.Activities.HomeScreen;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.SubjectDatabase;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.TimeTableHandler;
import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.HelperClasses.TimingClass;
import com.jazzyarchitects.studentassistant.Models.ClassTime;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class DailyTimeTable extends Fragment {

    static View activityView;
    int dayCount, periodCount;
    static Context context;
    Calendar calendar;
    int linearLayoutIds[]={R.id.ll1,R.id.ll2,R.id.ll3,R.id.ll4};
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


        for(int i:linearLayoutIds){
            LinearLayout linearLayout=(LinearLayout)activityView.findViewById(i);
            linearLayout.setOnTouchListener(onTouchListener);
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

        ArrayList<ClassTime> startTimes=timeTableHandler.getClassTimes();
        for (int i = 0; i < periodCount; i++) {
            tableRow.addView(Constants.DailyTimeTable.getTimeView(context, i, startTimes.get(i)));
            View cell=Constants.DailyTimeTable.getSubjectView(context,subjectDatabase.findSubjectById(timeTableHandler.getSubjectId(dayToday,i)),i);
            tableRow1.addView(cell);
        }

        subjectDatabase.close();
        timeTableHandler.close();
        tableLayout.addView(tableRow);
        tableLayout.addView(tableRow1);
    }


    float[] originalHSV=new float[3];
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
//            Log.e("SundayView", "Motion Event: " + event.getAction());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    showDarkBackground(v);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    showOriginalBackground(v);
                    break;
                case MotionEvent.ACTION_UP:
                    showOriginalBackground(v);
                    //TODO: update names and ids according to viewpager fragment order
                    switch (v.getId()){
                        case R.id.ll1:
                            eventOptionClickListener.OnEventClick(0);
                            break;
                        case R.id.ll2:
                            eventOptionClickListener.OnEventClick(1);
                            break;
                        case R.id.ll3:
                            eventOptionClickListener.OnEventClick(2);
                            break;
                        case R.id.ll4:
                            eventOptionClickListener.OnEventClick(3);
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    showOriginalBackground(v);
                    break;
            }
            return true;
        }
    };


    ColorDrawable originalColorDrawable=null;
    void showDarkBackground(View v){
        ColorDrawable drawable=(ColorDrawable)v.getBackground();
        originalColorDrawable=drawable;
        int color=drawable.getColor();
        Color.colorToHSV(color, originalHSV);
        float[] newColor=originalHSV;
        newColor[2] = 0.1f;
        v.setBackgroundDrawable(new ColorDrawable(Color.HSVToColor(newColor)));
    }

    void showOriginalBackground(View v){
        if(originalColorDrawable==null){
            return;
        }
        try{
            v.setBackgroundDrawable(originalColorDrawable);
        }catch (Exception e){
            e.printStackTrace();
        }
        originalColorDrawable=null;
    }

    EventOptionClickListener eventOptionClickListener;
    public interface EventOptionClickListener{
        void OnEventClick(int position);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        eventOptionClickListener=(EventOptionClickListener)activity;
    }
}
