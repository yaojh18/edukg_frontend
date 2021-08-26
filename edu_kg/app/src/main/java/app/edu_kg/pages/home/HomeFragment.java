package app.edu_kg.pages.home;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import app.edu_kg.R;
import app.edu_kg.databinding.FragmentHomeBinding;
import app.edu_kg.pages.search.SearchActivity;
import app.edu_kg.utils.Request;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private LinearLayout mGallery;
    private int[] mImgIds;
    private LayoutInflater mInflater;

    int selectedColor = Color.BLUE;
    int defaultColor = Color.WHITE;
    int abandonColor = Color.GRAY;

    final String subjectDir = "subjectDir.txt";

    View selectedSubject = null;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        mInflater = inflater;
        initSubjectListPic();
        loadSubject();
        initSubjectSetting();
        initSearchButton(view);
        try {
            initEntityList(view);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void initSubjectListPic() {
        mImgIds = new int[homeViewModel.total_subject];
        for (int i = 0; i < homeViewModel.total_subject; ++i) {
            mImgIds[i] = R.drawable.menu_home;
        }
    }

    private void initSubjectListView() {
        mGallery = (LinearLayout) binding.idGallery;
        mGallery.removeAllViews();
        homeViewModel.selectSubject = -1;
        for (int i = 0; i < homeViewModel.total_subject; ++i) {
            if(homeViewModel.selected[i]) {
                if(homeViewModel.selectSubject == -1) {
                    homeViewModel.selectSubject = i;
                }
                View view = mInflater.inflate(R.layout.subject_item,
                        mGallery, false);
                ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
                img.setImageResource(mImgIds[i]);
                TextView txt = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
                txt.setText(homeViewModel.subject[i]);
                txt.setTextColor(Color.BLACK);
                view.setId(i);
                view.setBackgroundColor(homeViewModel.selectSubject==i?selectedColor:defaultColor);
                if(homeViewModel.selectSubject==i) {
                    selectedSubject = view;
                }
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectedSubject != null) {
                            selectedSubject.setBackgroundColor(defaultColor);
                            v.setBackgroundColor(selectedColor);
                            homeViewModel.selectSubject = v.getId();
                            selectedSubject = v;

                        }
                    }
                });
                mGallery.addView(view);
            }
        }
    }

    private void initSubjectListEditView() {
        mGallery = (LinearLayout) binding.idGallery;
        mGallery.removeAllViews();

        for(int i = 0; i < homeViewModel.total_subject; ++i) {
            View view = mInflater.inflate(R.layout.subject_item,
                    mGallery, false);
            ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
            img.setImageResource(mImgIds[i]);
            TextView txt = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
            txt.setText(homeViewModel.subject[i]);
            txt.setTextColor(Color.BLACK);
            view.setId(i);

            view.setBackgroundColor(homeViewModel.selected[i]?defaultColor:abandonColor);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeViewModel.selected[v.getId()] = !homeViewModel.selected[v.getId()];
                    view.setBackgroundColor(homeViewModel.selected[v.getId()]?defaultColor:abandonColor);
                    //Log.e("test", String.valueOf(v.getId()));
                    //Log.e("test", Arrays.toString(homeViewModel.selected));
                }
            });
            mGallery.addView(view);
        }
    }

    private void initSearchButton(View v) {
        Button btn = (Button)v.findViewById(R.id.search_bar);
        btn.setOnClickListener(v1 -> {
            Log.e("test", "search button clicked");
            Intent i = new Intent(getActivity(), SearchActivity.class);
            startActivity(i);
        });
    }

    private void initSubjectSetting() {
        Button subjectSetting = binding.subjectSetting;

        if (homeViewModel.isEditting) {
            initSubjectListEditView();
            binding.subjectSetting.setText("完成");
        }
        else {
            initSubjectListView();
            binding.subjectSetting.setText("编辑");
        }

        subjectSetting.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                homeViewModel.isEditting = !homeViewModel.isEditting;
                if (homeViewModel.isEditting) {
                    initSubjectListEditView();
                    binding.subjectSetting.setText("完成");
                }
                else {
                    selectedSubject = mGallery.getChildAt(0);
                    homeViewModel.selectSubject = selectedSubject.getId();
                    initSubjectListView();
                    saveSubject();
                    binding.subjectSetting.setText("编辑");
                }
            }
        });
    }

    private void saveSubject() {
        try {
            FileOutputStream out = getActivity().openFileOutput(subjectDir, Context.MODE_PRIVATE);
            for(int i = 0; i < homeViewModel.total_subject; ++i) {
                out.write(homeViewModel.selected[i]? 1: 0);
            }
            out.flush();
            out.close();
            Log.e("test", "save successfully");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", e.toString());
        }
    }

    private void loadSubject() {
        FileInputStream in;
        try {
            in = getActivity().getApplicationContext().openFileInput(subjectDir);
            for(int i = 0; i < homeViewModel.total_subject; ++i) {
                int val = in.read();
                homeViewModel.selected[i] = val == 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initEntityList(View view) throws JSONException {
        RecyclerView messageRecycler = binding.entityBoard;
        messageRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        messageRecycler.setAdapter(homeViewModel.adapter);
        //JSONObject json = request.getSubjectEntityList(subjectList[selectedSubject.getId()]);
        JSONObject json = new JSONObject("{ \"code\": 200表示成功，其他尚未定义, \"data\": { \"result\": [ { \"label\": 搜索到的实体名称1, \"category\": 搜索到的实体所属类1, \"course\": 所属学科 }, { \"label\": 搜索到的实体名称2, \"category\":搜索到的实体所属类2, \"course\":所属学科 }, ... ], \"result_size\": 搜索到的实体数量 } }");
        JSONArray entities = json.getJSONObject("data").getJSONArray("result");
        homeViewModel.adapter.clearEntity();
        for(int i = 0; i < entities.length(); ++i) {
            String label = entities.getJSONObject(i).getString("label");
            String category = entities.getJSONObject(i).getString("category");
            homeViewModel.adapter.addEntity(label, category);
        }
    }
}