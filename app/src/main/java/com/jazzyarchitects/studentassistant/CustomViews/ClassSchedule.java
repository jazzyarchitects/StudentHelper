package com.jazzyarchitects.studentassistant.CustomViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.R;

/**
 * Created by Jibin_ism on 08-Jul-15.
 */
public class ClassSchedule extends LinearLayout implements Checkable {

    private String NAMESPACE="http://schemas.android.com/apk/res/android";
    LinearLayout linearLayout;
    TextView time, subject;
    View connector;
    int background;

    public ClassSchedule(Context context) {
        this(context, null);
    }

    public ClassSchedule(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassSchedule(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initiate(attrs);
    }

    public void initiate(AttributeSet attrs){
        inflate(getContext(),R.layout.class_schedule,this);

        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);
        time=(TextView)findViewById(R.id.timeDisplay);
        subject=(TextView)findViewById(R.id.subjectDisplay);
        connector=findViewById(R.id.connector);

        if(attrs!=null){
            background = getResources().getColor(attrs.getAttributeResourceValue(NAMESPACE, "background", 0));
            linearLayout.setBackgroundColor(background);
            TypedArray a=getContext().obtainStyledAttributes(attrs,R.styleable.ClassSchedule);
            String t=a.getString(R.styleable.ClassSchedule_time);
            String s=a.getString(R.styleable.ClassSchedule_subject);
            a.recycle();
            time.setText(t);
            subject.setText(s);
        }

    }

    public int getBackgroundColor(){
        return this.background;
    }

    @Override
    public void setChecked(boolean checked) {
        if(checked){
            connector.setBackgroundColor(background);
        }else{
            connector.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public void toggle() {

    }
}
