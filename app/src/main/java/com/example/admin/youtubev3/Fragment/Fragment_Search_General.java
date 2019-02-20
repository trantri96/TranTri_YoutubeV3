package com.example.admin.youtubev3.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.youtubev3.Adapter.SearchAdapter;
import com.example.admin.youtubev3.CustomView.CustomEditText;
import com.example.admin.youtubev3.CustomView.CustomTextView;
import com.example.admin.youtubev3.DataBase.DataBase;
import com.example.admin.youtubev3.Interface.DataSearch;
import com.example.admin.youtubev3.ManagerFragment.ManagerFragment;
import com.example.admin.youtubev3.Model.SearchVideo;
import com.example.admin.youtubev3.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_Search_General extends ManagerFragment implements View.OnClickListener, TextWatcher, SearchAdapter.OnItemClickListener, SearchAdapter.OnItemSelectedListener {
    private ImageView img_back, img_reset, img_micro;
    private View view;
    private ArrayList<String> arrayList;
    private RecyclerView recyclerView;
    private SearchAdapter adapter;
    private LinearLayoutManager layoutManager;
    private CustomEditText edt_search;
    private String search = "";
    private CustomTextView tv_notification;
    DataSearch dataSearch;
    private SQLiteDatabase db;
    private static final String DATABASE_NAME = "Youtube.sqlite";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_search__general, null);
        arrayList = new ArrayList<>();
        img_back = view.findViewById(R.id.img_back_search);
        img_back.setOnClickListener(this);
        img_reset = view.findViewById(R.id.img_reset_search);
        img_reset.setOnClickListener(this);
        img_micro = view.findViewById(R.id.img_micro_search);
        img_micro.setOnClickListener(this);
        recyclerView = view.findViewById(R.id.recycle_view_search);
        edt_search = view.findViewById(R.id.edt_search);
        edt_search.addTextChangedListener(this);
        adapter = new SearchAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
        adapter.setOnItemClickListener(this);
        adapter.setOnItemSelectedListener(this);
        tv_notification = view.findViewById(R.id.txtv_notification_search);
        db = DataBase.initDatabase(getActivity(), DATABASE_NAME);
        Cursor cursor = db.rawQuery("Select * From tbl_KeyWord", null);
        if (cursor.moveToFirst()) {
            do {
                String chuoi = cursor.getString(0);
                arrayList.add(chuoi);
            } while (cursor.moveToNext());
        }
        adapter = new SearchAdapter(getContext(), arrayList);
        recyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();
        adapter.setOnItemSelectedListener(this);
        adapter.setOnItemClickListener(this);
        tv_notification.setVisibility(arrayList.isEmpty() ? View.VISIBLE : View.GONE);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        dataSearch = (DataSearch) activity;
        super.onAttach(activity);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_search:
                getActivity().onBackPressed();
                break;
            case R.id.img_reset_search:
                search = "";
                edt_search.setText(search);
                break;
            case R.id.img_micro_search:
                getText();
                if (search.length() > 0) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("key", search);
                    db.insert("tbl_KeyWord", null, contentValues);
                    dataSearch.setDataSearch(search);
                } else {
                    Toast.makeText(getActivity(), "Giọng nói!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        getText();
        img_reset.setVisibility(search.length() > 0 ? View.VISIBLE : View.GONE);
        img_micro.setImageResource(search.length() > 0 ? R.drawable.ic_search_white_24dp : R.drawable.ic_mic_white_24dp);
    }

    private void getText() {
        this.search = edt_search.getText().toString().trim();
    }

    @Override
    public void setOnItemClick(int postion) {
        search = arrayList.get(postion);
        edt_search.setText(search);
        dataSearch.setDataSearch(search);
    }

    @Override
    public void setOnItemSelected(String chuoi) {
        search = chuoi;
        edt_search.setText(search);
    }
}
