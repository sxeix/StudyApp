package com.example.studyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

public class colors extends AppCompatActivity {
    SharedPrefs sharedPrefs;
    Switch p, b, g, r, y, o;

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
        setContentView(R.layout.colors_page);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_colors_page));


        p = (Switch) findViewById(R.id.pink_switch);
        b = (Switch) findViewById(R.id.blue_switch);
        r = (Switch) findViewById(R.id.red_switch);
        g = (Switch) findViewById(R.id.green_switch);
        y = (Switch) findViewById(R.id.yellow_switch);
        o = (Switch) findViewById(R.id.orange_switch);
        if (sharedPrefs.loadPinkMode()) {
            p.setChecked(true);
        }else if(sharedPrefs.loadBlueMode()){
            b.setChecked(true);
        }else if(sharedPrefs.loadRedMode()){
            r.setChecked(true);
        }else if(sharedPrefs.loadGreenMode()){
            g.setChecked(true);
        }else if(sharedPrefs.loadYellowMode()){
            y.setChecked(true);
        }else if(sharedPrefs.loadOrangeMode()){
            o.setChecked(true);

        }

        p.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (isChecked) {
                    sharedPrefs.setPinkMode(true);
                    b.setChecked(false);
                    r.setChecked(false);
                    g.setChecked(false);
                    y.setChecked(false);
                    o.setChecked(false);
                    restartApp();
                } else {
                    sharedPrefs.setPinkMode(false);
                    b.setChecked(false);
                    r.setChecked(false);
                    g.setChecked(false);
                    y.setChecked(false);
                    o.setChecked(false);
                    p.setChecked(false);
                    restartApp();
                }
            }
        });

        b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (isChecked) {
                    sharedPrefs.setBlueMode(true);
                    p.setChecked(false);
                    r.setChecked(false);
                    g.setChecked(false);
                    y.setChecked(false);
                    o.setChecked(false);
                    restartApp();
                } else {
                    sharedPrefs.setBlueMode(false);
                    p.setChecked(false);
                    r.setChecked(false);
                    g.setChecked(false);
                    y.setChecked(false);
                    o.setChecked(false);
                    b.setChecked(false);
                    restartApp();
                }
            }
        });

        r.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (isChecked) {
                    sharedPrefs.setRedMode(true);
                    b.setChecked(false);
                    p.setChecked(false);
                    g.setChecked(false);
                    y.setChecked(false);
                    o.setChecked(false);
                    restartApp();
                } else {
                    sharedPrefs.setRedMode(false);
                    b.setChecked(false);
                    p.setChecked(false);
                    g.setChecked(false);
                    y.setChecked(false);
                    o.setChecked(false);
                    r.setChecked(false);
                    restartApp();
                }
            }
        });

        g.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (isChecked) {
                    sharedPrefs.setGreenMode(true);
                    b.setChecked(false);
                    r.setChecked(false);
                    p.setChecked(false);
                    y.setChecked(false);
                    o.setChecked(false);
                    restartApp();
                } else {
                    sharedPrefs.setGreenMode(false);
                    b.setChecked(false);
                    r.setChecked(false);
                    p.setChecked(false);
                    y.setChecked(false);
                    o.setChecked(false);
                    g.setChecked(false);
                    restartApp();
                }
            }
        });

        y.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (isChecked) {
                    sharedPrefs.setYellowMode(true);
                    b.setChecked(false);
                    r.setChecked(false);
                    g.setChecked(false);
                    p.setChecked(false);
                    o.setChecked(false);
                    restartApp();
                } else {
                    sharedPrefs.setYellowMode(false);
                    b.setChecked(false);
                    r.setChecked(false);
                    g.setChecked(false);
                    p.setChecked(false);
                    o.setChecked(false);
                    y.setChecked(false);
                    restartApp();
                }
            }
        });

        o.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if (isChecked) {
                    sharedPrefs.setOrangeMode(true);
                    b.setChecked(false);
                    r.setChecked(false);
                    g.setChecked(false);
                    y.setChecked(false);
                    p.setChecked(false);
                    restartApp();
                } else {
                    sharedPrefs.setOrangeMode(false);
                    b.setChecked(false);
                    r.setChecked(false);
                    g.setChecked(false);
                    y.setChecked(false);
                    p.setChecked(false);
                    o.setChecked(false);
                    restartApp();
                }
            }
        });
    }

    public void restartApp() {
        Intent setgs = new Intent(getApplicationContext(), colors.class);
        Intent setgs2 = new Intent(getApplicationContext(), MainActivity.class);
        setgs2.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(setgs2);
        startActivity(setgs);
        finish();
    }

    /** Toolbar dropdown menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    /** Toolbar icons implementations*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.events){
            Intent eventspage = new Intent(colors.this, eventsPage.class);
            startActivity(eventspage);
        }else if(id == R.id.about){
            return true;
        }else if(id == R.id.settings){
            Intent settings = new Intent(colors.this, settingsPage.class);
            startActivity(settings);
        }else if(id == R.id.addModules){
            Intent modules = new Intent(colors.this, adding_modules.class);
            startActivity(modules);
        }else if(id == android.R.id.home){
            Intent modules = new Intent(colors.this, MainActivity.class);
            startActivity(modules);
        }
        return true;
    }
}
