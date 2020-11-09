package com.josystems.ethiopian.ortodox.reader;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.josystems.ethiopian.ortodox.utils.Constant;

public class ChapterViewModel extends ViewModel {
    private static MutableLiveData<Integer> fontSize;

    public ChapterViewModel() {
    }
    public static void init(Context ctx){
        if (fontSize==null){
            fontSize = new MutableLiveData<>();
            SharedPreferences preferences = ctx.getSharedPreferences(Constant.SHARED_PREF_NAME,Context.MODE_PRIVATE);
            int size = preferences.getInt(Constant.PREF_FONT_SIZE,Constant.DEFAULT_FONT_SIZE);
            fontSize.setValue(size);
        }
    }

    public static void setReaderFontSize(int newSize) {
        if (fontSize==null)
            fontSize = new MutableLiveData<>();
        fontSize.setValue(newSize);
    }

    public LiveData<Integer> getFontSize() {
        return fontSize;
    }

}
