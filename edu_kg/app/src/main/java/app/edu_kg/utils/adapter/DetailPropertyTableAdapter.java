package app.edu_kg.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.*;

import java.io.Serializable;
import java.util.List;

import app.edu_kg.R;

public class DetailPropertyTableAdapter extends Adapter<ViewHolder> {

    private static class DetailHolder extends ViewHolder{
        TextView attribute;
        TextView value;

        DetailHolder(View itemView) {
            super(itemView);
            attribute = (TextView) itemView.findViewById(R.id.attribute);
            value = (TextView) itemView.findViewById(R.id.value);
        }
        void bind(DetailMessage message) {
            attribute.setText(message.attribute);
            value.setText(message.value);
        }
    }

    public static class DetailMessage implements Serializable {
        public String attribute;
        public String value;

        public DetailMessage(String attribute, String value){
            this.attribute = attribute;
            this.value = value;
        }
    }

    private List<DetailMessage> itemList;

    public DetailPropertyTableAdapter(List<DetailMessage> itemList) {
        this.itemList = itemList;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.detail_property, parent, false);
        return new DetailHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DetailMessage message = itemList.get(position);
        ((DetailHolder) holder).bind(message);
    }
}
