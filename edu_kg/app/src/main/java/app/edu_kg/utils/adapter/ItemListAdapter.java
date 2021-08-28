package app.edu_kg.utils.adapter;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import app.edu_kg.R;
import app.edu_kg.utils.Constant;
import kotlin.Triple;

public class ItemListAdapter extends Adapter<ViewHolder> {

    private static class ItemHolder extends ViewHolder {
        TextView title;
        TextView content;
        ImageView image;

        ItemHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.first_text_view);
            content = (TextView) itemView.findViewById(R.id.second_text_view);
            image = (ImageView) itemView.findViewById(R.id.item_image_start);
        }
        void bind(ItemMessage message) {
            title.setText(message.label);
            if (message.category != null)
                content.setText(message.category);
            if (message.imageId != null)
                image.setImageResource(message.imageId);
        }
    }

    public static class ItemMessage {
        String label;
        String category;
        Integer imageId;
        String course;
        ItemMessage (String label, @Nullable String category, @Nullable String course, @Nullable Integer imageId){
            this.label = label;
            this.category = category;
            this.course = course;
            this.imageId = imageId;
        }
    }

    @FunctionalInterface
    interface RequestFunction {
        void request(String keyword, String course, Handler handler);
    }

    public List<ItemMessage> itemList;

    public ItemListAdapter(RequestFunction function, String keyword, String course) {
        itemList = new ArrayList<ItemMessage>();
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Constant.LIST_RESPONSE) {
                    Pair<Boolean, ArrayList<Triple<String, String, String>>> obj = (Pair<Boolean, ArrayList<Triple<String, String, String>>>) msg.obj;
                    if (obj.first) {
                        ArrayList<Triple<String, String, String>> newItemList = obj.second;
                        for (Triple<String, String, String> item : newItemList) {
                            itemList.add(new ItemMessage(item.getFirst(), item.getSecond(), item.getThird(), null));
                        }
                    } else {
                        itemList.add(new ItemMessage("请求出现错误", null, null, null));
                    }
                }
            }
        };
        function.request(keyword, course, handler);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemMessage message = itemList.get(position);
        ((ItemHolder) holder).bind(message);
    }
}