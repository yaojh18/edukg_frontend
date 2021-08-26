package app.edu_kg.utils;

import android.os.Handler;
import android.util.Pair;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.Objects;

import javax.net.ssl.HandshakeCompletedEvent;

import okhttp3.*;

public class Request {
    final static OkHttpClient client = new OkHttpClient();
    final static String ip = "183.172.242.16";

    public static void inputQuestion(String question, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/API/inputQuestion";
                HttpUrl urlQuery =
                        HttpUrl.parse(url).newBuilder().
                                addQueryParameter("inputQuestion", question)
                                .build();
                okhttp3.Request request =
                        new okhttp3.Request.Builder().
                                url(urlQuery).
                                get().
                                build();

                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    String message = json.getJSONObject("data").getString("result");
                    handler.sendMessage(handler.obtainMessage(Constant.MESSAGE_LIST_RESPONSE, message));
                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.MESSAGE_LIST_RESPONSE, "其他错误"));
                }
            }
        }).start();
    }

    public static void Login(String username, String password, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/user/login";
                RequestBody requestBody =
                        new FormBody.Builder()
                                .add("userName", username)
                                .add("password", password)
                                .build();
                okhttp3.Request request =
                        new okhttp3.Request.Builder().
                                url(url).
                                post(requestBody).
                                build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (!response.isSuccessful())
                        handler.sendMessage(handler.obtainMessage(Constant.LOGIN_RESPONSE,
                                new Pair<Boolean, String>(false, json.getString("msg"))));
                    else
                        handler.sendMessage(handler.obtainMessage(Constant.REGISTER_RESPONSE,
                                new Pair<Boolean, String>(true, json.getString("token"))));

                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.REGISTER_RESPONSE,
                            new Pair<Boolean, String>(true, "其他错误")));
                }
            }
        }).start();
    }

    public static void Register(String username, String password, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/user/register";
                RequestBody requestBody =
                        new FormBody.Builder()
                                .add("userName", username)
                                .add("password", password)
                                .build();
                okhttp3.Request request =
                        new okhttp3.Request.Builder().
                                url(url).
                                post(requestBody).
                                build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (!response.isSuccessful())
                        handler.sendMessage(handler.obtainMessage(Constant.REGISTER_RESPONSE,
                                new Pair<Boolean, String>(false, json.getString("msg"))));
                    else
                        handler.sendMessage(handler.obtainMessage(Constant.REGISTER_RESPONSE,
                                new Pair<Boolean, String>(true, json.getString("token"))));

                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.REGISTER_RESPONSE,
                            new Pair<Boolean, String>(false, "其他错误")));
                }
            }
        }).start();
    }

    public static void changePassword(String oldPassword, String newPassword, String token, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/user/changePassword";
                RequestBody requestBody =
                        new FormBody.Builder()
                                .add("token", token)
                                .add("oldPassword", oldPassword)
                                .add("newPassword", newPassword)
                                .build();
                okhttp3.Request request =
                        new okhttp3.Request.Builder().
                                url(url).
                                post(requestBody).
                                build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (!response.isSuccessful())
                        handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE,
                                new Pair<Boolean, String>(false, json.getString("msg"))));
                    else
                        handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE,
                                new Pair<Boolean, String>(true, json.getString("msg"))));

                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE,
                            new Pair<Boolean, String>(false, "其他错误")));
                }
            }
        }).start();
    }


    @Nullable
    public static JSONObject getSubjectEntityList(String subject) {
        final String url = "http://localhost:8080/API/homeList";
        RequestBody requestBody =
                new FormBody.Builder()
                        .add("course", subject)
                        .build();
        okhttp3.Request request =
                new okhttp3.Request.Builder().
                        url(url).
                        post(requestBody).
                        build();
        try{
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                return null;
            else{
                JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                return json;
            }
        } catch (Exception e) {
            return null;
        }
    }


    public static JSONObject getSearchResult(String searchStr, String subject, String order) {
        final String url = "http://localhost:8080/API/instanceList";
        RequestBody requestBody =
                new FormBody.Builder()
                        .add("searchKey ", searchStr)
                        .add("course", subject)

                        .build();
        okhttp3.Request request =
                new okhttp3.Request.Builder().
                        url(url).
                        post(requestBody).
                        build();
        try{
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                return null;
            else{
                JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static JSONObject getEntityDetail(String entityName) {
        final String url = "http://localhost:8080/API/instanceList";
        RequestBody requestBody =
                new FormBody.Builder()

                        .build();
        okhttp3.Request request =
                new okhttp3.Request.Builder().
                        url(url).
                        post(requestBody).
                        build();
        try{
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                return null;
            else{
                JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}

