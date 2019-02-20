package com.example.admin.youtubev3.CustomView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by Admin on 03/19/18.
 */

public class CustomEditText extends android.support.v7.widget.AppCompatEditText {
    private Context context;

    public CustomEditText(Context context) {
        super(context);
        this.context = context;
        setCustomTypeFace();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setCustomTypeFace();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        setCustomTypeFace();
    }

    private void setCustomTypeFace() {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/sf_ui_display_regular.ttf");
        setTypeface(typeface);
    }
}
