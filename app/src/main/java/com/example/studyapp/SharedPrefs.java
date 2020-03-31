package com.example.studyapp;
import android.content.SharedPreferences;
import android.content.Context;

public class SharedPrefs {
    SharedPreferences myShared;

    public SharedPrefs(Context context){
         myShared = context.getSharedPreferences("filename", Context.MODE_PRIVATE);
    }

    public void setPinkMode(Boolean state){
        SharedPreferences.Editor editor = myShared.edit();
        editor.putBoolean("AppTheme", state);
        editor.commit();
    }

    public boolean loadPinkMode(){
        Boolean state = myShared.getBoolean("AppTheme", false);
        return state;
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

    public void setRedMode(Boolean state){
        SharedPreferences.Editor editor = myShared.edit();
        editor.putBoolean("RedMode", state);
        editor.commit();
    }

    public boolean loadRedMode(){
        Boolean state = myShared.getBoolean("RedMode", false);
        return state;
    }

    public void setYellowMode(Boolean state){
        SharedPreferences.Editor editor = myShared.edit();
        editor.putBoolean("YellowMode", state);
        editor.commit();
    }

    public boolean loadYellowMode(){
        Boolean state = myShared.getBoolean("YellowMode", false);
        return state;
    }

    public void setGreenMode(Boolean state){
        SharedPreferences.Editor editor = myShared.edit();
        editor.putBoolean("GreenMode", state);
        editor.commit();
    }

    public boolean loadGreenMode(){
        Boolean state = myShared.getBoolean("GreenMode", false);
        return state;
    }

    public void setOrangeMode(Boolean state){
        SharedPreferences.Editor editor = myShared.edit();
        editor.putBoolean("OrangeMode", state);
        editor.commit();
    }

    public boolean loadOrangeMode(){
        Boolean state = myShared.getBoolean("OrangeMode", false);
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
