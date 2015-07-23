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

    int dayCount=1,periodCount=1;

    Context context;

    int col(int id) {
        return getResources().getColor(id);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e(TAG,"Created");
        context=getActivity();
        // Inflate the layout for this fragment
        activityLayout = View.inflate(getActivity(), R.layout.fragment_time_table, null);
//        card=(CardView)activityLayout.findViewById(R.id.card);

//        card.setOnLongClickListener(new DragStartListener());

        SharedPreferences pref=getActivity().getSharedPreferences(Constants.TimeTablePreferences.Preference,Context.MODE_PRIVATE);
        dayCount=pref.getInt(Constants.TimeTablePreferences.WorkingDaysInWeek,5);
        periodCount=pref.getInt(Constants.TimeTablePreferences.PeriodCount,8);

        createTable();
        populateSubjectList();

        addSubjects=(FloatingActionButton)activityLayout.findViewById(R.id.addSubject);
        addSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener!=null)
                    mListener.onFloatingButtonClicked();
            }
        });


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
        //TODO: not working active day
//        highlightToday();


        //Setting up time table
//        TimeTableOperations.setUpLabels();

//        colors = new int[]{col(R.color.color1), col(R.color.color2), col(R.color.color3), col(R.color.color4),
//                col(R.color.color5), col(R.color.color6), col(R.color.color7)};


//        populateSubjectList();
//        TimeTableOperations.setTableCellListeners();
//        TimeTableOperations.populateTimeTable();
        return activityLayout;
    }

    TableLayout tableLayout;
    public void createTable(){
        setupDayList();
        tableLayout = (TableLayout)activityLayout.findViewById(R.id.timeTableLayout);
        tableLayout.setWeightSum(periodCount+1);
//        tableLayout.setWeightSum(periodCount+1);
        tableLayout.removeAllViews();
        setupSubjects();
    }

    void setupDayList(){
        TableRow tableRow=(TableRow)activityLayout.findViewById(R.id.dayList);
        tableRow.setWeightSum(dayCount + 1);
        View v=new View(context);
        TableRow.LayoutParams params=new TableRow.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
        params.setMargins(0,0,1,1);
        v.setLayoutParams(params);
        v.setBackgroundColor(getResources().getColor(R.color.timeTableLabelCellColor));

        tableRow.addView(v);
        for(int i=1;i<=dayCount;i++){
            tableRow.addView(Constants.getDayView(context,TimeTableOperations.day[i-1]));
        }
    }

    /**
     * Table Row ids in multiples of 100
     * Cell ids in series of row ids
     */
    void setupSubjects(){
        TimeTableHandler handler=new TimeTableHandler(context);
        SubjectDatabase subjectHandler=new SubjectDatabase(context);
        for(int i=0;i<periodCount;i++){
            TableRow tableRow=new TableRow(context);
            int rowId=100*(i+1);
            tableRow.setId(rowId);
            tableRow.setWeightSum(dayCount + 1);
            tableRow.setMinimumHeight((int) getResources().getDimension(R.dimen.cellMinHeight));
            tableRow.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));
            tableRow.addView(Constants.getTimeView(context, TimeTableOperations.timings[i]));
            for(int j=0;j<dayCount;j++){
                View v=Constants.getSubjectView(context,subjectHandler.findSubjectById(handler.getSubjectId(j,i)),j,i);
                v.setId(rowId+j);
                v.setOnDragListener(new DragEventListener());
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subjectClick(v);
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
        ViewTag viewTag=(ViewTag)v.getTag();
        int periodIndex=viewTag.getPeriodIndex();
        int dayIndex=viewTag.getDayIndex();
        Log.e(TAG,"Clicked: ("+periodIndex+","+dayIndex+")");

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

    public void highlightToday() {
        TimeTableOperations.markDay(dayToday);
    }


    /**
     * All operations on time table
     */
    private static class TimeTableOperations {

        static String[] day = {"Mon", "Tue", "Wed", "Thu", "Fri","Sat"};
        static String[] timings = {"8:00 - 8:55", "9:00 - 9:55", "10:00 - 10:55", "11:00 - 11:55", "1:30 - 2:25",
                "2:30 - 3:25", "3:30 - 4:25", "4:30 - 5:25","8:00 - 8:55", "9:00 - 9:55", "10:00 - 10:55", "11:00 - 11:55", "1:30 - 2:25",
                "2:30 - 3:25", "3:30 - 4:25", "4:30 - 5:25"};
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
            tableLayout = (TableLayout) activityLayout.findViewById(R.id.timeTableLayout);
            tableRow = (TableRow) tableLayout.findViewById((periodIndex+1)*100);
            cell = (RelativeLayout) tableRow.findViewById(dayIndex);
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
