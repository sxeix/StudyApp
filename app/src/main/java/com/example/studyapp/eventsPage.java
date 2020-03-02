package com.example.studyapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.widget.RecyclerView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import studynowbackend.Timetable;
import studynowbackend.TimetableEvent;
import android.widget.ListAdapter;
import android.view.View.MeasureSpec;

public class eventsPage extends AppCompatActivity {
    ListView vCustom;
    ListView vDaily;
    ListView vWeekly;
    ListView vMonthly;
    ListView vYearly;
    ArrayList<HashMap<String, String>> lCustom;
    ArrayList<HashMap<String, String>> lDaily;
    ArrayList<HashMap<String, String>> lWeekly;
    ArrayList<HashMap<String, String>> lMonthly;
    ArrayList<HashMap<String, String>> lYearly;
    SimpleAdapter aCustom;
    SimpleAdapter aDaily;
    SimpleAdapter aWeekly;
    SimpleAdapter aMonthly;
    SimpleAdapter aYearly;

    SharedPrefs sharedPrefs;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /** This clump basically does everything. don't worry about it
         * only relevant things are the 5 ListView objects for each event type */
        lCustom = new ArrayList<HashMap<String, String>>();
        lDaily = new ArrayList<HashMap<String, String>>();
        lWeekly = new ArrayList<HashMap<String, String>>();
        lMonthly = new ArrayList<HashMap<String, String>>();
        lYearly = new ArrayList<HashMap<String, String>>();
        for (TimetableEvent e : Timetable.getInstance().getEvents()) { addToArrayList(e); }
        for (TimetableEvent e : Timetable.getInstance().getDailyEvents()) { addToArrayList(e); }
        for (TimetableEvent e : Timetable.getInstance().getWeeklyEvents()) { addToArrayList(e); }
        for (TimetableEvent e : Timetable.getInstance().getMonthlyEvents()) { addToArrayList(e); }
        for (TimetableEvent e : Timetable.getInstance().getYearlyEvents()) { addToArrayList(e); }
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
        setListViewHeightBasedOnChildren(vCustom);
        vDaily = (ListView) findViewById(R.id.dailyEventsListView);
        vDaily.setAdapter(aDaily);
        setListViewHeightBasedOnChildren(vDaily);
        vWeekly = (ListView) findViewById(R.id.weeklyEventsListView);
        vWeekly.setAdapter(aWeekly);
        setListViewHeightBasedOnChildren(vWeekly);
        vMonthly = (ListView) findViewById(R.id.monthlyEventsListView);
        vMonthly.setAdapter(aMonthly);
        setListViewHeightBasedOnChildren(vMonthly);
        vYearly = (ListView) findViewById(R.id.yearlyEventsListView);
        vYearly.setAdapter(aYearly);
        setListViewHeightBasedOnChildren(vYearly);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lCustom);
        final ArrayAdapter arrayAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lYearly);
        final ArrayAdapter arrayAdapter3 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lMonthly);
        final ArrayAdapter arrayAdapter4 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lWeekly);
        final ArrayAdapter arrayAdapter5 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lDaily);

        /** Ignore all bellow for now. Still working on popup window or page*/
        vCustom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final PopupMenu popup = new PopupMenu(eventsPage.this, view);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.description:
                                Intent description = new Intent(eventsPage.this, infoPage.class);
                                for(TimetableEvent e: Timetable.getInstance().getEvents()) {
                                    description.putExtra("Description", e.getDescription().toString());
                                }
                                startActivity(description);
                                return true;
                            case R.id.delete:
                                lCustom.remove(position);
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        vDaily.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final PopupMenu popup = new PopupMenu(eventsPage.this, view);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.description:
                                Intent description = new Intent(eventsPage.this, infoPage.class);
                                for(TimetableEvent e: Timetable.getInstance().getEvents()) {
                                    description.putExtra("Description", e.getDescription().toString());
                                }
                                startActivity(description);
                                return true;
                            case R.id.delete:
                                lDaily.remove(position);
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        vWeekly.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final PopupMenu popup = new PopupMenu(eventsPage.this, view);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.description:
                                Intent description = new Intent(eventsPage.this, infoPage.class);
                                for(TimetableEvent e: Timetable.getInstance().getEvents()) {
                                    description.putExtra("Description", e.getDescription().toString());
                                }
                                startActivity(description);
                                return true;
                            case R.id.delete:
                                lWeekly.remove(position);
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        vMonthly.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final PopupMenu popup = new PopupMenu(eventsPage.this, view);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.description:
                                Intent description = new Intent(eventsPage.this, infoPage.class);
                                for(TimetableEvent e: Timetable.getInstance().getEvents()) {
                                    description.putExtra("Description", e.getDescription().toString());
                                }
                                startActivity(description);
                                return true;
                            case R.id.delete:
                                lMonthly.remove(position);
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        vYearly.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final PopupMenu popup = new PopupMenu(eventsPage.this, view);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.description:
                                Intent description = new Intent(eventsPage.this, infoPage.class);
                                for(TimetableEvent e: Timetable.getInstance().getEvents()) {
                                    description.putExtra("Description", e.getDescription().toString());
                                }
                                startActivity(description);
                                return true;
                            case R.id.delete:
                                lYearly.remove(position);
                                return true;
                        }
                        return false;
                    }
                });
                popup.show();
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

    /** changes list view height depending on the number of items it has*/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // TODO will deal with when i can be bothered
            return;
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

    /** Adds events to correct list depending on event type*/
    public void addByFreq(HashMap<String, String> item, TimetableEvent x){
        switch (x.getRepeatFrequency()){
            case NoRepeat:
                lCustom.add(item); break;
            case Daily:
                lDaily.add(item); break;
            case Weekly:
                lWeekly.add(item); break;
            case Monthly:
                lMonthly.add(item); break;
            case Yearly:
                lYearly.add(item); break;
        }
    }

    /** creates HashMap of event and then adds to array*/
    public void addToArrayList(TimetableEvent e){
        HashMap<String, String> item = new HashMap<String, String>();
        item.put("line1", "Event: " + e.getName());
        item.put("line2", "Start Date: " + routineCheck(e));
        item.put("line3", "Start Time: " + dailyCheck(e));
        item.put("line4", "Location: " + e.getLocation());
        addByFreq(item, e);
    }
    @TargetApi(26)
    public String routineCheck(TimetableEvent e){
        switch(e.getRepeatFrequency()){
            case NoRepeat:
                return InputPage.formatCharacter(e.getStart().getDayOfMonth()) + "/" + InputPage.formatCharacter(e.getStart().getMonthValue()) + "/" + InputPage.formatCharacter(e.getStart().getYear());
            case Daily:
                return "Daily";
            case Weekly:
                return e.getStart().getDayOfWeek().name();
            case Monthly:
                int i = e.getStart().getDayOfMonth()%10;
                if(i == 1 && e.getStart().getDayOfMonth()!=11){return e.getStart().getDayOfMonth() + "st";}
                else if (i == 2 && e.getStart().getDayOfMonth()!=12){return e.getStart().getDayOfMonth() + "nd";}
                else if (i == 3 && e.getStart().getDayOfMonth()!=13){return e.getStart().getDayOfMonth() + "rd";}
                return e.getStart().getDayOfMonth() + "th";
            case Yearly:
                return InputPage.formatCharacter(e.getStart().getDayOfMonth()) + "/" + InputPage.formatCharacter(e.getStart().getMonthValue());
            default:
                return "oops";
        }
    }
    @TargetApi(26)
    public static String dailyCheck(TimetableEvent e){
        if (e.getAllDay()){ return "All Day"; }
        return InputPage.formatCharacter(e.getStart().getHour()) + ":" + InputPage.formatCharacter(e.getStart().getMinute());
    }
}