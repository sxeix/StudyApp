package com.example.studyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class eventsPage extends AppCompatActivity {
    Button button_back;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listView);

        final ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("test1"); arrayList.add("test1"); arrayList.add("test1");
        arrayList.add("test1"); arrayList.add("test1"); arrayList.add("test1");
        arrayList.add("test1"); arrayList.add("test1"); arrayList.add("test1");
        arrayList.add("test1"); arrayList.add("test1"); arrayList.add("test1");
        arrayList.add("test1"); arrayList.add("test1"); arrayList.add("test1");
        arrayList.add("test1"); arrayList.add("test1"); arrayList.add("test1");
        arrayList.add("test1"); arrayList.add("test1"); arrayList.add("test1");
        arrayList.add("test1"); arrayList.add("test1"); arrayList.add("test1");
        arrayList.add("test1"); arrayList.add("test1"); arrayList.add("test1");
        arrayList.add("test1"); arrayList.add("test1"); arrayList.add("test1");
        arrayList.add("test1"); arrayList.add("test1"); arrayList.add("test1");
        arrayList.add("test1"); arrayList.add("test1"); arrayList.add("test1");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(eventsPage.this, "Clicked: " + arrayList.get(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });

        button_back = findViewById(R.id.goback);
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
            Intent eventspage = new Intent(eventsPage.this, eventsPage.class);
            startActivity(eventspage);
        }else if(id == R.id.about){
            Intent infopage = new Intent(eventsPage.this, infoPage.class);
            startActivity(infopage);
        }else if(id == R.id.settings){
            Intent settings = new Intent(eventsPage.this, settingsPage.class);
            startActivity(settings);
        }
        return true;
    }

}
