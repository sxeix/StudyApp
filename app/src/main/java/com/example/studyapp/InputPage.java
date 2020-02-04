package com.example.studyapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.text.format.DateFormat;

import org.w3c.dom.Text;

// import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class InputPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    Button b_pick;
    TextView tv_result, tv_result2;

    Button endDT;
    TextView endDateResult, endTimeResult;

    // These integers can be used to be passed into the event class
    int day, month, year, hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    
    int option = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_page);


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

        configureBackButton();
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

    private void configureBackButton() {
        Button backButton = (Button) findViewById(R.id.BackPage);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
    public void onTimeSet(TimePicker view, int h, int m) {
        if (option == 1) {
            hourFinal = h;
            minuteFinal = m;

            tv_result.setText("Start Date: " + dayFinal + "/" + monthFinal + "/" + yearFinal);
            if(minuteFinal > 10) {
                tv_result2.setText("Start Time: " + hourFinal + ":" + minuteFinal);
            } else {
                tv_result2.setText("Start Time: " + hourFinal + ":0" + minuteFinal);
            }
        } else if (option == 2) {
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
}
