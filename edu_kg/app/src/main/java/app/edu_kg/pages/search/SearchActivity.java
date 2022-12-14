package app.edu_kg.pages.search;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import app.edu_kg.DataApplication;
import app.edu_kg.R;
import app.edu_kg.pages.link.LinkActivity;
import app.edu_kg.pages.outline.OutlineActivity;
import app.edu_kg.pages.result.ResultActivity;
import app.edu_kg.pages.test.TestActivity;
import app.edu_kg.pages.user.HistoryActivity;
import app.edu_kg.utils.Constant;

import app.edu_kg.utils.Functional;
import app.edu_kg.utils.InstanceIO;
import app.edu_kg.utils.adapter.ItemListAdapter;

public class SearchActivity extends AppCompatActivity implements ItemListAdapter.OnItemClickListener {

    private final String historyDir = "search_history";
    private int selectType = 0;

    private List<ItemListAdapter.ItemMessage> searchHistoryList;
    private ItemListAdapter adapter;
    private ArrayAdapter<String> entityFilterAdapter;
    private ArrayAdapter<String> otherFilterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Activity activity = this;

        try {
            searchHistoryList = (List<ItemListAdapter.ItemMessage>) InstanceIO.loadInstanceWithoutHandler(this, historyDir);
            if (searchHistoryList == null)
                throw new Exception();
        }catch (Exception e){
            searchHistoryList = new ArrayList<>();
        }
        adapter = new ItemListAdapter(searchHistoryList, this);

        // init search bar
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextInputLayout SearchInputLayout = findViewById(R.id.search_input_layout);
        EditText searchInputView = findViewById(R.id.search_input);
        AutoCompleteTextView orderView = findViewById(R.id.order);
        AutoCompleteTextView courseView = findViewById(R.id.subject);
        AutoCompleteTextView filterView = findViewById(R.id.filter);

        searchInputView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent event) {
                if (i == KeyEvent.KEYCODE_ENTER){
                    String searchInput = searchInputView.getText().toString();
                    String order = Functional.sortMethodChe2Eng(orderView.getText().toString());
                    String course = Functional.subjChe2Eng(courseView.getText().toString());
                    String filter = Functional.sortMethodChe2Eng(filterView.getText().toString());
                    String type = Constant.SEARCH_TYPE_LIST[selectType];

                    searchHistoryList.add(new ItemListAdapter.ItemMessage(searchInput, course,
                            Functional.subjEng2Che(course) + "\t" + type + "\t" + Functional.sortMethodEnd2Che(order) + '\t' + Functional.sortMethodEnd2Che(filter)));
                    adapter.notifyItemChanged(searchHistoryList.size() - 1);
                    InstanceIO.saveInstance(activity, searchHistoryList, historyDir);
                    jumpToResult(type, searchInput, order, course, filter);
                }
                return false;
            }
        });
        SearchInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchInput = searchInputView.getText().toString();
                String order = Functional.sortMethodChe2Eng(orderView.getText().toString());
                String course = Functional.subjChe2Eng(courseView.getText().toString());
                String filter = Functional.sortMethodChe2Eng(filterView.getText().toString());
                String type = Constant.SEARCH_TYPE_LIST[selectType];

                searchHistoryList.add(new ItemListAdapter.ItemMessage(searchInput, course,
                        Functional.subjEng2Che(course) + "\t" + type + "\t" + Functional.sortMethodEnd2Che(order) + '\t' + Functional.sortMethodEnd2Che(filter)));
                adapter.notifyItemChanged(searchHistoryList.size() - 1);
                InstanceIO.saveInstance(activity, searchHistoryList, historyDir);

                jumpToResult(type, searchInput, order, course, filter);
            }
        });

        // init history list
        RecyclerView historyRecycler = findViewById(R.id.board);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        historyRecycler.setLayoutManager(linearLayoutManager);
        historyRecycler.setAdapter(adapter);

        // init clear history
        TextView clearHistory = findViewById(R.id.clear_history);
        clearHistory.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view) {
                searchHistoryList.clear();
                adapter.notifyDataSetChanged();
                InstanceIO.saveInstance(activity, searchHistoryList, historyDir);
            }
        });

        // init tab
        entityFilterAdapter = new ArrayAdapter<>(this, R.layout.selector_item, Constant.ENTITY_FILTER_LIST);
        otherFilterAdapter = new ArrayAdapter<>(this, R.layout.selector_item, Constant.OTHER_FILTER_LIST);
        orderView.setAdapter(entityFilterAdapter);
        courseView.setAdapter(new ArrayAdapter<>(this, R.layout.selector_item, Constant.SUBJECT_LIST));
        if (!((DataApplication) getApplicationContext()).token.equals(""))
            filterView.setAdapter(new ArrayAdapter<>(this, R.layout.selector_item, Constant.FILTER_LIST_USER));
        else
            filterView.setAdapter(new ArrayAdapter<>(this, R.layout.selector_item, Constant.FILTER_LIST));

        TabLayout tabLayout = findViewById(R.id.search_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectType = tab.getPosition();
                if (selectType == 0)
                    orderView.setAdapter(entityFilterAdapter);
                else{
                    orderView.setText("??????", false);
                    orderView.setAdapter(otherFilterAdapter);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }


    private void jumpToResult(String type, String searchInput, String order, String course, String filter) {
        Intent intent = null;
        switch (type) {
            case "??????":
                intent = new Intent(this, ResultActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("order", order);
                intent.putExtra("filter", filter);
                break;
            case "??????":
                intent = new Intent(this, LinkActivity.class);
                intent.putExtra("type", type);
                break;
            case "??????":
                intent = new Intent(this, TestActivity.class);
                intent.putExtra("page_type", Constant.EXERCISE_LIST_PAGE);
                break;
            default:
                intent = new Intent(this, OutlineActivity.class);
                break;
        }
        intent.putExtra("searchInput", searchInput);
        intent.putExtra("course", course);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        AutoCompleteTextView filterView = findViewById(R.id.filter);
        if (!((DataApplication) getApplicationContext()).token.equals(""))
            filterView.setAdapter(new ArrayAdapter<>(this, R.layout.selector_item, Constant.FILTER_LIST_USER));
        else
            filterView.setAdapter(new ArrayAdapter<>(this, R.layout.selector_item, Constant.FILTER_LIST));
    }

    @Override
    public void onItemClick(int position) {
        ItemListAdapter.ItemMessage item = searchHistoryList.get(position);
        String[] tmp = item.category.split("\t");
        if ((tmp[3].equals("?????????") || tmp[3].equals("?????????")) && ((DataApplication)getApplicationContext()).token.equals(""))
            tmp[3] = "??????";
        jumpToResult(tmp[1], item.label, Functional.sortMethodChe2Eng(tmp[2]), item.course, Functional.sortMethodChe2Eng(tmp[3]));
    }
}