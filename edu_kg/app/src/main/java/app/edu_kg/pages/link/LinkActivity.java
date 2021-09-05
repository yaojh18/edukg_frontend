package app.edu_kg.pages.link;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;


import com.google.android.material.appbar.MaterialToolbar;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.edu_kg.DataApplication;
import app.edu_kg.R;
import app.edu_kg.pages.detail.DetailActivity;
import app.edu_kg.utils.Constant;
import app.edu_kg.utils.Functional;
import app.edu_kg.utils.Request;

import app.edu_kg.databinding.ActivityLinkBinding;
import app.edu_kg.utils.adapter.ItemListAdapter;

public class LinkActivity extends AppCompatActivity implements ItemListAdapter.OnItemClickListener {

    private ActivityLinkBinding binding;
    private Handler handler;
    private List<ItemListAdapter.ItemMessage> linkList = new ArrayList<>();;
    private static SpannableStringBuilder relationStyle = new SpannableStringBuilder();
    private ItemListAdapter adapter = new ItemListAdapter(linkList, this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLinkBinding.inflate(getLayoutInflater());
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
                linkList.clear();
                if(entities != null) {
                    for(int i = 0; i < entities.length(); ++i) {
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
                        int color = Constant.LINK_INSTANCE_COLOR[start_index % Constant.LINK_INSTANCE_COLOR.length];

                        linkList.add(new ItemListAdapter.ItemMessage(entity, Functional.subjEng2Che(course), entityType, null, false, color));

                        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
                        relationStyle.setSpan(foregroundColorSpan, start_index, end_index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                binding.relation.setText(relationStyle);
                adapter.notifyItemChanged(linkList.size() - 1);
            }
        };
    }


    private void initResult(Intent intent) {
        String searchInput = intent.getStringExtra("searchInput");
        String course = intent.getStringExtra("course");

        RecyclerView resultRecycler = binding.board;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        resultRecycler.setLayoutManager(linearLayoutManager);
        resultRecycler.setAdapter(adapter);

        Request.getLinkInstance(searchInput, course, handler);
    }

    private void initRelation(Intent intent) {
        relationStyle.clear();
        relationStyle.append(intent.getStringExtra("searchInput"));
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

    private void jumpToDetail(String name, String course, String token) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("course", course);
        intent.putExtra("token", token);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        ItemListAdapter.ItemMessage item = linkList.get(position);
        String name = item.label;
        String course = Functional.subjChe2Eng(item.course);
        String token = ((DataApplication)getApplicationContext()).token;
        jumpToDetail(name, course, token);
    }
}