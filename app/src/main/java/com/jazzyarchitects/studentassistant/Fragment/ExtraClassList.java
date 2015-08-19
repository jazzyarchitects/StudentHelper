package com.jazzyarchitects.studentassistant.Fragment;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.Adapters.EventListAdapter;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.EventHandler;
import com.jazzyarchitects.studentassistant.Models.Event;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExtraClassList extends Fragment {
    Context context;
    TextView addClass, noClass;
    EventHandler eventHandler;
    RecyclerView recyclerView;
    ArrayList<Event> classList;
    RelativeLayout emptyView;
    EventListAdapter eventListAdapter;

    public ExtraClassList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Events");
        } catch (Exception e) {
            e.printStackTrace();
        }
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        noClass = (TextView) view.findViewById(R.id.noEvent);
        emptyView=(RelativeLayout)view.findViewById(R.id.emptyView);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        noClass.setText("Yipeee!! No Extra classes!!");
        updateEventList();

        return view;
    }

    public void updateEventList() {
        eventHandler = new EventHandler(context);
        classList = new ArrayList<>();
        classList = eventHandler.getAllEventsWhereQuery("Extra Class");

        if (classList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            eventListAdapter = new EventListAdapter(classList, context);
            recyclerView.setAdapter(eventListAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.e("check", "onResume called");
        updateEventList();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("check", "onDetach called");
    }
}
