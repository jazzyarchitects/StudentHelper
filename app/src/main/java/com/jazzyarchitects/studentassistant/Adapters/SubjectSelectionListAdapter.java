package com.jazzyarchitects.studentassistant.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;

/**
 * Created by Jibin_ism on 13-Jul-15.
 */
public class SubjectSelectionListAdapter extends RecyclerView.Adapter<SubjectSelectionListAdapter.ViewHolder> {

    ArrayList<Subject> subjectSelections;
    Context mContext;
    LongClickListener mListener=null;

    public SubjectSelectionListAdapter(Context context, ArrayList<Subject> subjects) {
        super();
        this.subjectSelections=subjects;
        this.mContext=context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView=View.inflate(mContext,R.layout.layout_subject_selection_list,null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Subject subject=subjectSelections.get(position);
        viewHolder.cell.setTag(subject);
        viewHolder.linearLayout.setBackgroundColor(subject.getColor());
        if(Constants.isColorDark(subject.getColor())){
            viewHolder.subjectName.setTextColor(Color.WHITE);
        }else{
            viewHolder.subjectName.setTextColor(Color.BLACK);
        }
        viewHolder.subjectName.setText(subject.getSubject());
        viewHolder.cell.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.onLongClick(v);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectSelections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout cell, linearLayout;
        TextView subjectName;

        public ViewHolder(View itemView) {
            super(itemView);
            cell=(LinearLayout)itemView.findViewById(R.id.subject);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.subject1);
            subjectName=(TextView)cell.findViewById(R.id.subjectName);
        }
    }

    public interface LongClickListener{
        void onLongClick(View v);
    }

    public void setOnLongClickListener(LongClickListener longClickListener){
        this.mListener=longClickListener;
    }
}
