package com.example.studyapp;

import studynowbackend.RepeatFrequency;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class InputPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener {
    // Dynamic Variables

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

    // These integers can be used to be passed into the event class

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

    // Variable to define the repeat frequency selected by the user
    RepeatFrequency spinnerOption;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        /**Imports theme mode user preferences*/
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


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_page);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_input_page));


        // Spinner code
        Spinner mySpinner = (Spinner) findViewById(R.id.routine);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(InputPage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Repeat));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        mySpinner.setOnItemSelectedListener(this);


        // Following code initializes two buttons that are used to indicate all day and routine events#
        // Each switch shares the same function to update the variable holding the boolean
        Switch allSwitch = (Switch) findViewById(R.id.dayEventSwitch);
        allSwitch.setOnCheckedChangeListener(this);


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


         /* AddEvent button is declared along with instances of string input
         When AddEvent button is clicked, strings from the input boxes are retrieved
         Furthermore, event is created and inserted into the TimeTable */
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
                if (set1 && set2 && !t.isEmpty() && !l.isEmpty() && !d.isEmpty()) {
                    TimetableEvent x = new TimetableEvent(t, d, l, start, end, allDayEventBool, spinnerOption);
                    Timetable.getInstance().addEvent(x);
                    sortListByType(x);
                    Intent mainActivity = new Intent(InputPage.this, MainActivity.class);
                    startActivity(mainActivity);
                    Toast.makeText(InputPage.this, getResources().getString(R.string.event_added), Toast.LENGTH_SHORT).show();
                } else {
                    // TODO Remove this method in the future after testing is complete
                    testEvents();
                    Toast.makeText(InputPage.this, getResources().getString(R.string.input_needed), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @TargetApi(26)
    public void testEvents() {
        String d1 = "2020-03-01 12:30";
        String d2 = "2020-03-01 13:30";
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        TimetableEvent a = new TimetableEvent("Test1", "Desc1", "Location1",
                LocalDateTime.parse(d1, f), LocalDateTime.parse(d2, f), false, RepeatFrequency.NoRepeat);

        Timetable.getInstance().addEvent(a);
        sortListByType(a);

        String d3 = "2020-03-01 14:30";
        String d4 = "2020-03-01 15:30";
        TimetableEvent b = new TimetableEvent("Test2", "Desc2", "Location3",
                LocalDateTime.parse(d3, f), LocalDateTime.parse(d4, f), false, RepeatFrequency.Daily);

        Timetable.getInstance().addEvent(b);
        sortListByType(b);

        String d5 = "2020-03-01 09:30";
        String d6 = "2020-03-01 11:30";
        TimetableEvent c = new TimetableEvent("Test3", "Desc3", "Location3",
                LocalDateTime.parse(d5, f), LocalDateTime.parse(d6, f), true, RepeatFrequency.NoRepeat);

        Timetable.getInstance().addEvent(c);
        sortListByType(c);

        String d7 = "2020-03-01 16:30";
        String d8 = "2020-03-01 17:30";
        TimetableEvent d = new TimetableEvent("Test4", "Desc4", "Location4",
                LocalDateTime.parse(d7, f), LocalDateTime.parse(d8, f), true, RepeatFrequency.Weekly);

        Timetable.getInstance().addEvent(d);
        sortListByType(d);
    }



    /**
     * This method is used for updating the switches and changing them from true to false
     *
     * @param buttonView button that is being referred to.
     * @param isChecked true or false depending on user's choice.
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.dayEventSwitch) allDayEventBool = isChecked;
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
            Intent eventspage = new Intent(InputPage.this, eventsPage.class);
            startActivity(eventspage);
        } else if (id == R.id.about) {
            Intent infopage = new Intent(InputPage.this, infoPage.class);
            startActivity(infopage);
        } else if (id == R.id.settings) {
            Intent settings = new Intent(InputPage.this, settingsPage.class);
            startActivity(settings);
        } else if(id == R.id.addModules){
            Intent modules = new Intent(InputPage.this, adding_modules.class);
            startActivity(modules);
        }else if (id == android.R.id.home) {
            Intent modules = new Intent(InputPage.this, MainActivity.class);
            startActivity(modules);
        }
        return true;
    }



    /**
     * This function retrieves the user's date choice
     */
    public void calendarOpen() {
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(InputPage.this, InputPage.this, year, month, day);
        datePickerDialog.show();
    }



    /**
     * Adjusts date for appropriate viewing and usage.
     * Takes user's input for time.
     *
     * @param view DatePicker for user input.
     * @param y selected year.
     * @param m selected month.
     * @param d selected day.
     */
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



    /**
     *  Finalizes user's input into variables and displays the current choice.
     *
     * @param view Used for user input.
     * @param h selected hour.
     * @param m selected minute.
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
            tv_result.setText(getResources().getString(R.string.start_date) + "\n" + String.format(getResources().getString(R.string.full_date), requiresFormat(dayFinal) + dayFinal, requiresFormat(monthFinal) + monthFinal, yearFinal));
            tv_result2.setText(getResources().getString(R.string.start_time) + "\n " + String.format(getResources().getString(R.string.time), requiresFormat(hourFinal) + hourFinal, requiresFormat(minuteFinal) + minuteFinal));
        } else if (option == 2) {
            set2 = true;
            end = LocalDateTime.parse(dateTime, format);
            hourFinal = h;
            minuteFinal = m;
            endDateResult.setText("  " + getResources().getString(R.string.end_date) + "\n   " + String.format(getResources().getString(R.string.full_date), requiresFormat(dayFinal) + dayFinal, requiresFormat(monthFinal) + monthFinal, yearFinal));
            endTimeResult.setText("  " + getResources().getString(R.string.end_time) + "\n   " + String.format(getResources().getString(R.string.time), requiresFormat(hourFinal) + hourFinal, requiresFormat(minuteFinal) + minuteFinal));
        }
    }



    /**
     * Ensures integers are displayed accordingly for date and time i.e. 2 becomes 02.
     *
     * @param a the date/time integer that needs fixing.
     * @return a string to be displayed.
     */
    public static String formatCharacter(int a) {
        if (a < 10) {
            return "0" + a;
        }
        return Integer.toString(a);
    }



    /**
     * Selects an event repeat frequency from the spinner.
     *
     * @param parent n/a
     * @param view n/a
     * @param position index of selected item on the spinner.
     * @param id associated with item selected.
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: // Do Not Repeat
                spinnerOption = RepeatFrequency.NoRepeat;
                break;
            case 1: // Daily
                spinnerOption = RepeatFrequency.Daily;
                break;
            case 2: // Weekly
                spinnerOption = RepeatFrequency.Weekly;
                break;
            case 3: // Monthly
                spinnerOption = RepeatFrequency.Monthly;
                break;
            case 4: // Yearly
                spinnerOption = RepeatFrequency.Yearly;
                break;
            default:
                Toast.makeText(InputPage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // does nothing
    }

    /**
     * takes a TimetableEvent and places it in the correct position in the list
     * @param a the TimetableEvent to be correctly sorted into the list
     * @param list Custom/Daily/Weekly/Monthly/Yearly events list
     * @param type 1 = custom, 2 = daily, 3 = weekly, 4 = monthly, 5 = yearly
     */
    public static void sortList(TimetableEvent a, ArrayList<TimetableEvent> list, int type) {
        if (list.size() > 1) {
            int index = list.indexOf(a);
            switch (type) {
                case 1:
                    while (index > 0) {
                        if (a.getAllDay()) {
                            if (a.getStart().toLocalDate().isAfter(list.get(index - 1).getStart().toLocalDate())) {
                                moveTimetableObject(list, a, index); return;
                            }index--;
                        } else if (a.getStart().toLocalDate().isBefore(list.get(index - 1).getStart().toLocalDate()) || (a.getStart().toLocalDate().isEqual(list.get(index - 1).getStart().toLocalDate()) && (a.getStart().toLocalTime().isBefore(list.get(index - 1).getStart().toLocalTime()) && !list.get(index - 1).getAllDay())))
                            index--;
                        else {
                            moveTimetableObject(list, a, index); return;
                        }
                    }moveTimetableObject(list, a, 0); break;
                case 2:
                    if (a.getAllDay()) {
                        moveTimetableObject(list, a, 0); return;
                    }
                    while (index > 0) {
                        if (a.getStart().toLocalTime().isBefore(list.get(index - 1).getStart().toLocalTime()) && !list.get(index - 1).getAllDay()) {
                            index--;
                        } else {
                            moveTimetableObject(list, a, index); return;
                        }
                    }moveTimetableObject(list, a, 0); break;
                case 3:
                    while (index > 0) {
                        if (a.getAllDay()) {
                            if (a.getStart().getDayOfWeek().getValue() > list.get(index - 1).getStart().getDayOfWeek().getValue()) {
                                moveTimetableObject(list, a, index); return;
                            }index--;
                        } else if (a.getStart().getDayOfWeek().getValue() < list.get(index - 1).getStart().getDayOfWeek().getValue() || (a.getStart().getDayOfWeek().getValue() == list.get(index - 1).getStart().getDayOfWeek().getValue() && (a.getStart().toLocalTime().isBefore(list.get(index - 1).getStart().toLocalTime()) && !list.get(index - 1).getAllDay())))
                            index--;
                        else {
                            moveTimetableObject(list, a, index); return;
                        }
                    }moveTimetableObject(list, a, 0); break;
                case 4:
                    while (index > 0) {
                        if (a.getAllDay()) {
                            if (a.getStart().getDayOfMonth() > list.get(index - 1).getStart().getDayOfMonth()) {
                                moveTimetableObject(list, a, index); return;
                            }index--;
                        } else if (a.getStart().getDayOfMonth() < list.get(index - 1).getStart().getDayOfMonth() || (a.getStart().getDayOfMonth() == list.get(index - 1).getStart().getDayOfMonth() && (a.getStart().toLocalTime().isBefore(list.get(index - 1).getStart().toLocalTime()) && !list.get(index - 1).getAllDay()))) {
                            index--;
                        } else {
                            moveTimetableObject(list, a, index); return;
                        }
                    }moveTimetableObject(list, a, 0); break;
                case 5:
                    while (index > 0) {
                        if (a.getAllDay()) {
                            if (a.getStart().getDayOfYear() > list.get(index - 1).getStart().getDayOfYear()) {
                                moveTimetableObject(list, a, index); return;
                            }index--;
                        } else if (a.getStart().getDayOfYear() < list.get(index - 1).getStart().getDayOfYear() || (a.getStart().getDayOfYear() == list.get(index - 1).getStart().getDayOfYear() && (a.getStart().toLocalTime().isBefore(list.get(index - 1).getStart().toLocalTime()) && !list.get(index - 1).getAllDay()))) {
                            index--;
                        } else {
                            moveTimetableObject(list, a, index); return;
                        }
                    }moveTimetableObject(list, a, 0); break;
            }
        }
    }

    /**
     * moves the TimetableEvent through the ArrayList
     * @param list the list that contains the TimetableEvent that will be moved
     * @param event the TimetableEvent to be moved
     * @param index the position in the list that it will be moved to
     */
    private static void moveTimetableObject(ArrayList<TimetableEvent> list, TimetableEvent event, int index){
        list.remove(event); list.add(index, event);
    }

    /**
     * takes the TimetableEvent and finds which array it is a part of and then runs the correct sortList method with the correct parameters
     * @param x the TimetableEvent instance
     */
    public static void sortListByType(TimetableEvent x) {
        switch (x.getRepeatFrequency()) {
            case NoRepeat:
                sortList(x, Timetable.getInstance().getEvents(), 1); break;
            case Daily:
                sortList(x, Timetable.getInstance().getDailyEvents(), 2); break;
            case Weekly:
                sortList(x, Timetable.getInstance().getWeeklyEvents(), 3); break;
            case Monthly:
                sortList(x, Timetable.getInstance().getMonthlyEvents(), 4); break;
            case Yearly:
                sortList(x, Timetable.getInstance().getYearlyEvents(), 5); break;
        }
    }

    /**
     * returns an extra 0 (or whatever character the language requires) if the number is less than 10 else it returns an empty string
     * is used to change dates from 1/3/2020 into 01/03/2020 or change times from 11:5 into 11:05
     * @param a the integer
     * @return either a String containing the formatted character with respect to the current language or an empty string
     */
    public String requiresFormat(int a) {
        if (a < 10) return getResources().getString(R.string.formatted_character);
        return "";
    }
}
