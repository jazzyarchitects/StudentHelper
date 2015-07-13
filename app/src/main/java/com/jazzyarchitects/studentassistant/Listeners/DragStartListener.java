package com.jazzyarchitects.studentassistant.Listeners;

import android.content.ClipData;
import android.view.View;

import com.jazzyarchitects.studentassistant.Models.Subject;

/**
 * Created by Jibin_ism on 12-Jul-15.
 */
public class DragStartListener implements View.OnLongClickListener {
    @Override
    public boolean onLongClick(View v) {
        Subject subject=(Subject)v.getTag();
//        Log.e("DragStartListener","View: "+v.toString());
//        Log.e("DragStartListener","Subject "+subject.toString());
        ClipData clipData=ClipData.newPlainText("SubjectName", subject.getSubject());
        View.DragShadowBuilder shadowBuilder=new View.DragShadowBuilder(v);
        v.startDrag(clipData, shadowBuilder,v, 0);
        return true;
    }
}
