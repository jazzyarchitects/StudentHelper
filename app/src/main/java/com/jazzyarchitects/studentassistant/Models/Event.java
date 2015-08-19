package com.jazzyarchitects.studentassistant.Models;

/**
 * Created by Aanisha on 17-Aug-15.
 */
public class Event {
    String id="";
    String event = "";
    String subject = "";
    String date = "",month="",year="",hour="",min="";
    String notes = "";

    public Event(String event, String subject, String date,String month,String year, String hour,String min, String notes) {
        this.event = event;
        this.subject = subject;
        this.date = date;
        this.month=month;
        this.year=year;
        this.hour=hour;
        this.min=min;
        this.notes = notes;
    }

    public Event() {
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id=id;
    }

    public String getEvent() {
        return this.event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String subject){
        this.subject=subject;
    }

    public String getDate(){
        return this.date;
    }

    public void setDate(String date){
        this.date=date;
    }

    public String getMonth(){
        return this.month;
    }

    public void setMonth(String month){
        this.month=month;
    }

    public String getYear(){
        return  this.year;
    }

    public void setYear(String year){
        this.year=year;
    }

    public String getHour(){
        return this.hour;
    }

    public void setHour(String hour){
        this.hour=hour;
    }

    public String getMin(){
        return this.min;
    }

    public void setMin(String min){
        this.min=min;
    }

    public String getNotes(){
        return this.notes;
    }

    public void setNotes(String notes){
        this.notes=notes;
    }

}
