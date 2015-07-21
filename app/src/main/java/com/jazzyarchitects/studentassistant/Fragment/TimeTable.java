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
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.jazzyarchitects.studentassistant.Activities.HomeScreen;
import com.jazzyarchitects.studentassistant.Adapters.SubjectSelectionListAdapter;
import com.jazzyarchitects.studentassistant.CustomViews.SubjectDetailDialog;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.SubjectDatabase;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.TimeTableHandler;
import com.jazzyarchitects.studentassistant.HelperClasses.Constants;
import com.jazzyarchitects.studentassistant.Models.Subject;
import com.jazzyarchitects.studentassistant.Models.TimeTableIds;
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
    static String TAG="TimeTableFragment";
    boolean selected = false;
    protected static View activityLayout;
    static int dayToday = -1;
    int[] colors;

    LinearLayout subjectList;

    boolean TESTING = true;

    TimeTableHandler timeTableHandler;
    FloatingActionButton addSubjects;

    CardView card;

    static Subject draggingSubject = null;

    static Vibrator vibrator;
    static Context sContext;

    int col(int id) {
        return getResources().getColor(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG,"Created");
        // Inflate the layout for this fragment
        activityLayout = View.inflate(getActivity(), R.layout.fragment_time_table, null);
//        card=(CardView)activityLayout.findViewById(R.id.card);

//        card.setOnLongClickListener(new DragStartListener());

        addSubjects=(FloatingActionButton)activityLayout.findViewById(R.id.addSubject);
        addSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null)
                    mListener.onFloatingButtonClicked();
            }
        });

        SharedPreferences timeTablePref=getActivity().getSharedPreferences(Constants.TimeTablePreferences.Preference,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=timeTablePref.edit();
        editor.putInt(Constants.TimeTablePreferences.PeriodCount,10);
        editor.putInt(Constants.TimeTablePreferences.WorkingDaysInWeek,5);
        editor.apply();

        vibrator=(Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        sContext=getActivity();

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
        highlightToday();


        //Setting up time table
        TimeTableOperations.setUpLabels();

        colors = new int[]{col(R.color.color1), col(R.color.color2), col(R.color.color3), col(R.color.color4),
                col(R.color.color5), col(R.color.color6), col(R.color.color7)};


        populateSubjectList();
        TimeTableOperations.setTableCellListeners();
        TimeTableOperations.populateTimeTable();
        return activityLayout;
    }

    public void populateSubjectList() {
        Log.e(TAG,"Populating time table");
        ArrayList<Subject> subjects= (new SubjectDatabase(getActivity())).getAllSubject();

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
        if (v instanceof RelativeLayout) {
            int dayIndex = TimeTableIds.getDay(((View) v.getParent().getParent()).getId());
            int periodIndex = TimeTableIds.getPeriod(((View) v.getParent().getParent().getParent()).getId());

            selected = !selected;

            Subject subject = TimeTableOperations.getSubject(dayIndex, periodIndex);

            if (selected && subject != null) {
                refreshDetails(subject);
            }
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

    public void highlightToday() {
        TimeTableOperations.markDay(dayToday);
    }


    /**
     * All operations on time table
     */
    private static class TimeTableOperations {

        static String[] day = {"Mon", "Tue", "Wed", "Thu", "Fri"};
        static String[] timings = {"8:00 - 8:50", "8:50 - 9:40", "9:40 - 10:30", "10:30 - 11:20", "11:20 - 12:10",
                "13:30 - 14:20", "14:20 - 15:10", "15:10 - 16:00", "16:00 - 16:50", "16:50 - 17:40"};
        static TableLayout tableLayout;
        static TableRow tableRow;
        static RelativeLayout cell, innerCell;
        static ImageView assignmentIcon;
        static TextView textView;
        static MaterialRippleLayout rippleLayout;

        public static void setUpLabels() {
            Log.d(TAG,"Setting up labels");
            tableLayout = (TableLayout) activityLayout.findViewById(TimeTableIds.table);
            TableRow days = (TableRow) activityLayout.findViewById(R.id.days);
            for (int i = 1; i < days.getChildCount(); i++) {
                if (days.getChildAt(i) instanceof TextView) {
                    TextView v = (TextView) days.getChildAt(i);
                    v.setText(day[i - 1]);
                }
            }

            for (int i = 0, j = 0; i < tableLayout.getChildCount(); i++) {
                if (tableLayout.getChildAt(i) instanceof TableRow) {
                    TextView time = (TextView) ((TableRow) tableLayout.getChildAt(i)).getChildAt(0);
                    time.setText(timings[j]);
                    j++;
                }
            }
        }

        private static void getCellDetails(int dayIndex, int periodIndex) {
            Log.v(TAG,"Getting cell: ("+dayIndex+","+periodIndex+")");
            tableLayout = (TableLayout) activityLayout.findViewById(TimeTableIds.table);
            tableRow = (TableRow) tableLayout.findViewById(TimeTableIds.period[periodIndex]);
            cell = (RelativeLayout) tableRow.findViewById(TimeTableIds.day[dayIndex]);
            innerCell=(RelativeLayout)cell.findViewById(R.id.cell);
            rippleLayout=(MaterialRippleLayout)cell.findViewById(R.id.rippleLayout);
            assignmentIcon = (ImageView) cell.findViewById(TimeTableIds.assignmentIcon);
            textView = (TextView) cell.findViewById(TimeTableIds.subjectName);
        }

        public static Subject getSubject(int dayIndex, int periodIndex) {
            getCellDetails(dayIndex, periodIndex);
            return (Subject) cell.getTag();
        }

        public static TextView getSubjectView(int dayIndex, int periodIndex) {
            getCellDetails(dayIndex, periodIndex);
            return textView;
        }

        public static void setSubject(int dayIndex, int periodIndex, Subject subject) {
            getCellDetails(dayIndex, periodIndex);

            if (subject == null) {
                textView.setText("____");
                cell.setTag(null);
                assignmentIcon.setVisibility(View.GONE);
                innerCell.setBackgroundColor(Color.WHITE);
                textView.setTextColor(Color.BLACK);
//                Log.v(TAG,"Null subject in ("+dayIndex+","+periodIndex+")");
            } else {
//                Log.v(TAG, "Setting subject: " + subject.getSubject() + ",id: " + subject.getId() + " in (" + dayIndex + "," + periodIndex + ")");
                textView.setText(subject.getSubject());
                cell.setTag(subject);
                if(dayToday-2!=dayIndex)
                    cell.setBackgroundColor(subject.getColor());
                else
                    cell.setBackgroundColor(Color.parseColor("#7AB317"));
                if (Constants.isColorDark(subject.getColor())) {
                    textView.setTextColor(Color.parseColor("#fefefe"));
                }
                if (subject.hasAssignment()) {
                    assignmentIcon.setVisibility(View.VISIBLE);
                }
            }
        }

        public static void changeBackground(int dayIndex, int periodIndex, int color) {
            getCellDetails(dayIndex, periodIndex);
            cell.setBackgroundColor(color);
        }

        public static int getLightenColor(int color) {
            float[] hsv = new float[3];
            Color.colorToHSV(color, hsv);
            hsv[1] = 0.1f;
            return Color.HSVToColor(hsv);
        }

        public static void markDay(int dayOfWeek) {
            if (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY) {
                return;
            }
            tableLayout = (TableLayout) activityLayout.findViewById(R.id.table);
            for (int i = 0; i < tableLayout.getChildCount(); i++) {
                View child = tableLayout.getChildAt(i);
                if (child instanceof TableRow) {
                    RelativeLayout cell = (RelativeLayout) ((TableRow) child).getChildAt(dayOfWeek - 1);
                    cell.setBackgroundColor(Color.parseColor("#7AB317"));
                }
            }
        }

        public static void populateTimeTable(){
            Log.e(TAG,"Populating Time Table--1");
            TimeTableHandler handler=new TimeTableHandler(sContext);
            SubjectDatabase subjectHandler=new SubjectDatabase(sContext);
            for(int i=0;i<TimeTableIds.day.length;i++){
                for (int j = 0; j < TimeTableIds.period.length; j++) {
                    String id=handler.getSubjectId(i,j);
                    Subject subject=subjectHandler.findSubjectById(id);
                    setSubject(i,j,subject);
                }
            }
            handler.close();
            subjectHandler.close();
            Log.e(TAG,"Finished Populating Time Table--2");
        }

        public static void removeMaterialRipple(int dayIndex, int periodIndex) {
            getCellDetails(dayIndex, periodIndex);
            rippleLayout = (MaterialRippleLayout) cell.findViewById(R.id.rippleLayout);
        }

        public static void setTableCellListeners() {
            for (int i = 0; i < TimeTableIds.day.length; i++) {
                for (int j = 0; j < TimeTableIds.period.length; j++) {
                    getCellDetails(i, j);
                    cell.setOnDragListener(new DragEventListener());
                    final int finalI = i;
                    final int finalJ = j;
                    rippleLayout.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
//                            Log.e("Table","Cell long click..."+finalI+","+finalJ);
                            TimeTableOperations.setSubject(finalI, finalJ, null);
                            TimeTableHandler handler=new TimeTableHandler(sContext);
                            handler.removeSubject(finalI, finalJ);
                            handler.close();
                            vibrator.vibrate(100);
                            return false;
                        }
                    });
                }
            }
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeScreen) activity).setActivityClickListener(new HomeScreen.ActivityClickListener() {
            @Override
            public void onSubjectClick(View view) {
                subjectClick(view);
            }
        });
        try{
            mListener=(OnFragmentInteractionListener)activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+"must implement OnFragmentInteractionListener");
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
                    int dayIndex = TimeTableIds.getDay(v.getId());
                    int periodIndex = TimeTableIds.getPeriod(((View) v.getParent()).getId());
                    Log.e(TAG,"DragEvent: ACTION_DRAG_DROPPED in ("+dayIndex+","+periodIndex+")");
                    TimeTableOperations.setSubject(dayIndex, periodIndex, draggingSubject);
                    updateSubject(dayIndex, periodIndex, draggingSubject);

                    if (dayIndex == dayToday -2) {
                        rl.setBackgroundColor(Color.parseColor("#7AB317"));
                    } else {
                        rl.setBackgroundColor(draggingSubject.getColor());
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Subject existingSubject = (Subject) v.getTag();
                    int dI = TimeTableIds.getDay(v.getId());
                    Log.e(TAG,"DragEvent: ACTION_DRAG_EXITED dI: "+dI+" dayToday: "+dayToday);
                    if (dI + 2 == dayToday) {
                        rl.setBackgroundColor(Color.parseColor("#7AB317"));
                    }else if (existingSubject == null) {
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

    private static void updateSubject(int dayIndex, int periodIndex, Subject subject){
        Log.e(TAG,"Updating subject: "+subject.getId());
        TimeTableHandler handler=new TimeTableHandler(sContext);
        handler.insertSubject(dayIndex,periodIndex,subject.getId());
    }

    public class DragStartListener implements View.OnLongClickListener{

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

    public interface OnFragmentInteractionListener{
        void onFloatingButtonClicked();
    }


}
