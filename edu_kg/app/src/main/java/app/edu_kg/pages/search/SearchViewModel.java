package app.edu_kg.pages.search;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.edu_kg.R;
import app.edu_kg.pages.result.ResultActivity;
import app.edu_kg.pages.test.TestActivity;
import app.edu_kg.utils.Functional;

public class SearchViewModel extends ViewModel {
    HistoryListAdapter adapter;
    static int select_type = 0;
    public SearchViewModel() {
        adapter = new HistoryListAdapter();
    }
}

class HistoryListAdapter extends RecyclerView.Adapter {

    private static class HistoryHolder extends RecyclerView.ViewHolder {
        TextView firstText;
        TextView secondText;
        ImageView endIcon;
        HistoryHolder(View itemView) {
            super(itemView);
            firstText = (TextView) itemView.findViewById(R.id.first_text_view);
            secondText = (TextView) itemView.findViewById(R.id.second_text_view);
            endIcon = itemView.findViewById(R.id.item_image_end);
            endIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String searchInput = firstText.getText().toString();
                    String[] setting = secondText.getText().toString().split(" ");
                    String type = setting[0];
                    String course = setting[1];
                    String order = setting[2];
                    Intent intent;
                    if(type.equals("试题")) {
                        intent = new Intent(v.getContext(), TestActivity.class);
                    }
                    else if(type.equals("提纲")) {
                        intent = new Intent(v.getContext(), ResultActivity.class);
                    }
                    else {
                        intent = new Intent(v.getContext(), ResultActivity.class);
                    }
                    intent.putExtra("searchInput", searchInput);
                    intent.putExtra("type", type);
                    intent.putExtra("course", course);
                    intent.putExtra("order", order);
                    Log.e("test", searchInput + setting[0] + setting[1] + setting[2]);
                    v.getContext().startActivity(intent);
                }
            });

        }

        void bind(app.edu_kg.pages.search.HistoryListAdapter.History history) {
            firstText.setText(history.searchKey);
            secondText.setText(history.type+" "+history.course+" "+history.order);
        }
    }


    private static class History {
        String searchKey;
        String type;
        String course;
        String order;
        History (String searchKey, String type, String course, String order) {
            this.searchKey = searchKey;
            this.type = type;
            this.course = course;
            this.order = order;
        }
    }

    private List<HistoryListAdapter.History> historyList;

    public HistoryListAdapter() {
        historyList = new ArrayList<History>();
    }

    public void addHistory(String searchKey, String type, String course, String order) {
        historyList.add(new HistoryListAdapter.History(searchKey, type, course, order));
    }

    public void clearHistory() {
        historyList.clear();
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoryListAdapter.History message = historyList.get(position);
        ((HistoryListAdapter.HistoryHolder) holder).bind(message);
    }
}