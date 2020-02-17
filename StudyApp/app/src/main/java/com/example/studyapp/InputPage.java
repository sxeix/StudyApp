package com.example.studyapp;

import studynowbackend.Timetable;
import studynowbackend.TimetableEvent;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.lang.annotation.Target;
import java.time.LocalDateTime;


import org.w3c.dom.Text;

// import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class InputPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button b_pick;
    TextView tv_result, tv_result2;

    Button endDT;
    TextView endDateResult, endTimeResult;

    Button addEvent;

    EditText title, location, description;
    String t, l, d;

    /** These integers can be used to be passed into the event class*/
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    boolean set1 = false, set2 = false;
    LocalDateTime start, end;
    int option = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        b_pick = (Button) findViewById(R.id.pickDate);
        tv_result = (TextView) findViewById(R.id.dateTV);
        tv_result2 = (TextView) findViewById(R.id.timeTV);

        b_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option = 1;
                calendarOpen();
            }
        });

        endDT = (Button) findViewById(R.id.pickEndDate);
        endDateResult = (TextView) findViewById(R.id.dateTVend);
        endTimeResult = (TextView) findViewById(R.id.timeTVend);

        endDT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option = 2;
                calendarOpen();
            }
        });

        title = (EditText) findViewById(R.id.eventTitleInput);
        location = (EditText) findViewById(R.id.eventLocationInput);
        description = (EditText) findViewById(R.id.eventDescInput);
        addEvent = (Button) findViewById(R.id.addEventButton);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t = title.getText().toString();
                l = location.getText().toString();
                d = description.getText().toString();

                if (set1 && set2 && t.equals("") == false && l.equals("") == false && d.equals("") == false ) {
                    Toast.makeText(InputPage.this, "Event added", Toast.LENGTH_SHORT).show();
                    TimetableEvent x = new TimetableEvent(t, d, l, start, end);
                    eventsPage.EVENTS.AddEventUnchecked(x);
                } else {
                    Toast.makeText(InputPage.this, "Input needed", Toast.LENGTH_SHORT).show();
                    /**Make a notification asking the user to input more information*/
                }
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
            Intent eventspage = new Intent(InputPage.this, eventsPage.class);
            startActivity(eventspage);
        }else if(id == R.id.about){
            Intent infopage = new Intent(InputPage.this, infoPage.class);
            startActivity(infopage);
        }else if(id == R.id.settings){
            Intent settings = new Intent(InputPage.this, settingsPage.class);
            startActivity(settings);
        }else if(id == android.R.id.home){
            this.finish();
        }
        return true;
    }

    // This function opens the calendar and time picker for the user
    public void calendarOpen() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(InputPage.this, InputPage.this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int y, int m, int d) {
        yearFinal = y;
        monthFinal = m + 1;
        dayFinal = d;

        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(InputPage.this, InputPage.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    /*
    This method will update the text displaying the time and date depending on whether the user is choosing a start or end time/date
     */
    @Override
    @TargetApi(26)
    public void onTimeSet(TimePicker view, int h, int m) {
        String date = formatCharacter(dayFinal) + formatCharacter(monthFinal) + formatCharacter(yearFinal);
        String time = formatCharacter(h) + formatCharacter(m) + "00";
        String dateTime = date + time;
        DateTimeFormatter format = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
        if (option == 1) {
            set1 = true;
            start = LocalDateTime.parse(dateTime, format);
            hourFinal = h;
            minuteFinal = m;

            tv_result.setText("Start Date: " + dayFinal + "/" + monthFinal + "/" + yearFinal);
            if(minuteFinal > 10) {
                tv_result2.setText("Start Time: " + hourFinal + ":" + minuteFinal);
            } else {
                tv_result2.setText("Start Time: " + hourFinal + ":0" + minuteFinal);
            }
        } else if (option == 2) {
            set2 = true;
            end = LocalDateTime.parse(dateTime, format);
            hourFinal = h;
            minuteFinal = m;
            endDateResult.setText("End Date: " + dayFinal + "/" + monthFinal + "/" + yearFinal);
            if(minuteFinal > 10) {
                endTimeResult.setText("End Time: " + hourFinal + ":" + minuteFinal);
            } else {
                endTimeResult.setText("End Time: " + hourFinal + ":0" + minuteFinal);
            }

        }
    }
    public String formatCharacter(int a) {
        if (a<10) {
            return "0" + Integer.toString(a);
        }
        return Integer.toString(a);
    }

}