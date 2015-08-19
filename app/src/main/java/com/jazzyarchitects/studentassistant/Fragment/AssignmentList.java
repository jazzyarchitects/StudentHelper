package com.jazzyarchitects.studentassistant.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.Activities.AddEvent;
import com.jazzyarchitects.studentassistant.Adapters.EventListAdapter;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.EventHandler;
import com.jazzyarchitects.studentassistant.Models.Event;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;

public class AssignmentList extends Fragment {
    Context context;
    TextView addAssignment, noAssignment;
    EventHandler eventHandler;
    RecyclerView recyclerView;
    ArrayList<Event> assignmentList;
    EventListAdapter eventListAdapter;

    public AssignmentList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        addAssignment = (TextView) view.findViewById(R.id.addEvent);
        noAssignment = (TextView) view.findViewById(R.id.noEvent);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        addAssignment.setText("+ADD NEW ASSIGNMENT");
        noAssignment.setText("Yipeee!! No Assignments!!");
        updateEventList();

        addAssignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddEvent.class));
            }
        });
        return view;
    }

    public void updateEventList() {
        Log.e("check","Update Event List");
        eventHandler = new EventHandler(context);
        assignmentList = new ArrayList<>();
        assignmentList = eventHandler.getAllEventsWhereQuery("Assignment");

        if (assignmentList.isEmpty()) {
            Log.e("check","Event List empty");
            noAssignment.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noAssignment.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            eventListAdapter = new EventListAdapter(assignmentList, context);
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
