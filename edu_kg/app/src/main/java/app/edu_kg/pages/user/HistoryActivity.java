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
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import app.edu_kg.R;
import app.edu_kg.pages.detail.DetailActivity;
import app.edu_kg.utils.Constant;
import app.edu_kg.utils.Request;
import app.edu_kg.utils.adapter.ItemListAdapter;

public class HistoryActivity extends AppCompatActivity implements ItemListAdapter.OnItemClickListener{

    private int type;
    private ArrayList<ItemListAdapter.ItemMessage> itemList;
    private ItemListAdapter adapter;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_page);
        Intent intent = getIntent();
        Activity activity = this;

        // get data
        itemList = new ArrayList<ItemListAdapter.ItemMessage>();
        type = intent.getIntExtra("page_type", Constant.HISTORY_PAGE);
        token = intent.getStringExtra("token");

        // init toolbar
        MaterialToolbar toolbar = findViewById(R.id.list_page_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // init recycle
        RecyclerView itemRecycler = findViewById(R.id.list_page_recycler);
        adapter = new ItemListAdapter(itemList, this);
        itemRecycler.setLayoutManager(new LinearLayoutManager(this));
        itemRecycler.setAdapter(adapter);

        Handler handler = new Handler(Looper.getMainLooper()) {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Constant.LIST_RESPONSE_SUCCESS) {
                    ArrayList<ItemListAdapter.ItemMessage> obj = (ArrayList<ItemListAdapter.ItemMessage>) msg.obj;
                    itemList.addAll(obj);
                    adapter.notifyDataSetChanged();
                }
                else if (msg.what == Constant.LIST_RESPONSE_FAIL){
                    AlertDialog dialog = new MaterialAlertDialogBuilder(activity)
                            .setTitle("取回数据错误")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    activity.finish();
                                }
                            })
                            .show();
                }
            }
        };

        switch (type){
            case Constant.HISTORY_PAGE:
                toolbar.setTitle("浏览历史");
                Request.getUserHistoryList(token, handler);
                break;

            case Constant.COLLECTION_PAGE:
                toolbar.setTitle("用户收藏");
                Request.getFavoritesList(token, handler);
                break;
        }

    }

    @Override
    public void onItemClick(int position) {
        ItemListAdapter.ItemMessage item = itemList.get(position);
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("course", item.course);
        intent.putExtra("name", item.label);
        intent.putExtra("token", token);
        startActivity(intent);
    }
}