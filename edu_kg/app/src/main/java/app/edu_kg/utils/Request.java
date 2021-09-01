package app.edu_kg.utils;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import kotlin.Pair;
import kotlin.Triple;
import okhttp3.*;

public class Request {
    final static OkHttpClient client = new OkHttpClient();
    final static String ip = "183.172.241.137";

    public static void inputQuestion(String question, @Nullable String course, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/API/inputQuestion";
                HttpUrl.Builder urlQuery = HttpUrl.parse(url).newBuilder()
                                .addQueryParameter("inputQuestion", question);
                if (course != null)
                    urlQuery.addQueryParameter("course", course);
                okhttp3.Request request = new okhttp3.Request.Builder()
                                .url(urlQuery.build())
                                .get()
                                .build();

                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    String message = json.getJSONObject("data").getString("result");
                    handler.sendMessage(handler.obtainMessage(Constant.MESSAGE_LIST_RESPONSE, message));
                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.MESSAGE_LIST_RESPONSE, "服务器错误!"));
                }
            }
        }).start();
    }

    public static void Login(String username, String password, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/user/login";
                RequestBody requestBody = new FormBody.Builder()
                                .add("userName", username)
                                .add("password", password)
                                .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                                .url(url)
                                .post(requestBody)
                                .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (!response.isSuccessful())
                        handler.sendMessage(handler.obtainMessage(Constant.LOGIN_RESPONSE_FAIL, json.getString("msg")));
                    else
                        handler.sendMessage(handler.obtainMessage(Constant.LOGIN_RESPONSE_SUCCESS, json.getString("token")));

                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.LOGIN_RESPONSE_FAIL, "未知错误"));
                }
            }
        }).start();
    }

    public static void Register(String username, String password, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/user/register";
                RequestBody requestBody = new FormBody.Builder()
                                .add("userName", username)
                                .add("password", password)
                                .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                                .url(url)
                                .post(requestBody)
                                .build();

                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (!response.isSuccessful())
                        handler.sendMessage(handler.obtainMessage(Constant.REGISTER_RESPONSE_FAIL, json.getString("msg")));
                    else
                        handler.sendMessage(handler.obtainMessage(Constant.REGISTER_RESPONSE_SUCCESS, json.getString("token")));

                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.REGISTER_RESPONSE_FAIL, "未知错误"));
                }
            }
        }).start();
    }

    public static void changePassword(String oldPassword, String newPassword, String token, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/user/changePassword";
                RequestBody requestBody = new FormBody.Builder()
                                .add("token", token)
                                .add("oldPassword", oldPassword)
                                .add("newPassword", newPassword)
                                .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                                .url(url)
                                .post(requestBody)
                                .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (!response.isSuccessful())
                        handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE_FAIL,json.getString("msg")));
                    else
                        handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE_SUCCESS,json.getString("msg")));

                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE_FAIL, "未知错误"));
                }
            }
        }).start();
    }

    public static void getUserHistory(String token, Handler handler) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/user/getHistoriesList";
                HttpUrl urlQuery = HttpUrl.parse(url).newBuilder()
                                .addQueryParameter("token", token)
                                .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                                .url(urlQuery)
                                .get()
                                .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (response.isSuccessful()){
                        JSONArray data = json.getJSONArray("data");
                        ArrayList<Triple<String, String, String>> res = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++){
                            JSONObject item = data.getJSONObject(i);
                            res.add(new Triple<>(item.getString("instanceName"), item.getString("course"), null));
                        }
                        handler.sendMessage(handler.obtainMessage(Constant.LIST_RESPONSE_SUCCESS, res));
                    }
                    else throw new Exception();

                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.LIST_RESPONSE_FAIL, ""));
                }
            }
        }).start();
    }

    public static void getUserCollection(String token, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/user/getFavoritesList";
                HttpUrl urlQuery = HttpUrl.parse(url).newBuilder()
                        .addQueryParameter("token", token)
                        .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(urlQuery)
                        .get()
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (response.isSuccessful()){
                        JSONArray data = json.getJSONArray("data");
                        ArrayList<Triple<String, String, String>> res = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++){
                            JSONObject item = data.getJSONObject(i);
                            res.add(new Triple<>(item.getString("instanceName"), item.getString("course"), null));
                        }
                        handler.sendMessage(handler.obtainMessage(Constant.LIST_RESPONSE_SUCCESS, res));
                    }
                    else throw new Exception();

                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.LIST_RESPONSE_FAIL, ""));
                }
            }
        }).start();
    }

    public static void getExerciseRecommendation(String token, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/user/changePassword";
                RequestBody requestBody = new FormBody.Builder()
                                .add("token", token)
                                .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                                .url(url)
                                .post(requestBody)
                                .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    handler.sendMessage(handler.obtainMessage(Constant.QUESTION_LIST_RESPONSE, json));
                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.QUESTION_LIST_RESPONSE, "error"));
                }
            }
        }).start();
    }

    public static void getHomeList(String course, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/API/homeList";
                HttpUrl urlQuery = HttpUrl.parse(url)
                                .newBuilder()
                                .addQueryParameter("course", course)
                                .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                                .url(urlQuery)
                                .get()
                                .build();

                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (response.isSuccessful()){
                        JSONArray data = json.getJSONObject("data").getJSONArray("result");
                        ArrayList<Triple<String, String, String>> res = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++){
                            JSONObject item = data.getJSONObject(i);
                            res.add(new Triple<>(item.getString("label"), item.getString("course"), item.getString("category")));
                        }
                        handler.sendMessage(handler.obtainMessage(Constant.HOME_ENTITY_RESPONSE_SUCCESS, res));
                    }
                    else throw new Exception();
                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.HOME_ENTITY_RESPONSE_FAIL, ""));
                }
            }
        }).start();
    }

    public static void getInfoByInstanceName(String name, String course, String token, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/API/infoByInstanceName";
                HttpUrl.Builder urlQuery = HttpUrl.parse(url).newBuilder()
                        .addQueryParameter("name", name)
                        .addQueryParameter("course", course);

                if (!token.equals(""))
                    urlQuery.addQueryParameter("token", token);
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(urlQuery.build())
                        .get()
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (response.isSuccessful()){
                        JSONArray data = json.getJSONObject("data").getJSONArray("property");
                        ArrayList<Pair<String, String>> property = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++){
                            JSONObject item = data.getJSONObject(i);
                            property.add(new Pair<>(item.getString("predicateLabel"), item.getString("object")));
                        }

                        data = json.getJSONObject("data").getJSONArray("relationship");
                        ArrayList<Pair<String, String>> relationship = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++){
                            JSONObject item = data.getJSONObject(i);
                            relationship.add(new Pair<>(item.getString("predicate_label"), item.getString("object_label")));
                        }

                        handler.sendMessage(handler.obtainMessage(Constant.HOME_ENTITY_RESPONSE_SUCCESS, property));
                    }
                    else throw new Exception();
                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.HOME_ENTITY_RESPONSE_FAIL, ""));
                }
            }
        }).start();
    }

    public static void getInstanceList(String searchKey, String course, String order, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/API/instanceList";
                HttpUrl urlQuery =
                        HttpUrl.parse(url).newBuilder().
                                addQueryParameter("searchKey", searchKey).
                                addQueryParameter("course", course).
                                addQueryParameter("order", order)
                                .build();
                okhttp3.Request request =
                        new okhttp3.Request.Builder().
                                url(urlQuery).
                                get().
                                build();

                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    handler.sendMessage(handler.obtainMessage(Constant.INSTANCE_LIST_RESPONSE, json));
                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.INSTANCE_LIST_RESPONSE, "error"));
                }
            }
        }).start();
    }

    public static void getQuestionList(String name, String course, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/API/questionList";
                HttpUrl urlQuery =
                        HttpUrl.parse(url).newBuilder().
                                addQueryParameter("name", name).
                                addQueryParameter("course", course)
                                .build();
                okhttp3.Request request =
                        new okhttp3.Request.Builder().
                                url(urlQuery).
                                get().
                                build();

                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    handler.sendMessage(handler.obtainMessage(Constant.QUESTION_LIST_RESPONSE, json));
                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.QUESTION_LIST_RESPONSE, "error"));
                }
            }
        }).start();
    }

    public static void getLinkInstance(String context, String course, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/API/linkInstance";
                HttpUrl urlQuery =
                        HttpUrl.parse(url).newBuilder().
                                addQueryParameter("context", context).
                                addQueryParameter("course", course)
                                .build();
                okhttp3.Request request =
                        new okhttp3.Request.Builder().
                                url(urlQuery).
                                get().
                                build();

                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    handler.sendMessage(handler.obtainMessage(Constant.LINK_INSTANCE_RESPONSE, json));
                    Log.e("test", "getLinkInstance successful");
                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.LINK_INSTANCE_RESPONSE, "error"));
                    Log.e("test", "getLinkInstance fail");
                }
            }
        }).start();
    }


}

