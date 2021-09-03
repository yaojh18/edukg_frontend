package app.edu_kg.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;


public class InstanceIO {
    public static void saveInstance(Context context, Object json, String name) {
        try {
            FileOutputStream out = context.openFileOutput(name + ".dt", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(out);
            os.writeObject(json);
            os.close();
            out.close();
        } catch (Exception e) {
            Log.v("Error", e.toString());
        }
    }


    public static void loadInstance(Context context, String name, Handler handler) {
        try {
            FileInputStream in = context.openFileInput(name + ".dt");
            ObjectInputStream is = new ObjectInputStream(in);
            Object res = is.readObject();
            is.close();
            in.close();
            handler.sendMessage(handler.obtainMessage(Constant.INSTANCE_LOAD_SUCCESS, res));
        } catch (Exception e) {
            handler.sendMessage(handler.obtainMessage(Constant.INSTANCE_LOAD_FAIL, ""));
        }
    }

    public static Object loadInstanceWithoutHandler(Context context, String name) {
        try {
            FileInputStream in = context.openFileInput(name + ".dt");
            ObjectInputStream is = new ObjectInputStream(in);
            Object res = is.readObject();
            is.close();
            in.close();
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isInstanceExist(Context context, String name) {
        String[] fileList = context.fileList();
        return Arrays.asList(fileList).contains(name + ".dt");
    }

    public static void saveBitmap(Context context, Bitmap bitmap, String name) {
        try {
            FileOutputStream out = context.openFileOutput(name + ".dt", Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSY, 0, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            Log.v("Error", e.toString());
        }
    }

    public static Bitmap loadBitmap(Context context, String name) {
        try {
            FileInputStream in = context.openFileInput(name + ".dt");
            Bitmap bitmap = BitmapFactory.decodeStream(in);
            in.close();
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }
}
