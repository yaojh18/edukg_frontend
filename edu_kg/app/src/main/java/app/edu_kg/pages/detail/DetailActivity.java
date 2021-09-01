package app.edu_kg.pages.detail;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import app.edu_kg.DataApplication;
import app.edu_kg.R;
import app.edu_kg.pages.test.TestActivity;
import app.edu_kg.pages.user.LogActivity;
import app.edu_kg.utils.Constant;
import app.edu_kg.utils.Functional;
import app.edu_kg.utils.Request;
import app.edu_kg.utils.adapter.DetailPropertyTableAdapter;
import kotlin.Pair;
import kotlin.Triple;


public class DetailActivity extends AppCompatActivity {

    private String course;
    private String name;
    private String token;
    private List<DetailPropertyTableAdapter.DetailMessage> propertyList;
    private DetailPropertyTableAdapter adapter;
    private List<Pair<String, String>> relationList;
    private boolean isFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        Activity activity = this;

        // set title
        course = intent.getStringExtra("course");
        name = intent.getStringExtra("name");
        token = intent.getStringExtra("token");
        TextView nameView = findViewById(R.id.detail_name);
        nameView.setText(name);
        nameView.setMaxWidth(getWindowManager().getDefaultDisplay().getWidth() - 280);
        ((TextView) findViewById(R.id.detail_course)).setText(Functional.subjEng2Che(course));

        // set toolbar
        MaterialToolbar toolbar = findViewById(R.id.detail_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // set favorite and share
        ImageButton favorite = findViewById(R.id.detail_star);
        ImageButton share = findViewById(R.id.detail_share);

        // init handler and launcher
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Intent data = result.getData();
                        if (result.getResultCode() == Activity.RESULT_OK){
                            if (data.getStringExtra("func_type").equals("login")){
                                DataApplication localData = (DataApplication) getApplicationContext();
                                localData.username = data.getStringExtra("username");
                                localData.token = data.getStringExtra("token");
                                token = localData.token;
                            }
                        }
                    }
                });
        Handler handler = new Handler(Looper.getMainLooper()) {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Constant.DETAIL_RESPONSE_SUCCESS) {
                    Triple<ArrayList<DetailPropertyTableAdapter.DetailMessage>, ArrayList<Pair<String, String>>, Boolean> obj =
                            (Triple<ArrayList<DetailPropertyTableAdapter.DetailMessage>, ArrayList<Pair<String, String>>, Boolean>) msg.obj;
                    propertyList.addAll(obj.getFirst());
                    adapter.notifyDataSetChanged();
                    relationList.addAll(obj.getSecond());
                    isFavorite = obj.getThird();
                    favorite.setSelected(isFavorite);
                }
                else if (msg.what == Constant.DETAIL_RESPONSE_FAIL){
                    AlertDialog dialog = new MaterialAlertDialogBuilder(activity)
                            .setTitle("不存在这个实体")
                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    activity.finish();
                                }
                            })
                            .show();
                }
                else if (msg.what == Constant.ADD_FAVORITE_RESPONSE_SUCCESS) {
                    isFavorite = !isFavorite;
                    favorite.setSelected(isFavorite);
                }
                else if (msg.what == Constant.ADD_FAVORITE_RESPONSE_FAIL){
                    String obj = (String) msg.obj;
                    Snackbar.make(activity.getWindow().getDecorView().getRootView(), obj, Snackbar.LENGTH_SHORT)
                            .setAction("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) { }
                            })
                            .show();
                }
            }
        };

        // init favorite and share onclick
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (token.equals("")){
                    Intent intent = new Intent(activity, LogActivity.class);
                    intent.putExtra("func_type", "login");
                    launcher.launch(intent);
                }
                else{
                    if (!isFavorite)
                        Request.addFavorite(token, name, course, handler);
                    else
                        Request.deleteFavorite(token, name, course, handler);
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO
            }
        });

        // set exercise link
        CardView exercise = findViewById(R.id.detail_exercise);
        exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TestActivity.class);
                intent.putExtra("searchInput", name);
                intent.putExtra("course", course);
                intent.putExtra("page_type", Constant.EXERCISE_LIST_PAGE);
                startActivity(intent);
            }
        });

        // set data
        propertyList = new ArrayList<>();
        relationList = new ArrayList<>();
        adapter = new DetailPropertyTableAdapter(propertyList);
        RecyclerView propertyRecycler = findViewById(R.id.detail_property_recycler);
        propertyRecycler.setLayoutManager(new LinearLayoutManager(this));
        propertyRecycler.setAdapter(adapter);

        Request.getInfoByInstanceName(name, course, token, handler);

    }
}