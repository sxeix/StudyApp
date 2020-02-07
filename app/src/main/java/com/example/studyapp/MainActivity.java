package com.example.studyapp;

import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureNextButton();

        /**calls toolbar by ID, created in layout/toolbar.xml and activity.main.xml.*/
        Toolbar toolbar = findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);
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

        if(id == R.id.search){
            Toast.makeText(getApplicationContext(), "You clicked Search", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.about){
            Toast.makeText(getApplicationContext(), "Made by Segmentation Fault: \nJames\nDaniel\nRoyston\nRyan\nChris\nModule: Innovative Product Development",
                    Toast.LENGTH_SHORT).show();
        }else if(id == R.id.settings){
            Toast.makeText(getApplicationContext(), "You clicked Settings", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void configureNextButton() {
        Button nextButton = (Button) findViewById(R.id.AddEvent);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, InputPage.class));
            }
        });
    }
}
