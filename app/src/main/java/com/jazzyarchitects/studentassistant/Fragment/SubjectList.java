package com.jazzyarchitects.studentassistant.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView addSubject, noSubject;
    SubjectDatabase subjectDatabase;
    Context context;
    RecyclerView recyclerView;
    SubjectListAdapter subjectListAdapter;
    ArrayList<Subject> subjectList;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubjectList.
     */
    // TODO: Rename and change types and number of parameters
    public static SubjectList newInstance(String param1, String param2) {
        SubjectList fragment = new SubjectList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SubjectList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_subject_list, container, false);
        addSubject = (TextView) view.findViewById(R.id.addSubject);
        noSubject = (TextView) view.findViewById(R.id.noSubject);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        updateSubjectList();

        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddSubject.class));
            }
        });

        return view;
    }

    public void updateSubjectList(){
        subjectDatabase = new SubjectDatabase(context);
        subjectList = new ArrayList<>();
        subjectList = subjectDatabase.getAllSubject();

        if (subjectList.isEmpty()) {
            noSubject.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noSubject.setVisibility(View.GONE);
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
        updateSubjectList();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
