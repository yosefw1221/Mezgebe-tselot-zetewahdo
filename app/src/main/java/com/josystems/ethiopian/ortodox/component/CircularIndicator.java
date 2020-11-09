package com.josystems.ethiopian.ortodox.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.appcompat.widget.LinearLayoutCompat;

import com.josystems.ethiopian.ortodox.R;


public class CircularIndicator extends LinearLayoutCompat {
    private int SIZE = 1;
    private int COLOR = Color.WHITE;
    private int ACTIVE = 0;
    private float CIRCLE_SIZE = 15f;
    private TextView[] indicators;

    public CircularIndicator(Context context,int size) {
        super(context);
        setup(size);
    }

    public CircularIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.circular_indicator);
        SIZE = typedArray.getInt(R.styleable.circular_indicator_size,1);
        CIRCLE_SIZE = typedArray.getDimension(R.styleable.circular_indicator_circle_size,CIRCLE_SIZE);
        ACTIVE = typedArray.getInt(R.styleable.circular_indicator_active,0);
        COLOR = typedArray.getColor(R.styleable.circular_indicator_color,Color.WHITE);
    }

    public CircularIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(R.styleable.circular_indicator);
        SIZE = typedArray.getInt(R.styleable.circular_indicator_size,1);
        CIRCLE_SIZE = typedArray.getDimension(R.styleable.circular_indicator_circle_size,CIRCLE_SIZE);
        ACTIVE = typedArray.getInt(R.styleable.circular_indicator_active,0);
        COLOR = typedArray.getColor(R.styleable.circular_indicator_color,Color.WHITE);
    }
    public void setup(int size){
        removeAllViews();
        setOrientation(HORIZONTAL);
        indicators = new TextView[size];
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new TextView(getContext());
            indicators[i].setText(Html.fromHtml("&#9679;"));
            indicators[i].setTextSize(TypedValue.COMPLEX_UNIT_SP,CIRCLE_SIZE);
            indicators[i].setTextColor(Color.DKGRAY);
            indicators[i].setPadding(5,1,5,1);
            addView(indicators[i]);
        }
        indicators[ACTIVE].setTextColor(COLOR);
    }
    public void setActive(int active){
        for(TextView indicator : indicators){
            indicator.setTextColor(Color.DKGRAY);
        }
        indicators[active].setTextColor(COLOR);
    }
}
