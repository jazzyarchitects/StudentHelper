package com.jazzyarchitects.studentassistant.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jazzyarchitects.studentassistant.CustomViews.ColorPickerDialog;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;

/**
 * Created by Aanisha on 14-Jun-15.
 */
public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.ViewHolder> {

    Context context;
    ArrayList<String> subjectList;

    public SubjectListAdapter(Context context) {
        this.context = context;
        subjectList=new ArrayList<>();
    }

    /**
     * View Holder Class to point to recycler view item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        EditText editTextSubject;
        View viewColorPicker;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            editTextSubject = (EditText) itemLayoutView.findViewById(R.id.subjectName);
            viewColorPicker = itemLayoutView.findViewById(R.id.subjectColor);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //create new view
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.layout_subject, viewGroup, false);

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        /** Setting list View item details */
        viewHolder.editTextSubject.setHint("SubjectName");
        viewHolder.viewColorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPickerDialog = new ColorPickerDialog(context, context.getResources().getColor(R.color.white),
                        new ColorPickerDialog.OnColorSelectedListener() {

                            @Override
                            public void onColorSelected(int color) {
                                viewHolder.viewColorPicker.setBackgroundColor(color);
                            }
                        });
                colorPickerDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

}

