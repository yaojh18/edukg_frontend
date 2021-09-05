package app.edu_kg.pages.outline;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


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

import app.edu_kg.databinding.ActivityOutlineBinding;
import app.edu_kg.utils.adapter.ItemListAdapter;

public class OutlineActivity extends AppCompatActivity implements ItemListAdapter.OnItemClickListener {

    private ActivityOutlineBinding binding;
    private Handler handler;
    private List<ItemListAdapter.ItemMessage> outlineList = new ArrayList<>();;
    private ItemListAdapter adapter = new ItemListAdapter(outlineList, this);
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOutlineBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        intent = getIntent();
        initHandler();
        initResult();
        initBack();
        initTitle();
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
                    entities = json.getJSONObject("data").getJSONArray("list");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                outlineList.clear();
                if(entities != null) {
                    for(int i = 0; i < entities.length(); ++i) {
                        String label = null;
                        String category = null;
                        JSONArray relationship_list = null;
                        String secondTitle = "";
                        try {
                            label = (i + 1) + ". " + entities.getJSONObject(i).getString("label");
                            category = "(" + entities.getJSONObject(i).getString("category") + ")";
                            relationship_list = entities.getJSONObject(i).getJSONArray("relationship_list");
                            for(int j = 0; j < relationship_list.length(); ++j) {
                                if(j != 0) {
                                    secondTitle += "\n";
                                }
                                secondTitle += "--" + (i + 1) + "." + (j + 1) + "  " + relationship_list.getString(j);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        outlineList.add(new ItemListAdapter.ItemMessage(label, Functional.subjEng2Che(intent.getStringExtra("course")), category, null, false, secondTitle));
                    }
                }
                adapter.notifyItemChanged(outlineList.size() - 1);
            }
        };
    }


    private void initResult() {
        String searchInput = intent.getStringExtra("searchInput");
        String course = intent.getStringExtra("course");

        RecyclerView resultRecycler = binding.board;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        resultRecycler.setLayoutManager(linearLayoutManager);
        resultRecycler.setAdapter(adapter);
        Request.getOutline(searchInput, course, handler);
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

    private void initTitle() {
        TextView title = binding.searchInput;
        title.setText(intent.getStringExtra("searchInput"));
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
        ItemListAdapter.ItemMessage item = outlineList.get(position);
        item.showSecondTitle = !item.showSecondTitle;
        adapter.notifyItemChanged(position);
    }
}