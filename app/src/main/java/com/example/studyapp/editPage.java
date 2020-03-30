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
                t = title.getText().toString();
                l = location.getText().toString();
                d = description.getText().toString();
                if (set1 && set2 && !t.isEmpty() && !l.isEmpty() && !d.isEmpty()) {
                    TimetableEvent x = new TimetableEvent(t, d, l, start, end, allDayEventBool, spinnerOption);
                    Timetable.getInstance().AddEvent(x);
                    sortListByType(x);
                    Toast.makeText(editPage.this, "Event Created", Toast.LENGTH_SHORT).show();
                } else {
                    // TODO Remove this method in the future after testing is complete
                    testEvents();
                    Toast.makeText(editPage.this, "Input needed", Toast.LENGTH_SHORT).show();
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

        Timetable.getInstance().AddEvent(a);
        sortListByType(a);

        String d3 = "2020-03-01 14:30";
        String d4 = "2020-03-01 15:30";
        TimetableEvent b = new TimetableEvent("Test2", "Desc2", "Location3",
                LocalDateTime.parse(d3, f), LocalDateTime.parse(d4, f), false, RepeatFrequency.Daily);

        Timetable.getInstance().AddEvent(b);
        sortListByType(b);

        String d5 = "2020-03-01 09:30";
        String d6 = "2020-03-01 11:30";
        TimetableEvent c = new TimetableEvent("Test3", "Desc3", "Location3",
                LocalDateTime.parse(d5, f), LocalDateTime.parse(d6, f), true, RepeatFrequency.NoRepeat);

        Timetable.getInstance().AddEvent(c);
        sortListByType(c);

        String d7 = "2020-03-01 16:30";
        String d8 = "2020-03-01 17:30";
        TimetableEvent d = new TimetableEvent("Test4", "Desc4", "Location4",
                LocalDateTime.parse(d7, f), LocalDateTime.parse(d8, f), true, RepeatFrequency.Weekly);

        Timetable.getInstance().AddEvent(d);
        sortListByType(d);
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
            tv_result.setText("Start Date:" + formatCharacter(dayFinal) + "/" + formatCharacter(monthFinal) + "/" + formatCharacter(yearFinal));
            tv_result2.setText("Start Time:" + formatCharacter(hourFinal) + ":" + formatCharacter(minuteFinal));
        } else if (option == 2) {
            set2 = true;
            end = LocalDateTime.parse(dateTime, format);
            hourFinal = h;
            minuteFinal = m;
            endDateResult.setText("  End Date:\n   " + formatCharacter(dayFinal) + "/" + formatCharacter(monthFinal) + "/" + formatCharacter(yearFinal));
            endTimeResult.setText("  End Time:\n   " + formatCharacter(hourFinal) + ":" + formatCharacter(minuteFinal));
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

    @TargetApi(26)
    public void sortList(TimetableEvent a, ArrayList<TimetableEvent> list){
        if (list.size() > 1) {
            if(a.getAllDay()){
                list.remove(a);
                list.add(0, a);
                return;
            }
            int index = list.indexOf(a);
            while (index > 0) {
                if (a.getStart().isBefore(list.get(index-1).getStart()))index--;
                else {
                    list.remove(a);
                    list.add(index, a);
                    return;
                }
            }
            list.remove(a);
            list.add(0, a);
        }
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

    public void sortListByType(TimetableEvent x){
        switch(x.getRepeatFrequency()){
            case NoRepeat:
                sortList(x, Timetable.getInstance().getEvents()); break;
            case Daily:
                sortList(x, Timetable.getInstance().getDailyEvents()); break;
            case Weekly:
                sortList(x, Timetable.getInstance().getWeeklyEvents()); break;
            case Monthly:
                sortList(x, Timetable.getInstance().getMonthlyEvents()); break;
            case Yearly:
                sortList(x, Timetable.getInstance().getYearlyEvents()); break;
            default:
                return;
        }
    }
}