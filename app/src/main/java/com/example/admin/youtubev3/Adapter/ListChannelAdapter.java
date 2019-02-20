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
import com.example.admin.youtubev3.Model.ChannelYoutube;
import com.example.admin.youtubev3.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListChannelAdapter extends RecyclerView.Adapter<ListChannelAdapter.ViewHolderChannel> {
    private Context context;
    private ArrayList<ChannelYoutube> arrayList;
    private LayoutInflater layoutInflater;
    OnItemClick onItemClick;

    public interface OnItemClick {
        public void setOnItemClick(int postion);
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public ListChannelAdapter(Context context, ArrayList<ChannelYoutube> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolderChannel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_channel, parent, false);
        return new ViewHolderChannel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderChannel holder, final int position) {
        ChannelYoutube channelYoutube = arrayList.get(position);
        Picasso.with(context).load(channelYoutube.getThumnail()).into(holder.circle_thumnail);
        holder.tv_title.setText(channelYoutube.getTitle());
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
                                String link = "https://www.youtube.com/channel/"+arrayList.get(position).getId()+"?view_as=subscriber";
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

    public class ViewHolderChannel extends RecyclerView.ViewHolder {
        private CustomTextView tv_title;
        private CircleImageView circle_thumnail;
        private ImageView img_menu;
        private RelativeLayout relativeLayout;

        public ViewHolderChannel(View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.txtv_item_title_channel);
            circle_thumnail = itemView.findViewById(R.id.item_img_thumnail_channel);
            img_menu = itemView.findViewById(R.id.img_item_more_menu_channel);
            relativeLayout = itemView.findViewById(R.id.item_layout_channel);
        }
    }
}
