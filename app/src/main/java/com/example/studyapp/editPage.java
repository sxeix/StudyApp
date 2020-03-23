package com.example.studyapp;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
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
import android.widget.Toast;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import studynowbackend.RepeatFrequency;
import studynowbackend.Timetable;
import studynowbackend.TimetableEvent;


public class editPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemSelectedListener  {
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

    // Variable to define the repeat frequency selected by the user
    RepeatFrequency spinnerOption;

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
        setContentView(R.layout.activity_edit_page);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_edit_page));
        // Spinner code
        Spinner mySpinner = (Spinner)findViewById(R.id.editroutine);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(editPage.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Repeat));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        mySpinner.setOnItemSelectedListener(this);
        // Following code initializes two buttons that are used to indicate all day and routine events#
        // Each switch shares the same function to update the variable holding the boolean
        Switch allSwitch = (Switch) findViewById(R.id.editdayEventSwitch);
        allSwitch.setOnCheckedChangeListener(this);


        // Starts the sequence of methods to get the user's start date and time input
        b_pick = (Button) findViewById(R.id.editpickDate);
        tv_result = (TextView) findViewById(R.id.editdateTV);
        tv_result2 = (TextView) findViewById(R.id.edittimeTV);

        b_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                option = 1;
                calendarOpen();
            }
        });

        // Starts the sequence of methods to get the user's end date and time input
        endDT = (Button) findViewById(R.id.editpickEndDate);
        endDateResult = (TextView) findViewById(R.id.editdateTVend);
        endTimeResult = (TextView) findViewById(R.id.edittimeTVend);

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
        title = (EditText) findViewById(R.id.editeventTitleInput);
        location = (EditText) findViewById(R.id.editeventLocationInput);
        description = (EditText) findViewById(R.id.editeventDescInput);
        addEvent = (Button) findViewById(R.id.editEventButton);

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allSwitch.isChecked()) allDayEventBool = true;
                t = title.getText().toString();
                l = location.getText().toString();
                d = description.getText().toString();
                if (set1 && set2 && !t.isEmpty() && !l.isEmpty() && !d.isEmpty()) {
                    TimetableEvent x = new TimetableEvent(t, d, l, start, end, allDayEventBool, spinnerOption);
                    Timetable.getInstance().removeEvent(eventsPage.selectedEvent);
                    Timetable.getInstance().AddEvent(x);
                    InputPage.sortListByType(x);
                    Intent mainActivity = new Intent(editPage.this, MainActivity.class);
                    startActivity(mainActivity);
                    Toast.makeText(editPage.this, getResources().getString(R.string.event_edited), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(editPage.this, getResources().getString(R.string.input_needed), Toast.LENGTH_SHORT).show();
                }
            }
        });
        title.setText(eventsPage.selectedEvent.getName());
        location.setText(eventsPage.selectedEvent.getLocation());
        description.setText(eventsPage.selectedEvent.getDescription());
        mySpinner.setSelection(eventsPage.selectedEvent.getRepeatFrequency().ordinal());
        tv_result.setText(getResources().getString(R.string.start_date) + "\n" + String.format(getResources().getString(R.string.full_date), requiresFormat(eventsPage.selectedEvent.getStart().getDayOfMonth()) + eventsPage.selectedEvent.getStart().getDayOfMonth(), requiresFormat(eventsPage.selectedEvent.getStart().getMonthValue()) + eventsPage.selectedEvent.getStart().getMonthValue(), eventsPage.selectedEvent.getStart().getYear()));
        tv_result2.setText(getResources().getString(R.string.start_time) + "\n " + String.format(getResources().getString(R.string.time), requiresFormat(eventsPage.selectedEvent.getStart().getHour()) + eventsPage.selectedEvent.getStart().getHour(), requiresFormat(eventsPage.selectedEvent.getStart().getMinute()) + eventsPage.selectedEvent.getStart().getMinute()));
        endDateResult.setText("  " + getResources().getString(R.string.end_date) + "\n   " + String.format(getResources().getString(R.string.full_date), requiresFormat(eventsPage.selectedEvent.getEnd().getDayOfMonth()) + eventsPage.selectedEvent.getEnd().getDayOfMonth(), requiresFormat(eventsPage.selectedEvent.getEnd().getMonthValue()) + eventsPage.selectedEvent.getEnd().getMonthValue(), eventsPage.selectedEvent.getEnd().getYear()));
        endTimeResult.setText("  " + getResources().getString(R.string.end_time) + "\n   " + String.format(getResources().getString(R.string.time), requiresFormat(eventsPage.selectedEvent.getEnd().getHour()) + eventsPage.selectedEvent.getEnd().getHour(), requiresFormat(eventsPage.selectedEvent.getEnd().getMinute()) + eventsPage.selectedEvent.getEnd().getMinute()));
        allSwitch.setChecked(eventsPage.selectedEvent.getAllDay());
        set1 = true; set2 = true;
        start = eventsPage.selectedEvent.getStart();
        end = eventsPage.selectedEvent.getEnd();
    }

    // This method is used for updating the switches and changing them from true to false
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.getId() == R.id.dayEventSwitch) allDayEventBool = isChecked;
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
            Intent eventspage = new Intent(editPage.this, eventsPage.class);
            startActivity(eventspage);
        }else if(id == R.id.about){
            Intent infopage = new Intent(editPage.this, infoPage.class);
            startActivity(infopage);
        }else if(id == R.id.settings){
            Intent settings = new Intent(editPage.this, settingsPage.class);
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(editPage.this, editPage.this, year, month, day);
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(editPage.this, editPage.this, hour, minute, DateFormat.is24HourFormat(this));
        timePickerDialog.show();
    }

    /*
    This method will update the text displaying the time and date depending on whether the user is choosing a start or end time/date
     */
    @Override
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

    // This method is used for displaying date and time values correctly
    // If the value is less than 10 then it adds a '0' to the start of a string with the number
    public static String formatCharacter(int a) {
        if (a<10) {
            return "0" + a;
        }
        return Integer.toString(a);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0: // Do Not Repeat
                spinnerOption = RepeatFrequency.NoRepeat; break;
            case 1: // Daily
                spinnerOption = RepeatFrequency.Daily; break;
            case 2: // Weekly
                spinnerOption = RepeatFrequency.Weekly; break;
            case 3: // Monthly
                spinnerOption = RepeatFrequency.Monthly; break;
            case 4: // Yearly
                spinnerOption = RepeatFrequency.Yearly; break;
            default:
                Toast.makeText(editPage.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String requiresFormat(int a) {
        if (a < 10) {
            return getResources().getString(R.string.formatted_character);
        }
        return "";
    }
}