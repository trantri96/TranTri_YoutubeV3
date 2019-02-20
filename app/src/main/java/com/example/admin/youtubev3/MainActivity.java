package com.example.admin.youtubev3;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.admin.youtubev3.Fragment.Fragment_Home;
import com.example.admin.youtubev3.Fragment.Fragment_Library;
import com.example.admin.youtubev3.Fragment.Fragment_My_Video;
import com.example.admin.youtubev3.Fragment.Fragment_Search_Detail;
import com.example.admin.youtubev3.Fragment.Fragment_Search_General;
import com.example.admin.youtubev3.Fragment.Fragment_list_video;
import com.example.admin.youtubev3.Fragment.Fragment_play_video;
import com.example.admin.youtubev3.Interface.DataID;
import com.example.admin.youtubev3.Interface.DataSearch;
import com.example.admin.youtubev3.Interface.HomeData;

import com.example.admin.youtubev3.Interface.ListenerHistory;
import com.example.admin.youtubev3.Interface.ListenerMyVideo;
import com.example.admin.youtubev3.Interface.ListenerWatchLater;
import com.example.admin.youtubev3.Interface.LoadPlayList;
import com.example.admin.youtubev3.Interface.OnClick;
import com.example.admin.youtubev3.Interface.RelationData;
import com.example.admin.youtubev3.Interface.SearchDetail;
import com.example.admin.youtubev3.Interface.SearchKey;
import com.example.admin.youtubev3.Interface.SendPlayList;
import com.example.admin.youtubev3.Model.PlayListYoutube;
import com.example.admin.youtubev3.Model.VideoYoutube;
import com.github.pedrovgs.DraggablePanel;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements ListenerMyVideo, ListenerWatchLater, ListenerHistory, HomeData, SendPlayList, SearchDetail, RelationData, LoadPlayList, DataSearch, View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener, DataID, OnClick {
    private CardView cardView_top;
    private CardView cardView_buttom;
    private ImageView img_search, img_account;
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment = null;
    public static DraggablePanel draggablePanel;
    private Fragment_play_video fragment_play_video;
    private Fragment_list_video fragment_list_video;
    private Fragment_Search_Detail fragment_search_detail;
    private Fragment_Search_General fragment_search_general;
    private Fragment_Home fragment_home;
    SearchKey searchKey;
    LoadPlayList loadPlayList;
    HomeData homeData;
    RelationData relationData;
    SearchDetail searchDetail;
    SendPlayList sendPlayList;
    ListenerHistory listenerHistory;
    ListenerWatchLater listenerWatchLater;
    ListenerMyVideo listenerMyVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        fragment_home = new Fragment_Home();
        getSupportFragmentManager().beginTransaction().
                add(R.id.fram_content, fragment_home).
                commit();
        fragment_list_video = new Fragment_list_video();
        fragment_play_video = new Fragment_play_video();
        draggablePanel = findViewById(R.id.draggable_panel);
        draggablePanel.setFragmentManager(getSupportFragmentManager());
        draggablePanel.setTopFragment(fragment_play_video);
        draggablePanel.setBottomFragment(fragment_list_video);
        draggablePanel.setTopViewHeight(400);
        draggablePanel.initializeView();
    }

    private void Anhxa() {
        img_search = findViewById(R.id.img_search_desktop);
        img_search.setOnClickListener(this);
        img_account = findViewById(R.id.img_desktop_account);
        img_account.setOnClickListener(this);
        bottomNavigationView = findViewById(R.id.btn_navegation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        cardView_top = findViewById(R.id.card_view_top);
        cardView_buttom = findViewById(R.id.card_view_buttom);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_search_desktop:
                fragment = new Fragment_Search_General();
                LoadFragment(fragment);
                break;
            case R.id.img_desktop_account:
                Snackbar.make(img_account, "Hiện tại không hỗ trợ!", Snackbar.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_home:
                fragment = new Fragment_Home();
                LoadFragment(fragment);
                return true;
            case R.id.item_myvideo:
                fragment = new Fragment_My_Video();
                LoadFragment(fragment);
                return true;
            case R.id.item_library:
                fragment = new Fragment_Library();
                LoadFragment(fragment);
                return true;
        }
        return false;
    }


    public Fragment getActiveFragment() {
        Fragment f = null;
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            f = new Fragment_Home();
        } else {
            String tag = getSupportFragmentManager().getBackStackEntryAt((getSupportFragmentManager().getBackStackEntryCount()) - 1).getName();
            f = getSupportFragmentManager().findFragmentByTag(tag);
        }
        return f;
    }

    private void BackFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getActiveFragment() instanceof Fragment_Home) {
                        setSelectedItem(R.id.item_home);
                    } else {
                        if (getActiveFragment() instanceof Fragment_My_Video) {
                            setSelectedItem(R.id.item_myvideo);
                        } else {
                            if (getActiveFragment() instanceof Fragment_Library) {
                                setSelectedItem(R.id.item_library);
                            }
                        }
                    }
                    cardView_top.setVisibility(!(getActiveFragment() instanceof Fragment_Search_General) ? View.VISIBLE : View.GONE);
                    cardView_buttom.setVisibility(!(getActiveFragment() instanceof Fragment_Search_General) ? View.VISIBLE : View.GONE);
                }
            }, 100);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (draggablePanel.getVisiable()) {
            BackFragment();
        } else {
            if (draggablePanel.isMaximized()) {
                draggablePanel.minimize();
            } else {
                BackFragment();
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private void setSelectedItem(int actionId) {
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem menuItem = menu.getItem(i);
            ((MenuItemImpl) menuItem).setExclusiveCheckable(false);
            menuItem.setChecked(menuItem.getItemId() == actionId);
            ((MenuItemImpl) menuItem).setExclusiveCheckable(true);
        }
    }

    private void LoadFragment(Fragment fragment) {
        cardView_top.setVisibility((fragment instanceof Fragment_Search_General || fragment instanceof Fragment_Search_Detail) ? View.GONE : View.VISIBLE);
        cardView_buttom.setVisibility(fragment instanceof Fragment_Search_General ? View.GONE : View.VISIBLE);
        if (!fragment.getClass().getName().toString().equals(getActiveFragment().getClass().getName().toString())) {
            getSupportFragmentManager().beginTransaction().setAllowOptimization(true).
                    replace(R.id.fram_content, fragment, fragment.getClass().getName()).
                    addToBackStack(fragment.getClass().getName()).commit();
        }
    }


    @Override
    public void setOnClick() {
        draggablePanel.Init();
        draggablePanel.maximize();
    }

    @Override
    public void setDataID(VideoYoutube videoYoutube) {
        if (videoYoutube != null) {
            fragment_play_video.DoSomeThing(videoYoutube.getId());
            fragment_list_video.DoSomeThing(videoYoutube);
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof Fragment_Home) {
            homeData = (HomeData) fragment;
        } else {
            if (fragment instanceof Fragment_list_video) {
                relationData = (RelationData) fragment;
            } else {
                if (fragment instanceof Fragment_Search_Detail) {
                    searchKey = (SearchKey) fragment;
                    loadPlayList = (LoadPlayList) fragment;
                    searchDetail = (SearchDetail) fragment;
                    sendPlayList = (SendPlayList) fragment;
                    listenerHistory = (ListenerHistory) fragment;
                    listenerWatchLater = (ListenerWatchLater) fragment;
                    listenerMyVideo = (ListenerMyVideo) fragment;
                }
            }
        }
        super.onAttachFragment(fragment);
    }


    @Override
    public void setDataSearch(final String chuoi) {
        fragment_search_detail = new Fragment_Search_Detail();
        LoadFragment(fragment_search_detail);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                searchKey.setSearchKey(chuoi);
            }
        }, 100);
    }

    @Override
    public void setLoadPlayList(PlayListYoutube playList, String url) {
        loadPlayList.setLoadPlayList(playList, url);
    }

    @Override
    public <T> void setHomeData(ArrayList<T> arrayList) {
        homeData.setHomeData(arrayList);
    }

    @Override
    public <T> void setRelationData(ArrayList<T> arrayList) {
        relationData.setRelationData(arrayList);
    }

    @Override
    public <T> void setSearchDetail(ArrayList<T> arrayList) {
        searchDetail.setSearchDetail(arrayList);
    }

    @Override
    public void setSendPlayList(final String url, final String title) {
        fragment_search_detail = new Fragment_Search_Detail();
        LoadFragment(fragment_search_detail);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendPlayList.setSendPlayList(url, title);
            }
        }, 100);
    }

    @Override
    public void setListenerHistory(String check) {
        fragment_search_detail = new Fragment_Search_Detail();
        LoadFragment(fragment_search_detail);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listenerHistory.setListenerHistory("history");
            }
        }, 100);
    }

    @Override
    public void setListenerWatchLater(String check) {
        fragment_search_detail = new Fragment_Search_Detail();
        LoadFragment(fragment_search_detail);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listenerWatchLater.setListenerWatchLater("watchlater");
            }
        }, 100);
    }

    @Override
    public void setListenerMyVideo(String check) {
        fragment_search_detail = new Fragment_Search_Detail();
        LoadFragment(fragment_search_detail);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                listenerMyVideo.setListenerMyVideo("myvideo");
            }
        }, 100);
    }
}
