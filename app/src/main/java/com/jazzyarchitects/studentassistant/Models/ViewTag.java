package com.jazzyarchitects.studentassistant.Models;

/**
 * Created by Jibin_ism on 22-Jul-15.
 */
public class ViewTag {
    Subject subject=null;
    int dayIndex=0, periodIndex=0;

    public ViewTag(Subject subject, int dayIndex, int periodIndex) {
        this.subject = subject;
        this.dayIndex = dayIndex;
        this.periodIndex = periodIndex;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(int dayIndex) {
        this.dayIndex = dayIndex;
    }

    public int getPeriodIndex() {
        return periodIndex;
    }

    public void setPeriodIndex(int periodIndex) {
        this.periodIndex = periodIndex;
    }
}
