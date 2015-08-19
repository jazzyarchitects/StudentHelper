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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.Activities.AddSubject;
import com.jazzyarchitects.studentassistant.Adapters.SubjectListAdapter;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.SubjectDatabase;
import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;

public class SubjectList extends Fragment {
    TextView addSubject, noSubject;
    SubjectDatabase subjectDatabase;
    Context context;
    RecyclerView recyclerView;
    SubjectListAdapter subjectListAdapter;
    ArrayList<Subject> subjectList;
    RelativeLayout emptyView;


    public SubjectList() {
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
        context = getActivity(); try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Subjects");
        } catch (Exception e) {
            e.printStackTrace();
        }
        View view = inflater.inflate(R.layout.fragment_subject_list, container, false);
        addSubject = (TextView) view.findViewById(R.id.addSubject);
        noSubject = (TextView) view.findViewById(R.id.noSubject);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        emptyView=(RelativeLayout)view.findViewById(R.id.emptyView);

        updateSubjectList();

        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddSubject.class));
                getActivity().
                        overridePendingTransition(R.anim.slide_left_show, R.anim.slide_left_hide);
            }
        });
        return view;
    }

    public void updateSubjectList(){
        subjectDatabase = new SubjectDatabase(context);
        subjectList = new ArrayList<>();
        subjectList = subjectDatabase.getAllSubject();

        if (subjectList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            subjectListAdapter = new SubjectListAdapter(subjectList, context);
            recyclerView.setAdapter(subjectListAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("check","onResume called");
        updateSubjectList();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.e("check", "onDetach called");
    }
}
