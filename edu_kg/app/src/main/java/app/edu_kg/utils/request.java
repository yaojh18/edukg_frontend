package app.edu_kg.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import okhttp3.*;

public class request{
    static OkHttpClient client = new OkHttpClient();

    public static String inputQuestion(String question){
        final String url = "http://localhost:8080/API/inputQuestion";
        RequestBody requestBody =
                new FormBody.Builder()
                        .add("inputQuestion", question)
                        .build();
        Request request =
                new Request.Builder().
                        url(url).
                        post(requestBody).
                        build();
        try{
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                return "请求格式有误";
            else{
                JSONObject json = new JSONObject(Objects.requireNonNull(response.body()).string());
                return json.getJSONObject("data").getString("result");
            }
        } catch (JSONException e) {
            return "返回格式有误";
        } catch (Exception e) {
            return "其他错误";
        }

    }
}

