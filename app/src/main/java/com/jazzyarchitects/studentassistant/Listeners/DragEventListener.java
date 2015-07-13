package com.jazzyarchitects.studentassistant.Listeners;

import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.R;

/**
 * Created by Jibin_ism on 12-Jul-15.
 */
public class DragEventListener implements View.OnDragListener {
    @Override
    public boolean onDrag(View v, DragEvent event) {
        int action=event.getAction();
        RelativeLayout rl=(RelativeLayout)v.findViewById(R.id.cell);
        TextView name=(TextView)rl.findViewById(R.id.subjectName);
        String subjectName=event.getClipData().toString();
        Log.e("DragEventListener",subjectName);
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
                name.setText(subjectName);
                rl.setBackgroundColor(Color.WHITE);
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
