package app.edu_kg.pages.outline;

import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.android.material.appbar.MaterialToolbar;


import androidx.annotation.NonNull;
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

import app.edu_kg.utils.Request;

import app.edu_kg.databinding.ActivityOutlineBinding;

public class OutlineActivity extends AppCompatActivity {

    private ActivityOutlineBinding binding;
    private Handler handler;
    private OutlineListAdapter adapter;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOutlineBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        intent = getIntent();
        adapter = new OutlineListAdapter();
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
                RecyclerView resultRecycler = binding.board;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(binding.getRoot().getContext());
                resultRecycler.setLayoutManager(linearLayoutManager);
                resultRecycler.setAdapter(adapter);
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
                adapter.clear();
                if(entities != null) {
                    for(int i = 0; i < entities.length(); ++i) {
                        String label = null;
                        String category = null;
                        JSONArray relationship_list = null;
                        List<String> secondTitle = new ArrayList<>();
                        try {
                            label = (i + 1) + ".\t" + entities.getJSONObject(i).getString("label");
                            category = "(" + entities.getJSONObject(i).getString("category") + ")";
                            relationship_list = entities.getJSONObject(i).getJSONArray("relationship_list");
                            for(int j = 0; j < relationship_list.length(); ++j) {
                                secondTitle.add("--" + (i + 1) + "." + (j + 1) + "\t" + relationship_list.getString(j));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.addItem(label, intent.getStringExtra("course"), category, secondTitle);
                    }
                }
            }
        };
    }


    private void initResult() {
        String searchInput = intent.getStringExtra("searchInput");
        String course = intent.getStringExtra("course");
        Request.getOutline(searchInput, course, handler);
    }


    private void initBack() {
        MaterialToolbar back = binding.back;
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initTitle() {
        TextView title = binding.searchInput;
        title.setText(intent.getStringExtra("searchInput"));
    }

}

class OutlineListAdapter extends RecyclerView.Adapter {

    private static class OutlineHolder extends RecyclerView.ViewHolder {
        TextView firstTitle;
        TextView category;
        LinearLayout secondTitle;
        ImageView imageView;

        OutlineHolder(View itemView) {
            super(itemView);
            firstTitle = (TextView) itemView.findViewById(R.id.first_title);
            category = (TextView) itemView.findViewById(R.id.category);
            secondTitle = (LinearLayout) itemView.findViewById(R.id.second_title);
            imageView = (ImageView) itemView.findViewById(R.id.item_image_end);
        }

        void bind(app.edu_kg.pages.outline.OutlineListAdapter.Outline outline) {
            firstTitle.setText(outline.firstTitle);
            firstTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra("name", outline.firstTitle.split("\t", 2)[1]);
                    intent.putExtra("course", outline.subject);
                    intent.putExtra("token", ((DataApplication)v.getContext().getApplicationContext()).token);
                    v.getContext().startActivity(intent);
                }
            });
            secondTitle.removeAllViews();
            for(int i = 0; i < outline.secondTitle.size(); ++i) {
                addSecondTitle(outline ,i);
            }
            category.setText(outline.category);
            imageView.setBackgroundResource(R.drawable.ic_outline_expand);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(secondTitle.getVisibility() == View.GONE) {
                        secondTitle.setVisibility(View.VISIBLE);
                        imageView.setBackgroundResource(R.drawable.ic_outline_close);
                    }
                    else {
                        secondTitle.setVisibility(View.GONE);
                        imageView.setBackgroundResource(R.drawable.ic_outline_expand);
                    }

                }
            });
        }

        private void addSecondTitle(app.edu_kg.pages.outline.OutlineListAdapter.Outline outline, int i) {
            TextView textView = new TextView(secondTitle.getContext());
            textView.setText(outline.secondTitle.get(i));
            textView.setTextAppearance(R.style.TextAppearance_AppCompat_Body1);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra("name", outline.secondTitle.get(i).split("\t", 2)[1]);
                    intent.putExtra("course", outline.subject);
                    intent.putExtra("token", ((DataApplication)v.getContext().getApplicationContext()).token);
                    v.getContext().startActivity(intent);
                }
            });
            secondTitle.addView(textView);
        }
    }

    private static class Outline {
        String firstTitle;
        String category;
        String subject;
        List<String> secondTitle;

        Outline (String firstTitle, String subject, String category, List<String> secondTitle) {
            this.firstTitle = firstTitle;
            this.subject = subject;
            this.category = category;
            this.secondTitle = secondTitle;
        }
    }

    private List<app.edu_kg.pages.outline.OutlineListAdapter.Outline> outlineList;

    public OutlineListAdapter() {
        outlineList = new ArrayList<app.edu_kg.pages.outline.OutlineListAdapter.Outline>();
    }

    public void addItem(String firstTitle, String subject, String category, List<String> secondTitle) {
        outlineList.add(new OutlineListAdapter.Outline(firstTitle, subject, category, secondTitle));
    }


    public void clear() {
        outlineList.clear();
    }

    @Override
    public int getItemCount() {
        return outlineList.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.outline_item, parent, false);
        return new app.edu_kg.pages.outline.OutlineListAdapter.OutlineHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        app.edu_kg.pages.outline.OutlineListAdapter.Outline message = outlineList.get(position);
        ((app.edu_kg.pages.outline.OutlineListAdapter.OutlineHolder) holder).bind(message);
    }

}