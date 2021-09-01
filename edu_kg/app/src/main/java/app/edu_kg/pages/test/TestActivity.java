package app.edu_kg.pages.test;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.edu_kg.R;
import app.edu_kg.pages.result.ResultViewModel;
import app.edu_kg.utils.Constant;
import app.edu_kg.utils.Functional;
import app.edu_kg.utils.Request;

import app.edu_kg.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity {

    private TestViewModel testViewModel;
    private ActivityTestBinding binding;
    private Handler handler;
    private int scrollState = 0;
    private boolean scrollable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("test", "jump into TestActivity");
        super.onCreate(savedInstanceState);
        testViewModel = new ViewModelProvider(this).get(TestViewModel.class);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        initHandler();
        Intent intent = getIntent();
        initTest(view, intent);
        initBack();
        initNavBar();
    }

    private void initHandler() {
        handler = new Handler(Looper.getMainLooper()) {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void handleMessage(Message msg) {
                Log.e("test", "in handler");

                RecyclerView testRecycler = binding.question;
                LinearLayoutManager ms = new LinearLayoutManager(binding.getRoot().getContext());
                ms.setOrientation(LinearLayoutManager.HORIZONTAL);
                testRecycler.setLayoutManager(ms);
                testRecycler.setAdapter(testViewModel.adapter);

                int message_num = msg.what;
                if (message_num == Constant.QUESTION_LIST_RESPONSE){
                    JSONObject json = null;
                    try {
                        //Log.e("test", msg.obj.toString());
                        json = (JSONObject) msg.obj;
                    } catch(Exception e) {
                        testViewModel.adapter.addTest("载入失败，请重新载入", "", "", "", "", 0);
                        Log.e("test", "载入失败");
                        return;
                    }
                    JSONArray entities = null;
                    try {
                        entities = json.getJSONArray("data");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //Log.e("test", entities.toString());
                    //Log.e("test", String.valueOf(entities.length()));
                    for(int i = 0; i < entities.length(); ++i) {
                        String question = null;
                        String optA = null;
                        String optB = null;
                        String optC = null;
                        String optD = null;
                        int ans = 0;
                        try {
                            question = entities.getJSONObject(i).getString("qBody");
                            optA = entities.getJSONObject(i).getString("A");
                            optB = entities.getJSONObject(i).getString("B");
                            optC = entities.getJSONObject(i).getString("C");
                            optD = entities.getJSONObject(i).getString("D");
                            ans = entities.getJSONObject(i).getString("qAnswer").getBytes()[0] - (byte)'A';
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        testViewModel.adapter.addTest(question, optA, optB, optC, optD, ans);
                    }
                }
            }
        };
    }



    private void initTest(View view, Intent intent) {
        RecyclerView testRecycler = binding.question;
        testRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(scrollState == 0 && newState == 1) {
                    scrollable = true;
                }
                scrollState = newState;
                Log.e("test", "state:" + String.valueOf(newState));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(scrollable) {
                    if(dx > 1) {
                        if(testViewModel.questionIdx + 1 < testViewModel.adapter.getItemCount()) {
                            testViewModel.questionIdx += 1;
                        }
                    }
                    else if(dx < -1) {
                        if(testViewModel.questionIdx - 1 >= 0) {
                            testViewModel.questionIdx -= 1;
                        }
                    }
                    scrollable = false;
                }
                testRecycler.smoothScrollToPosition(testViewModel.questionIdx);
            }
        });
        String searchInput = intent.getStringExtra("searchInput");
        String course = Functional.subjChe2Eng(intent.getStringExtra("course"));
        testViewModel.adapter.clear();
        Request.getQuestionList(searchInput, course, handler);
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

    private void initNavBar() {
        BottomNavigationView navBar = binding.navView;
        navBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_all:
                        TestViewModel.checkAns = false;
                        binding.score.setVisibility(View.GONE);
                        break;
                    case R.id.nav_submit:
                        int grade = checkGrade();
                        TestViewModel.checkAns = true;
                        binding.score.setText(String.valueOf(grade) + "/" + String.valueOf(testViewModel.adapter.getItemCount()));
                        binding.score.setVisibility(View.VISIBLE);
                        break;
                }
                testViewModel.adapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private int checkGrade() {
        int correct = 0;
        for(int i = 0; i < testViewModel.adapter.getItemCount(); ++i) {
            if(testViewModel.adapter.getChoice(i) == testViewModel.adapter.getAns(i)) {
                correct += 1;
            }
        }
        return correct;
    }


}