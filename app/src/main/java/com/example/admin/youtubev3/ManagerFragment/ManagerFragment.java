package com.example.admin.youtubev3.ManagerFragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.example.admin.youtubev3.R;

import java.util.ArrayList;


public class ManagerFragment extends Fragment {
    LoadData loadData;

    public void setPreData(LoadData loadData) {
        this.loadData = loadData;
        loadData.execute();
    }

    public abstract class LoadData extends AsyncTask<Void, Void, Boolean> {
//        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(getContext());
//            progressDialog.setMessage("Loading...");
//            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
//            progressDialog.dismiss();
            super.onPostExecute(aBoolean);
        }
    }
}
