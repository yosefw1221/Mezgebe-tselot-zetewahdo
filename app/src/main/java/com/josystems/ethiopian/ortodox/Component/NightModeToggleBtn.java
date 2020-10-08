package com.josystems.ethiopian.ortodox.Component;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.josystems.ethiopian.ortodox.R;
import com.josystems.ethiopian.ortodox.utils.Constant;

public class NightModeToggleBtn extends AppCompatCheckBox {
    public NightModeToggleBtn(@NonNull Context context) {
        super(context);
        init();
    }

    public NightModeToggleBtn(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NightModeToggleBtn(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setPadding(10, 10, 10, 10);
        setButtonDrawable(R.drawable.toggle_nightmode);
        final SharedPreferences preferences = getContext().getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        setChecked(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int nightMode;
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                    nightMode = AppCompatDelegate.MODE_NIGHT_NO;
                else
                    nightMode = AppCompatDelegate.MODE_NIGHT_YES;
                editor.putInt(Constant.PREF_NIGHT_MODE, nightMode);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(nightMode);

            }
        });
    }
}
