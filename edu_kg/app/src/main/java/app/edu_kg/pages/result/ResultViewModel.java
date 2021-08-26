package app.edu_kg.pages.result;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.edu_kg.R;

public class ResultViewModel extends ViewModel {
    ResultListAdapter adapter;

    public ResultViewModel() {
        adapter = new ResultListAdapter();
    }
}

class ResultListAdapter extends RecyclerView.Adapter {

    private static class ResultHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        ResultHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.message_card_right_text);
        }

        void bind(app.edu_kg.pages.result.ResultListAdapter.Result result) {
            messageText.setText(result.text);
        }
    }


    private static class Result {
        String text;
        Result (String text){
            this.text = text;
        }
    }

    private List<ResultListAdapter.Result> resultList;

    public ResultListAdapter() {
        resultList = new ArrayList<Result>();
    }

    public void addResult(String text) {
        resultList.add(new ResultListAdapter.Result(text));
    }

    public void clearResult() {
        resultList.clear();
    }

    @Override
    public int getItemCount() {
        return resultList.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_card_right, parent, false);
        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ResultListAdapter.Result message = resultList.get(position);
        ((ResultListAdapter.ResultHolder) holder).bind(message);
    }
}