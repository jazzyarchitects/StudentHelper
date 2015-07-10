package com.jazzyarchitects.studentassistant.CustomViews;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;

import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.R;

/**
 * Created by Jibin_ism on 10-Jul-15.
 */
public class SubjectDetailDialog extends AppCompatDialog {

    Subject subject=null;

    public SubjectDetailDialog(Context context) {
        super(context);
    }

    public SubjectDetailDialog(Context context, Subject subject){
        super(context);
        this.subject=subject;
    }

    public SubjectDetailDialog(Context context, int theme) {
        super(context, theme);
    }

    protected SubjectDetailDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_detail_layout);
    }
}
