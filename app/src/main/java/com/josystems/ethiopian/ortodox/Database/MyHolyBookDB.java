package com.josystems.ethiopian.ortodox.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.josystems.ethiopian.ortodox.Model.HolyBook;

import java.util.ArrayList;
import java.util.List;

public class MyHolyBookDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HolyBookDB";
    private static final String TABLE_NAME = "MyHollyBook";
    private static final String[] COLUMN = {"id","icon","name","description","language","data","tags"};
    private Context context;

    public MyHolyBookDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(id TEXT PRIMARY KEY,icon TEXT,name TEXT,description TEXT,language TEXT,data TEXT,tags TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public MutableLiveData<List<HolyBook>> getAllHollyBooks(){
        MutableLiveData<List<HolyBook>> data = new MutableLiveData<>();
        List<HolyBook> holybooks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT id,icon,name,description,language,tags FROM "+TABLE_NAME+" ORDER BY name",null);
        while (cursor.moveToNext()) {
            HolyBook holy = new HolyBook();
            holy.setId(cursor.getString(0));
            holy.setIconUrl(cursor.getString(1));
            holy.setTitle(cursor.getString(2));
            holy.setDescription(cursor.getString(3));
            holy.setLanguage(cursor.getString(4));
            holybooks.add(holy);
        }
        data.setValue(holybooks);
        cursor.close();
    return data;
    }
    public void addHolyBook(HolyBook hollyBook){
        try {
            Log.println(Log.INFO,"ADD",hollyBook.getId());
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("id",hollyBook.getId());
            values.put("icon",hollyBook.getIconUrl());
            values.put("name",hollyBook.getTitle());
            values.put("description",hollyBook.getDescription());
            values.put("language",hollyBook.getLanguage());
            values.put("data",hollyBook.getDataString());
            values.put("tags",hollyBook.getTags());
            long s = db.insert(TABLE_NAME,null,values);
            Toast.makeText(context, hollyBook.getId()+"  "+s, Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    public HolyBook getHollyBook(String id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,COLUMN,"id=?",new String[]{id},null,null,null);
        HolyBook holy = new HolyBook();
        if (cursor.moveToNext()){
            holy.setId(cursor.getString(0));
            holy.setIconUrl(cursor.getString(1));
            holy.setTitle(cursor.getString(2));
            holy.setDescription(cursor.getString(3));
            holy.setLanguage(cursor.getString(4));
            holy.setDataString(cursor.getString(5));
            holy.setTags(cursor.getString(6));
        }
        cursor.close();
        return holy;
    }
    public boolean isAlreadyDownloaded(String id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,COLUMN,"id=?",new String[]{id},null,null,null);
        int count = cursor.getCount();
        Toast.makeText(context, "count "+count+" id "+id, Toast.LENGTH_SHORT).show();
        cursor.close();
        return count>0;
    }
    public MutableLiveData<List<HolyBook>> searchHollyBooks(String tag){
        MutableLiveData<List<HolyBook>> data = new MutableLiveData<>();
        List<HolyBook> holybooks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT id,icon,name,description,language,tags FROM "+TABLE_NAME+" WHERE tags LIKE "+"%"+tag+"%"+" ORDER BY name",null);
        while (cursor.moveToNext()) {
            HolyBook holy = new HolyBook();
            holy.setId(cursor.getString(0));
            holy.setIconUrl(cursor.getString(1));
            holy.setTitle(cursor.getString(2));
            holy.setDescription(cursor.getString(3));
            holy.setLanguage(cursor.getString(4));
            holybooks.add(holy);
        }
        data.setValue(holybooks);
        cursor.close();
        return data;
    }
}
