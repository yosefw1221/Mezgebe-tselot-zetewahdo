package com.josystems.ethiopian.ortodox.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.josystems.ethiopian.ortodox.Model.Chapter;
import com.josystems.ethiopian.ortodox.R;
import com.josystems.ethiopian.ortodox.Reader.ChapterViewModel;
import com.josystems.ethiopian.ortodox.utils.Constant;

import java.util.List;

public class ChapterPopup {
    private PopupWindow popup;
    public static int MIN_FONT_SIZE = 12;
    public static int DEFAULT_FONT_SIZE  = 20;

    public ChapterPopup(final Context context, List<Chapter> chapterList, onChapterSelectedListener listener) {
        ChapterPopupRecyclerAdapter adapter = new ChapterPopupRecyclerAdapter(context, chapterList, listener);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.chapter_popup_layout, null);
        final SharedPreferences preferences = context.getSharedPreferences(Constant.SHARED_PREF_NAME,Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        AppCompatSeekBar seekBar = layout.findViewById(R.id.popup_font_seekbar);
        AppCompatCheckBox nightMode = layout.findViewById(R.id.popup_nightmode);
        nightMode.setChecked(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nightMode;
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                    nightMode = AppCompatDelegate.MODE_NIGHT_NO;
                else
                    nightMode = AppCompatDelegate.MODE_NIGHT_YES;
                editor.putInt(Constant.PREF_NIGHT_MODE,nightMode);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(nightMode);
            }
        });
        seekBar.setMax(Constant.MAX_FONT_SIZE);
        seekBar.setProgress(preferences.getInt(Constant.PREF_FONT_SIZE,DEFAULT_FONT_SIZE)-Constant.MINIMUM_FONT_SIZE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ChapterViewModel.setReaderFontSize(i+ Constant.MINIMUM_FONT_SIZE);
                editor.putInt(Constant.PREF_FONT_SIZE,i+MIN_FONT_SIZE);
                editor.apply();

            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        RecyclerView recyclerView = layout.findViewById(R.id.popup_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        popup = new PopupWindow(layout, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.setOutsideTouchable(true);
    }

    public void show(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        int x = location[0] - v.getWidth() / 2;
        int y = location[1] + v.getHeight() / 2;
        popup.showAtLocation(v, Gravity.NO_GRAVITY, x, y);
    }

    public void dismiss() {
        popup.dismiss();
    }

    public PopupWindow getPopupWindow() {
        return popup;
    }

}
