package app.edu_kg.pages.result;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import app.edu_kg.R;
import app.edu_kg.pages.result.ResultViewModel;
import app.edu_kg.utils.Constant;
import app.edu_kg.utils.Functional;
import app.edu_kg.utils.Request;

import app.edu_kg.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    private ResultViewModel resultViewModel;
    private ActivityResultBinding binding;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("test", "jump into ResultActivity");
        super.onCreate(savedInstanceState);
        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initHandler();
        Intent intent = getIntent();
        initResult(view, intent);
        initBack();

    }

    private void initHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int message_num = msg.what;
                if (message_num == Constant.INSTANCE_LIST_RESPONSE){
                    JSONObject json = null;
                    try {
                        json = (JSONObject) msg.obj;
                    } catch(Exception e) {
                        resultViewModel.adapter.addResult("载入失败", "", "");
                        Log.e("test", "loading error");
                        return;
                    }
                    JSONArray entities = null;
                    try {
                        entities = json.getJSONObject("data").getJSONArray("result");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    resultViewModel.adapter.clearResult();
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
                        resultViewModel.adapter.addResult(label, category, course);
                    }
                }
                else if(message_num == Constant.LINK_INSTANCE_RESPONSE) {
                    JSONObject json = (JSONObject) msg.obj;
                    JSONArray entities = null;
                    try {
                        entities = json.getJSONObject("data").getJSONArray("result");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    resultViewModel.adapter.clearResult();
                    for(int i = 0; i < entities.length(); ++i) {
                        String label = null;
                        String category = null;
                        String course = null;
                        try {
                            label = entities.getJSONObject(i).getString("entity");
                            category = entities.getJSONObject(i).getString("entity_type");
                            course = entities.getJSONObject(i).getString("course");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        resultViewModel.adapter.addResult(label, category, course);
                    }
                }
            }
        };
    }



    private void initResult(View view, Intent intent) {
        RecyclerView resultRecycler = binding.board;
        resultRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        resultRecycler.setAdapter(resultViewModel.adapter);
        String searchInput = intent.getStringExtra("searchInput");
        String type = intent.getStringExtra("type");
        String course = Functional.subjChe2Eng(intent.getStringExtra("course"));
        String order = intent.getStringExtra("order");
        resultViewModel.adapter.clearResult();
        if(type.equals("实体")) {
            Request.getInstanceList(searchInput, course, order, handler);
        }
        else {
            Request.getLinkInstance(searchInput, course, handler);
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