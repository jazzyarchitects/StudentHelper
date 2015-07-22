package com.jazzyarchitects.studentassistant.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.jazzyarchitects.studentassistant.Fragment.DailyTimeTable;
import com.jazzyarchitects.studentassistant.Fragment.Settings;
import com.jazzyarchitects.studentassistant.Fragment.SubjectList;
import com.jazzyarchitects.studentassistant.Fragment.TimeTable;
import com.jazzyarchitects.studentassistant.R;

public class HomeScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TimeTable.OnFragmentInteractionListener {

    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;
    DrawerLayout drawerLayout;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


//
//        SharedPreferences timeTablePref=getSharedPreferences(Constants.TimeTablePreferences.Preference, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor=timeTablePref.edit();
//        editor.putInt(Constants.TimeTablePreferences.PeriodCount,8);
//        editor.putInt(Constants.TimeTablePreferences.WorkingDaysInWeek, 4);
//        editor.apply();

        //View setup
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        NavigationView navigationView=(NavigationView)findViewById(R.id.navigationView);
        frameLayout=(FrameLayout)findViewById(R.id.frame);

        //Setting toolbar
        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);

        drawerToggle=new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();


        Fragment fragment=new TimeTable();
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().replace(frameLayout.getId(),fragment).commit();
        navigationView.getMenu().findItem(R.id.timeTable).setChecked(true);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id=menuItem.getItemId();
        menuItem.setChecked(true);
        drawerLayout.closeDrawers();
        switch (id){
            case R.id.timeTable:
                replaceViewWithFragment(new TimeTable());
                break;
            case R.id.dailyTimeTable:
                replaceViewWithFragment(new DailyTimeTable());
                break;
            case R.id.subjectList:
                replaceViewWithFragment(new SubjectList());
                break;
            case R.id.settings:
                replaceViewWithFragment(new Settings());
                break;
            default:
                break;
        }
        return false;
    }

    ActivityClickListener activityClickListener;

    public void subjectClick(View view){
        activityClickListener.onSubjectClick(view);
    }

    @Override
    public void onFloatingButtonClicked() {
        replaceViewWithFragment(new SubjectList());
    }

    void replaceViewWithFragment(Fragment fragment){
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().replace(frameLayout.getId(),fragment).commit();
    }

    public interface ActivityClickListener{
        void onSubjectClick(View view);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawers();
        }else{
            super.onBackPressed();
        }
    }

    public void setActivityClickListener(ActivityClickListener activityClickListener){
        this.activityClickListener=activityClickListener;
    }
}
