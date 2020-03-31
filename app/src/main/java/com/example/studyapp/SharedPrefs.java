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



    /**
     * Updates the language preference.
     *
     * @param lang language selected by user.
     */
    public void setLangPref(String lang) {
        SharedPreferences.Editor editor = myShared.edit();
        editor.putString("language", lang);
        editor.commit();
    }



    /**
     * Gets the currently stored language preference.
     *
     * @return current application language preference.
     */
    public String getLangPref() {
        String lang = myShared.getString("language", "en-GB");
        return lang;
    }



    /**
     * Resets the preferences set by the user to default
     */
    public void restorePrefs() {
        SharedPreferences.Editor editor = myShared.edit();
        editor.clear();
        editor.commit();
    }
}
