package com.josystems.ethiopian.ortodox.utils;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.josystems.ethiopian.ortodox.model.HolyBook;

public class Analytics {

    public static void logAgreement(Context context, boolean agree) {
        try {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            bundle.putString("status", agree ? "AGREE" : "DISAGREE");
            firebaseAnalytics.logEvent("Agreement", bundle);
        }catch (Exception e){
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    public static void downloadEvent(Context context, HolyBook hollyBook, String status) {
        try {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            bundle.putString("title", hollyBook.getTitle());
            bundle.putString("language", hollyBook.getLanguage());
            bundle.putString("status", status);
            firebaseAnalytics.logEvent("HollyBook_Download", bundle);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
    public static void fontColor(Context context, String color) {
        try {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            bundle.putString("color", color);
            bundle.putString("theme", AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES ? "Night" : "Light");
            firebaseAnalytics.logEvent("FONT", bundle);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    public static void background(Context context, String value) {
        try {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            bundle.putString("color", value);
            bundle.putString("theme", AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES ? "Night" : "Light");
            firebaseAnalytics.logEvent("Background", bundle);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    public static void fontSpacing(Context context, int spacing) {
        try {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            bundle.putInt("spacing", spacing);
            firebaseAnalytics.logEvent("FONT", bundle);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
    public static void fontSize(Context context, int spacing) {
        try {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            bundle.putInt("size", spacing);
            firebaseAnalytics.logEvent("FONT", bundle);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
    public static void theme(Context context,boolean dark){
        try {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            bundle.putString("theme", dark?"DARK":"LIGHT");
            firebaseAnalytics.logEvent("APP", bundle);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
    public static void silent(Context context,boolean silent){
        try {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            bundle.putString("silent_when_praying", silent?"ENABLED":"DISABLED");
            firebaseAnalytics.logEvent("APP", bundle);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
    public static void dndPermission(Context context, boolean grant){
        try {
            FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(context);
            Bundle bundle = new Bundle();
            bundle.putString("DND_Permission", grant?"Grant":"Denied");
            firebaseAnalytics.logEvent("APP", bundle);
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }
}

