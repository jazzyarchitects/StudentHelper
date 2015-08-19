package com.jazzyarchitects.studentassistant.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD:app/src/main/java/com/jazzyarchitects/studentassistant/Fragment/ExamList.java
import android.app.Fragment;
=======
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
>>>>>>> a167390da76c107ec45d9069e4ca456ab92e8c6b:app/src/main/java/com/jazzyarchitects/studentassistant/Fragment/EventList.java
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.Activities.AddEvent;
import com.jazzyarchitects.studentassistant.Adapters.EventListAdapter;
<<<<<<< HEAD:app/src/main/java/com/jazzyarchitects/studentassistant/Fragment/ExamList.java
import com.jazzyarchitects.studentassistant.DatabaseHandlers.EventHandler;
=======
import com.jazzyarchitects.studentassistant.Adapters.EventViewPagerAdapter;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.EventHandler;
import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
>>>>>>> a167390da76c107ec45d9069e4ca456ab92e8c6b:app/src/main/java/com/jazzyarchitects/studentassistant/Fragment/EventList.java
import com.jazzyarchitects.studentassistant.Models.Event;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;

public class ExamList extends Fragment {

    Context context;
    TextView addExam, noExam;
    EventHandler eventHandler;
    RecyclerView recyclerView;
<<<<<<< HEAD:app/src/main/java/com/jazzyarchitects/studentassistant/Fragment/ExamList.java
    ArrayList<Event> examList;
=======
    ArrayList<Event> eventList;
    RelativeLayout emptyView;
>>>>>>> a167390da76c107ec45d9069e4ca456ab92e8c6b:app/src/main/java/com/jazzyarchitects/studentassistant/Fragment/EventList.java
    EventListAdapter eventListAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

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
<<<<<<< HEAD:app/src/main/java/com/jazzyarchitects/studentassistant/Fragment/ExamList.java
        addExam = (TextView) view.findViewById(R.id.addEvent);
        noExam = (TextView) view.findViewById(R.id.noEvent);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        addExam.setText("+ADD NEW EXAM");
        noExam.setText("Yipeee!! No Examinations!!");
        updateEventList();
=======
        addEvent = (TextView) view.findViewById(R.id.addEvent);

        tabLayout=(TabLayout)view.findViewById(R.id.tabLayout);
        viewPager=(ViewPager)view.findViewById(R.id.viewPager);

        //TODO: Change the fragment count
        EventViewPagerAdapter adapter=new EventViewPagerAdapter(getFragmentManager(),3);
        viewPager.setAdapter(adapter);
        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        try{
            Bundle args=getArguments();
            int position=args.getInt(Constants.EVENT_KEY);
            viewPager.setCurrentItem(position);
        }catch (Exception e){
            e.printStackTrace();
        }



//        noEvent = (TextView) view.findViewById(R.id.noEvent);
//        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
//        emptyView=(RelativeLayout)view.findViewById(R.id.emptyView);
//        updateEventList();
>>>>>>> a167390da76c107ec45d9069e4ca456ab92e8c6b:app/src/main/java/com/jazzyarchitects/studentassistant/Fragment/EventList.java

        addExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddEvent.class));
                getActivity().
                        overridePendingTransition(R.anim.slide_left_show, R.anim.slide_left_hide);
            }
        });
        return view;
    }

    public void updateEventList() {
        eventHandler = new EventHandler(context);
        examList = new ArrayList<>();
        examList = eventHandler.getAllEventsWhereQuery("Exam");

<<<<<<< HEAD:app/src/main/java/com/jazzyarchitects/studentassistant/Fragment/ExamList.java
        if (examList.isEmpty()) {
            noExam.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noExam.setVisibility(View.GONE);
=======
        if (eventList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
>>>>>>> a167390da76c107ec45d9069e4ca456ab92e8c6b:app/src/main/java/com/jazzyarchitects/studentassistant/Fragment/EventList.java
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            eventListAdapter = new EventListAdapter(examList, context);
            recyclerView.setAdapter(eventListAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }
}
