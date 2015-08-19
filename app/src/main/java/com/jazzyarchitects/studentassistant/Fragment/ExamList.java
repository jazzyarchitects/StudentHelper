package com.jazzyarchitects.studentassistant.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class ExamList extends Fragment {

    Context context;
    TextView addExam, noExam;
    EventHandler eventHandler;
    RecyclerView recyclerView;
    ArrayList<Event> examList;
    EventListAdapter eventListAdapter;

    public ExamList() {
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
        addExam = (TextView) view.findViewById(R.id.addEvent);
        noExam = (TextView) view.findViewById(R.id.noEvent);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        addExam.setText("+ADD NEW EXAM");
        noExam.setText("Yipeee!! No Examinations!!");
        updateEventList();

        addExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddEvent.class));
            }
        });
        return view;
    }

    public void updateEventList() {
        eventHandler = new EventHandler(context);
        examList = new ArrayList<>();
        examList = eventHandler.getAllEventsWhereQuery("Exam");

        if (examList.isEmpty()) {
            noExam.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noExam.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            eventListAdapter = new EventListAdapter(examList, context);
            recyclerView.setAdapter(eventListAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }
}
