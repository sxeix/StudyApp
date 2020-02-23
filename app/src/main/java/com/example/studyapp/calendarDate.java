package com.example.studyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import studynowbackend.Timetable;
import studynowbackend.TimetableEvent;
import android.annotation.TargetApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import java.util.ArrayList;

public class calendarDate extends AppCompatActivity {
    SharedPrefs sharedPrefs;
    ListView listView;
    TextView title;
    @Override
    @TargetApi(26)
    protected void onCreate(Bundle savedInstanceState) {
        sharedPrefs = new SharedPrefs(this);
        if(sharedPrefs.loadNightMode()){
            setTheme(R.style.LightMode);
        }else{
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_calendar_date);
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = findViewById(R.id.eventsForDay);
        title.setText("Events for " + MainActivity.sDay + "/" + MainActivity.sMonth + "/" + MainActivity.sYear);
        listView = (ListView) findViewById(R.id.listView);

        final ArrayList<String> arrayList = new ArrayList<>();
        for(TimetableEvent e: Timetable.getInstance().getEvents()){
            if(e.getStart().getYear() == MainActivity.sYear && e.getStart().getMonthValue() == MainActivity.sMonth
                    && e.getStart().getDayOfMonth() == MainActivity.sDay) {
                arrayList.add(e.getName() + ": " + e.getStart().getHour() + ":" + e.getStart().getMinute() + "-" +
                        e.getEnd().getHour() + ":" + e.getEnd().getMinute());
            }
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(calendarDate.this, "Clicked: " + arrayList.get(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    /** Toolbar dropdown menu*/
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    /** Toolbar icons implementations*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.events){
            Intent eventspage = new Intent(calendarDate.this, eventsPage.class);
            startActivity(eventspage);
        }else if(id == R.id.about){
            Intent infopage = new Intent(calendarDate.this, infoPage.class);
            startActivity(infopage);
        }else if(id == R.id.settings){
            Intent settings = new Intent(calendarDate.this, settingsPage.class);
            startActivity(settings);
        }else if(id == android.R.id.home){
            this.finish();
        }
        return true;
    }

}
