package com.example.admin.youtubev3.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;

import com.example.admin.youtubev3.R;

public class DialogManager {
    public static void ShowShare(Activity activity, String url) {
        Intent shareText = new Intent(Intent.ACTION_SEND);
        shareText.setType("text/plain");
        shareText.putExtra(Intent.EXTRA_TEXT, url);
        activity.startActivity(Intent.createChooser(shareText, "Chia sẻ"));
    }
}
