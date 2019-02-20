package com.example.admin.youtubev3.Fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.youtubev3.Adapter.ListVideoAdapter;
import com.example.admin.youtubev3.CustomView.CustomButton;
import com.example.admin.youtubev3.CustomView.CustomTextView;
import com.example.admin.youtubev3.DataBase.DataBase;
import com.example.admin.youtubev3.Dialog.DialogManager;
import com.example.admin.youtubev3.Interface.DataID;

import com.example.admin.youtubev3.Interface.OnClick;
import com.example.admin.youtubev3.Interface.RelationData;
import com.example.admin.youtubev3.ManagerFragment.ManagerFragment;
import com.example.admin.youtubev3.Model.VideoYoutube;
import com.example.admin.youtubev3.R;
import com.example.admin.youtubev3.ReadJson.ReadJsonVideo;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_list_video extends ManagerFragment implements RelationData, View.OnClickListener {
    private View view;
    private CustomTextView tv_title, tv_view_count, tv_like, tv_dislike, tv_share, tv_playlist, tv_description, tv_title_channel, tv_sub;
    private RelativeLayout relativeLayout;
    private ImageView img_arrow;
    private boolean flag = true;
    private CircleImageView circle_channel;
    private CustomButton btn_sub;
    private String view_count = "", description = "", subrice = "", id = "";
    private RecyclerView recyclerView;
    private ArrayList<VideoYoutube> array;
    private ListVideoAdapter adapter;
    private LinearLayoutManager layoutManager;
    DataID dataID;
    OnClick onClick;
    private ReadJsonVideo readJsonVideo;
    Activity activity;
    SQLiteDatabase db;
    private static final String DATABASE_NAME = "Youtube.sqlite";
    private static final String TABLE_HISTORY = "tbl_HistoryVideo";
    private static final String TABLE_WATCHLAST = "tbl_WatchLast";
    private static final String TABLE_MYVIDEO = "tbl_MyVideo";
    VideoYoutube videoYoutube;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_list_video, null);
        tv_title = view.findViewById(R.id.txtv_title_detail_video);
        tv_view_count = view.findViewById(R.id.txtv_detail_view_count);
        relativeLayout = view.findViewById(R.id.layout_detail);
        relativeLayout.setOnClickListener(this);
        img_arrow = view.findViewById(R.id.img_more_detail_video);
        tv_like = view.findViewById(R.id.txtv_like_detail);
        tv_like.setOnClickListener(this);
        tv_dislike = view.findViewById(R.id.txtv_dislike_detail);
        tv_dislike.setOnClickListener(this);
        tv_share = view.findViewById(R.id.txtv_share_detail);
        tv_share.setOnClickListener(this);
        tv_playlist = view.findViewById(R.id.txtv_playlist_add_detail);
        tv_playlist.setOnClickListener(this);
        tv_description = view.findViewById(R.id.txtv_description_deatail);
        tv_title_channel = view.findViewById(R.id.txtv_title_channel_detail);
        tv_sub = view.findViewById(R.id.txtv_subrice_channel_detail);
        btn_sub = view.findViewById(R.id.btn_subrice_detail);
        btn_sub.setOnClickListener(this);
        circle_channel = view.findViewById(R.id.img_thumnail_channel_detail);
        recyclerView = view.findViewById(R.id.recycle_view_list_video);
        //scroll recycleview mượt trong scrollview
        recyclerView.setNestedScrollingEnabled(false);
        return view;
    }

    public void DoSomeThing(VideoYoutube videoYoutube) {
        this.videoYoutube = videoYoutube;
        this.id = videoYoutube.getId();
        tv_title.setText(videoYoutube.getTitle());
        this.view_count = videoYoutube.getViewCount();
        tv_like.setText(videoYoutube.getLikeCount());
        tv_dislike.setText(videoYoutube.getDislikeCount());
        this.description = videoYoutube.getDescription();
        Convert_View(view_count);
        String url = "https://www.googleapis.com/youtube/v3/channels?part=snippet,contentDetails,statistics&id=" + videoYoutube.getChannelId() + "&key=AIzaSyAwNm1IjKJIvrlvD7pN7__Dfp43dy7slF0";
        String url_video = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&relatedToVideoId=" + videoYoutube.getId() + "&key=AIzaSyAwNm1IjKJIvrlvD7pN7__Dfp43dy7slF0";
        new ShowData().execute(url);
        new MultiVideo().execute(url_video);
    }

    @Override
    public void onAttach(Activity activity) {
        dataID = (DataID) activity;
        onClick = (OnClick) activity;
        this.activity = activity;
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        dataID = null;
        super.onDetach();
    }

    @Override
    public <T> void setRelationData(ArrayList<T> arrayList) {
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

    class MultiVideo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            final String[] chuoi = {""};
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, strings[0], null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            JSONObject id = jsonObject.getJSONObject("id");
                            String ID = id.getString("videoId");
                            chuoi[0] = chuoi[0] + "," + ID;
                        }
                        String url_multi = "https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails,statistics&id=" + chuoi[0] + "&maxResults=50&key=AIzaSyAwNm1IjKJIvrlvD7pN7__Dfp43dy7slF0";
                        readJsonVideo = new ReadJsonVideo(activity, url_multi, "relation");
                        readJsonVideo.setData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("ERRO", error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
            return null;
        }
    }

    class ShowData extends AsyncTask<String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, strings[0], null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            JSONObject snippet = jsonObject.getJSONObject("snippet");
                            String title = snippet.getString("title");
                            JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                            JSONObject high = thumbnails.getJSONObject("high");
                            String url = high.getString("url");
                            JSONObject statistics = jsonObject.getJSONObject("statistics");
                            String subscriberCount = statistics.getString("subscriberCount");
                            publishProgress(id + "," + title + "," + url + "," + subscriberCount);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("loi", error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            tv_title_channel.setText(values[0].split(",")[1]);
            long sub = Long.parseLong(values[0].split(",")[3]);
            subrice = values[0].split(",")[3];
            tv_sub.setText(sub < 1000 ? String.valueOf(sub) + " đăng kí" : (sub > 1000 && sub < 100000) ? String.valueOf(sub).substring(0, 1) + " ngàn đăng kí" : (sub > 100000 && sub < 1000000) ? String.valueOf(sub).substring(0, 2) + " ngàn đăng kí" :
                    (sub > 100000 && sub < 1000000) ? String.valueOf(sub).substring(0, 3) + " ngàn đăng kí" : (sub > 1000000 && sub < 10000000) ? String.valueOf(sub).substring(0, 1) + " tr đăng kí" : (sub > 10000000 && sub < 100000000) ? String.valueOf(sub).substring(0, 2) + " tr đăng kí" :
                            String.valueOf(sub).substring(0, 3) + " tr đăng kí");
            Picasso.with(getContext()).load(values[0].split(",")[2]).fit().centerCrop().into(circle_channel);
            super.onProgressUpdate(values);
        }
    }

    private void Convert_View(String data) {
        long view = Long.parseLong(data);
        if (flag) {
            tv_view_count.setText((view < 1000 ? String.valueOf(view) + "Lượt xem" : (view > 1000000 && view < 10000000) ? String.valueOf(view).substring(0, 1) + "," + String.valueOf(view).substring(1, 2) + " Triệu lượt xem" : (view > 10000000 && view < 100000000) ?
                    String.valueOf(view).substring(0, 2) + " Triệu lượt xem" : ((view > 100000000 && view < 1000000000) ? String.valueOf(view).substring(0, 3) + " Triệu lượt xem" : String.valueOf(view).substring(0, 1)) + " Tỷ lượt xem"));

        } else {
            DecimalFormat decimalFormat = new DecimalFormat();
            tv_view_count.setText(decimalFormat.format(view) + " lượt xem");
        }
    }

    private void Convert_Sub(String data) {
        long view = Long.parseLong(data);
        if (flag) {
            tv_sub.setText(view < 1000 ? String.valueOf(view) + " đăng kí" : (view > 1000 && view < 100000) ? String.valueOf(view).substring(0, 1) + " ngàn đăng kí" : (view > 100000 && view < 1000000) ? String.valueOf(view).substring(0, 2) + " ngàn đăng kí" :
                    (view > 100000 && view < 1000000) ? String.valueOf(view).substring(0, 3) + " ngàn đăng kí" : (view > 1000000 && view < 10000000) ? String.valueOf(view).substring(0, 1) + " tr đăng kí" : (view > 10000000 && view < 100000000) ? String.valueOf(view).substring(0, 2) + " tr đăng kí" :
                            String.valueOf(view).substring(0, 3) + " tr đăng kí");

        } else {
            DecimalFormat decimalFormat = new DecimalFormat();
            tv_sub.setText(decimalFormat.format(view) + " đăng kí");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_detail:
                img_arrow.setImageResource(flag ? R.drawable.ic_arrow_drop_up_black_24dp : R.drawable.ic_arrow_drop_down_black_24dp);
                tv_description.setVisibility(flag ? View.VISIBLE : View.GONE);
                tv_description.setText(flag ? this.description : "");
                this.flag = flag ? false : true;
                Convert_View(view_count);
                Convert_Sub(subrice);
                break;
            case R.id.txtv_share_detail:
                String link = "https://www.youtube.com/watch?v=" + this.id;
                DialogManager.ShowShare(getActivity(), link);
                break;
            case R.id.txtv_playlist_add_detail:
                db = DataBase.initDatabase(getActivity(), DATABASE_NAME);
                PopupMenu popupMenu = new PopupMenu(getContext(), tv_playlist);
                popupMenu.getMenuInflater().inflate(R.menu.menu_item, popupMenu.getMenu());
                popupMenu.getMenu().getItem(0).setVisible(false);
                popupMenu.getMenu().getItem(3).setVisible(false);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_add_xem_sau:
                                Cursor cursor_xs = db.rawQuery("Select * from tbl_WatchLast where id='" + videoYoutube.getId() + "'", null);
                                if (cursor_xs.getCount() == 0) {
                                    AddDatabase("watchlater", videoYoutube);
                                } else {
                                    Toast.makeText(getContext(), "Video đã tồn tại trong danh sách!", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.item_playlist:
                                Cursor cursor_mv = db.rawQuery("Select * from tbl_MyVideo where id='" + videoYoutube.getId() + "'", null);
                                if (cursor_mv.getCount() == 0) {
                                    AddDatabase("myvideo", videoYoutube);
                                } else {
                                    Toast.makeText(getContext(), "Video đã tồn tại trong danh sách!", Toast.LENGTH_SHORT).show();
                                }
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
                break;
            case R.id.btn_subrice_detail:
                Snackbar.make(btn_sub, "Bạn phải đăng nhập!", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.txtv_like_detail:
                Snackbar.make(tv_like, "Bạn phải đăng nhập", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.txtv_dislike_detail:
                Snackbar.make(tv_dislike, "Bạn phải đăng nhập", Snackbar.LENGTH_SHORT).show();
            default:
                break;
        }
    }

    void AddDatabase(String check, VideoYoutube videoYoutube) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", videoYoutube.getId());
        contentValues.put("title", videoYoutube.getTitle());
        contentValues.put("publishedAt", videoYoutube.getPublishedAt());
        contentValues.put("channelId", videoYoutube.getChannelId());
        contentValues.put("description", videoYoutube.getDescription().replace("'", ""));
        contentValues.put("thumbnails", videoYoutube.getThumbnails().replace(",", ""));
        contentValues.put("channelTitle", videoYoutube.getChannelId());
        contentValues.put("categoryId", videoYoutube.getCategoryId());
        contentValues.put("liveBroadcastContent", videoYoutube.getLiveBroadcastContent());
        contentValues.put("duration", videoYoutube.getDuration());
        contentValues.put("viewCount", videoYoutube.getViewCount());
        contentValues.put("likeCount", videoYoutube.getLikeCount());
        contentValues.put("dislikeCount", videoYoutube.getDislikeCount());
        db.insert(check.equals("history") ? TABLE_HISTORY : ((check.equals("watchlater") ? TABLE_WATCHLAST : TABLE_MYVIDEO)),
                null, contentValues);
        if (!check.equals("history"))
            Toast.makeText(getContext(), "Đã thêm video vào " + (check.equals("watchlater") ? "xem sau" : (check.equals("myvideo") ? "video của bạn" : "")), Toast.LENGTH_SHORT).show();
    }

}
