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
    private ArrayAdapter<String> subjectAdapter;
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

        searchInputView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent event) {
                if (i == KeyEvent.KEYCODE_ENTER){
                    String searchInput = searchInputView.getText().toString();
                    String order = Functional.sortMethodChe2Eng(orderView.getText().toString());
                    String course = Functional.subjChe2Eng(courseView.getText().toString());
                    String type = Constant.SEARCH_TYPE_LIST[selectType];

                    searchHistoryList.add(new ItemListAdapter.ItemMessage(searchInput, course,
                            Functional.subjEng2Che(course) + "\t" + type + "\t" + Functional.sortMethodEnd2Che(order),
                            null, false));
                    adapter.notifyItemChanged(searchHistoryList.size() - 1);
                    InstanceIO.saveInstance(activity, searchHistoryList, historyDir);
                    jumpToResult(type, searchInput, order, course);
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
                String type = Constant.SEARCH_TYPE_LIST[selectType];

                searchHistoryList.add(new ItemListAdapter.ItemMessage(searchInput, course,
                        Functional.subjEng2Che(course) + "\t" + type + "\t" + Functional.sortMethodEnd2Che(order),
                        null, false));
                adapter.notifyItemChanged(searchHistoryList.size() - 1);
                InstanceIO.saveInstance(activity, searchHistoryList, historyDir);
                jumpToResult(type, searchInput, order, course);
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
        subjectAdapter = new ArrayAdapter<>(activity, R.layout.selector_item, Constant.SUBJECT_LIST);
        entityFilterAdapter = new ArrayAdapter<>(activity, R.layout.selector_item, Constant.ENTITY_FILTER_LIST);
        otherFilterAdapter = new ArrayAdapter<>(activity, R.layout.selector_item, Constant.OTHER_FILTER_LIST);
        orderView.setAdapter(entityFilterAdapter);
        courseView.setAdapter(subjectAdapter);

        TabLayout tabLayout = findViewById(R.id.search_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectType = tab.getPosition();
                if (selectType == 0)
                    orderView.setAdapter(entityFilterAdapter);
                else{
                    orderView.setText("默认", false);
                    orderView.setAdapter(otherFilterAdapter);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
    }

    private void jumpToResult(String type, String searchInput, String order, String course) {
        Intent intent = null;
        if(type.equals("实体")) {
            intent = new Intent(this, ResultActivity.class);
            intent.putExtra("type", type);
        }
        else if(type.equals("文本")) {
            intent = new Intent(this, LinkActivity.class);
            intent.putExtra("type", type);
        }
        else if(type.equals("试题")) {
            intent = new Intent(this, TestActivity.class);
            intent.putExtra("page_type", Constant.EXERCISE_LIST_PAGE);
        }
        else{
            intent = new Intent(this, OutlineActivity.class);
        }
        intent.putExtra("searchInput", searchInput);
        intent.putExtra("course", course);
        intent.putExtra("order", order);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        ItemListAdapter.ItemMessage item = searchHistoryList.get(position);
        String[] tmp = item.category.split("\t");
        jumpToResult(tmp[1], item.label, Functional.sortMethodChe2Eng(tmp[2]), item.course);
    }
}