package com.example.admin.youtubev3.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.admin.youtubev3.CustomView.CustomTextView;
import com.example.admin.youtubev3.Dialog.DialogManager;
import com.example.admin.youtubev3.Model.PlayListYoutube;
import com.example.admin.youtubev3.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListPlaylistAdapter extends RecyclerView.Adapter<ListPlaylistAdapter.ViewHolderPlaylist> {
    private Context context;
    private ArrayList<PlayListYoutube> arrayList;
    private LayoutInflater layoutInflater;
    OnItemClick onItemClick;

    public interface OnItemClick {
        public void setOnItemClick(int postion);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public ListPlaylistAdapter(Context context, ArrayList<PlayListYoutube> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolderPlaylist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_playlist, parent, false);
        return new ViewHolderPlaylist(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderPlaylist holder, final int position) {
        PlayListYoutube playListYoutube = arrayList.get(position);
        Picasso.with(context).load(playListYoutube.getThumbnails()).fit().centerCrop().into(holder.img_thumnail);
        holder.img_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.img_menu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_share, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_share_general:
                                String link = "https://www.youtube.com/playlist?list=" + arrayList.get(position).getId() + "";
                                DialogManager.ShowShare((Activity) context, link);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        holder.tv_title.setText(playListYoutube.getTitle());
        holder.tv_title_channel.setText(playListYoutube.getChannelTitle());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.setOnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolderPlaylist extends RecyclerView.ViewHolder {
        private ImageView img_thumnail, img_menu;
        private CustomTextView tv_title, tv_title_channel;
        private RelativeLayout relativeLayout;

        public ViewHolderPlaylist(View itemView) {
            super(itemView);
            img_thumnail = itemView.findViewById(R.id.item_img_thumnail_playlist);
            img_menu = itemView.findViewById(R.id.item_img_menu_playlist);
            tv_title = itemView.findViewById(R.id.item_txtv_title_playlist);
            tv_title_channel = itemView.findViewById(R.id.item_txtv_title_channel_playlist);
            relativeLayout = itemView.findViewById(R.id.item_layout_playlist);
        }
    }
}
