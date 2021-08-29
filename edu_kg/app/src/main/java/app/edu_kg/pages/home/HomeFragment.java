package app.edu_kg.pages.home;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import app.edu_kg.MainActivity;
import app.edu_kg.MainViewModel;
import app.edu_kg.R;
import app.edu_kg.databinding.FragmentHomeBinding;
import app.edu_kg.pages.search.SearchActivity;
import app.edu_kg.utils.Constant;
import app.edu_kg.utils.Functional;
import app.edu_kg.utils.Request;
import app.edu_kg.utils.adapter.ItemListAdapter;
import app.edu_kg.utils.adapter.SubjectGridAdapter;
import kotlin.Triple;

public class HomeFragment extends Fragment implements ItemListAdapter.OnItemClickListener, SubjectGridAdapter.OnSubjectClickListener {
    private MainViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private Handler handler;
    private final boolean EDIT_SUBJECT = true;
    private boolean mode = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Context context = view.getContext();
        homeViewModel = ((MainActivity) context).mainViewModel;

        // init search button
        ImageView btn = binding.searchBar;
        btn.setOnClickListener(v1 -> {
            Intent i = new Intent(getActivity(), SearchActivity.class);
            startActivity(i);
        });

        // get list message
        handler = new Handler(Looper.getMainLooper()) {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Constant.HOME_ENTITY_RESPONSE_SUCCESS) {
                    ArrayList<Triple<String, String, String>> obj = (ArrayList<Triple<String, String, String>>) msg.obj;
                    homeViewModel.homeList.clear();
                    for (Triple<String, String, String> item : obj) {
                        homeViewModel.homeList.add(new ItemListAdapter.ItemMessage(item.getFirst(), item.getSecond(), item.getThird(), null));
                    }
                    homeViewModel.homeListAdapter.notifyDataSetChanged();
                }
                else if (msg.what == Constant.HOME_ENTITY_RESPONSE_FAIL){
                    AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                            .setTitle("取回数据错误")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) { }
                            })
                            .show();
                }
            }
        };
        Request.getHomeList("chinese", handler);

        // init list recycler
        RecyclerView itemRecycler = binding.entityBoard;
        homeViewModel.homeListAdapter = new ItemListAdapter(homeViewModel.homeList, this);
        itemRecycler.setLayoutManager(new LinearLayoutManager(context));
        itemRecycler.setAdapter(homeViewModel.homeListAdapter);

        // init subject recycler
        RecyclerView subjectRecycler = binding.subjectSetting;
        subjectRecycler.setLayoutManager(new GridLayoutManager(context, 5));
        homeViewModel.homeSubjectAdapter = new SubjectGridAdapter(homeViewModel.homeSubjectList,this);
        homeViewModel.presentSubjectAdapter = new SubjectGridAdapter(homeViewModel.presentSubjectList, this);
        subjectRecycler.setAdapter(homeViewModel.homeSubjectAdapter);

        // init subject manager
        LinearLayout subjectManager = binding.subjectManager;
        subjectManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = !mode;
                TextView text = binding.subjectManagerText;
                ImageView image = binding.subjectManagerImage;
                if (mode == EDIT_SUBJECT){
                    subjectRecycler.setAdapter(homeViewModel.presentSubjectAdapter);
                    text.setText(R.string.subject_manager_finish);
                    image.setImageResource(R.drawable.subject_manager_finish);
                }
                else{
                    // clear
                    Constant.homeSubjectMap.forEach((name, subject) -> {
                        subject.isSelected = false;
                    });
                    homeViewModel.homeSubjectList.clear();
                    homeViewModel.homeSubjectSelected = 0;

                    // update
                    for (Constant.SUBJECT_NAME name : homeViewModel.presentSet){
                        homeViewModel.homeSubjectList.add(Constant.homeSubjectMap.get(name));
                        if (homeViewModel.homeSubjectList.size() >= 5) break;
                    }
                    if (homeViewModel.presentSet.size() > 5){
                        homeViewModel.homeSubjectList.remove(4);
                        homeViewModel.homeSubjectList.add(Constant.homeSubjectMap.get(Constant.SUBJECT_NAME.UNFOLD));
                    }
                    SubjectGridAdapter.Subject firstSubject = homeViewModel.homeSubjectList.get(0);
                    firstSubject.isSelected = true;
                    Request.getHomeList(Functional.subjChe2Eng(firstSubject.name), handler);

                    // update adapter
                    subjectRecycler.setAdapter(homeViewModel.homeSubjectAdapter);
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

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSubjectClick(int position) {
        if (mode != EDIT_SUBJECT){
            List<SubjectGridAdapter.Subject> subjectList = homeViewModel.homeSubjectList;
            SubjectGridAdapter.Subject subject = subjectList.get(position);
            switch (subject.id){
                case FOLD:
                    while (subjectList.size() > 4){
                        subjectList.remove(subjectList.size() - 1);
                    }
                    subjectList.add(Constant.homeSubjectMap.get(Constant.SUBJECT_NAME.UNFOLD));
                    break;
                case UNFOLD:
                    subjectList.clear();
                    for (Constant.SUBJECT_NAME name : homeViewModel.presentSet){
                        subjectList.add(Constant.homeSubjectMap.get(name));
                    }
                    subjectList.add(Constant.homeSubjectMap.get(Constant.SUBJECT_NAME.FOLD));
                    break;
                default:
                    if (homeViewModel.homeSubjectSelected < subjectList.size()){
                        SubjectGridAdapter.Subject oldSubject = subjectList.get(homeViewModel.homeSubjectSelected);
                        oldSubject.isSelected = false;
                    }
                    subject.isSelected = true;
                    homeViewModel.homeSubjectSelected = position;
                    Request.getHomeList(Functional.subjChe2Eng(subjectList.get(homeViewModel.homeSubjectSelected).name), handler);
            }
            homeViewModel.homeSubjectAdapter.notifyDataSetChanged();
        }
        else{
            SubjectGridAdapter.Subject subject = homeViewModel.presentSubjectList.get(position);
            if (homeViewModel.presentSet.size() > 1 || !homeViewModel.presentSet.contains(subject.id)){
                subject.isOut = ! subject.isOut;
                if (subject.isOut)
                    homeViewModel.presentSet.remove(subject.id);
                else
                    homeViewModel.presentSet.add(subject.id);
                homeViewModel.presentSubjectAdapter.notifyDataSetChanged();
            }
        }
    }
}