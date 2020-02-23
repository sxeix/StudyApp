package com.example.studyapp;

import studynowbackend.Timetable;
import studynowbackend.TimetableEvent;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.time.LocalDateTime;


// import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class InputPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, CompoundButton.OnCheckedChangeListener {
    SharedPrefs sharedPrefs;
    // These variables are for selecting the Date and Time for start time and end time
    Button b_pick;
    TextView tv_result, tv_result2;
    Button endDT;
    TextView endDateResult, endTimeResult;

    // This is the variable for the addEvent button
    Button addEvent;

    // These are the variables regarding the title, location and description of an event
    EditText title, location, description;
    String t, l, d;

    /** These integers can be used to be passed into the event class*/
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;

    // Set1 and set2 are used to determine whether a start date and end time have been selected
    boolean set1 = false, set2 = false;
    // The two following booleans are for the switches
    boolean allDayEventBool, routineEventBool;
    // LocalDateTime variables to hold the start time and end time before being passed to the instance creation of an event
    LocalDateTime start, end;
    // Variable, option, is used to indicate whether the start time is being selected or the end time
    int option = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**Imports theme mode user preferences*/
        sharedPrefs = new SharedPrefs(this);
        if(sharedPrefs.loadNightMode()){
            setTheme(R.style.LightMode);
        }else{
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Following code initializes two buttons that are used to indicate all day and routine events#
        // Each switch shares the same function to update the variable holding the boolean
        Switch allSwitch = (Switch) findViewById(R.id.dayEventSwitch);
        allSwitch.setOnCheckedChangeListener(this);

        Switch routineSwitch = (Switch) findViewById(R.id.routineEventSwitch);
        routineSwitch.setOnCheckedChangeListener(this);


        // Starts the sequence of methods to get the user's start date and time input
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

        // Starts the sequence of methods to get the user's end date and time input
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


        // AddEvent button is declared along with instances of string input
        // When AddEvent button is clicked, strings from the input boxes are retrieved
        // Furthermore, event is created and inserted into the TimeTable
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

                if (set1 && set2 && t.equals("") == false && l.equals("") == false && d.equals("") == false) {
                    if (!allDayEventBool && !routineEventBool) {
                        Toast.makeText(InputPage.this, "Custom event added", Toast.LENGTH_SHORT).show();
                        TimetableEvent x = new TimetableEvent(t, d, l, start, end);
                        Timetable.getInstance().AddEventUnchecked(x);
                    } else if (allDayEventBool && !routineEventBool){
                        Toast.makeText(InputPage.this, "All Day Event Selected", Toast.LENGTH_SHORT).show();
                    } else if (!allDayEventBool && routineEventBool){
                        Toast.makeText(InputPage.this, "Routine Event Selected", Toast.LENGTH_SHORT).show();
                    } else Toast.makeText(InputPage.this, "Only select one Switch", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(InputPage.this, "Input needed", Toast.LENGTH_SHORT).show();
                    /**Make a notification asking the user to input more information*/
                }
            }
        });
    }

    // This method is used for updating the switches and changing them from true to false
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.dayEventSwitch) allDayEventBool = isChecked;
        else if (buttonView.getId() == R.id.routineEventSwitch) routineEventBool = isChecked;
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
            tv_result.setText("Start Date: " + formatCharacter(dayFinal) + "/" + formatCharacter(monthFinal) + "/" + formatCharacter(yearFinal));
            tv_result2.setText("Start Time: " + formatCharacter(hourFinal) + ":" + formatCharacter(minuteFinal));
        } else if (option == 2) {
            set2 = true;
            end = LocalDateTime.parse(dateTime, format);
            hourFinal = h;
            minuteFinal = m;
            endDateResult.setText("End Date: " + formatCharacter(dayFinal) + "/" + formatCharacter(monthFinal) + "/" + formatCharacter(yearFinal));
            endTimeResult.setText("End Time: " + formatCharacter(hourFinal) + ":" + formatCharacter(minuteFinal));
        }
    }

    // This method is used for displaying date and time values correctly
    // If the value is less than 10 then it adds a '0' to the start of a string with the number
    public String formatCharacter(int a) {
        if (a<10) {
            return "0" + a;
        }
        return Integer.toString(a);
    }

}