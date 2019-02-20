package com.example.admin.youtubev3.CustomView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends android.support.v7.widget.AppCompatButton {
    private Context context;

    public CustomButton(Context context) {
        super(context);
        this.context = context;
        Init();
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        Init();
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        Init();
    }

    private void Init() {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "font/sf_ui_display_regular.ttf");
        setTypeface(typeface);
    }
}
