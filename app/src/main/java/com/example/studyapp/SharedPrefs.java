package com.example.studyapp;
import android.content.SharedPreferences;
import android.content.Context;

public class SharedPrefs {
    SharedPreferences myShared;

    public SharedPrefs(Context context){
         myShared = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    public void setBlueMode(Boolean state){
        SharedPreferences.Editor editor = myShared.edit();
        editor.putBoolean("BlueMode", state);
        editor.commit();
    }

    public boolean loadBlueMode(){
        Boolean state = myShared.getBoolean("BlueMode", false);
        return state;
    }


    public void setLangPref(String lang) {
        SharedPreferences.Editor editor = myShared.edit();
        editor.putString("language", lang);
        editor.commit();
    }
    public String getLangPref() {
        String lang = myShared.getString("language", "en-GB");
        return lang;
    }
}
