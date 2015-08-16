package com.jazzyarchitects.studentassistant.Models;

/**
 * Created by Aanisha on 17-Aug-15.
 */
public class Event {
    String id="";
    String event = "";
    String subject = "";
    String date = "";
    String time = "";
    String notes = "";

    public Event(String event, String subject, String date, String time, String notes) {
        this.event = event;
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.notes = notes;
    }

    public Event() {
    }

    public String getId(){
        return this.id;
    }

    public void setId(String Id){
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

    public String getTime(){
        return this.time;
    }

    public void setTime(String time){
        this.time=time;
    }

    public String getNotes(){
        return this.notes;
    }

    public void setNotes(String notes){
        this.notes=notes;
    }

}
