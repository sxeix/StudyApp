package com.example.studyapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import studynowbackend.TimetableEvent;

import static com.example.studyapp.R.drawable.custom_popup;

public class popupWindowDesc extends Activity {
    SharedPrefs sharedPrefs;
    String description;
    TimetableEvent event = eventsPage.selectedEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_window_desc);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.y = -20;
        params.x = 0;
        getWindow().setAttributes(params);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        /**Imports theme mode user preferences*/
        sharedPrefs = new SharedPrefs(this);
        if(sharedPrefs.loadNightMode()){
            setTheme(R.style.LightMode);
            ScrollView scrollview = (ScrollView)findViewById(R.id.popupLayout);
            scrollview.setBackgroundResource(custom_popup);
        }else{
            setTheme(R.style.AppTheme);
            ScrollView scrollview = (ScrollView)findViewById(R.id.popupLayout);
            scrollview.setBackgroundResource(R.drawable.custom_popup1);
        }

        /**Code that gets the information sent from clicking description buttons!*/
        TextView textReceived = findViewById(R.id.description_box);
        description = getIntent().getExtras().getString("Description");
        textReceived.setText(description);
        TextView title = findViewById(R.id.desc_name);
        title.setText(event.getName());
        TextView loc = findViewById(R.id.desc_loc);
        loc.setText(getResources().getString(R.string.location_input) + ": " + event.getLocation());
        TextView date = findViewById(R.id.desc_date);
        date.setText(getResources().getString(R.string.start_date) + " " + routineCheck(event));
        TextView start_time = findViewById(R.id.desc_start);
        start_time.setText(getResources().getString(R.string.start_time) + " " + dailyCheck(event, 0));
        TextView end_time = findViewById(R.id.desc_end);
        end_time.setText(getResources().getString(R.string.end_time) + " " + dailyCheck(event, 1));
        TextView type = findViewById(R.id.desc_type);
        type.setText(getResources().getString(R.string.event_type) + ": " + returnFreqString(event));

    }

    public String returnFreqString(TimetableEvent x) {
        switch (x.getRepeatFrequency()) {
            case NoRepeat:
                return getResources().getString(R.string.type_custom);
            case Daily:
                return getResources().getString(R.string.type_daily);
            case Weekly:
                return getResources().getString(R.string.type_weekly);
            case Monthly:
                return getResources().getString(R.string.type_monthly);
            case Yearly:
                return getResources().getString(R.string.type_yearly);
            default:
                return "oops";
        }
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

    public String dailyCheck(TimetableEvent e, int type) {
        if (e.getAllDay()) {
            return getResources().getString(R.string.type_allday);
        } else {
            switch (type) {
                case 0:
                    return String.format(getResources().getString(R.string.time), requiresFormat(e.getStart().getHour()) + e.getStart().getHour(), requiresFormat(e.getStart().getMinute()) + e.getStart().getMinute());
                case 1:
                    return String.format(getResources().getString(R.string.time), requiresFormat(e.getEnd().getHour()) + e.getEnd().getHour(), requiresFormat(e.getEnd().getMinute()) + e.getEnd().getMinute());
                default:
                    return null;
            }
        }
    }

    public String requiresFormat(int a) {
        if (a < 10) {
            return getResources().getString(R.string.formatted_character);
        }
        return "";
    }
}
// getResources().getString(R.string.start_date
// %1$s (%2$s) by %3$s is the best newsletter in the world