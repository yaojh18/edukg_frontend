package app.edu_kg.pages.result;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;


import com.google.android.material.appbar.MaterialToolbar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        Intent intent = getIntent();
        initRelation(intent);
        initHandler();
        initResult(intent);
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
                RecyclerView resultRecycler = binding.board;
                resultRecycler.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
                resultRecycler.setAdapter(resultViewModel.adapter);
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

                            int color = Constant.LINK_INSTANCE_COLOR[start_index % Constant.LINK_INSTANCE_COLOR.length];
                            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
                            ResultViewModel.relationStyle.setSpan(foregroundColorSpan, start_index, end_index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
                binding.relation.setText(ResultViewModel.relationStyle);
            }
        };
    }


    private void initResult(Intent intent) {
        String searchInput = intent.getStringExtra("searchInput");
        String type = intent.getStringExtra("type");
        String course = Functional.subjChe2Eng(intent.getStringExtra("course"));
        String order = Functional.sortMethodChe2Eng(intent.getStringExtra("order"));
        if(type.equals("实体")) {
            Request.getInstanceList(searchInput, course, order, handler);
        }
        else {
            Request.getLinkInstance(searchInput, course, handler);
        }
    }

    private void initRelation(Intent intent) {
        ResultViewModel.relationStyle.clear();
        ResultViewModel.relationStyle.append(intent.getStringExtra("searchInput"));
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