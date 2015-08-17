package com.jazzyarchitects.studentassistant.AlarmManagers;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;

import com.jazzyarchitects.studentassistant.HelperClasses.Constants;

import java.util.Calendar;


public class AlarmService extends IntentService {

    public AlarmService() {
        super("AlarmService");
    }

    int[] classTimingsHours={8,9,10,11,1,2,3,4};
    int[] classTimingsMinutes={0,0,0,0,30,30,30,30};
    @Override
    protected void onHandleIntent(Intent intent) {
        setupAlarm();
    }

    private void setupAlarm(){
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar calendar=Calendar.getInstance();
        SharedPreferences sharedPreferences=getSharedPreferences(Constants.TimeTablePreferences.Preference, MODE_PRIVATE);
        int daysInWeek=sharedPreferences.getInt(Constants.TimeTablePreferences.WorkingDaysInWeek,5);
        if(calendar.get(Calendar.DAY_OF_WEEK)==daysInWeek+1){
            calendar.add(Calendar.DATE, (7-daysInWeek+1));
        } else{
            calendar.add(Calendar.DATE, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, classTimingsHours[0]);
        calendar.set(Calendar.MINUTE, classTimingsMinutes[0]);
        calendar.add(Calendar.MINUTE, -10);
        Intent i=new Intent(this,AlarmTriggerer.class);
        PendingIntent pi=PendingIntent.getService(this,12,i,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pi);
    }

}
