package com.jazzyarchitects.studentassistant.Adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import com.jazzyarchitects.studentassistant.Fragment.AssignmentList;
import com.jazzyarchitects.studentassistant.Fragment.ClassTestList;
import com.jazzyarchitects.studentassistant.Fragment.ExamList;
import com.jazzyarchitects.studentassistant.Fragment.ExtraClassList;

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
                fragment=new AssignmentList();
                break;
            case 1:
                fragment=new ExamList();
                break;
            case 2:
                fragment=new ClassTestList();
                break;
            case 3:
                fragment=new ExtraClassList();
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
                return "Assignments";
            case 1:
                return "Exams";
            case 2:
                return "Class Tests";
            case 3:
                return "Extra Classes";
            default:
                return "";
        }
    }
}
