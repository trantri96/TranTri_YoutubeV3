package com.example.admin.youtubev3.Adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;


import com.example.admin.youtubev3.CustomView.CustomTextView;
import com.example.admin.youtubev3.DataBase.DataBase;
import com.example.admin.youtubev3.Dialog.DialogManager;
import com.example.admin.youtubev3.Model.VideoYoutube;
import com.example.admin.youtubev3.R;
import com.squareup.picasso.Picasso;

import java.time.Duration;
import java.util.ArrayList;

public class ListVideoAdapter extends RecyclerView.Adapter<ListVideoAdapter.ViewHolderVideo> {
    private Context context;
    private ArrayList<VideoYoutube> arrayList;
    private LayoutInflater layoutInflater;
    private String kt;
    OnItemLayout onItemLayout;
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "Youtube.sqlite";
    private static final String TABLE_HISTORY = "tbl_HistoryVideo";
    private static final String TABLE_WATCHLAST = "tbl_WatchLast";
    private static final String TABLE_MYVIDEO = "tbl_MyVideo";
    OnListener onListener;

    public interface OnListener {
        public void setOnListener();
    }

    public void setOnListener(OnListener onListener) {
        this.onListener = onListener;
    }

    public interface OnItemLayout {
        void setOnItemLayoutClick(int postion);
    }

    public void setOnItemLayout(OnItemLayout onItemLayout) {
        this.onItemLayout = onItemLayout;
    }

    public ListVideoAdapter(Context context, ArrayList<VideoYoutube> arrayList, String kt) {
        this.context = context;
        this.arrayList = arrayList;
        this.kt = kt;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderVideo onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_video, parent, false);
        return new ViewHolderVideo(view, context, arrayList);
    }

    @Override
    public void onBindViewHolder(final ViewHolderVideo holder, final int position) {
        db = DataBase.initDatabase((Activity) context, DATABASE_NAME);
        final VideoYoutube videoYoutube = arrayList.get(position);
//        set thumnail
        Picasso.with(context).load(videoYoutube.getThumbnails()).fit().centerCrop().placeholder(R.drawable.ic_place_youtube).into(holder.img_thumnail);
//        set duration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            holder.tv_time.setText(videoYoutube.getLiveBroadcastContent().equals("live") ? (Html.fromHtml("This is <font color='white'>Trực tiếp</font>")) : "" + ((int) Duration.parse(videoYoutube.getDuration()).getSeconds()));
        }
        holder.tv_title.setText(videoYoutube.getTitle());
        long view = Long.parseLong(videoYoutube.getViewCount());
        holder.tv_view.setText(videoYoutube.getChannelTitle() + "  " + (view < 1000 ? String.valueOf(view) + "Lượt xem" : (view > 1000000 && view < 10000000) ? String.valueOf(view).substring(0, 1) + "," + String.valueOf(view).substring(1, 2) + " Triệu lượt xem" : (view > 10000000 && view < 100000000) ?
                String.valueOf(view).substring(0, 2) + " Triệu lượt xem" : ((view > 100000000 && view < 1000000000) ? String.valueOf(view).substring(0, 3) + " Triệu lượt xem" : String.valueOf(view).substring(0, 1)) + " Tỷ lượt xem"));
        holder.tv_publicAt.setText("Phát hành:" + videoYoutube.getPublishedAt().split("T")[0]);
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemLayout.setOnItemLayoutClick(position);
                //CSDL
                AddDatabase("history", videoYoutube);
            }
        });
        holder.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.img_menu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_item, popupMenu.getMenu());
                switch (kt) {
                    case "history":
                        popupMenu.getMenu().getItem(0).setTitle("Xóa khỏi lịch sử");
                        break;
                    case "watchlater":
                        popupMenu.getMenu().getItem(0).setTitle("Xóa khỏi xem sau");
                        popupMenu.getMenu().getItem(1).setVisible(false);
                        break;
                    case "myvideo":
                        popupMenu.getMenu().getItem(0).setTitle("Xóa khỏi video của bạn");
                        popupMenu.getMenu().getItem(2).setVisible(false);
                        break;
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_kqt:
                                switch (kt) {
                                    case "bt":
                                        RemoveItem(position);
                                        break;
                                    case "history":
                                        RemoveItem(position);
                                        db.delete(TABLE_HISTORY, "id=?", new String[]{videoYoutube.getId()});
                                        Toast.makeText(context, "Đã xóa video ra khỏi lịch sử", Toast.LENGTH_SHORT).show();
                                        if (arrayList.size() == 0) {
                                            onListener.setOnListener();
                                        }
                                        break;
                                    case "watchlater":
                                        RemoveItem(position);
                                        db.delete(TABLE_WATCHLAST, "id=?", new String[]{videoYoutube.getId()});
                                        Toast.makeText(context, "Đã xóa video ra khỏi xem sau", Toast.LENGTH_SHORT).show();
                                        if (arrayList.size() == 0) {
                                            onListener.setOnListener();
                                        }
                                        break;
                                    case "myvideo":
                                        RemoveItem(position);
                                        db.delete(TABLE_MYVIDEO, "id=?", new String[]{videoYoutube.getId()});
                                        Toast.makeText(context, "Đã xóa video ra khỏi video của bạn", Toast.LENGTH_SHORT).show();
                                        if (arrayList.size() == 0) {
                                            onListener.setOnListener();
                                        }
                                        break;

                                }
                                break;
                            case R.id.item_add_xem_sau:
                                Cursor cursor_xs = db.rawQuery("Select * from tbl_WatchLast where id='" + videoYoutube.getId() + "'", null);
                                if (cursor_xs.getCount() == 0) {
                                    AddDatabase("watchlater", videoYoutube);
                                } else {
                                    Toast.makeText(context, "Video đã tồn tại trong danh sách!", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.item_playlist:
                                Cursor cursor_mv = db.rawQuery("Select * from tbl_MyVideo where id='" + videoYoutube.getId() + "'", null);
                                if (cursor_mv.getCount() == 0) {
                                    AddDatabase("myvideo", videoYoutube);
                                } else {
                                    Toast.makeText(context, "Video đã tồn tại trong danh sách!", Toast.LENGTH_SHORT).show();
                                }

                                break;
                            case R.id.item_share:
                                String link = "https://www.youtube.com/watch?v=" + videoYoutube.getId();
                                DialogManager.ShowShare((Activity) context, link);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
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
            Toast.makeText(context, "Đã thêm video vào " + (check.equals("watchlater") ? "xem sau" : (check.equals("myvideo") ? "video của bạn" : "")), Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    void RemoveItem(int postion) {
        arrayList.remove(postion);
        notifyItemRemoved(postion);
        notifyItemRangeChanged(postion, arrayList.size());
    }

    public class ViewHolderVideo extends RecyclerView.ViewHolder {
        private Context context;
        private ArrayList<VideoYoutube> arrayList;
        private ImageView img_thumnail, img_menu;
        private CustomTextView tv_time, tv_title, tv_view, tv_publicAt;
        private LinearLayout layout;

        public ViewHolderVideo(View itemView, Context context, ArrayList<VideoYoutube> arrayList) {
            super(itemView);
            this.context = context;
            this.arrayList = arrayList;
            img_thumnail = itemView.findViewById(R.id.img_item_thumnail_video);
            img_menu = itemView.findViewById(R.id.item_more_video);
            tv_time = itemView.findViewById(R.id.txtv_item_duration);
            tv_title = itemView.findViewById(R.id.txtv_item_title_video);
            tv_view = itemView.findViewById(R.id.txtv_item_view_video);
            tv_publicAt = itemView.findViewById(R.id.txtv_item_publicAt_video);
            layout = itemView.findViewById(R.id.item_layout_list_video);
        }
    }
}
