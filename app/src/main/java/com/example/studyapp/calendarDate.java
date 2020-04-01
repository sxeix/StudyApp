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

import studynowbackend.RepeatFrequency;
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

    /** Adapter that hold the HashMaps of the TimetableEvent */
    private SimpleAdapter sa;

    /** Gets the current LocalDateTime that the user is using the application*/
    LocalDateTime currentDateTime = LocalDateTime.now();

    /** gets the correct theme for the page */
    SharedPrefs sharedPrefs;

    /** displays the SimpleAdapter containing the events on the page*/
    ListView listView;

    /** displays the date that the user clicked on*/
    TextView title;

    /** Array list that contains all of the events that occur on the day that the user clicked on*/
    ArrayList<TimetableEvent> todayEvent;

    /** ArrayList of HashMaps of the TimetableEvents that will be displayed on the page*/
    ArrayList<HashMap<String, String>> lCalendarDate;

    @Override
    @SuppressLint("StringFormatInvalid")
    protected void onCreate(Bundle savedInstanceState) {

        sharedPrefs = new SharedPrefs(this);

        if(sharedPrefs.loadPinkMode()){
            setTheme(R.style.AppTheme);
        }else if(sharedPrefs.loadBlueMode()){
            setTheme(R.style.BlueMode);
        }else if(sharedPrefs.loadRedMode()){
            setTheme(R.style.RedMode);
        }else if(sharedPrefs.loadGreenMode()){
            setTheme(R.style.GreenMode);
        }else if(sharedPrefs.loadYellowMode()){
            setTheme(R.style.YellowMode);
        }else if(sharedPrefs.loadOrangeMode()){
            setTheme(R.style.OrangeMode);
        }else{
            setTheme(R.style.AppTheme);
        }

        setContentView(R.layout.activity_calendar_date);
        super.onCreate(savedInstanceState);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_calendar_date));

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
                                if(todayEvent.get(position).getRepeatFrequency().equals(RepeatFrequency.NoRepeat))
                                    Timetable.getInstance().removeEvent(todayEvent.get(position));
                                else todayEvent.get(position).getExcludedDates().add(MainActivity.localDate);
                                Timetable.getInstance().save();
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
        }else if(id == R.id.addModules){
            Intent modules = new Intent(calendarDate.this, adding_modules.class);
            startActivity(modules);
        }else if(id == android.R.id.home){
            Intent modules = new Intent(calendarDate.this, MainActivity.class);
            startActivity(modules);
        }
        return true;
    }

    /**
     * takes an TimetableEvent and returns its repeat type
     * @param x the TimetableEvent instance
     * @return a String containing its type in the respective language(Custom/Daily/Weekly/Monthly/Yearly)
     */
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

    /**
     * checks whether the TimetableEvent in question is an AllDay event or not and returns the corresponding string
     * @param e the TimetableEvent instance to be checked
     * @return either "All Day" if an AllDay event or the event's start time in the corresponding language
     */
    public String dailyCheck(TimetableEvent e){
        if (e.getAllDay()) return getResources().getString(R.string.type_allday);
        return String.format(getResources().getString(R.string.time), requiresFormat(e.getStart().getHour()) + e.getStart().getHour(), requiresFormat(e.getStart().getMinute()) + e.getStart().getMinute());
    }

    /**
     * returns an extra 0 (or whatever character the language requires) if the number is less than 10 else it returns an empty string
     * is used to change dates from 1/3/2020 into 01/03/2020 or change times from 11:5 into 11:05
     * @param a the integer
     * @return either a String containing the formatted character with respect to the current language or an empty string
     */
    public String requiresFormat(int a){
        if(a<10) return getResources().getString(R.string.formatted_character);
        return "";
    }

    /**
     * arranges the ArrayList of events with respect to the current time/date the user is using the application.
     * this method also handles excludedEvents where the user has chosen to not have a routine event to occur on that day
     * @param list the ArrayList to be sorted
     */
    public void sortListRelativeOnDay(ArrayList<TimetableEvent> list){
        int size = list.size();
        TimetableEvent e; int i = 0; int count = 0;
        while(i<size && count<size){
            count++;
            e = list.get(i);
            if (e.getExcludedDates().indexOf(MainActivity.localDate) != -1){
                list.remove(e); continue;
            }
            if (e.getAllDay())continue;
            if (e.getEnd().toLocalTime().isBefore(currentDateTime.toLocalTime())&& MainActivity.localDate.equals(currentDateTime.toLocalDate())){
                list.remove(e);continue;
            }
            i++;
        }
    }

    /**
     * checks whether there are any events and displays a "No Events" message in the corresponding language if there are none
     */
    public void noEventsTextViewUpdater(){
        if (sa.getCount() == 0){
            TextView noEvOnDate = findViewById(R.id.noEventsOnDate);
            noEvOnDate.setVisibility(View.VISIBLE);
        }
    }
}
