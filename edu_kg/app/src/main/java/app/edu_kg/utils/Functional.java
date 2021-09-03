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
                return "chinese";
        }
    }
    public static String subjEng2Che(String ch) {
        switch(ch) {
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
                return "语文";
        }
    }

    public static String sortMethodChe2Eng(String ch) {
        switch(ch) {
            case "默认":
                return "default";
            case "拼音":
                return "pinyin";
            case "热度":
                return "accessCount";
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
                return "热度";
            default:
                return "错误";
        }
    }

    public static String getShareText(){
        return "test";
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
