package com.jazzyarchitects.studentassistant.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {


    public Settings() {
        // Required empty public constructor
    }

    EditText dayCountInput, periodCountInput;
    Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_settings, container, false);
        dayCountInput=(EditText)v.findViewById(R.id.dayCount);
        periodCountInput=(EditText)v.findViewById(R.id.periodCount);
        saveButton=(Button)v.findViewById(R.id.save);

        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Settings");
        } catch (Exception e) {
            e.printStackTrace();
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dayCount=Integer.parseInt(dayCountInput.getText().toString());
                int periodCount=Integer.parseInt(periodCountInput.getText().toString());
                SharedPreferences preferences=getActivity().getSharedPreferences(Constants.TimeTablePreferences.Preference, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=preferences.edit();
                editor.putInt(Constants.TimeTablePreferences.PeriodCount,periodCount);
                editor.putInt(Constants.TimeTablePreferences.WorkingDaysInWeek,dayCount);
                editor.apply();
            }
        });
        return v;
    }


}
