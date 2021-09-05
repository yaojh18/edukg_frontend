package app.edu_kg.pages.result;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.edu_kg.DataApplication;
import app.edu_kg.MainActivity;
import app.edu_kg.R;
import app.edu_kg.pages.detail.DetailActivity;
import app.edu_kg.pages.link.LinkActivity;
import app.edu_kg.pages.test.TestActivity;
import app.edu_kg.utils.Constant;
import app.edu_kg.utils.Functional;
import app.edu_kg.utils.InstanceIO;
import app.edu_kg.utils.Request;

import app.edu_kg.databinding.ActivityResultBinding;
import app.edu_kg.utils.adapter.ItemListAdapter;

public class ResultActivity extends AppCompatActivity implements ItemListAdapter.OnItemClickListener {

    private final String historyDir = "search_history";
    private ActivityResultBinding binding;
    private Handler handler;
    private List<ItemListAdapter.ItemMessage> resultList = new ArrayList<>();;
    private ItemListAdapter adapter = new ItemListAdapter(resultList, this);
    private ArrayAdapter<String> subjectAdapter;
    private ArrayAdapter<String> entityFilterAdapter;
    private List<ItemListAdapter.ItemMessage> searchHistoryList;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        initHistory();
        initHandler();
        initResult(intent);
        initSearchBar(intent);
        initTab();
    }

    private void initHistory() {
        try {
            searchHistoryList = (List<ItemListAdapter.ItemMessage>) InstanceIO.loadInstanceWithoutHandler(this, historyDir);
            if (searchHistoryList == null)
                throw new Exception();
        }catch (Exception e){
            searchHistoryList = new ArrayList<>();
        }
    }

    private void initHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int message_num = msg.what;
                JSONObject json = null;
                try {
                    json = (JSONObject) msg.obj;
                } catch(Exception e) {
                    return;
                }
                JSONArray entities = null;
                try {
                    entities = json.getJSONObject("data").getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resultList.clear();
                if(entities != null) {
                    for(int i = 0; i < entities.length(); ++i) {
                        String label = null;
                        String category = null;
                        String course = null;
                        try {
                            label = entities.getJSONObject(i).getString("label");
                            category = entities.getJSONObject(i).getString("category");
                            course = entities.getJSONObject(i).getString("course");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            assert course != null;
                            resultList.add(new ItemListAdapter.ItemMessage(label, Functional.subjEng2Che(course), category, null, false));
                        } catch(Exception e) {
                            Log.e("test", e.toString());
                        }
                    }
                    for (ItemListAdapter.ItemMessage item : resultList) {
                        if (InstanceIO.isInstanceExist(activity, item.label))
                            item.isChecked = true;
                    }
                }
                adapter.notifyItemChanged(resultList.size() - 1);
            }
        };
    }


    private void initResult(Intent intent) {
        String searchInput = intent.getStringExtra("searchInput");
        String type = intent.getStringExtra("type");
        String course = intent.getStringExtra("course");
        String order = intent.getStringExtra("order");

        RecyclerView resultRecycler = binding.board;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        resultRecycler.setLayoutManager(linearLayoutManager);
        resultRecycler.setAdapter(adapter);
        Request.getInstanceList(searchInput, course, order, handler);
    }

    private void jumpToDetail(String name, String course, String token) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("course", course);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        ItemListAdapter.ItemMessage item = resultList.get(position);
        String name = item.label;
        String course = Functional.subjChe2Eng(item.course);
        String token = ((DataApplication)getApplicationContext()).token;
        jumpToDetail(name, course, token);
    }

    private void initSearchBar(Intent intent) {
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextInputLayout SearchInputLayout = findViewById(R.id.search_input_layout);
        EditText searchInputView = findViewById(R.id.search_input);
        searchInputView.setText(intent.getStringExtra("searchInput"));
        AutoCompleteTextView orderView = findViewById(R.id.order);
        orderView.setText(Functional.sortMethodEnd2Che(intent.getStringExtra("order")));
        AutoCompleteTextView courseView = findViewById(R.id.subject);
        courseView.setText(Functional.subjEng2Che(intent.getStringExtra("course")));

        searchInputView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent event) {
                if (i == KeyEvent.KEYCODE_ENTER){
                    String searchInput = searchInputView.getText().toString();
                    String order = Functional.sortMethodChe2Eng(orderView.getText().toString());
                    String course = Functional.subjChe2Eng(courseView.getText().toString());
                    String type = "实体";
                    searchHistoryList.add(new ItemListAdapter.ItemMessage(searchInput, course, Functional.subjEng2Che(course) + "\t" + type + "\t" + Functional.sortMethodEnd2Che(order)));
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
                String type = "实体";
                searchHistoryList.add(new ItemListAdapter.ItemMessage(searchInput, course, Functional.subjEng2Che(course) + "\t" + type + "\t" + Functional.sortMethodEnd2Che(order)));
                InstanceIO.saveInstance(activity, searchHistoryList, historyDir);
                jumpToResult(type, searchInput, order, course);
            }
        });
    }

    private void initTab() {
        subjectAdapter = new ArrayAdapter<>(this, R.layout.selector_item, Constant.SUBJECT_LIST);
        entityFilterAdapter = new ArrayAdapter<>(this, R.layout.selector_item, Constant.ENTITY_FILTER_LIST);
        binding.order.setAdapter(entityFilterAdapter);
        binding.subject.setAdapter(subjectAdapter);
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
            // TODO
            intent = new Intent(this, TestActivity.class);
            intent.putExtra("page_type", Constant.EXERCISE_LIST_PAGE);
        }
        intent.putExtra("searchInput", searchInput);
        intent.putExtra("course", course);
        intent.putExtra("order", order);
        startActivity(intent);
    }
}