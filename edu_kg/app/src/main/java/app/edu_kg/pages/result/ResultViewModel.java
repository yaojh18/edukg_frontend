package app.edu_kg.pages.result;

import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
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

import javax.xml.transform.Result;

import app.edu_kg.R;
import app.edu_kg.pages.detail.DetailActivity;
import app.edu_kg.pages.result.ResultActivity;
import app.edu_kg.utils.Constant;

public class ResultViewModel extends ViewModel {
    ResultListAdapter adapter;
    public static SpannableStringBuilder relationStyle = new SpannableStringBuilder();
    public ResultViewModel() {
        adapter = new ResultListAdapter();
    }
}

class ResultListAdapter extends RecyclerView.Adapter {

    private static class ResultHolder extends RecyclerView.ViewHolder {
        TextView firstText;
        TextView secondText;
        ImageView endIcon;
        ResultHolder(View itemView) {
            super(itemView);
            firstText = (TextView) itemView.findViewById(R.id.first_text_view);
            secondText = (TextView) itemView.findViewById(R.id.second_text_view);
            endIcon = itemView.findViewById(R.id.item_image_end);
            endIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String searchKey = firstText.getText().toString();
                    String[] setting = secondText.getText().toString().split(" ");
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra("searchKey", searchKey);
                    intent.putExtra("type", setting[0]);
                    intent.putExtra("course", setting[1]);
                    intent.putExtra("order", setting[2]);
                    v.getContext().startActivity(intent);
                }
            });

        }

        void bind(app.edu_kg.pages.result.ResultListAdapter.Result result) {
            firstText.setText(result.entity);
            secondText.setText(result.category + " " + result.course);
            if(result.type == ResultType.LINK_INSTANCE) {
                int color = Constant.LINK_INSTANCE_COLOR[result.start_index % Constant.LINK_INSTANCE_COLOR.length];
                firstText.setTextColor(color);

                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
                ResultViewModel.relationStyle.setSpan(foregroundColorSpan, result.start_index, result.end_index, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                

                /*
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View view) {
                        for(int i = 0; i < resultList.size(); ++i) {
                            Result re = resultList.get(i);
                            if(re.start_index != result.start_index) {
                                re.show = false;
                            }
                        }
                        ResultViewModel.adapter.notifyDataSetChanged();
                    }
                };

                ResultViewModel.relationStyle.setSpan(clickableSpan, result.start_index, result.end_index, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            */
            }
        }
    }

    public enum ResultType {
        ENTITY, LINK_INSTANCE;
    }

    private static class Result {
        ResultType type;
        String entity;
        String category;
        String course;
        int start_index;
        int end_index;

        Result (ResultType type, String entity, String category, String course, int start_index, int end_index){
            this.type = type;
            this.entity = entity;
            this.category = category;
            this.course = course;
            this.start_index = start_index;
            this.end_index = end_index;
        }

        Result (ResultType type, String entity, String category, String course){
            this.type = type;
            this.entity = entity;
            this.category = category;
            this.course = course;
            this.start_index = 0;
            this.end_index = 0;
        }
    }

    private List<ResultListAdapter.Result> resultList;

    public ResultListAdapter() {
        resultList = new ArrayList<Result>();
    }

    public void addEntity(ResultType type, String entity, String category, String course) {
        resultList.add(new ResultListAdapter.Result(type, entity, category, course));
    }

    public void addLinkInstance(ResultType type, String entity, String category, String course, int start_index, int end_index) {
        resultList.add(new ResultListAdapter.Result(type, entity, category, course, start_index, end_index));
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
        Log.e("test", "in create");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ResultHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.e("test", "in bind");
        ResultListAdapter.Result message = resultList.get(position);
        ((ResultListAdapter.ResultHolder) holder).bind(message);
    }
}