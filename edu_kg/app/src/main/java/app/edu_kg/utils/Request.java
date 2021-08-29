package app.edu_kg.utils;

import android.os.Handler;
import android.util.Pair;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import javax.net.ssl.HandshakeCompletedEvent;

import kotlin.Triple;
import okhttp3.*;

public class Request {
    final static OkHttpClient client = new OkHttpClient();
    final static String ip = "183.172.143.172";

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
                        handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE_FAIL,json.getString("msg")));
                    else
                        handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE_SUCCESS,json.getString("msg")));

                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE_FAIL, "未知错误"));
                }
            }
        }).start();
    }

    public static void userHistory(String token, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/user/changePassword";
                RequestBody requestBody =
                        new FormBody.Builder()
                                .add("token", token)
                                .build();
                okhttp3.Request request =
                        new okhttp3.Request.Builder().
                                url(url).
                                post(requestBody).
                                build();
//                try {
//                    Response response = client.newCall(request).execute();
//                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
//                    if (!response.isSuccessful())
//                        handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE,
//                                new Pair<Boolean, String>(false, json.getString("msg"))));
//                    else
//                        handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE,
//                                new Pair<Boolean, String>(true, json.getString("msg"))));
//
//                } catch (Exception e) {
//                    handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE,
//                            new Pair<Boolean, String>(false, "其他错误")));
//                }
                ArrayList<Triple<String, String, String>> result = new ArrayList<Triple<String, String, String>>();
                result.add(new Triple<>("test1", "test1", "test1"));
                result.add(new Triple<>("test2", "test2", "test2"));
                handler.sendMessage(handler.obtainMessage(Constant.LIST_RESPONSE_SUCCESS, result));
            }
        }).start();
    }

    public static void userCollection(String token, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/user/changePassword";
                RequestBody requestBody =
                        new FormBody.Builder()
                                .add("token", token)
                                .build();
                okhttp3.Request request =
                        new okhttp3.Request.Builder().
                                url(url).
                                post(requestBody).
                                build();
//                try {
//                    Response response = client.newCall(request).execute();
//                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
//                    if (!response.isSuccessful())
//                        handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE,
//                                new Pair<Boolean, String>(false, json.getString("msg"))));
//                    else
//                        handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE,
//                                new Pair<Boolean, String>(true, json.getString("msg"))));
//
//                } catch (Exception e) {
//                    handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE,
//                            new Pair<Boolean, String>(false, "其他错误")));
//                }
                ArrayList<Triple<String, String, String>> result = new ArrayList<Triple<String, String, String>>();
                result.add(new Triple<>("test1", "test1", "test1"));
                result.add(new Triple<>("test2", "test2", "test2"));
                handler.sendMessage(handler.obtainMessage(Constant.LIST_RESPONSE_SUCCESS, result));
            }
        }).start();
    }

    public static void exerciseRecommendation(String token, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/user/changePassword";
                RequestBody requestBody =
                        new FormBody.Builder()
                                .add("token", token)
                                .build();
                okhttp3.Request request =
                        new okhttp3.Request.Builder().
                                url(url).
                                post(requestBody).
                                build();
//                try {
//                    Response response = client.newCall(request).execute();
//                    JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
//                    if (!response.isSuccessful())
//                        handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE,
//                                new Pair<Boolean, String>(false, json.getString("msg"))));
//                    else
//                        handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE,
//                                new Pair<Boolean, String>(true, json.getString("msg"))));
//
//                } catch (Exception e) {
//                    handler.sendMessage(handler.obtainMessage(Constant.MODIFY_RESPONSE,
//                            new Pair<Boolean, String>(false, "其他错误")));
//                }
                ArrayList<Triple<String, String, String>> result = new ArrayList<Triple<String, String, String>>();
                result.add(new Triple<>("test1", "test1", "test1"));
                result.add(new Triple<>("test2", "test2", "test2"));
                handler.sendMessage(handler.obtainMessage(Constant.LIST_RESPONSE_SUCCESS, result));
            }
        }).start();
    }

    public static void getHomeList(String course, Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String url = "http://" + ip + ":8080/API/homeList";
                HttpUrl urlQuery =
                        HttpUrl.parse(url).newBuilder().
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
                    handler.sendMessage(handler.obtainMessage(Constant.HOME_ENTITY_RESPONSE, json));
                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.HOME_ENTITY_RESPONSE, "error"));
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
                } catch (Exception e) {
                    handler.sendMessage(handler.obtainMessage(Constant.LINK_INSTANCE_RESPONSE, "error"));
                }
            }
        }).start();
    }


}

