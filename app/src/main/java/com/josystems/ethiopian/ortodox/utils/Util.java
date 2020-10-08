package com.josystems.ethiopian.ortodox.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.MutableLiveData;

import com.josystems.ethiopian.ortodox.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Util {
    static MutableLiveData<Float> fontSize = new MutableLiveData<>();

    public static MutableLiveData<Float> getReaderFontSize() {
        return fontSize;
    }

    public static void setReaderFontSize(float newSize) {
        fontSize.setValue(newSize);
    }

    public static Drawable getHolyIconFromStorage(Context context, String id) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File imageDir = contextWrapper.getDir("imageDir", ContextWrapper.MODE_PRIVATE);
        File image = new File(imageDir, id + ".png");
        Drawable drawable = context.getResources().getDrawable(R.drawable.mary); // load default icon for faillback
        try {
            FileInputStream inputStream = new FileInputStream(image);
            drawable = Drawable.createFromStream(inputStream, image.getName() + ".png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public static void saveHolyIcontoStorage(Context context, String id, Drawable holyIcon) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File Path = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE);
        if (!Path.exists()) Path.mkdirs();
        File image = new File(Path, id + ".png");
        Bitmap bitmap = Bitmap.createBitmap(holyIcon.getIntrinsicWidth(), holyIcon.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        holyIcon.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        holyIcon.draw(canvas);
        try {
            FileOutputStream outputStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized void saveToSharedPref(Context context, String key, Object object) {
        SharedPreferences preferences = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static Drawable getTintedDrawable(Context context, int drawableID, int color) {
        return getTintedDrawable(context, drawableID, color, PorterDuff.Mode.DARKEN);
    }

    public static Drawable getTintedDrawable(Context context, int drawableID, int color, PorterDuff.Mode mode) {
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), drawableID, context.getTheme());
        if (color == 0) {
            DrawableCompat.setTintList(drawable, null);
        } else {
            DrawableCompat.setTint(drawable, color);
            DrawableCompat.setTintMode(drawable, mode);
        }
        return drawable;
    }

    public static String readJsonFromAsset(Context context, String filename) {
        AssetManager assetManager = context.getAssets();
        StringBuilder data = new StringBuilder();
        try {
            InputStream inputStream = assetManager.open(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            Log.e("readTextFromAsset: ", e.toString());
            return null;
        }
        return data.toString();
    }


    public synchronized void importHolyBooksFromFile(Context context, HashMap<Integer, String> filenameMap) {
        for (int i = 0; i < filenameMap.size(); i++) {

        }
    }
}
