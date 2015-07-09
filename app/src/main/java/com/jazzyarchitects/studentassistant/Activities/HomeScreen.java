package com.jazzyarchitects.studentassistant.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.jazzyarchitects.studentassistant.CustomViews.AlarmView;
import com.jazzyarchitects.studentassistant.CustomViews.ClassSchedule;
import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.R;


public class HomeScreen extends AppCompatActivity {

    AlarmView alarmView;
    RadioGroup firstHalfClasses,secondHalfClasses;
    LinearLayout ll;
    RelativeLayout topDetails, bottomDetails;
    ScrollView sl;
    ImageView userPic;
    int screenWidth, screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        alarmView=(AlarmView)findViewById(R.id.alarmClock);
        firstHalfClasses=(RadioGroup)findViewById(R.id.firstHalfClasses);
        secondHalfClasses=(RadioGroup)findViewById(R.id.secondHalfClasses);
        topDetails=(RelativeLayout)findViewById(R.id.topDetails);
        bottomDetails=(RelativeLayout)findViewById(R.id.bottomDetails);
        userPic=(ImageView)findViewById(R.id.userPic);
        ll=(LinearLayout)findViewById(R.id.ll);
        sl=(ScrollView)findViewById(R.id.sl);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        setScreenWidth(metrics.widthPixels);
        setScreenHeight(metrics.heightPixels);
        sl.getLayoutParams().height=getScreenHeight();


        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "Select Your Pic"), 1);

            }
        });

        topDetails.setVisibility(View.VISIBLE);
        ((ClassSchedule)firstHalfClasses.getChildAt(0)).setChecked(true);

        alarmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AlarmView)v).showTimePicker(getFragmentManager());
            }
        });

        for(int i=0;i<firstHalfClasses.getChildCount();i++){
            ClassSchedule v=(ClassSchedule)firstHalfClasses.getChildAt(i);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unCheckAll();
                    topDetails.setVisibility(View.VISIBLE);
                    bottomDetails.setVisibility(View.GONE);
                    ((ClassSchedule)v).setChecked(true);
                    topDetails.setBackgroundColor(((ClassSchedule) v).getBackgroundColor());
                }
            });
        }
        for(int i=0;i<secondHalfClasses.getChildCount();i++){
            ClassSchedule v=(ClassSchedule)secondHalfClasses.getChildAt(i);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unCheckAll();
                    topDetails.setVisibility(View.GONE);
                    bottomDetails.setVisibility(View.VISIBLE);
                    ((ClassSchedule)v).setChecked(true);
                    bottomDetails.setBackgroundColor(((ClassSchedule) v).getBackgroundColor());
                }
            });
        }
    }

    public void unCheckAll(){
       for(int i=0;i<firstHalfClasses.getChildCount();i++){
            ClassSchedule v=(ClassSchedule)firstHalfClasses.getChildAt(i);
            v.setChecked(false);

        }
        for(int i=0;i<secondHalfClasses.getChildCount();i++){
            ClassSchedule v=(ClassSchedule)secondHalfClasses.getChildAt(i);
            v.setChecked(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                String picturePath = getImagePath(data);
                userPic.setImageBitmap(Constants.getScaledBitmap(picturePath, 267, 200));
            }
        }
    }

    /**
     * Getting path of selected image
     */
    String getImagePath(Intent result) {
        Uri imageUri = result.getData();
        String[] filePathColoumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(imageUri, filePathColoumn, null, null, null);
        cursor.moveToFirst();
        int coloumnIndex = cursor.getColumnIndex(filePathColoumn[0]);
        String picturePath = cursor.getString(coloumnIndex);
        cursor.close();
        return picturePath;
    }


    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

}
