package app.edu_kg.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Functional {
    public static String subjChe2Eng(String ch) {
        switch(ch) {
            case "语文":
                return "chinese";
            case "英语":
                return "english";
            case "数学":
                return "math";
            case "物理":
                return "physics";
            case "化学":
                return "chemistry";
            case "生物":
                return "biology";
            case "地理":
                return "geo";
            case "历史":
                return "history";
            case "政治":
                return "politics";
            default:
                return "error";
        }
    }
    public static String subjEng2Che(String ch) {
        switch(ch) {
            case "chinese":
                return "语文";
            case "english":
                return "英语";
            case "math":
                return "数学";
            case "physics":
                return "物理";
            case "chemistry":
                return "化学";
            case "biology":
                return "生物";
            case "geo":
                return "地理";
            case "history":
                return "历史";
            case "politics":
                return "政治";
            default:
                return "错误";
        }
    }

    public static String sortMethodChe2Eng(String ch) {
        switch(ch) {
            case "默认":
                return "default";
            case "拼音":
                return "pinyin";
            case "访问数":
                return "accessCount";
            case "热门":
                return "popular";
            case "浏览过":
                return "history";
            case "已收藏":
                return "favorite";
            default:
                return "error";
        }
    }

    public static String sortMethodEnd2Che(String ch) {
        switch(ch) {
            case "default":
                return "默认";
            case "pinyin":
                return "拼音";
            case "accessCount":
                return "访问数";
            case "popular":
                return "热门";
            case "history":
                return "浏览过";
            case "favorite":
                return "已收藏";
            default:
                return "错误";
        }
    }

    public static String getShareText(String name, String description){
        if (description.length() > 50)
            description = description.substring(0, 50) + "...";
        return "#快乐学习APP#\n#" + name + "#\n" + description + "\n\n我在快乐学习APP学单词，今天又学到了新知识，快来下载看看吧！";
    }
    public static Bitmap getContactBitmapFromURI(Context context, Uri uri) {
        try {
            InputStream input = context.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            return null;
        }
    }
}
