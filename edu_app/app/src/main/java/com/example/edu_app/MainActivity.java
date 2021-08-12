package com.example.edu_app;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        request();
    }

    private void request() {
        String username = "hanyu";
        String password = "123456";
        String xdomain = "https://www.cnblogs.com/juncaoit/p/11433670.html";
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                //.add("username", username)
                //.add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(xdomain)
                .get()
                .build();

        //异步请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("test", "wrong");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("test", (response.body().string()));
            }
        });
    }
}

