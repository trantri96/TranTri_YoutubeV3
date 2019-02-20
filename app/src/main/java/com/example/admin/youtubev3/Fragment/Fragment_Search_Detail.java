package com.example.admin.youtubev3.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.youtubev3.Adapter.ListChannelAdapter;
import com.example.admin.youtubev3.Adapter.ListPlaylistAdapter;
import com.example.admin.youtubev3.Adapter.ListVideoAdapter;
import com.example.admin.youtubev3.CustomView.CustomTextView;
import com.example.admin.youtubev3.DataBase.DataBase;
import com.example.admin.youtubev3.Dialog.DialogManager;
import com.example.admin.youtubev3.Interface.DataID;
import com.example.admin.youtubev3.Interface.ListenerHistory;
import com.example.admin.youtubev3.Interface.ListenerMyVideo;
import com.example.admin.youtubev3.Interface.ListenerWatchLater;
import com.example.admin.youtubev3.Interface.LoadPlayList;
import com.example.admin.youtubev3.Interface.OnClick;
import com.example.admin.youtubev3.Interface.SearchDetail;
import com.example.admin.youtubev3.Interface.SearchKey;
import com.example.admin.youtubev3.Interface.SendPlayList;
import com.example.admin.youtubev3.ManagerFragment.ManagerFragment;
import com.example.admin.youtubev3.Model.ChannelYoutube;
import com.example.admin.youtubev3.Model.PlayListYoutube;
import com.example.admin.youtubev3.Model.VideoYoutube;
import com.example.admin.youtubev3.R;
import com.example.admin.youtubev3.ReadJson.ReadJsonVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_Search_Detail extends ManagerFragment implements ListenerMyVideo, ListenerWatchLater, ListenerHistory, SendPlayList, LoadPlayList,
        SearchDetail, SearchKey, View.OnClickListener {
    private View view;
    private ImageView img_back, img_reset, img_filter;
    private CustomTextView tv_title, tv_notification;
    private ReadJsonVideo readJsonVideo;
    private RecyclerView recyclerView;
    private ArrayList<VideoYoutube> array_video;
    private ListVideoAdapter adapter;
    private LinearLayoutManager layoutManager;
    DataID dataID;
    OnClick onClick;
    private Dialog dialog;
    TextView tv_content, tv_cancel, tv_apply;
    private String type = "video";
    private String key_word = "";
    private LoadPlayList loadPlayList;
    private String check = "";
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "Youtube.sqlite";
    private static final String TABLE_HISTORY = "tbl_HistoryVideo";
    private static final String TABLE_WATCHLAST = "tbl_WatchLast";
    private static final String TABLE_MYVIDEO = "tbl_MyVideo";
    String properties[] = {"id", "title", "publishedAt", "channelId", "description", "thumbnails", "channelTitle",
            "categoryId", "liveBroadcastContent", "duration", "viewCount", "likeCount", "dislikeCount"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search_detail, container, false);
        Anhxa();
        array_video = new ArrayList<>();
        return view;
    }

    private void Anhxa() {
        img_back = view.findViewById(R.id.img_back_seach_detail);
        img_back.setOnClickListener(this);
        img_reset = view.findViewById(R.id.img_reset_search_detail);
        img_reset.setOnClickListener(this);
        img_filter = view.findViewById(R.id.img_filter_detail);
        img_filter.setOnClickListener(this);
        tv_title = view.findViewById(R.id.txtv_title_search_detail);
        recyclerView = view.findViewById(R.id.recycle_view_search_detail);
        tv_notification = view.findViewById(R.id.txtv_nottification_search_detail);
    }

    @Override
    public void setSearchKey(String key) {
        tv_title.setText(key);
        this.key_word = key;
        ShowData(key_word, type);
    }

    void ShowData(String key, String type) {
        String link = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&q=" + key.replace(" ", "") + "&type=" + type + "&key=AIzaSyAwNm1IjKJIvrlvD7pN7__Dfp43dy7slF0";
        new LoadVideoID().execute(link);
    }

    void LoadVideoFromDataBase(String chuoi) {
        tv_title.setText(check.equals("history") ? "Lịch sử" : ((check.equals("watchlater") ? "Xem sau" : "Video của bạn")));
        db = DataBase.initDatabase(getActivity(), DATABASE_NAME);
        Cursor cursor = db.rawQuery("Select * From " + (check.equals("history") ? TABLE_HISTORY : ((check.equals("watchlater") ? TABLE_WATCHLAST : TABLE_MYVIDEO)) + ""), null);
        cursor.moveToFirst();
        for (int i = 0; i < (cursor.getCount() < 10 ? cursor.getCount() : 10); i++) {
            cursor.moveToPosition(i);
            for (int j = 0; j < properties.length; j++) {
                properties[j] = cursor.getString(j);
            }
            array_video.add(new VideoYoutube(properties[0], properties[1], properties[2], properties[3], properties[4], properties[5], properties[6]
                    , properties[7], properties[8], properties[9], properties[10], properties[11], properties[12]));
        }
        //thông báo khi không có dữ liệu
        tv_notification.setVisibility(array_video.isEmpty() ? View.VISIBLE : View.GONE);
        LoadVideo(array_video, chuoi);
    }

    void LoadVideo(final ArrayList<VideoYoutube> arrayList, String chuoi) {
        if (getContext() != null) {
            adapter = new ListVideoAdapter(getContext(), arrayList, chuoi);
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
                            dataID.setDataID(arrayList.get(postion));
                        }
                    }, 500);
                }
            });
        }
    }

    @Override
    public void setLoadPlayList(PlayListYoutube playList, String url) {
        tv_title.setText(playList.getTitle());
        String link = "https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails,statistics&id=" + url + "&maxResults=25&key=AIzaSyAwNm1IjKJIvrlvD7pN7__Dfp43dy7slF0";
        readJsonVideo = new ReadJsonVideo(getActivity(), link, "search_detail");
        readJsonVideo.setData();
    }

    @Override
    public <T> void setSearchDetail(final ArrayList<T> arrayList) {
        setPreData(new LoadData() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            array_video = (ArrayList<VideoYoutube>) arrayList;
                            LoadVideo(array_video, "bt");
                        }
                    });
                }
                return super.doInBackground(voids);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                tv_notification.setVisibility(arrayList.isEmpty() ? View.VISIBLE : View.GONE);
                super.onPostExecute(aBoolean);
            }
        });
    }

    @Override
    public void setSendPlayList(String url, String title) {
        new LoadPlayListFromChannel().execute(url);
        tv_title.setText(title);
    }

    @Override
    public void setListenerHistory(String check) {
        this.check = check;
        LoadVideoFromDataBase(check);
        ConvertImage(check);
    }

    void ConvertImage(String chuoi) {
        if (!chuoi.isEmpty()) {
            img_filter.setImageResource(R.drawable.ic_more_vert_white_24dp);
        }
    }

    @Override
    public void setListenerWatchLater(String check) {
        this.check = check;
        LoadVideoFromDataBase(check);
        ConvertImage(check);
    }

    @Override
    public void setListenerMyVideo(String check) {
        this.check = check;
        LoadVideoFromDataBase(check);
        ConvertImage(check);
    }

    class LoadVideoID extends AsyncTask<String, Void, Void> {
        private String multi_video = "";

        @Override
        protected Void doInBackground(String... strings) {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, strings[0], null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONObject json_id = jsonObject.getJSONObject("id");
                            String id = json_id.getString("videoId");
                            multi_video = multi_video.isEmpty() ? (multi_video + id) : (multi_video + "," + id);
                        }
                        String URL = "https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails,statistics&id=" + multi_video + "&maxResults=50&key=AIzaSyAwNm1IjKJIvrlvD7pN7__Dfp43dy7slF0";
                        readJsonVideo = new ReadJsonVideo(getActivity(), URL, "search_detail");
                        readJsonVideo.setData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("LOI", error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
            return null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        dataID = (DataID) activity;
        onClick = (OnClick) activity;
        loadPlayList = (LoadPlayList) activity;
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
            case R.id.img_back_seach_detail:
                getActivity().onBackPressed();
                break;
            case R.id.img_reset_search_detail:
                getActivity().onBackPressed();
                break;
            case R.id.img_filter_detail:
                if (check.equals("")) {
                    dialog = new Dialog(getContext());
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.custom_dialog);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
                    tv_content = dialog.findViewById(R.id.txtv_selected_dialog);
                    tv_content.setOnClickListener(this);
                    tv_cancel = dialog.findViewById(R.id.txtv_cancel_dialog);
                    tv_cancel.setOnClickListener(this);
                    tv_apply = dialog.findViewById(R.id.txtv_apply_dialog);
                    dialog.show();
                    tv_apply.setOnClickListener(this);
                } else {
                    PopupMenu popupMenu = new PopupMenu(getContext(), img_filter);
                    popupMenu.getMenuInflater().inflate(R.menu.menu_item, popupMenu.getMenu());
                    popupMenu.getMenu().getItem(0).setTitle("Xóa tất cả");
                    //ẩn các item còn lại
                    for (int i = 1; i < 4; i++) {
                        popupMenu.getMenu().getItem(i).setVisible(false);
                    }
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.item_kqt:
                                    array_video.clear();
                                    adapter.notifyDataSetChanged();
                                    db.delete(check.equals("history") ? TABLE_HISTORY : ((check.equals("watchlater") ? TABLE_WATCHLAST : TABLE_MYVIDEO)), null, null);
                                    Toast.makeText(getContext(), "Đã xóa xong!", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
                break;
            case R.id.txtv_cancel_dialog:
                dialog.cancel();
                break;
            case R.id.txtv_selected_dialog:
                PopupMenu popupMenu = new PopupMenu(getContext(), tv_content);
                popupMenu.getMenuInflater().inflate(R.menu.menu_filter, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_selectedt_video:
                                type = "video";
                                tv_content.setText(type.replace("v", "V"));
                                break;
                            case R.id.item_selectedt_channel:
                                type = "channel";
                                tv_content.setText(type.replace("c", "C"));
                                break;
                            case R.id.item_selectedt_playlist:
                                type = "playlist";
                                tv_content.setText(type.replace("p", "P"));
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                break;
            case R.id.txtv_apply_dialog:
                switch (type) {
                    case "channel":
                        String link_channel = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&q=" + key_word.replace(" ", "") + "&type=" + type + "&key=AIzaSyAwNm1IjKJIvrlvD7pN7__Dfp43dy7slF0";
                        new LoadDataChannel().execute(link_channel);
                        break;
                    case "playlist":
                        String link_playlist = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&q=" + key_word.replace(" ", "") + "&type=" + type + "&key=AIzaSyAwNm1IjKJIvrlvD7pN7__Dfp43dy7slF0";
                        new LoadDataPlaylist().execute(link_playlist);
                        break;
                    case "video":
                        LoadVideo(array_video, "bt");
                        break;
                    default:
                        break;
                }
                dialog.cancel();
                break;
            default:
                break;
        }
    }

    class LoadDataPlaylist extends AsyncTask<String, Void, Void> {
        private ArrayList<PlayListYoutube> array_playlist;
        private LinearLayoutManager layoutManager;
        private ListPlaylistAdapter adapter;

        @Override
        protected void onPreExecute() {
            array_playlist = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, strings[0], null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONObject json_id = jsonObject.getJSONObject("id");
                            String id = json_id.getString("playlistId");
                            JSONObject snippet = jsonObject.getJSONObject("snippet");
                            String publishedAt = snippet.getString("publishedAt");
                            String channelId = snippet.getString("channelId");
                            String title = snippet.getString("title");
                            String description = snippet.getString("description");
                            String channelTitle = snippet.getString("channelTitle");
                            JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                            JSONObject high = thumbnails.getJSONObject("high");
                            String url = high.getString("url");
                            array_playlist.add(new PlayListYoutube(id, publishedAt, channelId, title, description, url, channelTitle, ""));
                        }
                        if (getContext() != null) {
                            adapter = new ListPlaylistAdapter(getContext(), array_playlist);
                            recyclerView.setAdapter(adapter);
                            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            adapter.notifyDataSetChanged();
                            adapter.setOnItemClick(new ListPlaylistAdapter.OnItemClick() {
                                @Override
                                public void setOnItemClick(final int postion) {
                                    String link = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + array_playlist.get(postion).getId() + "&key=" + getContext().getResources().getString(R.string.KEY) + "";
                                    final RequestQueue request = Volley.newRequestQueue(getContext());
                                    JsonObjectRequest jo_request = new JsonObjectRequest(Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            String video = "";
                                            try {
                                                JSONArray array = response.getJSONArray("items");
                                                for (int i = 0; i < array.length(); i++) {
                                                    JSONObject jsonObject = array.getJSONObject(i);
                                                    JSONObject snippet = jsonObject.getJSONObject("snippet");
                                                    JSONObject resourceId = snippet.getJSONObject("resourceId");
                                                    String id = resourceId.getString("videoId");
                                                    video = video.isEmpty() ? (video + id) : (video + "," + id);
                                                }
                                                loadPlayList.setLoadPlayList(array_playlist.get(postion), video);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("LOI", error.toString());
                                        }
                                    });
                                    request.add(jo_request);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("LOI", error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    class LoadDataChannel extends AsyncTask<String, Void, Void> {
        private ArrayList<ChannelYoutube> array_channel;
        private ListChannelAdapter adapter;
        private LinearLayoutManager layoutManager;

        @Override
        protected void onPreExecute() {
            array_channel = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, strings[0], null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONObject snippet = jsonObject.getJSONObject("snippet");
                            String publishedAt = snippet.getString("publishedAt");
                            String channelId = snippet.getString("channelId");
                            String title = snippet.getString("title");
                            String description = snippet.getString("description");
                            JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                            JSONObject high = thumbnails.getJSONObject("high");
                            String url = high.getString("url");
                            array_channel.add(new ChannelYoutube(channelId, title, description, publishedAt, url, "", ""));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (getContext() != null) {
                        adapter = new ListChannelAdapter(getContext(), array_channel);
                        recyclerView.setAdapter(adapter);
                        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter.notifyDataSetChanged();
                        adapter.setOnItemClick(new ListChannelAdapter.OnItemClick() {
                            @Override
                            public void setOnItemClick(int postion) {
                                String link_playlist = "https://www.googleapis.com/youtube/v3/playlists?part=snippet,contentDetails&channelId=" + array_channel.get(postion).getId() + "&maxResults=25&key=" + getContext().getResources().getString(R.string.KEY);
                                new LoadPlayListFromChannel().execute(link_playlist);
                            }
                        });
                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("LOI", error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
            return null;
        }
    }

    class LoadPlayListFromChannel extends AsyncTask<String, Void, Void> {
        private ArrayList<PlayListYoutube> arrays;
        private ListPlaylistAdapter adapter;
        private LinearLayoutManager layoutManager;

        @Override
        protected void onPreExecute() {
            arrays = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... strings) {
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, strings[0], null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            JSONObject snippet = jsonObject.getJSONObject("snippet");
                            String publishedAt = snippet.getString("publishedAt");
                            String channelId = snippet.getString("channelId");
                            String title = snippet.getString("title");
                            String description = snippet.getString("description");
                            JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                            JSONObject high = thumbnails.getJSONObject("high");
                            String url = high.getString("url");
                            String channelTitle = snippet.getString("channelTitle");
                            JSONObject contentDetails = jsonObject.getJSONObject("contentDetails");
                            String itemCount = contentDetails.getString("itemCount");
                            arrays.add(new PlayListYoutube(id, publishedAt, channelId, title, description, url, channelTitle, itemCount));
                        }
                        if (getContext() != null) {
                            adapter = new ListPlaylistAdapter(getContext(), arrays);
                            recyclerView.setAdapter(adapter);
                            layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerView.setLayoutManager(layoutManager);
                            adapter.notifyDataSetChanged();
                            adapter.setOnItemClick(new ListPlaylistAdapter.OnItemClick() {
                                @Override
                                public void setOnItemClick(final int postion) {
                                    String link = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId=" + arrays.get(postion).getId() + "&key=" + getContext().getResources().getString(R.string.KEY) + "";
                                    final RequestQueue request = Volley.newRequestQueue(getContext());
                                    JsonObjectRequest jo_request = new JsonObjectRequest(Request.Method.GET, link, null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            String video = "";
                                            try {
                                                JSONArray array = response.getJSONArray("items");
                                                for (int i = 0; i < array.length(); i++) {
                                                    JSONObject jsonObject = array.getJSONObject(i);
                                                    JSONObject snippet = jsonObject.getJSONObject("snippet");
                                                    JSONObject resourceId = snippet.getJSONObject("resourceId");
                                                    String id = resourceId.getString("videoId");
                                                    video = video.isEmpty() ? (video + id) : (video + "," + id);
                                                }
                                                loadPlayList.setLoadPlayList(arrays.get(postion), video);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Log.d("LOI", error.toString());
                                        }
                                    });
                                    request.add(jo_request);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("LOI", error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
            return null;
        }
    }
}
