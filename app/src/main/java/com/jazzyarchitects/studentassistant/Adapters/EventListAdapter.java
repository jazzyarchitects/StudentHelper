package com.jazzyarchitects.studentassistant.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.Models.Event;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;

/**
 * Created by Aanisha on 17-Aug-15.
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {

    Context context;
    ArrayList<Event> eventList;

    public EventListAdapter(ArrayList<Event> list, Context context) {
        eventList = list;
        this.context = context;
    }

    /**
     * View Holder Class to point to recycler view item.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        View color;
        TextView txtViewSubject, txtViewEvent, txtViewDate;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            txtViewSubject = (TextView) itemLayoutView.findViewById(R.id.subject);
            txtViewEvent = (TextView) itemLayoutView.findViewById(R.id.eventType);
            txtViewDate = (TextView) itemLayoutView.findViewById(R.id.dateTime);
            color = itemLayoutView.findViewById(R.id.eventColor);
        }
    }

    @Override
    public EventListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        //create new view
        View itemLayoutView = LayoutInflater.from(context)
                .inflate(R.layout.listview_item_event, viewGroup, false);

        return new EventListAdapter.ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        /** Setting list View item details */
        String d=eventList.get(position).getDate()+"/"+eventList.get(position).getMonth()+"/"+eventList.get(position).getYear();
        String t=eventList.get(position).getHour()+":"+ eventList.get(position).getMin();
        viewHolder.txtViewEvent.setText(eventList.get(position).getEvent());
        viewHolder.txtViewSubject.setText(eventList.get(position).getSubject());
        viewHolder.txtViewDate.setText(d+"\t"+t);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}
