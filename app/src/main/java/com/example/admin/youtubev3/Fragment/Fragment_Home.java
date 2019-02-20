package com.example.admin.youtubev3.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.admin.youtubev3.Adapter.ListVideoAdapter;
import com.example.admin.youtubev3.Interface.DataID;
import com.example.admin.youtubev3.Interface.HomeData;
import com.example.admin.youtubev3.Interface.LiveVideo;
import com.example.admin.youtubev3.Interface.OnClick;
import com.example.admin.youtubev3.Interface.SendPlayList;
import com.example.admin.youtubev3.ManagerFragment.ManagerFragment;
import com.example.admin.youtubev3.Model.VideoYoutube;
import com.example.admin.youtubev3.R;
import com.example.admin.youtubev3.ReadJson.ReadJsonVideo;

import java.util.ArrayList;


public class Fragment_Home extends ManagerFragment implements HomeData, View.OnClickListener {
    private View view;
    private RecyclerView recyclerView;
    private String KEY = "";
    private static final String URL = "https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails,statistics&chart=mostPopular&regionCode=VN&videoCategoryId=0&maxResults=50&key=AIzaSyAwNm1IjKJIvrlvD7pN7__Dfp43dy7slF0";
    private ArrayList<VideoYoutube> array;
    private ListVideoAdapter adapter;
    private LinearLayoutManager layoutManager;
    OnClick onClick;
    DataID dataID;
    private ReadJsonVideo readJsonVideo;
    private Context context;
    private Activity activity;
    private LinearLayout layout_music, layout_game, layout_moive, layout_animation;
    SendPlayList sendPlayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_home, null);
        context = getActivity();
        array = new ArrayList<>();
        this.KEY = getContext().getResources().getString(R.string.KEY);
        recyclerView = view.findViewById(R.id.recycle_list_video);
        layout_music = view.findViewById(R.id.layout_home_music);
        layout_music.setOnClickListener(this);
        layout_game = view.findViewById(R.id.layout_home_game);
        layout_game.setOnClickListener(this);
        layout_moive = view.findViewById(R.id.layout_home_movie);
        layout_moive.setOnClickListener(this);
        layout_animation = view.findViewById(R.id.layout_home_animtion);
        layout_animation.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        setPreData(new LoadData() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                readJsonVideo = new ReadJsonVideo(activity, URL, "home");
                readJsonVideo.setData();
                return super.doInBackground(voids);
            }
        });
        super.onStart();
    }

    @Override
    public void onAttach(Activity activity) {
        this.activity = activity;
        dataID = (DataID) activity;
        onClick = (OnClick) activity;
        sendPlayList = (SendPlayList) activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        dataID = null;
        super.onDetach();
    }


    @Override
    public <T> void setHomeData(ArrayList<T> arrayList) {
        array = new ArrayList<>();
        array = (ArrayList<VideoYoutube>) arrayList;
        if (getContext() != null) {
            array = (ArrayList<VideoYoutube>) arrayList;
            adapter = new ListVideoAdapter(getContext(), array, "bt");
            recyclerView.setAdapter(adapter);
            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            adapter.setOnItemLayout(new ListVideoAdapter.OnItemLayout() {
                @Override
                public void setOnItemLayoutClick(final int postion) {
                    onClick.setOnClick();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dataID.setDataID(array.get(postion));
                        }
                    }, 500);
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_home_music:
                String link_music = "https://www.googleapis.com/youtube/v3/playlists?part=snippet,contentDetails&channelId=UCUgXK2UjZ8G_EM438aYkGrw&maxResults=25&key=" + getContext().getResources().getString(R.string.KEY);
                sendPlayList.setSendPlayList(link_music, "Âm nhạc");
                break;
            case R.id.layout_home_game:
                String link_game = "https://www.googleapis.com/youtube/v3/playlists?part=snippet,contentDetails&channelId=UCHKuLpFy9q8XDp0i9WNHkDw&maxResults=25&key=" + getContext().getResources().getString(R.string.KEY);
                sendPlayList.setSendPlayList(link_game, "Game");
                break;
            case R.id.layout_home_movie:
                String link_movie = "https://www.googleapis.com/youtube/v3/playlists?part=snippet,contentDetails&channelId=UCjmJDM5pRKbUlVIzDYYWb6g&maxResults=25&key=" + getContext().getResources().getString(R.string.KEY);
                sendPlayList.setSendPlayList(link_movie, "Phim ảnh");
                break;
            case R.id.layout_home_animtion:
                String link_anim = "https://www.googleapis.com/youtube/v3/playlists?part=snippet,contentDetails&channelId=UC5ezaYrzZpyItPSRG27MLpg&maxResults=25&key=" + getContext().getResources().getString(R.string.KEY);
                sendPlayList.setSendPlayList(link_anim, "Hoạt hình");
                break;
        }
    }
}
