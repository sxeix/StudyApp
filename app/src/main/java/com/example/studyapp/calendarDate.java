package com.example.studyapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import studynowbackend.RepeatFrequency;
import studynowbackend.Timetable;
import studynowbackend.TimetableEvent;
import android.annotation.TargetApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;


public class calendarDate extends AppCompatActivity {
    private SimpleAdapter sa;
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

        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        HashMap<String,String> item;
        for(TimetableEvent e: Timetable.getInstance().getEvents()){
            if(e.getStart().getYear() == MainActivity.sYear && e.getStart().getMonthValue() == MainActivity.sMonth && e.getStart().getDayOfMonth() == MainActivity.sDay
            || e.getRepeatFrequency()== RepeatFrequency.Daily
            || e.getStart().getDayOfWeek().getValue() == MainActivity.dayOfWeek && e.getRepeatFrequency()== RepeatFrequency.Weekly
            || e.getStart().getDayOfMonth() == MainActivity.sDay && e.getRepeatFrequency()== RepeatFrequency.Monthly
            || e.getStart().getMonthValue() == MainActivity.sMonth && e.getStart().getDayOfMonth() == MainActivity.sDay && e.getRepeatFrequency()== RepeatFrequency.Yearly) {
                item = new HashMap<String, String>();
                item.put("line1", "Event: " + e.getName());
                item.put("line2", "Location: " + e.getLocation());
                item.put("line3", "Start Time: " + InputPage.formatCharacter(e.getStart().getHour()) + ":" + InputPage.formatCharacter(e.getStart().getMinute()));
                item.put("line4", "Event Type: " + returnFreqString(e));
                list.add(item);
            }
        }
        sa = new SimpleAdapter(this, list,
                R.layout.activity_event_list,
                new String[] { "line1","line2","line3","line4" },
                new int[] {R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d});

        title = findViewById(R.id.eventsForDay);
        title.setText("Events for " + MainActivity.sDay + "/" + MainActivity.sMonth + "/" + MainActivity.sYear);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(sa);

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(calendarDate.this, "Clicked: " + arrayList.get(i).toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
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

    public String returnFreqString(TimetableEvent x){
        switch (x.getRepeatFrequency()){
            case NoRepeat:
                return "Custom";
            case Daily:
                return "Daily";
            case Weekly:
                return "Weekly";
            case Monthly:
                return "Monthly";
            case Yearly:
                return "Yearly";
            default:
                return "oops";
        }
    }

}
