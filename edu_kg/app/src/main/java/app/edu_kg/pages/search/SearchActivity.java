package app.edu_kg.pages.search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import app.edu_kg.R;
import app.edu_kg.pages.result.ResultActivity;
import app.edu_kg.pages.search.SearchViewModel;
import app.edu_kg.pages.test.TestActivity;
import app.edu_kg.utils.Request;

import app.edu_kg.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel searchViewModel;
    private ActivitySearchBinding binding;
    private final String[] typeList = {"实体", "文本", "试题", "提纲"};
    private final String historyDir = "history.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("test", "jump into SearchActivity");
        super.onCreate(savedInstanceState);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initSearch();
        initHistory(view);
        clearHistory();
        initTypeSetting();
        initBack();
    }

    private void initSearch() {
        TextInputLayout SearchInputLayout = binding.searchInputLayout;
        EditText searchInput = binding.searchInput;
        searchInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent event) {
                Log.e("test", "get key");
                if (i == KeyEvent.KEYCODE_ENTER) {
                    jumpToResult(view);
                }
                return false;
            }
        });
        SearchInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToResult(view);
            }
        });

    }

    private void jumpToResult(View view) {
        Log.e("test", "jump to result");
        String type = typeList[searchViewModel.select_type];
        String searchInput = binding.searchInput.getText().toString();
        String course = binding.subject.getSelectedItem().toString();
        String order = binding.order.getSelectedItem().toString();
        addHistory(searchInput, type, course, order);
        Intent intent = null;
        if(type.equals("试题")) {
            intent = new Intent(SearchActivity.this, TestActivity.class);
        }
        else if(type.equals("提纲")) {
            intent = new Intent(SearchActivity.this, ResultActivity.class);
        }
        else {
            intent = new Intent(SearchActivity.this, ResultActivity.class);
        }
        intent.putExtra("searchInput", searchInput);
        intent.putExtra("type", type);
        intent.putExtra("course", course);
        intent.putExtra("order", order);
        initHistory(view);
        startActivity(intent);
    }

    private void initHistory(View view) {
        RecyclerView historyRecycler = binding.board;
        historyRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        historyRecycler.setAdapter(searchViewModel.adapter);
        String[] histories = loadHistory();
        searchViewModel.adapter.clearHistory();
        if(histories != null) {
            for(int i = histories.length - 1; i >= 0 ; --i) {
                String[] history = histories[i].split(" ");
                try {
                    searchViewModel.adapter.addHistory(history[0], history[1], history[2], history[3]);
                } catch(Exception e) {
                    Log.e("history error", e.toString());
                }
            }
        }
    }

    private String[] loadHistory() {
        FileInputStream in;
        try {
            in = getApplicationContext().openFileInput(historyDir);
            int length = in.available();//获取文件长度
            byte[] buffer = new byte[length];//创建byte数组用于读入数据
            in.read(buffer);
            String result = new String(buffer);//将byte数组转换成指定格式的字符串
            if(result.equals("")) {
                return null;
            }
            in.close();//关闭文件输入流
            String[] history = result.split("\n");
            return history;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addHistory(String searchInput, String type, String subject, String order) {
        try {
            FileOutputStream out = openFileOutput(historyDir, Context.MODE_APPEND);
            out.write((searchInput + " " + type + " " + subject + " " + order + "\n").getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
            Log.e("test", "add successfully");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", e.toString());
        }
    }

    private void clearHistory() {
        binding.clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileOutputStream out = openFileOutput(historyDir, Context.MODE_PRIVATE);
                    out.close();
                    initHistory(view);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test", e.toString());
                }
            }
        });
    }

    private void initTypeSetting() {
        for(int i = 0; i < 4; ++i) {
            TextView textView = (TextView)binding.typeSetting.getChildAt(i);
            textView.setId(i);
            if(searchViewModel.select_type == i) {
                textView.setBackgroundColor(Color.GRAY);
            }
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binding.typeSetting.getChildAt(searchViewModel.select_type).setBackgroundColor(Color.WHITE);
                    searchViewModel.select_type = v.getId();
                    v.setBackgroundColor(Color.GRAY);

                    if(v.getId() != 0) {
                        binding.order.setVisibility(View.GONE);
                    }
                    else {
                        binding.order.setVisibility(View.VISIBLE);
                    }

                }
            });
        }
    }

    private void initBack() {
        MaterialToolbar back = binding.back;
        back.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test", "finish");
                finish();
            }
        });
    }

}