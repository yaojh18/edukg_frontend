package app.edu_kg.pages.test;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import app.edu_kg.R;

public class TestViewModel extends ViewModel {
    TestListAdapter adapter;
    int questionIdx = 0;
    public static boolean checkAns = false;
    public TestViewModel() {
        adapter = new TestListAdapter();
    }
}

class TestListAdapter extends RecyclerView.Adapter {

    private static class TestHolder extends RecyclerView.ViewHolder {
        TextView questionText;
        RadioButton option1;
        RadioButton option2;
        RadioButton option3;
        RadioButton option4;
        RadioGroup optionGroup;
        TestHolder(View itemView) {
            super(itemView);
            questionText = (TextView) itemView.findViewById(R.id.question);
            option1 = (RadioButton) itemView.findViewById(R.id.option1);
            option2 = (RadioButton) itemView.findViewById(R.id.option2);
            option3 = (RadioButton) itemView.findViewById(R.id.option3);
            option4 = (RadioButton) itemView.findViewById(R.id.option4);
            optionGroup = (RadioGroup) itemView.findViewById(R.id.optionGroup);
        }

        void bind(app.edu_kg.pages.test.TestListAdapter.Test test) {
            questionText.setText(test.question);
            option1.setText(test.opt1);
            option2.setText(test.opt2);
            option3.setText(test.opt3);
            option4.setText(test.opt4);

            option1.setBackgroundColor(Color.TRANSPARENT);
            option2.setBackgroundColor(Color.TRANSPARENT);
            option3.setBackgroundColor(Color.TRANSPARENT);
            option4.setBackgroundColor(Color.TRANSPARENT);
            if(TestViewModel.checkAns) {
                if(test.choice >= 0) {
                    optionGroup.getChildAt(test.choice).setBackgroundColor(Color.RED);
                }
                optionGroup.getChildAt(test.ans).setBackgroundColor(Color.GREEN);
            }

            optionGroup.clearCheck();
            test.choice = -1;

            option1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    test.choice = 0;
                }
            });
            option2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    test.choice = 1;
                }
            });
            option3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    test.choice = 2;
                }
            });
            option4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    test.choice = 3;
                }
            });
        }
    }


    private static class Test {
        String question;
        String opt1;
        String opt2;
        String opt3;
        String opt4;
        int ans;
        int choice = -1;
        Test (String question, String opt1, String opt2, String opt3, String opt4, int ans){
            this.question = question;
            this.opt1 = opt1;
            this.opt2 = opt2;
            this.opt3 = opt3;
            this.opt4 = opt4;
            this.ans = ans;
        }
    }

    private List<TestListAdapter.Test> resultList;

    public TestListAdapter() {
        resultList = new ArrayList<Test>();
    }

    public void addTest(String question, String opt1, String opt2, String opt3, String opt4, int ans) {
        resultList.add(new TestListAdapter.Test(question, opt1, opt2, opt3, opt4, ans));
    }

    public int getAns(int idx) {
        return resultList.get(idx).ans;
    }

    public int getChoice(int idx) {
        return resultList.get(idx).choice;
    }

    public void clear() {
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
                .inflate(R.layout.test_item, parent, false);
        return new TestHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TestListAdapter.Test message = resultList.get(position);
        ((TestListAdapter.TestHolder) holder).bind(message);
    }
}