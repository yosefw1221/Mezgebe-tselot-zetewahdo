package com.josystems.ethiopian.ortodox.utils;

import android.graphics.Color;

import androidx.appcompat.app.AppCompatDelegate;

public class Constant {
    public static final int MINIMUM_FONT_SIZE = 15;
    public static final String SHARED_PREF_NAME = "setting";
    public static final String PREF_FONT_SIZE = "font_size";
    public static final String PREF_FONT_SPACING = "font_spacing";
    public static final String URL_TELEGRAM = "https://t.me/mezgebe_tselot_zetewahdo";
    public static final String URL_FACEBOOK = "https://www.facebook.com/mezgebetselotzetewahdo";
    public static final String URL_EMAIL = "yosefworku18@gmail.com";
    public static final String PREF_LANGUAGE = "language";
    public static final String PREF_NIGHT_MODE = "night_mode";
    public static final int DEFAULT_FONT_SIZE = 25;
    public static final int MAX_FONT_SIZE = 50;
    public static final int DEFAULT_FONT_SPACING = 5;
    public static final int FONT_SPACING_MEDIUM = 8;
    public static final int FONT_SPACING_LARGE = 13;
    public static final String PREF_DEVICE_SILENT_MODE = "device_state";
    public static final String PREF_SILENT_WHEN_PRAYING = "silent_when_praying";
    public static final String PREF_FIRST_LAUNCH = "first_launch";


    public static String PREF_FONT_COLOR(){
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            return "font_color_night";
         else
            return "font_color_day";
    }
    public static String PREF_BACKGROUND_COLOR(){
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            return "background_color_night";
        }else {
            return "background_color_day";
        }
    }
    public static int getDefaultFontColor() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            return Color.WHITE;
        } else
            return Color.parseColor("#000017");
    }

    public static int getDefaultBackground() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            return Color.parseColor("#3C3C3C");
        } else
            return 0;
    }

}
