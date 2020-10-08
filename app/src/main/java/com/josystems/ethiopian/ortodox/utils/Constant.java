package com.josystems.ethiopian.ortodox.utils;

import android.graphics.Color;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.josystems.ethiopian.ortodox.R;

public class Constant {
    public static final int MINIMUM_FONT_SIZE = 15;
    public static final String SHARED_PREF_NAME = "setting";
    public static final String PREF_FONT_SIZE = "font_size";
    public static final String PREF_FONT_SPACING = "font_spacing";
    public static final String URL_TELEGRAM = "https://t.me/josefworku";
    public static final String URL_FACEBOOK = "https://www.fb.com/";
    public static final String URL_EMAIL = "yosefworku18@gmail.com";
    public static final String PREF_LANGUAGE = "language";
    public static final String PREF_NIGHT_MODE = "night_mode";
    public static final int DEFAULT_FONT_SIZE = 25;
    public static final int MAX_FONT_SIZE = 50;
    public static final int DEFAULT_FONT_SPACING = 5;
    public static final int FONT_SPACING_MEDIUM = 8;
    public static final int FONT_SPACING_LARGE = 13;



    public static String PREF_FONT_COLOR(){
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            return "font_color_night";
         else
            return "font_color_day";
    }
    public static String PREF_BACKGROUND_COLOR(){
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            Log.e(">>>>>>>>>>>>>>", "PREF_BACKGROUND_COLOR KEY:  background_color_night");
            return "background_color_night";
        }else {
            Log.e(">>>>>>>>>>>>>>", "PREF_BACKGROUND_COLOR KEY:  background_color_day");
            return "background_color_day";
        }
    }
    public static int getDefaultFontColor() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            return Color.WHITE;
        } else
            return Color.DKGRAY;
    }

    public static int getDefaultBackground() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            return Color.parseColor("#3C3C3C");
        } else
            return 0;
    }

}
