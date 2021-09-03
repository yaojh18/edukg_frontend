package app.edu_kg.pages.test;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationBarView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import app.edu_kg.R;
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
    Activity activity = this;

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
                Log.e("test", msg.obj.toString());
                int message_num = msg.what;
                if (message_num == Constant.QUESTION_LIST_RESPONSE){
                    try {
                        JSONObject json = (JSONObject) msg.obj;
                        JSONArray entities = json.getJSONArray("data");
                        if (entities.length() == 0)
                            throw new Exception();
                        for(int i = 0; i < entities.length(); ++i) {
                            String question = entities.getJSONObject(i).getString("qBody");
                            String optA = entities.getJSONObject(i).getString("A");
                            String optB = entities.getJSONObject(i).getString("B");
                            String optC = entities.getJSONObject(i).getString("C");
                            String optD = entities.getJSONObject(i).getString("D");
                            int ans = entities.getJSONObject(i).getString("qAnswer").getBytes()[0] - (byte)'A';
                            testViewModel.adapter.addTest(question, optA, optB, optC, optD, ans);
                        }
                    }
                    catch(Exception e) {
                        AlertDialog dialog = new MaterialAlertDialogBuilder(activity)
                                .setTitle("服务器错误或习题不存在")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        activity.finish();
                                    }
                                })
                                .show();
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
        int pageType = intent.getIntExtra("page_type", Constant.EXERCISE_LIST_PAGE);
        if (pageType == Constant.RECOMMENDATION_PAGE){
            String token = intent.getStringExtra("token");
            Request.getExerciseRecommendation(token, handler);
            Log.e("test", "recommendation");
        }
        else if (pageType == Constant.EXERCISE_LIST_PAGE){
            String searchInput = intent.getStringExtra("searchInput");
            String course = intent.getStringExtra("course");
            Request.getQuestionList(searchInput, course, handler);
        }
        /*
        testViewModel.adapter.addTest("question 1", "opt1", "opt2", "opt3", "opt4", 0);
        testViewModel.adapter.addTest("question 2", "opt1","opt2", "opt3", "opt4", 0);
        testViewModel.adapter.addTest("question 1", "opt1", "opt2", "opt3", "opt4", 0);
        testViewModel.adapter.addTest("question 3", "opt1", "opt2", "opt3", "opt4", 0);
         */
        testViewModel.adapter.clear();
    }


    private void initBack() {
        MaterialToolbar back = binding.backBar;
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