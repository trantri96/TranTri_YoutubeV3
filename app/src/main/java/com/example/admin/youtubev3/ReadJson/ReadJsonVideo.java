package com.example.admin.youtubev3.ReadJson;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.youtubev3.Interface.HomeData;
import com.example.admin.youtubev3.Interface.RelationData;
import com.example.admin.youtubev3.Interface.SearchDetail;
import com.example.admin.youtubev3.Model.VideoYoutube;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReadJsonVideo {
    private Activity activity;
    private String url;
    private ArrayList<VideoYoutube> arrayList;
    private String key = "";
    HomeData homeData;
    RelationData relationData;
    SearchDetail searchDetail;

    public ReadJsonVideo() {
    }

    public ReadJsonVideo(Activity activity, String url, String key) {
        this.activity = activity;
        this.url = url;
        this.key = key;
        switch (key) {
            case "home":
                homeData = (HomeData) activity;
                break;
            case "relation":
                relationData = (RelationData) activity;
                break;
            case "search_detail":
                searchDetail = (SearchDetail) activity;
                break;
            default:
                break;

        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setData() {
        arrayList = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    final String[] id = new String[1];
                    final String[] publishedAt = new String[1];
                    final String[] channelId = new String[1];
                    final String[] title = new String[1];
                    final String[] description = new String[1];
                    final String[] url = new String[1];
                    final String[] channelTitle = new String[1];
                    final String[] categoryId = new String[1];
                    final String[] liveBroadcastContent = new String[1];
                    final String[] duration = new String[1];
                    final String[] viewCount = new String[1];
                    final String[] likeCount = new String[1];
                    final String[] dislikeCount = new String[1];
                    JSONArray jsonArray = response.getJSONArray("items");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        id[0] = jsonObject.getString("id");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject snippet = null;
                                    try {
                                        snippet = jsonObject.getJSONObject("snippet");
                                        publishedAt[0] = snippet.getString("publishedAt");
                                        channelId[0] = snippet.getString("channelId");
                                        title[0] = snippet.getString("title");
                                        description[0] = snippet.getString("description");
                                        JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                                        JSONObject standard = thumbnails.getJSONObject("standard");
                                        url[0] = standard.getString("url");
                                        channelTitle[0] = snippet.getString("channelTitle");
                                        categoryId[0] = snippet.getString("categoryId");
                                        liveBroadcastContent[0] = snippet.getString("liveBroadcastContent");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject contentDetails = null;
                                    try {
                                        contentDetails = jsonObject.getJSONObject("contentDetails");
                                        duration[0] = contentDetails.getString("duration");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject statistics = null;
                                    try {
                                        statistics = jsonObject.getJSONObject("statistics");
                                        viewCount[0] = statistics.getString("viewCount");
                                        likeCount[0] = statistics.getString("likeCount");
                                        dislikeCount[0] = statistics.getString("dislikeCount");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                        arrayList.add(new VideoYoutube(id[0], title[0], publishedAt[0], channelId[0], description[0], url[0], channelTitle[0], categoryId[0], liveBroadcastContent[0], duration[0], viewCount[0], likeCount[0], dislikeCount[0]));
                    }
                    switch (key) {
                        case "home":
                            homeData.setHomeData(arrayList);
                            break;
                        case "relation":
                            relationData.setRelationData(arrayList);
                            break;
                        case "search_detail":
                            searchDetail.setSearchDetail(arrayList);
                            break;
                        default:
                            break;
                    }
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
    }
}
