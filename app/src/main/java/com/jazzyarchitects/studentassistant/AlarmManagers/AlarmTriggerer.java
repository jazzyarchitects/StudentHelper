package com.jazzyarchitects.studentassistant.AlarmManagers;

import android.app.IntentService;
import android.content.Intent;

import com.jazzyarchitects.studentassistant.Notifications.DayScheduleClassNotification;


public class AlarmTriggerer extends IntentService {


    public AlarmTriggerer() {
        super("AlarmTriggerer");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DayScheduleClassNotification.notify(this,12);
    }

}
