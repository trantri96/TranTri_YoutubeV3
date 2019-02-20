package com.example.admin.youtubev3.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.youtubev3.Adapter.ListVideoAdapter;
import com.example.admin.youtubev3.DataBase.DataBase;
import com.example.admin.youtubev3.Interface.DataID;
import com.example.admin.youtubev3.Interface.ListenerHistory;
import com.example.admin.youtubev3.Interface.ListenerMyVideo;
import com.example.admin.youtubev3.Interface.ListenerWatchLater;
import com.example.admin.youtubev3.Interface.OnClick;
import com.example.admin.youtubev3.Interface.SendPlayList;
import com.example.admin.youtubev3.Model.VideoYoutube;
import com.example.admin.youtubev3.R;

import java.util.ArrayList;


public class Fragment_Library extends Fragment implements View.OnClickListener {
    private View view;
    private CardView card_last_time;
    private LinearLayout layout_history, layout_my_video, layout_watche_last,
            layout_report, layout_developer, layout_version;
    private ArrayList<VideoYoutube> arrayList;
    private static final String DATABASE_NAME = "Youtube.sqlite";
    SQLiteDatabase db;
    String properties[] = {"id", "title", "publishedAt", "channelId", "description", "thumbnails", "channelTitle",
            "categoryId", "liveBroadcastContent", "duration", "viewCount", "likeCount", "dislikeCount"};
    private RecyclerView recycle_history;
    private ListVideoAdapter adapter;
    private LinearLayoutManager layoutManager;
    OnClick onClick;
    DataID dataID;
    Activity activity;
    ListenerHistory listenerHistory;
    ListenerWatchLater listenerWatchLater;
    ListenerMyVideo listenerMyVideo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_library, container, false);
        card_last_time = view.findViewById(R.id.card_view_last_time);
        layout_history = view.findViewById(R.id.item_layout_history);
        layout_history.setOnClickListener(this);
        layout_my_video = view.findViewById(R.id.item_layout_my_video);
        layout_my_video.setOnClickListener(this);
        layout_watche_last = view.findViewById(R.id.item_layout_watch_last);
        layout_watche_last.setOnClickListener(this);
        layout_report = view.findViewById(R.id.item_layout_report);
        layout_report.setOnClickListener(this);
        layout_developer = view.findViewById(R.id.item_layout_developer);
        layout_developer.setOnClickListener(this);
        layout_version = view.findViewById(R.id.item_layout_version);
        layout_version.setOnClickListener(this);
        recycle_history = view.findViewById(R.id.recycle_view_last_time);
        arrayList = new ArrayList<>();

        //CSDL
        db = DataBase.initDatabase(getActivity(), DATABASE_NAME);
        Cursor cursor = db.rawQuery("Select * From tbl_HistoryVideo", null);
        cursor.moveToFirst();
        for (int i = 0; i < (cursor.getCount() < 10 ? cursor.getCount() : 10); i++) {
            cursor.moveToPosition(i);
            for (int j = 0; j < properties.length; j++) {
                properties[j] = cursor.getString(j);
            }
            arrayList.add(new VideoYoutube(properties[0], properties[1], properties[2], properties[3], properties[4], properties[5], properties[6]
                    , properties[7], properties[8], properties[9], properties[10], properties[11], properties[12]));
        }
        card_last_time.setVisibility(arrayList.isEmpty() ? View.GONE : View.VISIBLE);
        adapter = new ListVideoAdapter(getContext(), arrayList, "history");
        recycle_history.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recycle_history.setLayoutManager(layoutManager);
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
                card_last_time.setVisibility(arrayList.isEmpty() ? View.GONE : View.VISIBLE);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        dataID = (DataID) activity;
        onClick = (OnClick) activity;
        listenerHistory = (ListenerHistory) activity;
        listenerWatchLater = (ListenerWatchLater) activity;
        listenerMyVideo = (ListenerMyVideo) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        dataID = null;
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_layout_report:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "khaiphan16121997@gmail.com.vn", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "[Góp ý & Sửa lỗi]");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
                break;
            case R.id.item_layout_developer:
                ShowDialog("Nhà phát triển", "khaiphan16121997@gmail.com");
                break;
            case R.id.item_layout_version:
                ShowDialog("Phiên bản", "Youtube_YT_1.0.0_Beta");
                break;
            case R.id.item_layout_history:
                listenerHistory.setListenerHistory("history");
                break;
            case R.id.item_layout_my_video:
                listenerMyVideo.setListenerMyVideo("myvideo");
                break;
            case R.id.item_layout_watch_last:
                listenerWatchLater.setListenerWatchLater("watchlater");
                break;
            default:
                break;
        }
    }

    public void ShowDialog(String title, String message) {
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        TextView tv_message = dialog.findViewById(R.id.txtv_sub_left_dialog);
        tv_message.setText(message);
        tv_message.setTextSize(15);
        tv_message.setTextColor(getContext().getResources().getColor(R.color.black));
        TextView tv_content = dialog.findViewById(R.id.txtv_selected_dialog);
        tv_content.setVisibility(View.GONE);
        TextView tv_title = dialog.findViewById(R.id.txtv_title_dialog);
        tv_title.setText(title);
        TextView tv_cancel = dialog.findViewById(R.id.txtv_cancel_dialog);
        tv_cancel.setVisibility(View.GONE);
        TextView tv_apply = dialog.findViewById(R.id.txtv_apply_dialog);
        tv_apply.setVisibility(View.GONE);
        dialog.show();
    }
}
