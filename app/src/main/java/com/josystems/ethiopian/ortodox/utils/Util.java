package com.josystems.ethiopian.ortodox.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.josystems.ethiopian.ortodox.database.MyHolyBookDB;
import com.josystems.ethiopian.ortodox.model.HolyBook;
import com.josystems.ethiopian.ortodox.R;
import com.josystems.ethiopian.ortodox.ui.home.HomeViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;

public class Util {
    static final MutableLiveData<Float> fontSize = new MutableLiveData<>();

    public static MutableLiveData<Float> getReaderFontSize() {
        return fontSize;
    }

    public static void setReaderFontSize(float newSize) {
        fontSize.setValue(newSize);
    }

    public static void getHolyIconWithCallback(final Context context, final String id, String iconUrl, final CallBack callBack) {
        final Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), R.drawable.placeholder, context.getTheme()); // load default icon for faillbac//
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File imageDir = contextWrapper.getDir("imageDir", MODE_PRIVATE);
        File image = new File(imageDir, id + ".png");
        if (id == null || id.isEmpty() || iconUrl == null || iconUrl.isEmpty())
            callBack.onFinish(drawable);
        if (!image.exists() && !(iconUrl == null || iconUrl.isEmpty())) {
            Picasso.get().load(iconUrl).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable d = new BitmapDrawable(context.getResources(), bitmap);
                    callBack.onFinish(d);

                }

                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    callBack.onFinish(drawable);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
        } else if (image.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(image);
                Drawable d = Drawable.createFromStream(inputStream, image.getName() + ".png");
                callBack.onFinish(d);
            } catch (Exception e) {
                FirebaseCrashlytics.getInstance().recordException(e);
                callBack.onFinish(drawable);
            }
        } else
            callBack.onFinish(drawable);
    }


    public static void saveHolyIcontoStorage(Context context, String id, Drawable holyIcon) {
        ContextWrapper contextWrapper = new ContextWrapper(context);
        File Path = contextWrapper.getDir("imageDir", MODE_PRIVATE);
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
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    public static void downloadHolyIconToStorage(final Context context, final String id, String holyIconUrl) {
        if (holyIconUrl == null || holyIconUrl.isEmpty()) return;
        Picasso.get().load(holyIconUrl).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                saveHolyIcontoStorage(context, id, drawable);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    public static Drawable getTintedDrawable(Context context, int drawableID, int color) {
        return getTintedDrawable(context, drawableID, color, PorterDuff.Mode.DARKEN);
    }

    public static Drawable getTintedBackground(Context context, int color) {
        return getTintedDrawable(context, R.drawable.bg, color, PorterDuff.Mode.DARKEN);
    }

    public static Drawable getTintedDrawable(Context context, int drawableID, int color, PorterDuff.Mode mode) {
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(), drawableID, context.getTheme());
        if (Build.VERSION.SDK_INT<21) {
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            drawable = wrappedDrawable.mutate();
        }
        if (color == Color.WHITE)
            return ResourcesCompat.getDrawable(context.getResources(), R.drawable.whitebg, context.getTheme());
         else if (color == 0)
            DrawableCompat.setTintList(drawable, null);
        else {
            DrawableCompat.setTint(drawable, color);
            DrawableCompat.setTintMode(drawable, mode);
        }
        if (Build.VERSION.SDK_INT<21)
            drawable.invalidateSelf();
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

    public static void silentPhone(Context context, boolean silent) {
        try {
            SharedPreferences pref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE);
            if (!pref.getBoolean(Constant.PREF_SILENT_WHEN_PRAYING, isNotificationGranted(context)))
                return;
            SharedPreferences.Editor editor = pref.edit();
            AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (manager == null) return;
            if (silent) {
                editor.putInt(Constant.PREF_DEVICE_SILENT_MODE, manager.getRingerMode());
                manager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                editor.apply();
            } else {
                manager.setRingerMode(pref.getInt(Constant.PREF_DEVICE_SILENT_MODE, 2));
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public static void showAgreementDialog(final Context context) {
        SharedPreferences pref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, MODE_PRIVATE);
        boolean firstLaunch = pref.getBoolean(Constant.PREF_FIRST_LAUNCH, true);
        if (!firstLaunch) return;
        final SharedPreferences.Editor editor = pref.edit();
        new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(Html.fromHtml("<font color=\"red\">ማሳሰቢያ</font>"))
                .setMessage(Html.fromHtml("በስመ አብ ወወልድ ወመንፈስ ቅዱስ አሀዱ አምላክ አሜን።" +
                        "</br>ውድ የእግዚአብሔር ቤተሰቦች ይህ መተግበሪያ የተሰራው በተለያየ ምክኒያት <font color=\"red\">የጸሎት መፅሀፍትን" +
                        " ማግኘት ለማይችሉ</font> ምዕመናን ሲሆን፤ <font color=\"red\">የጸሎት መፅሀፍትን እንዲተካ ታስቦ ያልተሰራ</font> መሆኑን ለማሳሰብ እወዳለሁ።</br>" +
                        "የጸሎት መፅሀፍት ያላችሁ ወይም በቀላሉ ማግኘት የምትችሉ ምዕመናን የጸሎት መፅሀፍትን ብትጠቀሙ ይመከራል።"))
                .setPositiveButton(Html.fromHtml("<font color\"red\">እስማማለሁ</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        editor.putBoolean(Constant.PREF_FIRST_LAUNCH, false);
                        editor.apply();
                        dialogInterface.dismiss();
                        Analytics.logAgreement(context, true);
                    }
                })
                .setNegativeButton("አቋርጥ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Analytics.logAgreement(context, false);
                        ((Activity) context).finish();
                    }
                })
                .show();

    }

    public static boolean isNotificationGranted(Context ctx) {
        NotificationManager nm = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (nm != null)
                return nm.isNotificationPolicyAccessGranted();
            return false;
        }
        return true;
    }

    public interface CallBack {
        void onFinish(Drawable drawable);
    }

    public static class importHolyBooksFromAsset extends AsyncTask<Context, Void, Context> {

        @Override
        protected Context doInBackground(Context... contexts) {
            Context context = contexts[0];
            MyHolyBookDB db = new MyHolyBookDB(context);
            if (db.getTotalHolyBooks() == 0) {
                HolyBook wudaseMariam = new HolyBook("wudase_mariam_amharic", "", "ውዳሴ ማርያም", "", "አማርኛ", "wudase_mariam_am", 202010270900L);
                HolyBook wudaseMariamGeez = new HolyBook("wudase_mariam_geez", "", "ውዳሴሀ ማርያም", "", "ግዕዝ", "wudase_mariam_geez", 202010270900L);
                HolyBook melkeaEyesus = new HolyBook("melkea_eyesus_geez", "", "መልክአ ኢየሱስ", "", "ግዕዝ", "melkea_eyesus_geez", 202010270900L);
                HolyBook melkeaMariamGeez = new HolyBook("melkea_mariam_geez", "", "መልክአ ማርያም", "", "ግዕዝ", "melkea_mariam_geez", 202010270900L);
                HolyBook wudaseAmlakAmharic = new HolyBook("wudase_amlak_amharic", "", "ውዳሴ አምላክ", "", "አማርኛ", "wudase_amlak_amharic", 202010270900L);
                HolyBook[] holyBook = {wudaseMariam, wudaseMariamGeez, melkeaEyesus, melkeaMariamGeez, wudaseAmlakAmharic};
                Drawable[] drawables = {ResourcesCompat.getDrawable(context.getResources(), R.drawable.mary, context.getTheme()),
                        ResourcesCompat.getDrawable(context.getResources(), R.drawable.dngel_mariam, context.getTheme()),
                        ResourcesCompat.getDrawable(context.getResources(), R.drawable.jesus, context.getTheme()),
                        ResourcesCompat.getDrawable(context.getResources(), R.drawable.st_mary, context.getTheme()),
                        ResourcesCompat.getDrawable(context.getResources(), R.drawable.jusus_with_holybook, context.getTheme())};
                db.addHolyBooks(holyBook, drawables);
            }
            return context;
        }

        @Override
        protected void onPostExecute(Context context) {
            super.onPostExecute(context);
            HomeViewModel.refreshHolyBooks(context);
        }
    }

}
