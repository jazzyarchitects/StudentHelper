package com.jazzyarchitects.studentassistant.Listeners;

import android.graphics.Color;
import android.view.DragEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.R;

/**
 * Created by Jibin_ism on 12-Jul-15.
 */
public class DragEventListener implements View.OnDragListener {
    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action=event.getAction();
        RelativeLayout rl=(RelativeLayout)v.findViewById(R.id.cell);
        Subject subject;
            subject = (Subject) v.getTag();
        if(subject==null) {
            subject = new Subject("0", "___");
        }
        switch (action){
            case DragEvent.ACTION_DRAG_STARTED:
//                Log.d("DragEventListener","Started");
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
//                Log.e("DragEventListener","Entered "+subject.getSubject());
                rl.setBackgroundColor(Color.parseColor("#A0C55F"));
                break;
            case DragEvent.ACTION_DROP:
//                Log.e("DragEventListener","Dropped "+subject.getSubject());
                rl.setBackgroundColor(subject.getColor());
                break;
            case DragEvent.ACTION_DRAG_EXITED:
//                Log.e("DragEventListener","Exitted "+subject.getSubject());
                rl.setBackgroundColor(Color.parseColor("#fefefe"));
                break;
            default:
//                Log.d("DragEventListener","Default Case "+action);
                break;
        }

        return true;
    }
}
