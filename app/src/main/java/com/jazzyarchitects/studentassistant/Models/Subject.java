package com.jazzyarchitects.studentassistant.Models;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jibin_ism on 09-Jul-15.
 */
public class Subject implements Parcelable {

    String id = "", subject = "";
    String professor="", notes="";
    int assignmentCount=0;
    int bunkedClasses = 0, attendance = 100, totalClasses = 1;
    int color = 0;

    public Subject(String id, String subject) {
        this.id = id;
        this.subject = subject;
    }

    public Subject() {
    }

    public Subject(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static Parcelable.Creator<Subject> CREATOR = new Parcelable.ClassLoaderCreator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel source, ClassLoader loader) {
            return new Subject(source);
        }

        @Override
        public Subject createFromParcel(Parcel source) {
            return new Subject(source);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getColor() {
        if(this.color==0){
            return Color.parseColor("#fefefe");
        }else{
            return this.color;
        }
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getBunkedClasses() {
        return bunkedClasses;
    }

    public void setBunkedClasses(int bunkedClasses) {
        this.bunkedClasses = bunkedClasses;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendence) {
        this.attendance = attendence;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }

    public double getAttendancePercentage() {
        return 100 - (100 * (bunkedClasses / totalClasses));
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getAssignmentCount() {
        return assignmentCount;
    }

    public void setAssignmentCount(int assignmentCount) {
        this.assignmentCount = assignmentCount;
    }

    public boolean hasAssignment(){
        return assignmentCount>0;
    }
    public void incrementAssignmentCount(){
        this.assignmentCount++;
    }
}
