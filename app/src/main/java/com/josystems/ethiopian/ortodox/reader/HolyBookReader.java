package com.josystems.ethiopian.ortodox.reader;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;

import com.josystems.ethiopian.ortodox.adapter.ChapterPopup;
import com.josystems.ethiopian.ortodox.adapter.onChapterSelectedListener;
import com.josystems.ethiopian.ortodox.component.CircularIndicator;
import com.josystems.ethiopian.ortodox.database.MyHolyBookDB;
import com.josystems.ethiopian.ortodox.model.Chapter;
import com.josystems.ethiopian.ortodox.model.HolyBook;
import com.josystems.ethiopian.ortodox.R;
import com.josystems.ethiopian.ortodox.utils.Constant;
import com.josystems.ethiopian.ortodox.utils.Util;

import java.util.List;

public class HolyBookReader extends AppCompatActivity {
    RelativeLayout actionBar;
    CountDownTimer timer;
    ChapterPopup chapterPopup;
    ConstraintLayout parent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.hollybook_reader);

        parent = findViewById(R.id.holybook_parent);
        String id = getIntent().getStringExtra("id");
        MyHolyBookDB db = new MyHolyBookDB(this);
        HolyBook holybook = db.getHollyBook(id);
        final ViewPager viewPager = findViewById(R.id.readerPager);
         actionBar = findViewById(R.id.reader_actionbar);
        AppCompatImageButton outline = findViewById(R.id.reader_outline);
        AppCompatImageButton close = findViewById(R.id.reader_back);
        final CircularIndicator indicator = findViewById(R.id.reader_indicator);
        List<Chapter> chapterList = Reader.parseHollyBookChapters(holybook.getDataString(),holybook.getTitle());
        indicator.setup(chapterList.size());
        ChapterFragmentAdapter fragmentAdapter = new ChapterFragmentAdapter(getSupportFragmentManager(), chapterList);
        viewPager.setAdapter(fragmentAdapter);
        int bg = getSharedPreferences(Constant.SHARED_PREF_NAME,MODE_PRIVATE).getInt(Constant.PREF_BACKGROUND_COLOR(),Constant.getDefaultBackground());
        parent.setBackground(Util.getTintedBackground(this,bg));
        chapterPopup = new ChapterPopup(this, chapterList, new onChapterSelectedListener() {
            @Override
            public void onSelected(int chapter) {
                viewPager.setCurrentItem(chapter,true);
                chapterPopup.dismiss();
            }
        });
        chapterPopup.getPopupWindow().setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                autoHideActionBar(true,false);
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        outline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoHideActionBar(true,true);
                chapterPopup.show(view);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                autoHideActionBar(false,true);
            }
            @Override
            public void onPageSelected(int position) {
                indicator.setActive(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    private void autoHideActionBar(boolean force,boolean show){
        if (timer!=null)timer.cancel();
        boolean isShow = actionBar.getVisibility()==View.VISIBLE;
        if (force) {
            if (show == isShow) return;
            animate(actionBar);
            actionBar.setVisibility(show ? View.VISIBLE : View.GONE);
            return;
        }
        if (show){
            if (!isShow){
                animate(actionBar);
                actionBar.setVisibility(View.VISIBLE);
            }
        }
        timer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long l) {
            }
            @Override
            public void onFinish() {
                animate(actionBar);
                actionBar.setVisibility(View.GONE);
            }
        }.start();
    }
    private void animate(View v){
        Transition transition = new Slide(Gravity.TOP);
        transition.addTarget(v);
        transition.setDuration(300);
        TransitionManager.beginDelayedTransition(parent,transition);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Util.silentPhone(this,true);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Util.silentPhone(this,false);
    }
}