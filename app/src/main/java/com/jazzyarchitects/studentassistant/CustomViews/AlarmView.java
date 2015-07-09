package com.jazzyarchitects.studentassistant.CustomViews;

import android.app.FragmentManager;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.jazzyarchitects.studentassistant.HelperClasses.TimingClass;
import com.jazzyarchitects.studentassistant.R;

import java.util.Calendar;

/**
 * Created by Jibin_ism on 07-Jul-15.
 */
public class AlarmView extends LinearLayout{

    private String NAMESPACE="http://schemas.android.com/apk/res/android";
    public boolean is24HourMode=false;
    public ImageView image;
    public TextView textView;
    private Context context;

    public AlarmView(Context context) {
        this(context,null);
    }

    public AlarmView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public AlarmView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        initialize(attrs);
    }

    int h=0,m=0;
    public void initialize(AttributeSet attrs){
        inflate(context, R.layout.labelled_image_view, this);
        image=(ImageView)findViewById(R.id.image);
        textView=(TextView)findViewById(R.id.text);
    }


    public void setTime(int hour, int minutes){
        textView.setText(TimingClass.getTime(hour,minutes,is24HourMode));
    }

    public void showTimePicker(FragmentManager fm){
        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
                h=hour;
                m=minute;
                setTime(h,m);
            }
        };
        Calendar calendar=Calendar.getInstance();
        TimePickerDialog.newInstance(timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),is24HourMode)
                .show(fm,"Time Picker");
    }


}
