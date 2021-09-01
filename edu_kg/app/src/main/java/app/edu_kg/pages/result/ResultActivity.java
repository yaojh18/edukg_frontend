package app.edu_kg.pages.result;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import app.edu_kg.R;
import app.edu_kg.pages.result.ResultActivity;
import app.edu_kg.pages.result.ResultViewModel;
import app.edu_kg.pages.test.TestActivity;
import app.edu_kg.utils.Constant;
import app.edu_kg.utils.Functional;
import app.edu_kg.utils.Request;

import app.edu_kg.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    private ResultViewModel resultViewModel;
    private ActivityResultBinding binding;
    private Handler handler;
    private final String historyDir = "history.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("test", "jump into ResultActivity");
        super.onCreate(savedInstanceState);
        resultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        initHandler();
        initResult(view, intent);
        initBack();
    }

    private void initHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Log.e("test", "in handler");
                int message_num = msg.what;
                JSONObject json = null;
                try {
                    json = (JSONObject) msg.obj;
                } catch(Exception e) {
                    Log.e("test", "loading error");
                    return;
                }
                Log.e("test", json.toString());
                JSONArray entities = null;
                try {
                    entities = json.getJSONObject("data").getJSONArray("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RecyclerView historyRecycler = binding.board;
                historyRecycler.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
                historyRecycler.setAdapter(resultViewModel.adapter);
                resultViewModel.adapter.clearResult();
                if(entities != null) {
                    for(int i = 0; i < entities.length(); ++i) {
                        if (message_num == Constant.INSTANCE_LIST_RESPONSE) {
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
                                resultViewModel.adapter.addEntity(ResultListAdapter.ResultType.ENTITY, label, category, course);
                            } catch(Exception e) {
                                Log.e("test", e.toString());
                            }
                        }
                        else if (message_num == Constant.LINK_INSTANCE_RESPONSE) {
                            initRelation(getIntent());
                            String entity = null;
                            String entityType = null;
                            String course = null;
                            int start_index = 0;
                            int end_index = 0;
                            try {
                                entity = entities.getJSONObject(i).getString("entity");
                                entityType = entities.getJSONObject(i).getString("entity_type");
                                course = entities.getJSONObject(i).getString("course");
                                start_index = Integer.parseInt(entities.getJSONObject(i).getString("start_index"));
                                end_index = Integer.parseInt(entities.getJSONObject(i).getString("end_index"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            resultViewModel.adapter.addLinkInstance(ResultListAdapter.ResultType.LINK_INSTANCE, entity, entityType, course, start_index, end_index);
                            //binding.relation.setText(ResultViewModel.relationStyle);
                        }
                    }
                }
            }
        };
    }

    /*
    private void jumpToResult(View view) {
        Log.e("test", "jump to result");
        String type = typeList[ResultViewModel.select_type];
        String searchInput = binding.searchInput.getText().toString();
        String course = binding.subject.getSelectedItem().toString();
        String order = binding.order.getSelectedItem().toString();
        addHistory(searchInput, type, course, order);
        Intent intent = null;
        if(type.equals("试题")) {
            intent = new Intent(ResultActivity.this, TestActivity.class);
        }
        else if(type.equals("提纲")) {
            intent = new Intent(ResultActivity.this, ResultActivity.class);
        }
        else {
            intent = new Intent(ResultActivity.this, ResultActivity.class);
        }
        intent.putExtra("searchInput", searchInput);
        intent.putExtra("type", type);
        intent.putExtra("course", course);
        intent.putExtra("order", order);
        initHistory(view);
        startActivity(intent);
    }
    */


    private void initResult(View view, Intent intent) {
        RecyclerView resultRecycler = binding.board;
        resultRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        resultRecycler.setAdapter(resultViewModel.adapter);
        String searchInput = intent.getStringExtra("searchInput");
        String type = intent.getStringExtra("type");
        String course = Functional.subjChe2Eng(intent.getStringExtra("course"));
        String order = intent.getStringExtra("order");
        if(type.equals("实体")) {
            Request.getInstanceList(searchInput, course, order, handler);
        }
        else {
            Request.getLinkInstance(searchInput, course, handler);
        }
    }

    private void initRelation(Intent intent) {
        TextView relation = binding.relation;
        relation.setVisibility(View.VISIBLE);
        ResultViewModel.relationStyle.clear();
        ResultViewModel.relationStyle.append(intent.getStringExtra("searchInput"));
        relation.setText(ResultViewModel.relationStyle);
    }

    private void initBack() {
        MaterialToolbar back = binding.back;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("test", "finish");
                finish();
            }
        });
    }

}