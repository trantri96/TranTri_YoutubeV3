package com.example.admin.youtubev3.Fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.admin.youtubev3.CustomView.CustomTextView;
import com.example.admin.youtubev3.Interface.DataID;
import com.example.admin.youtubev3.MainActivity;
import com.example.admin.youtubev3.ManagerFragment.ManagerFragment;
import com.example.admin.youtubev3.Model.VideoYoutube;
import com.example.admin.youtubev3.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.google.android.youtube.player.YouTubePlayerView;

public class Fragment_play_video extends Fragment implements YouTubePlayer.OnInitializedListener {
    private View view;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private String ID = "";
    private int REQUESTVIDEO = 100;
    private YouTubePlayer youTubePlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_play_video, null);
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        getFragmentManager().beginTransaction().add(R.id.frame_youtube, youTubePlayerFragment).commit();
        youTubePlayerFragment.initialize(getResources().getString(R.string.KEY), this);
        return view;
    }

    public void DoSomeThing(String id) {
        this.ID = id;
        if (youTubePlayer != null) {
            youTubePlayer.loadVideo(ID);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(ID);
        this.youTubePlayer = youTubePlayer;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTVIDEO) {
            youTubePlayerFragment.initialize("AIzaSyAwNm1IjKJIvrlvD7pN7__Dfp43dy7slF0", this);
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(getActivity(), REQUESTVIDEO);
        } else {
            Toast.makeText(getContext(), "Erro!", Toast.LENGTH_SHORT).show();
        }
    }
}
