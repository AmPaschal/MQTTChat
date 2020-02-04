package com.amusuopaschal.mqttchat;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static SessionManager instance = null;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "pref";

    private static final String KEY_USER_ID = "user_id";

    public SessionManager(Context context){
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static SessionManager getInstance(Context context){
        if (instance == null){
            instance = new SessionManager(context);
        }
        return instance;
    }

    public void setUserId(String id){
        editor.putString(KEY_USER_ID, id);
        editor.commit();
    }

    public String getUserId(){
        return pref.getString(KEY_USER_ID, null);
    }


}
