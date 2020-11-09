package com.josystems.ethiopian.ortodox.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.josystems.ethiopian.ortodox.model.HolyBook;
import com.josystems.ethiopian.ortodox.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class MyHolyBookDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HolyBookDB";
    private static final String TABLE_NAME = "MyHollyBook";
    private static final String[] COLUMN = {"id", "icon", "name", "description", "language", "data", "tags", "lastUpdate"};
    private final Context context;

    public MyHolyBookDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id TEXT PRIMARY KEY,icon TEXT,name TEXT,description TEXT,language TEXT,data TEXT,tags TEXT,lastUpdate LONG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public int getTotalHolyBooks() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_NAME, null);
        return cursor.getCount();
    }

    public List<HolyBook> getAllHollyBooks() {

        List<HolyBook> holybooks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id,icon,name,description,language,tags,lastUpdate FROM " + TABLE_NAME + " ORDER BY name", null);
        while (cursor.moveToNext()) {
            HolyBook holy = new HolyBook();
            holy.setId(cursor.getString(0));
            holy.setIconUrl(cursor.getString(1));
            holy.setTitle(cursor.getString(2));
            holy.setDescription(cursor.getString(3));
            holy.setLanguage(cursor.getString(4));
            holy.setLastUpdate(cursor.getLong(6));
            holybooks.add(holy);
        }
        cursor.close();
        return holybooks;
    }

    public void addHolyBook(HolyBook hollyBook) {
        try {
            Log.println(Log.INFO, "ADD", hollyBook.getId());
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id", hollyBook.getId());
            values.put("icon", hollyBook.getIconUrl());
            values.put("name", hollyBook.getTitle());
            values.put("description", hollyBook.getDescription());
            values.put("language", hollyBook.getLanguage());
            values.put("data", hollyBook.getDataString());
            values.put("tags", hollyBook.getTags());
            values.put("lastUpdate", hollyBook.getLastUpdate());
            if (getHollyBook(hollyBook.getId()) == null)
                db.insert(TABLE_NAME, null, values);
            else
                db.update(TABLE_NAME, values, "id=?", new String[]{hollyBook.getId()});
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            FirebaseCrashlytics.getInstance().recordException(e);
        }

    }

    public void addHolyBooks(HolyBook[] holyBooks, Drawable[] holyIcons) {
        for (int i = 0; i < holyBooks.length; i++) {
            holyBooks[i].setDataString(Util.readJsonFromAsset(context, holyBooks[i].getDataString()));
            addHolyBook(holyBooks[i]);
            Util.saveHolyIcontoStorage(context, holyBooks[i].getId(), holyIcons[i]);
        }
    }

    public void addHolyBooks(List<HolyBook> holyBooks) {
        for (HolyBook holyBook : holyBooks) {
            addHolyBook(holyBook);
        }
    }

    public HolyBook getHollyBook(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMN, "id=?", new String[]{id}, null, null, null);
        HolyBook holy = null;
        if (cursor.moveToNext()) {
            holy = new HolyBook();
            holy.setId(cursor.getString(0));
            holy.setIconUrl(cursor.getString(1));
            holy.setTitle(cursor.getString(2));
            holy.setDescription(cursor.getString(3));
            holy.setLanguage(cursor.getString(4));
            holy.setDataString(cursor.getString(5));
            holy.setTags(cursor.getString(6));
            holy.setLastUpdate(cursor.getLong(7));
        }
        cursor.close();
        return holy;
    }

    public boolean isAlreadyDownloaded(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, COLUMN, "id=?", new String[]{id}, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public int getHollyBookState(String ids, long lastUpdate) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,new String[]{"id","lastUpdate"},"id=?",new String[]{ids},null,null,null);
        if (cursor.moveToNext()) {
            if (cursor.getLong(1) < lastUpdate)
                return 1;
            else
                return 2;
        } else
            return 0;

    }

    public MutableLiveData<List<HolyBook>> searchHollyBooks(String tag) {
        MutableLiveData<List<HolyBook>> data = new MutableLiveData<>();
        List<HolyBook> holybooks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id,icon,name,description,language,tags,lastUpdate FROM " + TABLE_NAME + " WHERE tags LIKE " + "%" + tag + "%" + " ORDER BY name", null);
        while (cursor.moveToNext()) {
            HolyBook holy = new HolyBook();
            holy.setId(cursor.getString(0));
            holy.setIconUrl(cursor.getString(1));
            holy.setTitle(cursor.getString(2));
            holy.setDescription(cursor.getString(3));
            holy.setLanguage(cursor.getString(4));
            holy.setLastUpdate(cursor.getLong(6));
            holybooks.add(holy);
        }
        data.setValue(holybooks);
        cursor.close();
        return data;
    }
}
