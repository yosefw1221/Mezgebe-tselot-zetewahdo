package com.josystems.ethiopian.ortodox;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.josystems.ethiopian.ortodox.utils.Constant;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE);
        int nightMode = preferences.getInt(Constant.PREF_NIGHT_MODE, 1);
        if (nightMode != AppCompatDelegate.getDefaultNightMode())
            AppCompatDelegate.setDefaultNightMode(nightMode);

    }
}
