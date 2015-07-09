package com.jazzyarchitects.studentassistant.Models;

import com.jazzyarchitects.studentassistant.R;

/**
 * Created by Jibin_ism on 09-Jul-15.
 */
public class TimeTableIds{
    public static int tableHolder= R.id.timeTableLayout;
    public static int table=R.id.table;
    public static int[] period={R.id.p1, R.id.p2, R.id.p3,R.id.p4, R.id.p5, R.id.p6,R.id.p7, R.id.p8, R.id.p9,R.id.p10};
    public static int[] day={R.id.day1,R.id.day2,R.id.day3,R.id.day4,R.id.day5};

    public static int getDay(int id){
        return getPosition(id,day);
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
