package com.example.studyapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class adding_modules extends AppCompatActivity {
    SharedPrefs sharedPrefs;
    private TextView displayStart;
    private TextView displayStartDate;
    private TextView displayEnd;
    private TextView displayEndDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private DatePickerDialog.OnDateSetListener enddateSetListener;

    private Switch layoutLecture;
    private LinearLayout lLayout;

    private Switch classLayout;
    private LinearLayout cLayout;

    TextView lecture_Start_Time;
    TextView lecture_display_start;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinuet;
    String ampm;
    TextView lecture_End_Time;
    TextView lecture_display_end;

    TextView class_Start_Time;
    TextView class_display_start;
    TimePickerDialog classTimePicker;
    Calendar classCalendar;
    int CcurrentHour;
    int CcurrentMinute;
    TextView class_End_Time;
    TextView class_display_end;
    private static final String TAG = "addingModules";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPrefs = new SharedPrefs(this);
        /**Imports theme mode user preferences*/
        if (sharedPrefs.loadNightMode()) {
            setTheme(R.style.LightMode);
        } else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_modules);
        configureButton();
        /**calls toolbar by ID, created in layout/toolbar.xml and activity.main.xml.*/
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_adding_modules));

        displayStart = (TextView) findViewById(R.id.startDate);
        displayStartDate = (TextView) findViewById(R.id.startDate2);

        displayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(adding_modules.this, android.R.style.Theme_Holo_Dialog_MinWidth, dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                displayStartDate.setText(date);
            }
        };

        displayEnd = (TextView) findViewById(R.id.endDate);
        displayEndDate = (TextView) findViewById(R.id.endDate2);

        displayEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(adding_modules.this, android.R.style.Theme_Holo_Dialog_MinWidth, enddateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        enddateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyyy: " + month + "/" + dayOfMonth + "/" + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                displayEndDate.setText(date);
            }
        };

        layoutLecture = (Switch) findViewById(R.id.lectureSwitch);
        lLayout = (LinearLayout) findViewById(R.id.lectures);
        layoutLecture.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lLayout.setVisibility(View.VISIBLE);
                } else {
                    lLayout.setVisibility(View.GONE);
                }
            }
        });

        classLayout = (Switch) findViewById(R.id.classSwitch);
        cLayout = (LinearLayout) findViewById(R.id.classes);
        classLayout.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cLayout.setVisibility(View.VISIBLE);
                } else {
                    cLayout.setVisibility(View.GONE);
                }
            }
        });

        lecture_Start_Time = (TextView) findViewById(R.id.lecture_Start_Time);
        lecture_display_start = (TextView) findViewById(R.id.lecture_Start_Time2);
        lecture_Start_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinuet = calendar.get(Calendar.MINUTE);


                timePickerDialog = new TimePickerDialog(adding_modules.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            ampm = "PM";
                        } else {
                            ampm = "AM";
                        }
                        lecture_display_start.setText(String.format("%02d:%02d", hourOfDay, minute) + ampm);
                    }
                }, currentHour, currentMinuet, false);
                timePickerDialog.show();
            }


        });
        lecture_End_Time = (TextView) findViewById(R.id.lecture_End_Time);
        lecture_display_end = (TextView) findViewById(R.id.lecture_End_Time2);
        lecture_End_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinuet = calendar.get(Calendar.MINUTE);


                timePickerDialog = new TimePickerDialog(adding_modules.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            ampm = "PM";
                        } else {
                            ampm = "AM";
                        }
                        lecture_display_end.setText(String.format("%02d:%02d", hourOfDay, minute) + ampm);
                    }
                }, currentHour, currentMinuet, false);
                timePickerDialog.show();
            }
        });

        class_Start_Time = (TextView) findViewById(R.id.class_Start_Time);
        class_display_start = (TextView) findViewById(R.id.class_Start_Time2);
        class_Start_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classCalendar = Calendar.getInstance();
                CcurrentHour = classCalendar.get(Calendar.HOUR_OF_DAY);
                CcurrentMinute = classCalendar.get(Calendar.MINUTE);

                classTimePicker = new TimePickerDialog(adding_modules.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            ampm = "PM";
                        } else {
                            ampm = "AM";
                        }
                        class_display_start.setText(String.format("%02d:%02d", hourOfDay, minute) + ampm);
                    }
                }, CcurrentHour, CcurrentMinute, false);
                classTimePicker.show();
            }
        });
        class_End_Time = (TextView) findViewById(R.id.class_End_Time);
        class_display_end = (TextView) findViewById(R.id.class_End_Time2);
        class_End_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classCalendar = Calendar.getInstance();
                CcurrentHour = classCalendar.get(Calendar.HOUR_OF_DAY);
                CcurrentMinute = classCalendar.get(Calendar.MINUTE);

                classTimePicker = new TimePickerDialog(adding_modules.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay >= 12) {
                            ampm = "PM";
                        } else {
                            ampm = "AM";
                        }
                        class_display_end.setText(String.format("%02d:%02d", hourOfDay, minute) + ampm);
                    }
                }, CcurrentHour, CcurrentMinute, false);
                classTimePicker.show();
            }
        });
    }

    private void configureButton() {
        Button addModule = (Button) findViewById(R.id.buttonAddModules);
        addModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Module added", Toast.LENGTH_SHORT).show();
            }
        });
    }
}