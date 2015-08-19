package com.jazzyarchitects.studentassistant.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.jazzyarchitects.studentassistant.Fragment.DailyTimeTable;
import com.jazzyarchitects.studentassistant.Fragment.SubjectList;
import com.jazzyarchitects.studentassistant.Fragment.SundayView;

/**
 * Created by Jibin_ism on 19-Aug-15.
 */
public class EventViewPagerAdapter extends FragmentStatePagerAdapter {

    int eventCount;

    public EventViewPagerAdapter(FragmentManager fm, int eventCount) {
        super(fm);
        this.eventCount=eventCount;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        //TODO: update fragments here
        switch (position){
            case 0:
                fragment=new DailyTimeTable();
                break;
            case 1:
                fragment=new SundayView();
                break;
            case 2:
                fragment=new SubjectList();
                break;
            default:
                fragment=null;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return eventCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //TODO: update titles
        switch (position){
            case 0:
                return "Title 1";
            case 1:
                return "Title 2";
            case 2:
                return "Title 3";
            default:
                return "";
        }
    }
}
