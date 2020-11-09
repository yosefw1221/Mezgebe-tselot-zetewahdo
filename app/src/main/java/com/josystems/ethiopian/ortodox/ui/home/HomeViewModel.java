package com.josystems.ethiopian.ortodox.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.josystems.ethiopian.ortodox.database.MyHolyBookDB;
import com.josystems.ethiopian.ortodox.model.HolyBook;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private static MutableLiveData<List<HolyBook>> hollybookList;

    public void init(Context c){
        MyHolyBookDB myHolyBookDB = new MyHolyBookDB(c);
        if (hollybookList==null)
        hollybookList = new MutableLiveData<>();
        hollybookList.setValue(myHolyBookDB.getAllHollyBooks());
    }
    public HomeViewModel() {
    }
    public static void refreshHolyBooks(Context c){
        MyHolyBookDB myHolyBookDB = new MyHolyBookDB(c);
        if (hollybookList==null)
            hollybookList = new MutableLiveData<>();
        hollybookList.setValue(myHolyBookDB.getAllHollyBooks());
    }

    public LiveData<List<HolyBook>> getAllHollyBooks() {
        return hollybookList;
    }
}