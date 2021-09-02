package app.edu_kg.utils;

import android.content.Context;
import android.util.Log;
import android.os.Handler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;


public class InstanceIO {
    public static void saveInstance(Context context, Object json, String name) {
        try {
            FileOutputStream out = context.openFileOutput(name + ".json", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(json);
            os.close();
            out.close();
        } catch (IOException e) {
            Log.v("储存失败","储存失败");
        }
    }

    public static void loadInstance(Context context, String name, Handler handler) {
        try {
            FileInputStream in = context.openFileInput(name + ".json");
            ObjectInputStream is = new ObjectInputStream(in);
            Object res = is.readObject();
            is.close();
            in.close();
            handler.sendMessage(handler.obtainMessage(Constant.DETAIL_RESPONSE_SUCCESS, res));
        } catch (Exception e) {
            handler.sendMessage(handler.obtainMessage(Constant.DETAIL_RESPONSE_FAIL, ""));
        }
    }

    public static boolean isInstanceExist(Context context, String name) {
        String[] fileList = context.fileList();
        return Arrays.asList(fileList).contains(name + ".json");
    }
}
