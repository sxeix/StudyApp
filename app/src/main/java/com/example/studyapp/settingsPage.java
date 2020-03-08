package com.example.studyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Locale;

public class settingsPage extends AppCompatActivity {
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

    public void onRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.radio_eng:
                setLocale("en-GB");break;
            case R.id.radio_ja:
                setLocale("ja"); break;
            case R.id.radio_can:
                setLocale("zh-rHK"); break;
        }
    }

    public void setLocale(String lang) {
        if(lang.equals("zh-rHK")){
            myLocale = new Locale("zh", "HK");
            lang = "zh-HK";
        } else {
            myLocale = new Locale(lang);
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
}