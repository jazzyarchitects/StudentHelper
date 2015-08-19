package com.jazzyarchitects.studentassistant.Adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.jazzyarchitects.studentassistant.DatabaseHandlers.TimeTableHandler;
import com.jazzyarchitects.studentassistant.HelperClasses.TimingClass;
import com.jazzyarchitects.studentassistant.Models.ClassTime;
import com.jazzyarchitects.studentassistant.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jibin_ism on 18-Aug-15.
 */
public class TimeSettingRowAdapter extends RecyclerView.Adapter<TimeSettingRowAdapter.ViewHolder> {

    int periodCount = 1;
    ArrayList<ClassTime> classTimes;
    Context context;
    FragmentManager fragmentManager;


    public TimeSettingRowAdapter(Context context, int periodCount, @Nullable ArrayList<ClassTime> classTimes, FragmentManager fragmentManager) {
        super();
        this.context = context;
        this.periodCount = periodCount;
        this.classTimes = classTimes;
        this.fragmentManager = fragmentManager;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView;
        itemView = inflater.inflate(R.layout.time_setting_rows, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ClassTime classTime;
        if (classTimes != null) {
            try {
                classTime = classTimes.get(position);
            } catch (IndexOutOfBoundsException e) {
                return;
            }
        } else {
            classTime = null;
        }
        final int periodIndex = position+1;
        holder.textView.setText("" + periodIndex);
        if (classTime != null) {
            holder.editText.setText(TimingClass.getTime(classTime.getHour(), classTime.getMinute(), true));
        } else {
            holder.editText.setText("Not Set");
        }
        holder.editText.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return true;
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hours, int minutes) {
                        holder.editText.setText(TimingClass.getTime(hours, minutes, true));
                        holder.editText.setTag(new ClassTime(hours, minutes));
                        TimeTableHandler handler = new TimeTableHandler(context);
                        try {
                            handler.addPeriodTime(periodIndex, new ClassTime(hours, minutes));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                };
                Calendar calendar = Calendar.getInstance();
                TimePickerDialog.newInstance(timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                        .show(fragmentManager, "TIME PICKER DIALOG");
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return periodCount;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        EditText editText;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.periodName);
            editText = (EditText) itemView.findViewById(R.id.timeInput);
        }
    }


}
