package com.jazzyarchitects.studentassistant.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.HelperClasses.TimeTableIds;
import com.jazzyarchitects.studentassistant.R;


public class TimeTable extends AppCompatActivity {

    Toolbar toolbar;
    protected static View activityLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLayout=View.inflate(this,R.layout.activity_time_table,null);
        setContentView(activityLayout);
        toolbar=(Toolbar)findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        for(int i=0;i<TimeTableIds.day.length;i++){
            for(int j=0;j<TimeTableIds.period.length;j++){
                TextView cell=TimeTableOperations.getSubject(i,j);
                cell.setText(i+" - "+i*j);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_time_table, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public static class TimeTableOperations{
        public static TextView getSubject(int dayIndex, int periodIndex){
            TableLayout tableLayout=(TableLayout)activityLayout.findViewById(TimeTableIds.table);
            TableRow tableRow=(TableRow)tableLayout.findViewById(TimeTableIds.period[periodIndex]);
            return (TextView)tableRow.findViewById(TimeTableIds.day[dayIndex]);
        }
    }
}
