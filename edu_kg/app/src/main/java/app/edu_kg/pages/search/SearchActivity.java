package app.edu_kg.pages.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import app.edu_kg.utils.request;

import app.edu_kg.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel searchViewModel;
    private ActivitySearchBinding binding;
    private final String historyDir = "history.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("test", "jump into SearchActivity");
        super.onCreate(savedInstanceState);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        this.getSupportActionBar().hide();
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initSearch();
        initHistory(view);
        clearHistory();
    }

    private void initSearch() {
        TextInputLayout SearchInputLayout = binding.searchInputLayout;
        SearchInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("test", "jump to result");
                String searchInput = binding.searchInput.getText().toString();
                String type = binding.type.getSelectedItem().toString();
                String subject = binding.subject.getSelectedItem().toString();
                String order = binding.order.getSelectedItem().toString();
                addHistory(searchInput, type, subject, order);
                Intent intent = new Intent(SearchActivity.this, ResultActivity.class);
                intent.putExtra("searchInput", searchInput);
                intent.putExtra("type", type);
                intent.putExtra("subject", subject);
                intent.putExtra("order", order);
                startActivity(intent);
                initHistory(view);
            }
        });
    }

    private void initHistory(View view) {
        RecyclerView historyRecycler = binding.board;
        historyRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        historyRecycler.setAdapter(searchViewModel.adapter);
        String[] history = loadHistory();
        searchViewModel.adapter.clearHistory();
        if(history != null) {
            for(int i = history.length - 1; i >= 0 ; --i) {
                searchViewModel.adapter.addHistory(history[i]);
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

}