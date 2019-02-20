package com.example.admin.youtubev3.CustomView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Admin on 03/19/18.
 */

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {
    private Context context;

    public CustomTextView(Context context) {
        super(context);
        this.context = context;
        setCustomTypeFace();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setCustomTypeFace();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setCustomTypeFace();
    }

    private void setCustomTypeFace() {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "font/sf_ui_display_regular.ttf");
        setTypeface(tf);
    }


}
