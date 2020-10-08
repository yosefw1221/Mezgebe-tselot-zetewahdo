package com.josystems.ethiopian.ortodox.ui.home;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.josystems.ethiopian.ortodox.Database.MyHolyBookDB;
import com.josystems.ethiopian.ortodox.Model.HolyBook;

import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<List<HolyBook>> hollybookList;
    private MyHolyBookDB myHolyBookDB;
    public void init(Context c){
        myHolyBookDB = new MyHolyBookDB(c);
        hollybookList = myHolyBookDB.getAllHollyBooks();
    }
    public HomeViewModel() {

    }

    public LiveData<List<HolyBook>> getAllHollyBooks() {
        return hollybookList;
    }
}