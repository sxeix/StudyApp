package com.example.studyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class settingsPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    SharedPrefs sharedPrefs;
    Switch p, b, g, r, y, o;
    Locale myLocale;
    private Button alertButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mySpinner.setAdapter(adapter);
//        mySpinner.setOnItemSelectedListener(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_settings_page));

        Button colorbtn = findViewById(R.id.color_btn);
        colorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent colorspage = new Intent(settingsPage.this, colors.class);
                startActivity(colorspage);
            }
        });


//        colorMode = (Switch) findViewById(R.id.light_mode_switch);
//        if (sharedPrefs.loadBlueMode()) {
//            colorMode.setChecked(true);
//        }
//        colorMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
//                if (isChecked) {
//                    sharedPrefs.setBlueMode(true);
//                    restartApp();
//                } else {
//                    sharedPrefs.setBlueMode(false);
//                    restartApp();
//                }
//            }
//        });


        Spinner LanSpinner = (Spinner) findViewById(R.id.langSpinner);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.lang));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        LanSpinner.setAdapter(myAdapter);
        setSpinnerLang(LanSpinner);
        LanSpinner.setOnItemSelectedListener(this);

        alertButton = (Button) findViewById(R.id.resetButton);

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(settingsPage.this);

                builder.setCancelable(true);
                builder.setTitle(getResources().getString(R.string.reset));
                builder.setMessage(getResources().getString(R.string.resetConfirmation));

                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton(getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // This is intentional to display that the option is functional and is ready to have functionality implemented
                        startActivity(new Intent(settingsPage.this, MainActivity.class));
                    }
                });
                builder.show();
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
        } else if(id == R.id.addModules){
            Intent modules = new Intent(settingsPage.this, adding_modules.class);
            startActivity(modules);
        }else if (id == android.R.id.home) {
            Intent modules = new Intent(settingsPage.this, MainActivity.class);
            startActivity(modules);
        }
        return true;
    }

    public void setLocale(String lang) {
        myLocale = new Locale(lang);
        switch(lang) {
            case "zh_hk":
                myLocale = new Locale("zh", "HK"); break;
            case "es_es":
                myLocale = new Locale("es", "ES"); break;
            case "pt_pt":
                myLocale = new Locale("pt", "PT"); break;
            case "zh_cn":
                myLocale = new Locale("zh", "CN"); break;
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
                        if (checkLocaleDifferent(currentPref, "zh_hk")) setLocale("zh_hk");
                        break;
                    case 3:
                        if (checkLocaleDifferent(currentPref, "zh_cn")) setLocale("zh_cn");
                        break;
                    case 4:
                        if (checkLocaleDifferent(currentPref, "ru")) setLocale("ru");
                        break;
                    case 5:
                        if (checkLocaleDifferent(currentPref, "fr")) setLocale("fr");
                        break;
                    case 6:
                        if (checkLocaleDifferent(currentPref, "bg")) setLocale("bg");
                        break;
                    case 7:
                        if (checkLocaleDifferent(currentPref, "tr")) setLocale("tr");
                        break;
                    case 8:
                        if (checkLocaleDifferent(currentPref, "es_es")) setLocale("es_es");
                        break;
                    case 9:
                        if (checkLocaleDifferent(currentPref, "pt_pt")) setLocale("pt_pt");
                        break;
                    case 10:
                        if (checkLocaleDifferent(currentPref, "de")) setLocale("de");
                        break;
                    case 11:
                        if (checkLocaleDifferent(currentPref, "no")) setLocale("no");
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
            case "zh_hk":
                spinny.setSelection(2); break;
            case "zh_cn":
                spinny.setSelection(3); break;
            case "ru":
                spinny.setSelection(4); break;
            case "fr":
                spinny.setSelection(5); break;
            case "bg":
                spinny.setSelection(6); break;
            case "tr":
                spinny.setSelection(7); break;
            case "es_es":
                spinny.setSelection(8); break;
            case "pt_pt":
                spinny.setSelection(9); break;
            case "de":
                spinny.setSelection(10); break;
            case "no":
                spinny.setSelection(11); break;

        }
    }

}