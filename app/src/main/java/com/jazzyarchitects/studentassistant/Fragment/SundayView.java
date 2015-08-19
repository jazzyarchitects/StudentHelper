package com.jazzyarchitects.studentassistant.Fragment;


import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SundayView extends Fragment {

    TextView assignmentCount, examinationCount, eventCount, extraClassCount;
    int linearLayoutIds[]={R.id.ll1,R.id.ll2,R.id.ll3,R.id.ll4};

    public SundayView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sunday_view, container, false);
        assignmentCount = (TextView) v.findViewById(R.id.assignmentCount);
        examinationCount = (TextView) v.findViewById(R.id.examinationCount);
        eventCount = (TextView) v.findViewById(R.id.eventCount);
        extraClassCount = (TextView) v.findViewById(R.id.extraClassCount);

        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Week's Schedule");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        for(int i:linearLayoutIds){
            LinearLayout linearLayout=(LinearLayout)v.findViewById(i);
            linearLayout.setOnTouchListener(onTouchListener);
        }


        return v;
    }


    float[] originalHSV=new float[3];
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
//            Log.e("SundayView","Motion Event: "+event.getAction());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    showDarkBackground(v);
                    break;
                case MotionEvent.ACTION_CANCEL:
                    showOriginalBackground(v);
                    break;
                case MotionEvent.ACTION_UP:
                    showOriginalBackground(v);
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
        Color.colorToHSV(color,originalHSV);
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


}
