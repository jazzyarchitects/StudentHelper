package com.jazzyarchitects.studentassistant.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import com.jazzyarchitects.studentassistant.Adapters.EventViewPagerAdapter;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.EventHandler;
import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.Models.Event;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;

public class EventList extends Fragment {
    Context context;
    TextView addEvent, noEvent;
    EventHandler eventHandler;
    RecyclerView recyclerView;
    ArrayList<Event> eventList;
    RelativeLayout emptyView;
    EventListAdapter eventListAdapter;
    ViewPager viewPager;
    TabLayout tabLayout;

    public EventList() {
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

        addEvent.setOnClickListener(new View.OnClickListener() {
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
        eventList = new ArrayList<>();
        eventList = eventHandler.getAllEvents();

        if (eventList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            eventListAdapter = new EventListAdapter(eventList, context);
            recyclerView.setAdapter(eventListAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
