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
        TextView firstText;
        TextView secondText;
        ResultHolder(View itemView) {
            super(itemView);
            firstText = (TextView) itemView.findViewById(R.id.first_text_view);
            secondText = (TextView) itemView.findViewById(R.id.second_text_view);
        }

        void bind(app.edu_kg.pages.result.ResultListAdapter.Result result) {
            firstText.setText(result.entity);
            secondText.setText(result.category + " " + result.course);
        }
    }


    private static class Result {
        String entity;
        String category;
        String course;
        Result (String entity, String category, String course){
            this.entity = entity;
            this.category = category;
            this.course = course;
        }
    }

    private List<ResultListAdapter.Result> resultList;

    public ResultListAdapter() {
        resultList = new ArrayList<Result>();
    }

    public void addResult(String entity, String category, String course) {
        resultList.add(new ResultListAdapter.Result(entity, category, course));
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