package com.example.studyapp;
import android.content.SharedPreferences;
import android.content.Context;

public class SharedPrefs {
    SharedPreferences myShared;

    public SharedPrefs(Context context){
        myShared = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    public void setNightMode(Boolean state){
        SharedPreferences.Editor editor = myShared.edit();
        editor.putBoolean("NightMode", state);
        editor.commit();
    }

    public boolean loadNightMode(){
        Boolean state = myShared.getBoolean("NightMode", false);
        return state;
    }
}
