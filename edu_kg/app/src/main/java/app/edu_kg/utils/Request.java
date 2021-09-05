package app.edu_kg.utils;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import app.edu_kg.utils.adapter.DetailPropertyTableAdapter;
import app.edu_kg.utils.adapter.ItemListAdapter;
import kotlin.Pair;
import kotlin.Triple;
import okhttp3.*;

public class Request {
    final static OkHttpClient client = new OkHttpClient();
    final static String ip = "183.172.240.128";

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

    public static void login(String username, String password, Handler handler) {
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

    public static void register(String username, String password, Handler handler) {
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

    public static void getUserHistoryList(String token, Handler handler) {
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
                        ArrayList<ItemListAdapter.ItemMessage> res = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++){
                            JSONObject item = data.getJSONObject(i);
                            res.add(new ItemListAdapter.ItemMessage(item.getString("instanceName"), item.getString("course"), null));
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

    public static void getFavoritesList(String token, Handler handler) {
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
                        ArrayList<ItemListAdapter.ItemMessage> res = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++){
                            JSONObject item = data.getJSONObject(i);
                            res.add(new ItemListAdapter.ItemMessage(item.getString("instanceName"), item.getString("course"), null));
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
                final String url = "http://" + ip + ":8080/user/recommendQuestion";
                HttpUrl urlQuery = HttpUrl.parse(url)
                                .newBuilder()
                                .addQueryParameter("token", token)
                                .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                                .url(urlQuery)
                                .get()
                                .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (response.isSuccessful())
                        handler.sendMessage(handler.obtainMessage(Constant.QUESTION_LIST_RESPONSE, json));
                    else
                        throw new Exception();
                } catch (Exception e) {
                    Log.e("testa", e.toString());
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
                        ArrayList<ItemListAdapter.ItemMessage> res = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++){
                            JSONObject item = data.getJSONObject(i);
                            res.add(new ItemListAdapter.ItemMessage(item.getString("label"), item.getString("course"), item.getString("category")));
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
                        ArrayList<DetailPropertyTableAdapter.DetailMessage> property = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++){
                            JSONObject item = data.getJSONObject(i);
                            property.add(new DetailPropertyTableAdapter.DetailMessage(item.getString("predicateLabel"), item.getString("object")));
                        }
                        property.sort(new Comparator<DetailPropertyTableAdapter.DetailMessage>() {
                            @Override
                            public int compare(DetailPropertyTableAdapter.DetailMessage t1, DetailPropertyTableAdapter.DetailMessage t2) {
                                if (t1.value.length() != t2.value.length())
                                    return t1.value.length() - t2.value.length();
                                else
                                    return t1.attribute.hashCode() - t2.attribute.hashCode();
                            }
                        });
                        JSONObject relation = new JSONObject();
                        JSONArray nodes = new JSONArray();
                        JSONObject self = new JSONObject();
                        self.put("name", name);
                        self.put("category", 0);
                        nodes.put(self);
                        JSONArray links = new JSONArray();

                        Map<String, Integer> unique = new HashMap<>();
                        data = json.getJSONObject("data").getJSONArray("relationship");
                        for (int i = 0; i < data.length(); i++){
                            JSONObject item = data.getJSONObject(i);
                            if (item.has("object_label")){
                                JSONObject link = new JSONObject();
                                link.put("source", name);
                                link.put("target", item.getString("object_label"));
                                link.put("name", item.getString("predicate_label"));

                                if (!unique.containsKey(item.getString("object_label"))){
                                    JSONObject node = new JSONObject();
                                    node.put("name", item.getString("object_label"));
                                    node.put("category", 1);
                                    nodes.put(node);
                                    unique.put(item.getString("object_label"), 1);
                                }
                                else {
                                    int cnt = unique.get(item.getString("object_label"));
                                    JSONObject style = new JSONObject("{normal:{curveness:" + String.valueOf(cnt * 0.2) + "}}");
                                    link.put("lineStyle", style);
                                    unique.put(item.getString("object_label"), cnt + 1);
                                }
                                links.put(link);
                            }
                            else {
                                JSONObject link = new JSONObject();
                                link.put("source", item.getString("subject_label"));
                                link.put("target", name);
                                link.put("name", item.getString("predicate_label"));

                                if (!unique.containsKey(item.getString("subject_label"))){
                                    JSONObject node = new JSONObject();
                                    node.put("name", item.getString("subject_label"));
                                    node.put("category", 1);
                                    nodes.put(node);
                                    unique.put(item.getString("subject_label"), 1);
                                }
                                else {
                                    int cnt = unique.get(item.getString("subject_label"));
                                    JSONObject style = new JSONObject("{normal:{curveness:" + String.valueOf(cnt * 0.4) + "}}");
                                    link.put("lineStyle", style);
                                    unique.put(item.getString("subject_label"), cnt + 1);
                                }
                                links.put(link);
                            }
                        }

                        relation.put("data", nodes);
                        relation.put("links", links);

                        handler.sendMessage(handler.obtainMessage(Constant.DETAIL_RESPONSE_SUCCESS,
                                new Triple<>(property, relation.toString(), new Pair<>(json.getJSONObject("data").getBoolean("isFavorite"), json.getJSONObject("data").getBoolean("hasQuestion")))));
                    }
                    else throw new Exception();
                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.DETAIL_RESPONSE_FAIL, ""));
                }
            }
        }).start();
    }

    public static void addFavorite(String token, String name, String course, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/user/addFavorites";
                RequestBody requestBody = new FormBody.Builder()
                        .add("token", token)
                        .add("name", name)
                        .add("course", course)
                        .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (!response.isSuccessful())
                        handler.sendMessage(handler.obtainMessage(Constant.ADD_FAVORITE_RESPONSE_FAIL, json.getString("msg")));
                    else
                        handler.sendMessage(handler.obtainMessage(Constant.ADD_FAVORITE_RESPONSE_SUCCESS, ""));

                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.ADD_FAVORITE_RESPONSE_FAIL, "未知错误"));
                }
            }
        }).start();
    }

    public static void deleteFavorite(String token, String name, String course, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/user/deleteFavorites";
                RequestBody requestBody = new FormBody.Builder()
                        .add("token", token)
                        .add("name", name)
                        .add("course", course)
                        .build();
                okhttp3.Request request = new okhttp3.Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                    if (!response.isSuccessful())
                        handler.sendMessage(handler.obtainMessage(Constant.ADD_FAVORITE_RESPONSE_FAIL, json.getString("msg")));
                    else
                        handler.sendMessage(handler.obtainMessage(Constant.ADD_FAVORITE_RESPONSE_SUCCESS, ""));

                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.ADD_FAVORITE_RESPONSE_FAIL, "未知错误"));
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
                                addQueryParameter("sortMethod", order)
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
                    if (response.isSuccessful())
                        handler.sendMessage(handler.obtainMessage(Constant.QUESTION_LIST_RESPONSE, json));
                    else
                        throw new Exception();
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

    public static void getOutline(String searchKey, String course, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/API/getOutline";
                HttpUrl urlQuery =
                        HttpUrl.parse(url).newBuilder().
                                addQueryParameter("searchKey", searchKey).
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
                    handler.sendMessage(handler.obtainMessage(Constant.OUTLINE_RESPONSE, json));
                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.OUTLINE_RESPONSE, "error"));
                }
            }
        }).start();
    }


}

