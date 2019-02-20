package com.example.admin.youtubev3.Fragment;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.admin.youtubev3.Adapter.ListVideoAdapter;
import com.example.admin.youtubev3.CustomView.CustomTextView;
import com.example.admin.youtubev3.DataBase.DataBase;
import com.example.admin.youtubev3.Interface.DataID;
import com.example.admin.youtubev3.Interface.OnClick;
import com.example.admin.youtubev3.Model.VideoYoutube;
import com.example.admin.youtubev3.R;

import java.util.ArrayList;


public class Fragment_My_Video extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private SQLiteDatabase db;
    private ListVideoAdapter adapter;
    private LinearLayoutManager layoutManager;
    private static final String DATABASE_NAME = "Youtube.sqlite";
    String properties[] = {"id", "title", "publishedAt", "channelId", "description", "thumbnails", "channelTitle",
            "categoryId", "liveBroadcastContent", "duration", "viewCount", "likeCount", "dislikeCount"};
    private ArrayList<VideoYoutube> arrayList;
    OnClick onClick;
    DataID dataID;
    private CustomTextView tv_notification;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_my_video, container, false);
        recyclerView = view.findViewById(R.id.recycle_view_my_video);
        tv_notification = view.findViewById(R.id.txtv_notification_my_video);
        arrayList = new ArrayList<>();
        db = DataBase.initDatabase(getActivity(), DATABASE_NAME);
        Cursor cursor = db.rawQuery("Select * From tbl_MyVideo", null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            for (int j = 0; j < properties.length; j++) {
                properties[j] = cursor.getString(j);
            }
            arrayList.add(new VideoYoutube(properties[0], properties[1], properties[2], properties[3], properties[4], properties[5], properties[6]
                    , properties[7], properties[8], properties[9], properties[10], properties[11], properties[12]));
        }
        tv_notification.setVisibility(arrayList.isEmpty() ? View.VISIBLE : View.GONE);
        adapter = new ListVideoAdapter(getContext(), arrayList, "myvideo");
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
        adapter.setOnItemLayout(new ListVideoAdapter.OnItemLayout() {
            @Override
            public void setOnItemLayoutClick(final int postion) {
                onClick.setOnClick();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dataID.setDataID(arrayList.get(postion));
                    }
                }, 500);
            }
        });
        adapter.setOnListener(new ListVideoAdapter.OnListener() {
            @Override
            public void setOnListener() {
                tv_notification.setVisibility(arrayList.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        dataID = (DataID) activity;
        onClick = (OnClick) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        dataID = null;
        super.onDetach();
    }
}
