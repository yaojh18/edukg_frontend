package app.edu_kg.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.*;

import java.io.Serializable;
import java.util.List;

import app.edu_kg.R;

public class ItemListAdapter extends Adapter<ViewHolder> {

    private static class ItemHolder extends ViewHolder implements View.OnClickListener{
        TextView title;
        TextView content;
        ImageView image;
        TextView secondTitle;
        OnItemClickListener onItemClickListener;

        ItemHolder(View itemView, OnItemClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(this);
            onItemClickListener = listener;
            title = (TextView) itemView.findViewById(R.id.first_text_view);
            content = (TextView) itemView.findViewById(R.id.second_text_view);
            image = (ImageView) itemView.findViewById(R.id.item_image_start);
            secondTitle = (TextView) itemView.findViewById(R.id.second_title);
        }
        void bind(ItemMessage message) {
            title.setText(message.label);
            if (message.category != null)
                content.setText(message.category);
            if (message.imageId != null)
                image.setImageResource(message.imageId);
            if (message.titleColor != null) {
                title.setTextColor(message.titleColor);
            }
            if (message.secondTitle != null) {
                secondTitle.setText(message.secondTitle);

                if (message.showSecondTitle) {
                    secondTitle.setVisibility(View.VISIBLE);
                }
                else {
                    secondTitle.setVisibility(View.GONE);
                }
            }
            title.setSelected(message.isChecked);
            content.setSelected(message.isChecked);
        }

        @Override
        public void onClick(View view) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public static class ItemMessage implements Serializable {
        public String label;
        public String category;
        public Integer imageId;
        public String course;
        public boolean isChecked;
        public Integer titleColor = null;
        public String secondTitle = null;
        public boolean showSecondTitle = false;
        static final int IMAGE_VIEW = 0;
        static final int TEXT_VIEW = 1;
        static final int OUTLINE_VIEW = 2;

        public ItemMessage(String label, String course, @Nullable String category){
            this.label = label;
            this.category = category;
            this.course = course;
            this.imageId = null;
            this.isChecked = false;
        }

        public ItemMessage(String label, String course, @Nullable String category, @Nullable Integer imageId, boolean isChecked){
            this.label = label;
            this.category = category;
            this.course = course;
            this.imageId = imageId;
            this.isChecked = isChecked;
        }

        public ItemMessage(String label, String course, @Nullable String category, @Nullable Integer imageId, boolean isChecked, int titleColor){
            this.label = label;
            this.category = category;
            this.course = course;
            this.imageId = imageId;
            this.isChecked = isChecked;
            this.titleColor = titleColor;
        }

        public ItemMessage(String label, String course, @Nullable String category, @Nullable Integer imageId, boolean isChecked, String secondTitle) {
            this.label = label;
            this.category = category;
            this.course = course;
            this.imageId = imageId;
            this.isChecked = isChecked;
            this.secondTitle = secondTitle;
        }
    }

    private List<ItemMessage> itemList;
    private OnItemClickListener onItemClickListener;


    public ItemListAdapter(List<ItemMessage> itemList, OnItemClickListener onItemClickListener) {
        this.itemList = itemList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        ItemMessage message = itemList.get(position);
        if (message.imageId != null)
            return ItemMessage.IMAGE_VIEW;
        else if(message.secondTitle != null) {
            return ItemMessage.OUTLINE_VIEW;
        }
        else
            return ItemMessage.TEXT_VIEW;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ItemMessage.IMAGE_VIEW)
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item_with_image, parent, false);
        else if (viewType == ItemMessage.OUTLINE_VIEW) {
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.outline_item, parent, false);
        }
        else
            view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.list_item, parent, false);
        return new ItemHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemMessage message = itemList.get(position);
        ((ItemHolder) holder).bind(message);
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}