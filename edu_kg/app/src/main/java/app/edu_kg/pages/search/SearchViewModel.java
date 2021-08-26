package app.edu_kg.pages.search;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

import app.edu_kg.R;
import app.edu_kg.pages.result.ResultActivity;

public class SearchViewModel extends ViewModel {
    HistoryListAdapter adapter;

    public SearchViewModel() {
        adapter = new HistoryListAdapter();
    }
}

class HistoryListAdapter extends RecyclerView.Adapter {

    private static class HistoryHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        HistoryHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.message_card_right_text);
            messageText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] history = messageText.getText().toString().split(" ");
                    Intent intent = new Intent(v.getContext(), ResultActivity.class);
                    intent.putExtra("searchKey", history[0]);
                    intent.putExtra("type", history[1]);
                    intent.putExtra("course", history[2]);
                    intent.putExtra("order", history[3]);
                    v.getContext().startActivity(intent);
                }
            });
        }

        void bind(app.edu_kg.pages.search.HistoryListAdapter.History history) {
            messageText.setText(history.text);
        }
    }


    private static class History {
        String text;
        History (String text){
            this.text = text;
        }
    }

    private List<HistoryListAdapter.History> historyList;

    public HistoryListAdapter() {
        historyList = new ArrayList<History>();
    }

    public void addHistory(String text) {
        historyList.add(new HistoryListAdapter.History(text));
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
                .inflate(R.layout.message_card_right, parent, false);
        return new HistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoryListAdapter.History message = historyList.get(position);
        ((HistoryListAdapter.HistoryHolder) holder).bind(message);
    }
}