package com.example.studyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import studynowbackend.Timetable;
import studynowbackend.TimetableEvent;

import android.view.View;
import android.widget.AdapterView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ListView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;


public class calendarDate extends AppCompatActivity {
    private SimpleAdapter sa;
    LocalDateTime currentDateTime = LocalDateTime.now();
    SharedPrefs sharedPrefs;
    ListView listView;
    TextView title;
    ArrayList<TimetableEvent> todayEvent;
    ArrayList<HashMap<String, String>> lCalendarDate;
    @Override
    @SuppressLint("StringFormatInvalid")
    protected void onCreate(Bundle savedInstanceState) {
        sharedPrefs = new SharedPrefs(this);
        if(sharedPrefs.loadNightMode()){
            setTheme(R.style.LightMode);
        }else{
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_calendar_date);
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_calendar_date));
        if (sharedPrefs.loadNightMode()) {
            CoordinatorLayout lLayout = (CoordinatorLayout) findViewById(R.id.eventOnDayCoord);
            lLayout.setBackgroundColor(Color.parseColor("#B9EEF5"));
        } else {
            CoordinatorLayout lLayout = (CoordinatorLayout) findViewById(R.id.eventOnDayCoord);
            lLayout.setBackgroundColor(Color.parseColor("#F5E2E1"));
        }

        lCalendarDate = new ArrayList<HashMap<String,String>>();
        todayEvent = Timetable.getInstance().getEventsOnDay(MainActivity.localDate);
        sortListRelativeOnDay(todayEvent);

        HashMap<String,String> item;
        for(TimetableEvent e: todayEvent){
            item = new HashMap<String, String>();
            item.put("line1", getResources().getString(R.string.title_input) + ": " + e.getName());
            item.put("line2", getResources().getString(R.string.location_input) + ": " + e.getLocation());
            item.put("line3", getResources().getString(R.string.start_time) + " " + dailyCheck(e));
            item.put("line4", getResources().getString(R.string.event_type) + ": " + returnFreqString(e));
            lCalendarDate.add(item);
        }
        sa = new SimpleAdapter(this, lCalendarDate,
                R.layout.activity_event_list,
                new String[] { "line1","line2","line3","line4" },
                new int[] {R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d});

        title = findViewById(R.id.eventsForDay);
        title.setText(String.format(getResources().getString(R.string.events_for), MainActivity.sDay, MainActivity.sMonth, MainActivity.sYear));
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(sa);
        listView.setNestedScrollingEnabled(true);
        noEventsTextViewUpdater();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /** Ignore all bellow for now. Still working on popup window or page*/
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                eventsPage.selectedEvent = todayEvent.get(position);
                final PopupMenu popup = new PopupMenu(calendarDate.this, view);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.description:
                                Intent description = new Intent(calendarDate.this, popupWindowDesc.class);
                                description.putExtra("Description", todayEvent.get(position).getDescription().toString());
                                startActivity(description);
                                return true;
                            case R.id.delete:
                                Timetable.getInstance().removeEvent(todayEvent.get(position));
                                todayEvent.remove(position);
                                lCalendarDate.remove(position);
                                sa.notifyDataSetChanged();
                                noEventsTextViewUpdater();
                                return true;
                            case R.id.edit_event:
                                Intent edit = new Intent(calendarDate.this, editPage.class);
                                startActivity(edit);
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
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
                return getResources().getString(R.string.type_custom);
            case Daily:
                return getResources().getString(R.string.type_daily);
            case Weekly:
                return getResources().getString(R.string.type_weekly);
            case Monthly:
                return getResources().getString(R.string.type_monthly);
            case Yearly:
                return getResources().getString(R.string.type_yearly);
            default:
                return "oops";
        }
    }
    public String dailyCheck(TimetableEvent e){
        if (e.getAllDay()){ return getResources().getString(R.string.type_allday); }
        return String.format(getResources().getString(R.string.time), requiresFormat(e.getStart().getHour()) + e.getStart().getHour(), requiresFormat(e.getStart().getMinute()) + e.getStart().getMinute());
    }
    public String requiresFormat(int a){
        if(a<10){
            return getResources().getString(R.string.formatted_character);
        }
        return "";
    }

    public void sortListRelativeOnDay(ArrayList<TimetableEvent> list){
        int size = list.size();
        TimetableEvent e; int i = 0; int count = 0;
        while(i<size && count<size){
            count++;
            e = list.get(i);
            if (e.getAllDay())continue;
            if (e.getEnd().toLocalTime().isBefore(currentDateTime.toLocalTime())&& MainActivity.localDate.equals(currentDateTime.toLocalDate())){
                list.remove(e);continue;
            }
            i++;
        }
    }

    public void noEventsTextViewUpdater(){
        if (sa.getCount() == 0){
            TextView noEvOnDate = findViewById(R.id.noEventsOnDate);
            noEvOnDate.setVisibility(View.VISIBLE);
        }
    }
}
