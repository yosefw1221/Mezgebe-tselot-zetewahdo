package com.josystems.ethiopian.ortodox.ui.setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.josystems.ethiopian.ortodox.R;
import com.josystems.ethiopian.ortodox.reader.ChapterViewModel;
import com.josystems.ethiopian.ortodox.utils.Analytics;
import com.josystems.ethiopian.ortodox.utils.Constant;
import com.josystems.ethiopian.ortodox.utils.Util;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

public class SettingFragment extends Fragment {

    AppCompatSeekBar fontSize;
    int selected_bg;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    AppCompatTextView appearance_text;
    RadioGroup backgroundGroup;
    AppCompatCheckBox nightMode;
    SwitchCompat silent;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        String tgunaTselyu = "<font color=red>ትግሁ ወጸልዩ </font>ከመ ኢትባኡ ውስተ መንሱት፤ ወደ ፈተናም እንዳትገቡ ትግታችሁ ጸልዩ” </br><font color=#75f>ማቴ.፳፮፥፵፩</font>";
        preferences = getContext().getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        final AppCompatImageView appearance_bg = root.findViewById(R.id.pref_appearance_overview_bg);
        appearance_text = root.findViewById(R.id.pref_appearance_overview_text);
        appearance_text.setText(Html.fromHtml(tgunaTselyu));
        fontSize = root.findViewById(R.id.pref_appearance_font_size);
        fontSize.setMax(Constant.MAX_FONT_SIZE);
        final AppCompatButton fontColor = root.findViewById(R.id.pref_appearance_font_color);
        nightMode = root.findViewById(R.id.pref_theme);
        AppCompatImageButton telegram = root.findViewById(R.id.pref_telegram);
        AppCompatImageButton facebook = root.findViewById(R.id.pref_facebook);
        AppCompatImageButton email = root.findViewById(R.id.pref_email);
        RadioGroup spacingGroup = root.findViewById(R.id.pref_spacing_group);
        backgroundGroup = root.findViewById(R.id.pref_background_group);

        final AppCompatRadioButton colorBg = root.findViewById(R.id.pref_appearance_background_color);
        final AppCompatRadioButton bg1 = root.findViewById(R.id.pref_appearance_background_1);
        final AppCompatRadioButton bg2 = root.findViewById(R.id.pref_appearance_background_2);
        final AppCompatRadioButton bg3 = root.findViewById(R.id.pref_appearance_background_3);
        final AppCompatRadioButton bg4 = root.findViewById(R.id.pref_appearance_background_4);
        AppCompatTextView fontColorTxt = root.findViewById(R.id.pref_font_color_txt);
        AppCompatTextView backgroundTxt = root.findViewById(R.id.pref_background_txt);
        boolean night = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES;
        nightMode.setText(night ? R.string.theme_night : R.string.theme);
        fontColorTxt.setText(night ? R.string.font_color_night : R.string.font_color);
        backgroundTxt.setText(night ? R.string.background_night : R.string.background);

        selected_bg = preferences.getInt(Constant.PREF_BACKGROUND_COLOR(), Constant.getDefaultBackground());
        int txtColor = preferences.getInt(Constant.PREF_FONT_COLOR(), Constant.getDefaultFontColor());
        appearance_bg.setImageDrawable(Util.getTintedBackground(getContext(), selected_bg));

        appearance_text.setTextColor(txtColor);
        fontColor.setBackgroundColor(txtColor);

        int font_size = preferences.getInt(Constant.PREF_FONT_SIZE, Constant.DEFAULT_FONT_SIZE);
        fontSize.setProgress(font_size - Constant.MINIMUM_FONT_SIZE);
        appearance_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, font_size);
        appearance_text.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, preferences.getInt(Constant.PREF_FONT_SPACING, Constant.DEFAULT_FONT_SPACING), getResources().getDisplayMetrics()), 1.0f);
        silent = root.findViewById(R.id.pref_silent_when_praying);
        silent.setChecked(preferences.getBoolean(Constant.PREF_SILENT_WHEN_PRAYING, Util.isNotificationGranted(getContext())));
        silent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    editor.putBoolean(Constant.PREF_SILENT_WHEN_PRAYING, false);
                    editor.apply();
                    Analytics.silent(getContext(), false);
                } else if (silentDeviceSound()){
                    editor.putBoolean(Constant.PREF_SILENT_WHEN_PRAYING, true);
                    editor.apply();
                }else
                    compoundButton.setChecked(false);
            }
        });
        if (colorBg.isChecked())
            colorBg.setBackground(Util.getTintedDrawable(getContext(), R.drawable.ic_palette, preferences.getInt(Constant.PREF_FONT_COLOR(), Constant.getDefaultBackground()), PorterDuff.Mode.MULTIPLY));
        colorBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ColorPickerDialog.Builder(getContext())
                        .setTitle("ቀለም")
                        .setPreferenceName("backgroundColor")
                        .setPositiveButton(getString(R.string.confirm),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        Drawable bg = Util.getTintedBackground(getContext(), envelope.getColor());
                                        appearance_bg.setImageDrawable(bg);
                                        colorBg.setBackground(Util.getTintedDrawable(getContext(), R.drawable.ic_palette, envelope.getColor(), PorterDuff.Mode.MULTIPLY));
                                        editor.putInt(Constant.PREF_BACKGROUND_COLOR(), envelope.getColor());
                                        editor.commit();
                                        Analytics.background(getContext(), "custom : " + envelope.getHexCode());
                                    }
                                })
                        .setNegativeButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                        .attachAlphaSlideBar(true) // the default value is true.
                        .attachBrightnessSlideBar(true)  // the default value is true.
                        .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                        .show();
            }
        });
        bg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appearance_bg.setImageDrawable(Util.getTintedBackground(getContext(), 0));
                editor.putInt(Constant.PREF_BACKGROUND_COLOR(), 0);
                editor.commit();
                Analytics.background(getContext(), "background : paper");
            }
        });
        bg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appearance_bg.setImageDrawable(Util.getTintedBackground(getContext(), Color.WHITE));
                editor.putInt(Constant.PREF_BACKGROUND_COLOR(), Color.WHITE);
                editor.commit();
                Analytics.background(getContext(), "background : white");
            }
        });
        bg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appearance_bg.setImageDrawable(Util.getTintedBackground(getContext(), getResources().getColor(R.color.darkGray)));
                editor.putInt(Constant.PREF_BACKGROUND_COLOR(), getResources().getColor(R.color.darkGray));
                editor.commit();
                Analytics.background(getContext(), "background : dark");
            }
        });
        bg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appearance_bg.setImageDrawable(Util.getTintedBackground(getContext(), Color.parseColor("#639C1064")));
                editor.putInt(Constant.PREF_BACKGROUND_COLOR(), Color.parseColor("#639C1064"));
                editor.commit();
                Analytics.background(getContext(), "background : pink");
            }
        });

        int spacing = preferences.getInt(Constant.PREF_FONT_SPACING, Constant.DEFAULT_FONT_SPACING);
        spacingGroup.check(spacing == Constant.DEFAULT_FONT_SPACING ?
                R.id.pref_appearance_font_spacing1 : spacing == Constant.FONT_SPACING_MEDIUM ?
                R.id.pref_appearance_font_spacing2 : R.id.pref_appearance_font_spacing3);

        spacingGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int spacing;
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.pref_appearance_font_spacing1:
                        spacing = Constant.DEFAULT_FONT_SPACING;
                        break;
                    case R.id.pref_appearance_font_spacing2:
                        spacing = Constant.FONT_SPACING_MEDIUM;
                        break;
                    default:
                        spacing = Constant.FONT_SPACING_LARGE;
                }
                Analytics.fontSpacing(getContext(), spacing);
                appearance_text.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, spacing, getResources().getDisplayMetrics()), 1.0f);
                editor.putInt(Constant.PREF_FONT_SPACING, spacing);
                editor.apply();
            }
        });
        fontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                int s = Constant.MINIMUM_FONT_SIZE + i;
                appearance_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, s);
                ChapterViewModel.setReaderFontSize(s);
                editor.putInt(Constant.PREF_FONT_SIZE, s);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Analytics.fontSize(getContext(), seekBar.getProgress());
            }
        });
        fontColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ColorPickerDialog.Builder(getContext())
                        .setTitle("የፊደላት ቀለም")
                        .setPreferenceName("MyColorPickerDialog")
                        .setPositiveButton(getString(R.string.confirm),
                                new ColorEnvelopeListener() {
                                    @Override
                                    public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                                        appearance_text.setTextColor(envelope.getColor());
                                        fontColor.setBackgroundColor(envelope.getColor());
                                        editor.putInt(Constant.PREF_FONT_COLOR(), envelope.getColor());
                                        editor.commit();
                                        Analytics.fontColor(getContext(), envelope.getHexCode());
                                    }
                                })
                        .setNegativeButton(getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                        .attachAlphaSlideBar(true) // the default value is true.
                        .attachBrightnessSlideBar(true)  // the default value is true.
                        .setBottomSpace(12) // set a bottom space between the last slidebar and buttons.
                        .show();
            }
        });


        nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nightMode;
                if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                    nightMode = AppCompatDelegate.MODE_NIGHT_NO;
                else
                    nightMode = AppCompatDelegate.MODE_NIGHT_YES;
                editor.putInt(Constant.PREF_NIGHT_MODE, nightMode);
                editor.apply();
                Analytics.theme(getContext(), nightMode == AppCompatDelegate.MODE_NIGHT_YES);
                AppCompatDelegate.setDefaultNightMode(nightMode);
            }
        });
        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tg = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(Constant.URL_TELEGRAM));
                tg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(tg);
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fb = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(Constant.URL_FACEBOOK));
                fb.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(fb);
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", Constant.URL_EMAIL, null));
                email.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(email);
            }
        });
        AppCompatRadioButton srb = (AppCompatRadioButton) backgroundGroup.getChildAt(3);
        srb.setChecked(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        backgroundGroup.check(selected_bg == 0 ? R.id.pref_appearance_background_1 :
                selected_bg == getResources().getColor(R.color.darkGray) ? R.id.pref_appearance_background_2 :
                        selected_bg == Color.parseColor("#6BCF0C32") ? R.id.pref_appearance_background_3 :
                                selected_bg == Color.parseColor("#EE26C809") ? R.id.pref_appearance_background_4 :
                                        R.id.pref_appearance_background_color);
        nightMode.setChecked(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
    }

    private boolean silentDeviceSound() {
        if (Util.isNotificationGranted(getContext())) {
            return true;
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                startActivityForResult(intent, 3);
                Toast.makeText(getContext(), getString(R.string.grant_notif_permision), Toast.LENGTH_SHORT).show();
            }
            return false;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 3) {
            if (Util.isNotificationGranted(getContext())) {
                editor.putBoolean(Constant.PREF_SILENT_WHEN_PRAYING,true);
                editor.commit();
                silent.setChecked(true);
                Analytics.dndPermission(getContext(),true);
                Toast.makeText(getContext(), getString(R.string.notif_granted), Toast.LENGTH_SHORT).show();
            } else {
                editor.putBoolean(Constant.PREF_SILENT_WHEN_PRAYING,false);
                editor.commit();
                silent.setChecked(false);
                Analytics.dndPermission(getContext(),false);
                Toast.makeText(getContext(), getString(R.string.notification_permision_denied), Toast.LENGTH_SHORT).show();
                //Analytics.logAppEvent("DO_NOT_DISTURB","ACCESS_DINED");
            }

        }
    }

}