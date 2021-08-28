package app.edu_kg.pages.user;



import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

import app.edu_kg.R;
import app.edu_kg.utils.Constant;
import app.edu_kg.utils.Request;
import app.edu_kg.utils.adapter.ItemListAdapter;
import kotlin.Triple;

public class HistoryActivity extends AppCompatActivity implements ItemListAdapter.OnItemClickListener{

    private int type;
    private ArrayList<ItemListAdapter.ItemMessage> itemList;
    private ItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_page);
        Intent intent = getIntent();
        Activity activity = this;

        itemList = new ArrayList<ItemListAdapter.ItemMessage>();
        type = intent.getIntExtra("page_type", Constant.HISTORY_PAGE);
        String token = intent.getStringExtra("token");

        MaterialToolbar toolbar = findViewById(R.id.list_page);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView itemRecycler = findViewById(R.id.list_page_recycler);
        adapter = new ItemListAdapter(itemList, this);
        itemRecycler.setLayoutManager(new LinearLayoutManager(this));
        itemRecycler.setAdapter(adapter);

        Handler handler = new Handler(Looper.getMainLooper()) {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Constant.LIST_RESPONSE_SUCCESS) {
                    ArrayList<Triple<String, String, String>> obj = (ArrayList<Triple<String, String, String>>) msg.obj;
                    for (Triple<String, String, String> item : obj) {
                        itemList.add(new ItemListAdapter.ItemMessage(item.getFirst(), item.getSecond(), item.getThird(), null));
                    }
                    adapter.notifyDataSetChanged();
                }
                else if (msg.what == Constant.LIST_RESPONSE_FAIL){
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setMessage("请求出现错误");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            activity.finish();
                        }
                    });
                    builder.create();
                }
            }
        };

        switch (type){
            case Constant.HISTORY_PAGE:
                toolbar.setTitle("浏览历史");
                Request.userHistory(token, handler);
                break;

            case Constant.COLLECTION_PAGE:
                toolbar.setTitle("用户收藏");
                Request.userCollection(token, handler);
                break;

            case Constant.RECOMMENDATION_PAGE:
                toolbar.setTitle("试题推荐");
                Request.exerciseRecommendation(token, handler);
                break;
        }

    }

    @Override
    public void onItemClick(int position) {
        ItemListAdapter.ItemMessage item = itemList.get(position);
        switch (type){
            // start a new activity
            case Constant.HISTORY_PAGE:
                Log.v("Item selected: ", String.valueOf(position));
                break;

            case Constant.COLLECTION_PAGE:
                Log.v("Item selected: ", String.valueOf(position));
                break;

            case Constant.RECOMMENDATION_PAGE:
                Log.v("Item selected: ", String.valueOf(position));
                break;
        }
    }
}