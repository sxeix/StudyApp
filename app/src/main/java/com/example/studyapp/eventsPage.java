package com.example.studyapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import studynowbackend.Timetable;
import studynowbackend.TimetableEvent;

import android.widget.ListAdapter;
import android.view.View.MeasureSpec;
import android.widget.TextView;

public class eventsPage extends AppCompatActivity {
    public static TimetableEvent selectedEvent;
    LocalDateTime currentDateTime = LocalDateTime.now();
    ListView vCustom; ListView vDaily; ListView vWeekly; ListView vMonthly; ListView vYearly;
    ArrayList<TimetableEvent> customEvents = new ArrayList<>(Timetable.getInstance().getEvents());
    ArrayList<TimetableEvent> dailyEvents = new ArrayList<>(Timetable.getInstance().getDailyEvents());
    ArrayList<TimetableEvent> weeklyEvents = new ArrayList<>(Timetable.getInstance().getWeeklyEvents());
    ArrayList<TimetableEvent> monthlyEvents = new ArrayList<>(Timetable.getInstance().getMonthlyEvents());
    ArrayList<TimetableEvent> yearlyEvents = new ArrayList<>(Timetable.getInstance().getYearlyEvents());
    ArrayList<HashMap<String, String>> lCustom;
    ArrayList<HashMap<String, String>> lDaily;
    ArrayList<HashMap<String, String>> lWeekly;
    ArrayList<HashMap<String, String>> lMonthly;
    ArrayList<HashMap<String, String>> lYearly;
    SimpleAdapter aCustom; SimpleAdapter aDaily; SimpleAdapter aWeekly; SimpleAdapter aMonthly; SimpleAdapter aYearly;
    SharedPrefs sharedPrefs;
    ArrayList<ListView> listViews = new ArrayList<ListView>();
    ArrayList<SimpleAdapter> adapters = new ArrayList<SimpleAdapter>();
    ArrayList<ArrayList<HashMap<String, String>>> hashMapLists = new ArrayList<ArrayList<HashMap<String, String>>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**Imports theme mode user preferences*/
        sharedPrefs = new SharedPrefs(this);
        if (sharedPrefs.loadNightMode()) {
            setTheme(R.style.LightMode);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_page);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_events_page));
        CoordinatorLayout lLayout = (CoordinatorLayout) findViewById(R.id.eventpagecoord);
        if (sharedPrefs.loadNightMode()) {
            lLayout.setBackgroundColor(Color.parseColor("#B9EEF5"));

        } else {
            lLayout.setBackgroundColor(Color.parseColor("#F5E2E1"));
        }
        sortListRelativeCustom(customEvents);
        sortListRelativeDaily(dailyEvents);
        sortListRelativeWeekly(weeklyEvents);
        sortListRelativeMonthly(monthlyEvents);
        sortListRelativeYearly(yearlyEvents);
        /** This clump basically does everything. don't worry about it
         * only relevant things are the 5 ListView objects for each event type */
        lCustom = new ArrayList<HashMap<String, String>>();
        lDaily = new ArrayList<HashMap<String, String>>();
        lWeekly = new ArrayList<HashMap<String, String>>();
        lMonthly = new ArrayList<HashMap<String, String>>();
        lYearly = new ArrayList<HashMap<String, String>>();
        for (TimetableEvent e : customEvents) {
            addToArrayList(e);
        }
        for (TimetableEvent e : dailyEvents) {
            addToArrayList(e);
        }
        for (TimetableEvent e : weeklyEvents) {
            addToArrayList(e);
        }
        for (TimetableEvent e : monthlyEvents) {
            addToArrayList(e);
        }
        for (TimetableEvent e : yearlyEvents) {
            addToArrayList(e);
        }
        aCustom = new SimpleAdapter(this, lCustom,
                R.layout.activity_event_list,
                new String[]{"line1", "line2", "line3", "line4"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d});
        aDaily = new SimpleAdapter(this, lDaily,
                R.layout.activity_event_list,
                new String[]{"line1", "line2", "line3", "line4"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d});
        aWeekly = new SimpleAdapter(this, lWeekly,
                R.layout.activity_event_list,
                new String[]{"line1", "line2", "line3", "line4"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d});
        aMonthly = new SimpleAdapter(this, lMonthly,
                R.layout.activity_event_list,
                new String[]{"line1", "line2", "line3", "line4"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d});
        aYearly = new SimpleAdapter(this, lYearly,
                R.layout.activity_event_list,
                new String[]{"line1", "line2", "line3", "line4"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d});
        vCustom = (ListView) findViewById(R.id.customEventsListView);
        vCustom.setAdapter(aCustom);
        setListViewHeightBasedOnChildren(vCustom, 0);
        vDaily = (ListView) findViewById(R.id.dailyEventsListView);
        vDaily.setAdapter(aDaily);
        setListViewHeightBasedOnChildren(vDaily, 1);
        vWeekly = (ListView) findViewById(R.id.weeklyEventsListView);
        vWeekly.setAdapter(aWeekly);
        setListViewHeightBasedOnChildren(vWeekly, 2);
        vMonthly = (ListView) findViewById(R.id.monthlyEventsListView);
        vMonthly.setAdapter(aMonthly);
        setListViewHeightBasedOnChildren(vMonthly, 3);
        vYearly = (ListView) findViewById(R.id.yearlyEventsListView);
        vYearly.setAdapter(aYearly);
        setListViewHeightBasedOnChildren(vYearly, 4);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listViews.addAll(Arrays.asList(vCustom, vDaily, vWeekly, vMonthly, vYearly));
        adapters.addAll(Arrays.asList(aCustom, aDaily, aWeekly, aMonthly, aYearly));
        hashMapLists.addAll(Arrays.asList(lCustom, lDaily, lWeekly, lMonthly, lYearly));

        /** Ignore all bellow for now. Still working on popup window or page*/
        vCustom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                listViewClick(parent, view, position, id, 0);
            }
        });
        vDaily.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                listViewClick(parent, view, position, id, 1);
            }
        });
        vWeekly.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                listViewClick(parent, view, position, id, 2);
            }
        });
        vMonthly.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                listViewClick(parent, view, position, id, 3);
            }
        });
        vYearly.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                listViewClick(parent, view, position, id, 4);
            }
        });
    }

    /**
     * Toolbar dropdown menu
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Toolbar icons implementations
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.events) {
            return true;
        } else if (id == R.id.about) {
            Intent infopage = new Intent(eventsPage.this, infoPage.class);
            startActivity(infopage);
        } else if (id == R.id.settings) {
            Intent settings = new Intent(eventsPage.this, settingsPage.class);
            startActivity(settings);
        } else if (id == android.R.id.home) {
            this.finish();
        }
        return true;
    }

    /**
     * changes list view height depending on the number of items it has
     */
    public void setListViewHeightBasedOnChildren(ListView listView, int type) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter.getCount() == 0) {
            switch(type){
                case 0:
                    TextView noEvCus = findViewById(R.id.noEventsCustom);
                    noEvCus.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    TextView noEvDa = findViewById(R.id.noEventsDaily);
                    noEvDa.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    TextView noEvWe = findViewById(R.id.noEventsWeekly);
                    noEvWe.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    TextView noEvMo = findViewById(R.id.noEventsMonthly);
                    noEvMo.setVisibility(View.VISIBLE);
                    break;
                case 4:
                    TextView noEvYe = findViewById(R.id.noEventsYearly);
                    noEvYe.setVisibility(View.VISIBLE);
                    break;
            }
        }
        int totalHeight = 0;
        int UNBOUNDED = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(UNBOUNDED, UNBOUNDED);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    /**
     * Adds events to correct list depending on event type
     */
    public void addByFreq(HashMap<String, String> item, TimetableEvent x) {
        switch (x.getRepeatFrequency()) {
            case NoRepeat:
                lCustom.add(item);
                break;
            case Daily:
                lDaily.add(item);
                break;
            case Weekly:
                lWeekly.add(item);
                break;
            case Monthly:
                lMonthly.add(item);
                break;
            case Yearly:
                lYearly.add(item);
                break;
        }
    }

    /**
     * creates HashMap of event and then adds to array
     */
    public void addToArrayList(TimetableEvent e) {
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("line1", getResources().getString(R.string.title_input) + ": " + e.getName());
        item.put("line2", getResources().getString(R.string.start_date) + " " + routineCheck(e));
        item.put("line3", getResources().getString(R.string.start_time) + " " + dailyCheck(e));
        item.put("line4", getResources().getString(R.string.location_input) + ": " + e.getLocation());
        addByFreq(item, e);
    }

    @SuppressLint("StringFormatMatches")
    public String routineCheck(TimetableEvent e) {
        switch (e.getRepeatFrequency()) {
            case NoRepeat:
                return String.format(getResources().getString(R.string.full_date), requiresFormat(e.getStart().getDayOfMonth()) + e.getStart().getDayOfMonth(), requiresFormat(e.getStart().getMonthValue()) + e.getStart().getMonthValue(), e.getStart().getYear());
            case Daily:
                return getResources().getString(R.string.type_daily);
            case Weekly:
                return dayOfWeek(e);
            case Monthly:
                return String.format(getResources().getString(R.string.month_date), e.getStart().getDayOfMonth(), monthEnd(e));
            case Yearly:
                return String.format(getResources().getString(R.string.short_date), requiresFormat(e.getStart().getDayOfMonth()) + e.getStart().getDayOfMonth(), requiresFormat(e.getStart().getMonthValue()) + e.getStart().getMonthValue());
            default:
                return "oops";
        }
    }

    public String monthEnd(TimetableEvent e) {
        int i = e.getStart().getDayOfMonth() % 10;
        if (i == 1 && e.getStart().getDayOfMonth() != 11) {
            return "st";
        } else if (i == 2 && e.getStart().getDayOfMonth() != 12) {
            return "nd";
        } else if (i == 3 && e.getStart().getDayOfMonth() != 13) {
            return "rd";
        }
        return "th";
    }

    public String dayOfWeek(TimetableEvent e) {
        switch (e.getStart().getDayOfWeek().name()) {
            case "MONDAY":
                return getResources().getString(R.string.day_monday);
            case "TUESDAY":
                return getResources().getString(R.string.day_tuesday);
            case "WEDNESDAY":
                return getResources().getString(R.string.day_wednesday);
            case "THURSDAY":
                return getResources().getString(R.string.day_thursday);
            case "FRIDAY":
                return getResources().getString(R.string.day_friday);
            case "SATURDAY":
                return getResources().getString(R.string.day_saturday);
            case "SUNDAY":
                return getResources().getString(R.string.day_sunday);
            default:
                return "oops";
        }
    }

    public String dailyCheck(TimetableEvent e) {
        if (e.getAllDay()) {
            return getResources().getString(R.string.type_allday);
        }
        return String.format(getResources().getString(R.string.time), requiresFormat(e.getStart().getHour()) + e.getStart().getHour(), requiresFormat(e.getStart().getMinute()) + e.getStart().getMinute());
    }

    public void listViewClick(AdapterView<?> parent, View view, final int position, long id, int type) {
        selectedEvent = getTypeListByIndex(type).get(position);
        final PopupMenu popup = new PopupMenu(eventsPage.this, view);
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.description:
                        Intent description = new Intent(eventsPage.this, popupWindowDesc.class);
                        description.putExtra("Description", getTypeListByIndex(type).get(position).getDescription().toString());
                        startActivity(description);
                        return true;
                    case R.id.delete:
                        Timetable.getInstance().removeEvent(getTypeListByIndex(type).get(position));
                        hashMapLists.get(type).remove(position);
                        setListViewHeightBasedOnChildren(listViews.get(type), type);
                        adapters.get(type).notifyDataSetChanged();
                        return true;
                    case R.id.edit_event:
                        Intent edit = new Intent(eventsPage.this, editPage.class);
                        startActivity(edit);
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }

    public ArrayList<TimetableEvent> getTypeListByIndex(int x) {
        switch (x) {
            case 0:
                return customEvents;
            case 1:
                return dailyEvents;
            case 2:
                return weeklyEvents;
            case 3:
                return monthlyEvents;
            case 4:
                return yearlyEvents;
            default:
                return null;
        }
    }

    public String requiresFormat(int a) {
        if (a < 10) {
            return getResources().getString(R.string.formatted_character);
        }
        return "";
    }

    public void sortListRelativeCustom(ArrayList<TimetableEvent> list){
        int size = list.size();
        TimetableEvent e; int i = 0; int count = 0;
        while(i<size && count<size){
            count++;
            e = list.get(i);
            if (e.getEnd().isBefore(currentDateTime)){
                list.remove(e); list.add(e);
                // TODO may decide to have custom events be removed permanently if it's past end date
                continue;
            }
            i++;
        }
    }
    public void sortListRelativeDaily(ArrayList<TimetableEvent> list){
        int size = list.size();
        TimetableEvent e; int i = 0; int count = 0;
        while(i<size && count<size){
            count++;
            e = list.get(i);
            if (e.getAllDay())continue;
            if (e.getEnd().toLocalTime().isBefore(currentDateTime.toLocalTime())){
                list.remove(e); list.add(e);
                continue;
            }
            i++;
        }
    }
    public void sortListRelativeWeekly(ArrayList<TimetableEvent> list){
        int size = list.size();
        TimetableEvent e; int i = 0; int count = 0;
        while(i<size && count<size){
            count++;
            e = list.get(i);
            if (e.getAllDay()){
                if(e.getEnd().getDayOfWeek().getValue()< currentDateTime.getDayOfWeek().getValue()){
                    list.remove(e); list.add(e); continue;
                }
            }
            else if(e.getEnd().getDayOfWeek().getValue()< currentDateTime.getDayOfWeek().getValue() || (e.getEnd().getDayOfWeek().getValue() == currentDateTime.getDayOfWeek().getValue() && e.getEnd().toLocalTime().isBefore(currentDateTime.toLocalTime()))){
                list.remove(e); list.add(e); continue;
            }
            i++;
        }
    }
    public void sortListRelativeMonthly(ArrayList<TimetableEvent> list){
        int size = list.size();
        TimetableEvent e; int i = 0; int count = 0;
        while(i<size && count<size){
            count++;
            e = list.get(i);
            if (e.getAllDay()){
                if(e.getEnd().getDayOfMonth()< currentDateTime.getDayOfMonth()){
                    list.remove(e); list.add(e); continue;
                }
            }
            else if(e.getEnd().getDayOfMonth()< currentDateTime.getDayOfMonth() || (e.getEnd().getDayOfMonth() == currentDateTime.getDayOfMonth() && e.getEnd().toLocalTime().isBefore(currentDateTime.toLocalTime()))){
                list.remove(e); list.add(e); continue;
            }
            i++;
        }
    }
    public void sortListRelativeYearly(ArrayList<TimetableEvent> list){
        int size = list.size();
        TimetableEvent e; int i = 0; int count = 0;
        while(i<size && count<size){
            count++;
            e = list.get(i);
            if (e.getAllDay()){
                if(e.getEnd().getDayOfYear()< currentDateTime.getDayOfYear()){
                    list.remove(e); list.add(e); continue;
                }
            }
            else if(e.getEnd().getDayOfYear()< currentDateTime.getDayOfYear() || (e.getEnd().getDayOfYear() == currentDateTime.getDayOfYear() && e.getEnd().toLocalTime().isBefore(currentDateTime.toLocalTime()))){
                list.remove(e); list.add(e); continue;
            }
            i++;
        }
    }
}