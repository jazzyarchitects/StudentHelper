package com.jazzyarchitects.studentassistant.Listeners;

import android.content.ClipData;
import android.view.View;

/**
 * Created by Jibin_ism on 12-Jul-15.
 */
public class DragStartListener implements View.OnLongClickListener {
    @Override
    public boolean onLongClick(View v) {
        ClipData clipData=ClipData.newPlainText("","");
        View.DragShadowBuilder shadowBuilder=new View.DragShadowBuilder(v);
        v.startDrag(clipData, shadowBuilder,v, 0);
        return true;
    }
}
