package com.jazzyarchitects.studentassistant.Models;

/**
 * Created by Jibin_ism on 09-Jul-15.
 */
public class SubjectCell {

    String id="", subject="";

    public SubjectCell(String id, String subject) {
        this.id = id;
        this.subject = subject;
    }

    public SubjectCell() {
    }

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
}
