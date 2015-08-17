package com.jazzyarchitects.studentassistant.Fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.Activities.HomeScreen;
import com.jazzyarchitects.studentassistant.Adapters.SubjectSelectionListAdapter;
import com.jazzyarchitects.studentassistant.CustomViews.SubjectDetailDialog;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.SubjectDatabase;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.TimeTableHandler;
import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.Models.ViewTag;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeTable extends Fragment {


    public TimeTable() {
        // Required empty public constructor
    }

    OnFragmentInteractionListener mListener;
    RelativeLayout rl;
    boolean isInEditMode=false;
    static String TAG = "TimeTableFragment";
    protected static View activityLayout;
    static int dayToday = -1;

    boolean TESTING = true;
    FloatingActionButton addSubjects;
    static Subject draggingSubject = null;
    static Vibrator vibrator;
    static Context sContext;

    int dayCount = 1, periodCount = 1;

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG, "Created");
        context = getActivity();
        // Inflate the layout for this fragment
        activityLayout = View.inflate(getActivity(), R.layout.fragment_time_table, null);
        SharedPreferences pref = getActivity().getSharedPreferences(Constants.TimeTablePreferences.Preference, Context.MODE_PRIVATE);
        dayCount = pref.getInt(Constants.TimeTablePreferences.WorkingDaysInWeek, 5);
        periodCount = pref.getInt(Constants.TimeTablePreferences.PeriodCount, 8);

        rl=(RelativeLayout)activityLayout.findViewById(R.id.rl);

        createTable();
        populateSubjectList();

        addSubjects = (FloatingActionButton) activityLayout.findViewById(R.id.addSubject);
        addSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onFloatingButtonClicked();
            }
        });


        vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        sContext = getActivity();

        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Weekly Time Table");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        if (TESTING && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            dayToday = Calendar.WEDNESDAY;
        } else {
            dayToday = calendar.get(Calendar.DAY_OF_WEEK);
        }
        setHasOptionsMenu(true);
        return activityLayout;
    }

    TableLayout tableLayout;

    public void createTable() {
        setupDayList();
        tableLayout = (TableLayout) activityLayout.findViewById(R.id.timeTableLayout);
        tableLayout.setWeightSum(periodCount + 1);
//        tableLayout.setWeightSum(periodCount+1);
        tableLayout.removeAllViews();
        setupSubjects();
    }

    void setupDayList() {
        TableRow tableRow = (TableRow) activityLayout.findViewById(R.id.dayList);
        tableRow.setWeightSum(dayCount + 1);
        View v = new View(context);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        params.setMargins(0, 0, 1, 1);
        v.setLayoutParams(params);
        v.setBackgroundColor(getResources().getColor(R.color.timeTableLabelCellColor));

        tableRow.addView(v);
        for (int i = 1; i <= dayCount; i++) {
            tableRow.addView(Constants.getDayView(context, TimeTableOperations.day[i - 1]));
        }
    }

    /**
     * Table Row ids in multiples of 100
     * Cell ids in series of row ids
     */
    void setupSubjects() {
        final TimeTableHandler handler = new TimeTableHandler(context);
        SubjectDatabase subjectHandler = new SubjectDatabase(context);
        for (int i = 0; i < periodCount; i++) {
            TableRow tableRow = new TableRow(context);
            int rowId = 100 * (i + 1);
            tableRow.setId(rowId);
            tableRow.setWeightSum(dayCount + 1);
            tableRow.setMinimumHeight((int) getResources().getDimension(R.dimen.cellMinHeight));
            tableRow.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
            tableRow.addView(Constants.getTimeView(context, TimeTableOperations.timings[i]));
            for (int j = 0; j < dayCount; j++) {
                View v = Constants.getSubjectView(context, subjectHandler.findSubjectById(handler.getSubjectId(j, i)), j, i);
                v.setId(rowId + j);
                v.setOnDragListener(new DragEventListener());
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subjectClick(v);
                    }
                });
                v.findViewById(R.id.cell).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        vibrator.vibrate(100);
                        ViewTag tag = (ViewTag) v.getTag();
                        int dayIndex = tag.getDayIndex();
                        int periodIndex = tag.getPeriodIndex();
                        TimeTableHandler handler1=new TimeTableHandler(context);
                        handler1.removeSubject(dayIndex, periodIndex);
                        handler.close();
                        setSubject(v, new ViewTag(null, dayIndex, periodIndex));
                        return false;
                    }
                });
                tableRow.addView(v);
            }
            tableLayout.addView(tableRow);
        }
        handler.close();
        subjectHandler.close();

    }

    public void populateSubjectList() {
        Log.e(TAG, "Populating time table");
        ArrayList<Subject> subjects = (new SubjectDatabase(getActivity())).getAllSubject();

        RecyclerView recyclerView = (RecyclerView) activityLayout.findViewById(R.id.subjectList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.subjectListColoumnCount)));
        SubjectSelectionListAdapter adapter = new SubjectSelectionListAdapter(getActivity(), subjects);
        adapter.setOnLongClickListener(new SubjectSelectionListAdapter.LongClickListener() {
            @Override
            public void onLongClick(View v) {
                v.setOnLongClickListener(new DragStartListener());
            }
        });
        recyclerView.setAdapter(adapter);
    }

    /**
     * Invoked when the cell is clicked
     *
     * @param v clicked view
     */
    public void subjectClick(View v) {
        ViewTag viewTag = (ViewTag) v.getTag();
        Subject subject = viewTag.getSubject();
        if (subject != null) {
            refreshDetails(subject);
        }

    }

    /**
     * Update subject details
     *
     * @param subject selected subject
     */
    public void refreshDetails(Subject subject) {
        SubjectDetailDialog detailDialog = new SubjectDetailDialog(getActivity(), subject);
        detailDialog.show();

    }

    /**
     * All operations on time table
     */
    private static class TimeTableOperations {
        static String[] day = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        static String[] timings = {"8:00 - 8:55", "9:00 - 9:55", "10:00 - 10:55", "11:00 - 11:55", "1:30 - 2:25",
                "2:30 - 3:25", "3:30 - 4:25", "4:30 - 5:25", "8:00 - 8:55", "9:00 - 9:55", "10:00 - 10:55", "11:00 - 11:55", "1:30 - 2:25",
                "2:30 - 3:25", "3:30 - 4:25", "4:30 - 5:25"};
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeScreen) activity).setActivityClickListener(new HomeScreen.ActivityClickListener() {
            @Override
            public void onSubjectClick(View view) {
                subjectClick(view);
            }

            @Override
            public boolean onBackKeyPressed() {
                if (rl.getVisibility() == View.VISIBLE) {
                    rl.setVisibility(View.GONE);
                    return true;
                }
                return false;
            }

        });
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement OnFragmentInteractionListener");
        }
    }


    static class DragEventListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.cell);
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    rl.setBackgroundColor(Color.parseColor("#A0C55F"));
                    break;
                case DragEvent.ACTION_DROP:
                    vibrator.vibrate(100);
                    ViewTag viewTag = (ViewTag) v.findViewById(R.id.cell).getTag();
                    int dayIndex = viewTag.getDayIndex();
                    int periodIndex = viewTag.getPeriodIndex();
                    Log.e(TAG, "DragEvent: ACTION_DRAG_DROPPED in (" + dayIndex + "," + periodIndex + ")");
                    viewTag.setSubject(draggingSubject);
                    setSubject(v, viewTag);
                    updateSubject(dayIndex, periodIndex, draggingSubject);

                    if (dayIndex == dayToday - 2) {
                        rl.setBackgroundColor(Color.parseColor("#7AB317"));
                    } else {
                        rl.setBackgroundColor(draggingSubject.getColor());
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    viewTag=(ViewTag)v.findViewById(R.id.cell).getTag();
                    Subject existingSubject=viewTag.getSubject();
                    int dI=viewTag.getDayIndex();
                    Log.e(TAG, "DragEvent: ACTION_DRAG_EXITED dI: " + dI + " dayToday: " + dayToday);
                    if (dI + 2 == dayToday) {
                        rl.setBackgroundColor(Color.parseColor("#7AB317"));
                    } else if (existingSubject == null) {
                        rl.setBackgroundColor(Color.parseColor("#fefefe"));
                    } else {
                        rl.setBackgroundColor(existingSubject.getColor());
                    }
                    break;
                default:
                    break;
            }

            return true;
        }
    }

    static void setSubject(View v, ViewTag viewTag) {
        v.setTag(viewTag.getSubject());

        TextView textView;
        RelativeLayout cell, innerCell;
        ImageView assignmentIcon;

        textView = (TextView) v.findViewById(R.id.subjectName);
        cell = (RelativeLayout) v;
        assignmentIcon = (ImageView) v.findViewById(R.id.assignmentIcon);
        innerCell = (RelativeLayout) v.findViewById(R.id.cell);

        innerCell.setTag(viewTag);

        Subject subject = viewTag.getSubject();
        if (subject == null) {
            textView.setText("____");
            assignmentIcon.setVisibility(View.GONE);
            innerCell.setBackgroundColor(Color.WHITE);
            textView.setTextColor(Color.BLACK);
        } else {
            textView.setText(subject.getSubject());
            cell.setTag(subject);
            cell.setBackgroundColor(subject.getColor());
            if (Constants.isColorDark(subject.getColor())) {
                textView.setTextColor(Color.parseColor("#fefefe"));
            }
            if (subject.hasAssignment()) {
                assignmentIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    private static void updateSubject(int dayIndex, int periodIndex, Subject subject) {
        Log.e(TAG, "Updating subject: " + subject.getId());
        TimeTableHandler handler = new TimeTableHandler(sContext);
        handler.insertSubject(dayIndex, periodIndex, subject.getId());
        handler.close();
    }

    public class DragStartListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            vibrator.vibrate(100);
            ClipData clipData = ClipData.newPlainText("", "");
            draggingSubject = (Subject) v.getTag();
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(clipData, shadowBuilder, v, 0);
            return false;
        }
    }

    public interface OnFragmentInteractionListener {
        void onFloatingButtonClicked();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.time_table,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.editTimeTable){
            rl.setVisibility(View.VISIBLE);
            isInEditMode=true;
        }
        return true;
    }
}
