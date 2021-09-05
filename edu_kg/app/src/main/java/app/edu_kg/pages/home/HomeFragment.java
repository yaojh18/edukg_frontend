package app.edu_kg.pages.home;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import app.edu_kg.DataApplication;
import app.edu_kg.MainActivity;
import app.edu_kg.R;
import app.edu_kg.databinding.FragmentHomeBinding;
import app.edu_kg.pages.detail.DetailActivity;
import app.edu_kg.pages.search.SearchActivity;
import app.edu_kg.utils.Constant;
import app.edu_kg.utils.Functional;
import app.edu_kg.utils.InstanceIO;
import app.edu_kg.utils.Request;
import app.edu_kg.utils.adapter.ItemListAdapter;
import app.edu_kg.utils.adapter.SubjectGridAdapter;

public class HomeFragment extends Fragment implements ItemListAdapter.OnItemClickListener, SubjectGridAdapter.OnSubjectClickListener {
    private DataApplication localData;
    private FragmentHomeBinding binding;
    private MainActivity context;

    private Handler handler;
    private final boolean EDIT_SUBJECT = true;
    private boolean mode = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = (MainActivity) view.getContext();
        localData = (DataApplication) context.getApplicationContext();

        // init search button
        ImageView btn = binding.searchBar;
        btn.setOnClickListener(v1 -> {
            Intent i = new Intent(getActivity(), SearchActivity.class);
            startActivity(i);
        });

        // get list message
        handler = new Handler(Looper.getMainLooper()) {
            @SuppressLint({"NotifyDataSetChanged", "ShowToast"})
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Constant.HOME_ENTITY_RESPONSE_SUCCESS) {
                    ArrayList<ItemListAdapter.ItemMessage> obj = (ArrayList<ItemListAdapter.ItemMessage>) msg.obj;
                    localData.homeList.clear();
                    localData.homeList.addAll(obj);
                    for (ItemListAdapter.ItemMessage item : localData.homeList){
                        if (InstanceIO.isInstanceExist(context, item.label))
                            item.isChecked = true;
                    }
                    localData.homeListAdapter.notifyDataSetChanged();
                }
                else if (msg.what == Constant.HOME_ENTITY_RESPONSE_FAIL){
                    if (view.isShown())
                        Snackbar.make(view, "取回数据错误", Snackbar.LENGTH_SHORT)
                                .setAction("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) { }
                                })
                                .show();
                }
            }
        };
        Request.getHomeList(Functional.subjChe2Eng(localData.homeSubjectList.get(localData.homeSubjectSelected).name), handler);

        // init list recycler
        RecyclerView itemRecycler = binding.entityBoard;
        localData.homeListAdapter = new ItemListAdapter(localData.homeList, this);
        itemRecycler.setLayoutManager(new LinearLayoutManager(context));
        itemRecycler.setAdapter(localData.homeListAdapter);

        // init subject recycler
        RecyclerView subjectRecycler = binding.subjectSetting;
        subjectRecycler.setLayoutManager(new GridLayoutManager(context, 5));
        localData.homeSubjectAdapter = new SubjectGridAdapter(localData.homeSubjectList,this);
        localData.presentSubjectAdapter = new SubjectGridAdapter(localData.presentSubjectList, this);
        subjectRecycler.setAdapter(localData.homeSubjectAdapter);

        // init subject manager
        LinearLayout subjectManager = binding.subjectManager;
        subjectManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = !mode;
                TextView text = binding.subjectManagerText;
                ImageView image = binding.subjectManagerImage;
                if (mode == EDIT_SUBJECT){
                    subjectRecycler.setAdapter(localData.presentSubjectAdapter);
                    text.setText(R.string.subject_manager_finish);
                    image.setImageResource(R.drawable.subject_manager_finish);
                }
                else{
                    // clear
                    Constant.HOME_SUBJECT_MAP.forEach((name, subject) -> {
                        subject.isSelected = false;
                    });
                    localData.homeSubjectList.clear();
                    localData.homeSubjectSelected = 0;

                    // update
                    for (Constant.SUBJECT_NAME name : localData.presentSet){
                        localData.homeSubjectList.add(Constant.HOME_SUBJECT_MAP.get(name));
                        if (localData.homeSubjectList.size() >= 5) break;
                    }
                    if (localData.presentSet.size() > 5){
                        localData.homeSubjectList.remove(4);
                        localData.homeSubjectList.add(Constant.HOME_SUBJECT_MAP.get(Constant.SUBJECT_NAME.UNFOLD));
                    }
                    SubjectGridAdapter.Subject firstSubject = localData.homeSubjectList.get(0);
                    firstSubject.isSelected = true;
                    Request.getHomeList(Functional.subjChe2Eng(firstSubject.name), handler);

                    // update adapter
                    subjectRecycler.setAdapter(localData.homeSubjectAdapter);
                    text.setText(R.string.subject_manager);
                    image.setImageResource(R.drawable.subject_add);
                }
            }
        });

        return view;
    }

    // do your work here.
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(context, DetailActivity.class);
        ItemListAdapter.ItemMessage item = localData.homeList.get(position);
        item.isChecked = true;
        localData.homeListAdapter.notifyItemChanged(position);
        intent.putExtra("course", item.course);
        intent.putExtra("name", item.label);
        intent.putExtra("token", localData.token);
        startActivity(intent);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSubjectClick(int position) {
        if (mode != EDIT_SUBJECT){
            List<SubjectGridAdapter.Subject> subjectList = localData.homeSubjectList;
            SubjectGridAdapter.Subject subject = subjectList.get(position);
            switch (subject.id){
                case FOLD:
                    while (subjectList.size() > 4){
                        SubjectGridAdapter.Subject sub = subjectList.remove(subjectList.size() - 1);
                        sub.isSelected = false;
                    }
                    if (localData.homeSubjectSelected >= 4){
                        localData.homeSubjectSelected = 0;
                        subjectList.get(0).isSelected = true;
                        Request.getHomeList(Functional.subjChe2Eng(subjectList.get(0).name), handler);
                    }
                    subjectList.add(Constant.HOME_SUBJECT_MAP.get(Constant.SUBJECT_NAME.UNFOLD));
                    break;
                case UNFOLD:
                    subjectList.clear();
                    for (Constant.SUBJECT_NAME name : localData.presentSet){
                        subjectList.add(Constant.HOME_SUBJECT_MAP.get(name));
                    }
                    subjectList.add(Constant.HOME_SUBJECT_MAP.get(Constant.SUBJECT_NAME.FOLD));
                    break;
                default:
                    if (localData.homeSubjectSelected < subjectList.size()){
                        SubjectGridAdapter.Subject oldSubject = subjectList.get(localData.homeSubjectSelected);
                        oldSubject.isSelected = false;
                    }
                    subject.isSelected = true;
                    localData.homeSubjectSelected = position;
                    Request.getHomeList(Functional.subjChe2Eng(subjectList.get(localData.homeSubjectSelected).name), handler);
            }
            localData.homeSubjectAdapter.notifyDataSetChanged();
        }
        else{
            SubjectGridAdapter.Subject subject = localData.presentSubjectList.get(position);
            if (localData.presentSet.size() > 1 || !localData.presentSet.contains(subject.id)){
                subject.isOut = ! subject.isOut;
                if (subject.isOut)
                    localData.presentSet.remove(subject.id);
                else
                    localData.presentSet.add(subject.id);
                localData.presentSubjectAdapter.notifyItemChanged(position);
            }
        }
    }
}