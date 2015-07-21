package com.jazzyarchitects.studentassistant.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;


/**
 * Created by Aanisha on 14-Jun-15.
 */
public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.ViewHolder> {

    Context context;
    ArrayList<Subject> subjectList;

    public SubjectListAdapter(ArrayList<Subject> list, Context context) {
        subjectList = list;
        this.context = context;
    }

    /**
     * View Holder Class to point to recycler view item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtViewSubject, txtViewTeacher;
        LinearLayout linearLayout;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            txtViewSubject = (TextView) itemLayoutView.findViewById(R.id.subject);
            txtViewTeacher = (TextView) itemLayoutView.findViewById(R.id.teacher);
            linearLayout = (LinearLayout) itemLayoutView.findViewById(R.id.linearLayout);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        //create new view
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.listview_item_subject, viewGroup, false);

        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        /** Setting list View item details */
        viewHolder.txtViewTeacher.setText(subjectList.get(position).getTeacher());
        viewHolder.txtViewSubject.setText(subjectList.get(position).getSubject());
        viewHolder.linearLayout.setBackgroundColor(subjectList.get(position).getColor());
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

}

