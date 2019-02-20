package com.example.admin.youtubev3.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.admin.youtubev3.DataBase.DataBase;
import com.example.admin.youtubev3.Model.SearchVideo;
import com.example.admin.youtubev3.R;

import java.util.ArrayList;

/**
 * Created by Admin on 03/22/18.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolderSearch> {
    private Context context;
    private ArrayList<String> arrayList;
    private LayoutInflater layoutInflater;
    private OnItemSelectedListener itemSelected;
    private OnItemClickListener itemclick;
    SQLiteDatabase db;
    private static final String DATABABE_NAME = "Youtube.sqlite";

    public interface OnItemSelectedListener {
        void setOnItemSelected(String chuoi);
    }

    public interface OnItemClickListener {
        void setOnItemClick(int postion);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener itemSlected) {
        this.itemSelected = itemSlected;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.itemclick = onItemClickListener;
    }

    public SearchAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolderSearch onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_list_search, parent, false);
        return new ViewHolderSearch(view, context, arrayList);
    }

    @Override
    public void onBindViewHolder(ViewHolderSearch holder, final int position) {
        holder.tv_title.setText(arrayList.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemclick.setOnItemClick(position);
            }
        });
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                AlertDialog.Builder al = new AlertDialog.Builder(context);
                al.setTitle("Xác nhận!");
                al.setMessage("Bạn có muốn xóa hay không?");
                al.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db = DataBase.initDatabase((Activity) context, DATABABE_NAME);
                        db.delete("tbl_KeyWord", "key=?", new String[]{arrayList.get(position)});
                        Toast.makeText(context, "Đã xóa từ khóa " + arrayList.get(position) + "", Toast.LENGTH_SHORT).show();
                        RemoveItem(position);
                    }
                }).create().show();
                return false;
            }
        });
        holder.img_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemSelected.setOnItemSelected(arrayList.get(position));
            }
        });
    }

    void RemoveItem(int postion) {
        arrayList.remove(arrayList.get(postion));
        notifyItemChanged(postion);
        notifyItemRangeChanged(postion, arrayList.size());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolderSearch extends RecyclerView.ViewHolder {
        private Context context;
        private ArrayList<String> arrayList;
        private ImageView img_search, img_select;
        private TextView tv_title;
        private RelativeLayout layout;

        public ViewHolderSearch(View itemView, Context context, ArrayList<String> arrayList) {
            super(itemView);
            this.context = context;
            this.arrayList = arrayList;
            img_search = itemView.findViewById(R.id.img_icon_history);
            tv_title = itemView.findViewById(R.id.txtv_item_search);
            img_select = itemView.findViewById(R.id.img_select_text_search);
            layout = itemView.findViewById(R.id.layout_item_search);
        }
    }
}
