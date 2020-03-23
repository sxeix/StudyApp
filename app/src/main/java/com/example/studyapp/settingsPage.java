package com.example.studyapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;

public class settingsPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public Switch colorMode;
    SharedPrefs sharedPrefs;
    Locale myLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPrefs = new SharedPrefs(this);
        if (sharedPrefs.loadNightMode()) {
            setTheme(R.style.LightMode);
        } else {
            setTheme(R.style.AppTheme);
        }

//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mySpinner.setAdapter(adapter);
//        mySpinner.setOnItemSelectedListener(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_settings_page));

        colorMode = (Switch) findViewById(R.id.light_mode_switch);
        if (sharedPrefs.loadNightMode()) {
            colorMode.setChecked(true);
        }
        colorMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (isChecked) {
                    sharedPrefs.setNightMode(true);
                    restartApp();
                } else {
                    sharedPrefs.setNightMode(false);
                    restartApp();
                }
            }
        });
        Spinner LanSpinner = (Spinner) findViewById(R.id.langSpinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.lang));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LanSpinner.setAdapter(myAdapter);
        setSpinnerLang(LanSpinner);
        LanSpinner.setOnItemSelectedListener(this);
//        LanSpinner.setDropDownVerticalOffset(100);

    }

    public void restartApp() {
        Intent setgs = new Intent(getApplicationContext(), settingsPage.class);
        Intent setgs2 = new Intent(getApplicationContext(), MainActivity.class);
        setgs2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(setgs2);
        startActivity(setgs);
        finish();
    }


    /**
     * Toolbar dropdown menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            Intent eventspage = new Intent(settingsPage.this, eventsPage.class);
            startActivity(eventspage);
        } else if (id == R.id.about) {
            Intent infopage = new Intent(settingsPage.this, infoPage.class);
            startActivity(infopage);
        } else if (id == R.id.settings) {
            return true;
        } else if (id == android.R.id.home) {
            this.finish();
        }
        return true;
    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        switch(lang) {
            case "zh":
                myLocale = new Locale("zh", "HK"); break;
            case "es":
                myLocale = new Locale("es", "ES"); break;
            case "pt":
                myLocale = new Locale("pt", "PT"); break;
        }
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        sharedPrefs.setLangPref(lang);
        startActivity(refresh);
    }

    public boolean checkLocaleDifferent(String cur, String select) {
        return !cur.equals(select.toLowerCase());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String currentPref = sharedPrefs.getLangPref().toLowerCase();
        switch (position) {
            case 0:
                if (checkLocaleDifferent(currentPref, "en-GB")) setLocale("en-GB");
                break;
            case 1:
                if (checkLocaleDifferent(currentPref, "ja")) setLocale("ja");
                break;
            case 2:
                if (checkLocaleDifferent(currentPref, "zh")) setLocale("zh");
                break;
            case 3:
                if (checkLocaleDifferent(currentPref, "ru")) setLocale("ru");
                break;
            case 4:
                if (checkLocaleDifferent(currentPref, "fr")) setLocale("fr");
                break;
            case 5:
                if (checkLocaleDifferent(currentPref, "bg")) setLocale("bg");
                break;
            case 6:
                if (checkLocaleDifferent(currentPref, "tr")) setLocale("tr");
                break;
            case 7:
                if (checkLocaleDifferent(currentPref, "es")) setLocale("es");
                break;
            case 8:
                if (checkLocaleDifferent(currentPref, "pt")) setLocale("pt");
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void setSpinnerLang(Spinner spinny){
        String currentLang = sharedPrefs.getLangPref().toLowerCase();
        switch(currentLang){
            case "en-gb":
                spinny.setSelection(0); break;
            case "ja":
                spinny.setSelection(1); break;
            case "zh":
                spinny.setSelection(2); break;
            case "ru":
                spinny.setSelection(3); break;
            case "fr":
                spinny.setSelection(4); break;
            case "bg":
                spinny.setSelection(5); break;
            case "tr":
                spinny.setSelection(6); break;
            case "es":
                spinny.setSelection(7); break;
            case "pt":
                spinny.setSelection(8); break;

        }
    }
}