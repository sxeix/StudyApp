package com.example.studyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.renderscript.ScriptGroup;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.CalendarView;
import java.util.Calendar;
import studynowbackend.Timetable;
import studynowbackend.TimetableEvent;
import java.util.GregorianCalendar;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

import android.annotation.TargetApi;

public class MainActivity extends AppCompatActivity {
    SharedPrefs sharedPrefs;
    public static CalendarView calendar;
    public static int sDay, sMonth, sYear, dayOfWeek;
    public static LocalDate localDate;
    public Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPrefs = new SharedPrefs(this);
        Timetable.getInstance().load(this);


        /**Imports theme mode user preferences*/
        if (sharedPrefs.loadNightMode()) {
            setTheme(R.style.LightMode);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureNextButton();
        /**calls toolbar by ID, created in layout/toolbar.xml and activity.main.xml.*/
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);

        calendar = (CalendarView) findViewById(R.id.calendarView);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                sYear = year;
                sMonth = month + 1;
                sDay = dayOfMonth;
                Calendar calendar = new GregorianCalendar(year, month, dayOfMonth);
                dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                localDate = LocalDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate();
                startActivity(new Intent(MainActivity.this, calendarDate.class));
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
            Intent eventspage = new Intent(MainActivity.this, eventsPage.class);
            startActivity(eventspage);
        } else if (id == R.id.about) {
            Intent infopage = new Intent(MainActivity.this, infoPage.class);
            startActivity(infopage);
        } else if (id == R.id.settings) {
            Intent settings = new Intent(MainActivity.this, settingsPage.class);
            startActivity(settings);
        }
        return true;
    }

    private void configureNextButton() {
        Button nextButton = (Button) findViewById(R.id.AddEvent);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InputPage.class));
            }
        });
    }
}