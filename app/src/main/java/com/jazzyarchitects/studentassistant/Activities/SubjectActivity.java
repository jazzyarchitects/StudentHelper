package com.jazzyarchitects.studentassistant.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jazzyarchitects.studentassistant.Adapters.SubjectListAdapter;
import com.jazzyarchitects.studentassistant.R;


public class SubjectActivity extends AppCompatActivity {

    RecyclerView subjectList;
    TextView add;
    static int subjectCount = 2;
    Toolbar toolbar;
    SubjectListAdapter subjectListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        subjectList = (RecyclerView) findViewById(R.id.subjectList);
        add = (TextView) findViewById(R.id.add);
        //View setup
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        //Setting toolbar
        setSupportActionBar(toolbar);

        subjectList.setLayoutManager(new LinearLayoutManager(this));
        subjectListAdapter = new SubjectListAdapter(this);
        subjectList.setAdapter(subjectListAdapter);
        subjectList.setItemAnimator(new DefaultItemAnimator());

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // insert into main view
                ViewGroup insertPoint = (ViewGroup) findViewById(R.id.subjectList);
                View view = vi.inflate(R.layout.layout_subject, insertPoint, false);
                insertPoint.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_subject, menu);
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
            startActivity(new Intent(this, AlarmActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
