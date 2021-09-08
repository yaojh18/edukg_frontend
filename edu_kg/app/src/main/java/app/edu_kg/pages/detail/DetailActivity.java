package app.edu_kg.pages.detail;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.common.UiError;
import com.sina.weibo.sdk.openapi.IWBAPI;
import com.sina.weibo.sdk.openapi.WBAPIFactory;
import com.sina.weibo.sdk.share.WbShareCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.edu_kg.DataApplication;
import app.edu_kg.R;
import app.edu_kg.pages.test.TestActivity;
import app.edu_kg.pages.user.LogActivity;
import app.edu_kg.utils.Constant;
import app.edu_kg.utils.Functional;
import app.edu_kg.utils.InstanceIO;
import app.edu_kg.utils.Request;
import app.edu_kg.utils.adapter.DetailPropertyTableAdapter;
import kotlin.Pair;
import kotlin.Triple;


public class DetailActivity extends AppCompatActivity implements WbShareCallback {

    private String course;
    private String name;
    private String token;
    private List<DetailPropertyTableAdapter.DetailMessage> propertyList;
    private DetailPropertyTableAdapter adapter;
    private String relationList;
    private String description;
    private Bitmap image;
    private boolean isFavorite;
    private boolean hasQuestion;


    private AuthInfo weiboAuthInfo;
    private IWBAPI weiboAPI;

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
        boolean loadFromFile= InstanceIO.isInstanceExist(this, name);
        hasQuestion = false;

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
        CardView exercise = findViewById(R.id.detail_exercise);

        // init handler and launcher
        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = Objects.requireNonNull(result.getData());
                            DataApplication localData = (DataApplication) getApplicationContext();
                            localData.username = data.getStringExtra("username");
                            localData.token = data.getStringExtra("token");
                            localData.userStateChanged = true;
                            token = localData.token;
                        }
                    }
                });
        Handler handler = new Handler(Looper.getMainLooper()) {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Constant.DETAIL_RESPONSE_SUCCESS || msg.what == Constant.INSTANCE_LOAD_SUCCESS) {
                    Triple<ArrayList<DetailPropertyTableAdapter.DetailMessage>, String, Triple<Boolean, Boolean, String>> obj;
                    if (msg.what == Constant.DETAIL_RESPONSE_SUCCESS) {
                        Pair<Triple<ArrayList<DetailPropertyTableAdapter.DetailMessage>, String, Triple<Boolean, Boolean, String>>, Bitmap> rawObj =
                                (Pair<Triple<ArrayList<DetailPropertyTableAdapter.DetailMessage>, String, Triple<Boolean, Boolean, String>>, Bitmap>) msg.obj;
                        obj = rawObj.getFirst();
                        image = rawObj.getSecond();
                        InstanceIO.saveInstance(activity, obj, name);
                        InstanceIO.saveBitmap(activity, image, name + "+image");
                    }else{
                        obj = (Triple<ArrayList<DetailPropertyTableAdapter.DetailMessage>, String, Triple<Boolean, Boolean, String>>) msg.obj;
                        image = InstanceIO.loadBitmap(activity, name + "+image");
                    }
                    propertyList = obj.getFirst();
                    if (propertyList.size() > 10){
                        Button showMore = activity.findViewById(R.id.detail_show_more);
                        showMore.setVisibility(View.VISIBLE);
                        adapter.itemList = propertyList.subList(0, 10);
                        adapter.notifyDataSetChanged();
                        showMore.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (showMore.getText().equals("点击展开更多")){
                                    showMore.setText("点击收起");
                                    adapter.itemList = propertyList;
                                    adapter.notifyDataSetChanged();
                                }
                                else {
                                    showMore.setText("点击展开更多");
                                    adapter.itemList = propertyList.subList(0, 10);
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                    else{
                        adapter.itemList = propertyList;
                    }
                    adapter.notifyDataSetChanged();

                    relationList = obj.getSecond();
                    description = obj.getThird().getThird();
                    if (!description.equals("")){
                        TextView text = activity.findViewById(R.id.detail_text);
                        text.setVisibility(View.VISIBLE);
                        text.setText(description);
                    }
                    if (image != null){
                        ImageView imageView = activity.findViewById(R.id.detail_image);
                        imageView.setVisibility(View.VISIBLE);
                        imageView.setImageBitmap(image);
                    }

                    isFavorite = obj.getThird().getFirst();
                    favorite.setSelected(isFavorite);

                    hasQuestion = obj.getThird().getSecond();
                    exercise.setVisibility(hasQuestion ? View.VISIBLE: View.GONE);

                    // init chart webview
                    WebView relation = findViewById(R.id.detail_relation_chart);
                    WebSettings settings = relation.getSettings();
                    settings.setJavaScriptEnabled(true);

                    relation.setWebViewClient(new WebViewClient());
                    relation.loadUrl("file:///android_asset/relation_chart.html");
                    relation.addJavascriptInterface(activity, "Android");

                }
                else if (msg.what == Constant.DETAIL_RESPONSE_FAIL || msg.what == Constant.INSTANCE_LOAD_FAIL){
                    if (loadFromFile)
                        Request.getInfoByInstanceName(name, course, token, this);
                    else {
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

        // init favorite onclick
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (token.equals("")){
                    Intent intent = new Intent(activity, LogActivity.class);
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

        // init share onclick
        weiboAuthInfo = new AuthInfo(this, Constant.APP_KEY, Constant.REDIRECT_URL, Constant.SCOPE);
        weiboAPI = WBAPIFactory.createWBAPI(this);
        weiboAPI.registerApp(this, weiboAuthInfo);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WeiboMultiMessage message = new WeiboMultiMessage();
                TextObject textObject = new TextObject();
                textObject.text = Functional.getShareText(name, description);
                message.textObject = textObject;
                if (image != null){
                    ImageObject imageObject = new ImageObject();
                    imageObject.setImageData(image);
                    message.imageObject = imageObject;
                }
                weiboAPI.shareMessage(message, false);
            }
        });

        // set exercise link
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
        adapter = new DetailPropertyTableAdapter(new ArrayList<>());
        RecyclerView propertyRecycler = findViewById(R.id.detail_property_recycler);
        propertyRecycler.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        propertyRecycler.setAdapter(adapter);
        if (loadFromFile)
            InstanceIO.loadInstance(this, name, handler);
        else
            Request.getInfoByInstanceName(name, course, token, handler);

    }
    @JavascriptInterface
    public String getRelationData(){
        return relationList;
    }

    @JavascriptInterface
    public void startNewActivity(String name){
        if (!name.equals(this.name)) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("course", course);
            intent.putExtra("name", name);
            intent.putExtra("token", ((DataApplication) getApplicationContext()).token);
            intent.putExtra("load", InstanceIO.isInstanceExist(this, name));
            startActivity(intent);
        }
    }

    @Override
    public void onComplete() {
        Snackbar.make(this.getWindow().getDecorView().getRootView(), "分享成功！", Snackbar.LENGTH_SHORT)
                .setAction("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { }
                })
                .show();
    }

    @Override
    public void onError(UiError uiError) {
        Snackbar.make(this.getWindow().getDecorView().getRootView(), "错误：" + uiError.errorMessage, Snackbar.LENGTH_SHORT)
                .setAction("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) { }
                })
                .show();
    }

    @Override
    public void onCancel() { }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (weiboAPI != null) {
            weiboAPI.doResultIntent(data, this);
        }
    }
}