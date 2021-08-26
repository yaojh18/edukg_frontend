package app.edu_kg.pages.home;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.edu_kg.R;
import app.edu_kg.pages.detail.DetailActivity;
import app.edu_kg.pages.home.EntityListAdapter;
import app.edu_kg.pages.result.ResultActivity;

public class HomeViewModel extends ViewModel {

    final int total_subject = 9;
    public boolean isEditting = false;
    public static int selectSubject;
    final static String[] subject = new String[]{"国文", "英文", "数学", "物理", "化学", "生物", "地理", "历史", "政治"};

    final static String[] subjectEng = new String[]{"chinese", "english", "math", "physics", "chemistry", "biology", "geo", "history", "politics"};

    boolean[] selected = new boolean[total_subject];

    EntityListAdapter adapter;

    public HomeViewModel() {
        adapter = new EntityListAdapter();
        Arrays.fill(selected, true);
    }
}

class EntityListAdapter extends RecyclerView.Adapter {

    private static class EntityHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        EntityHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.message_card_left_text);
            messageText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), DetailActivity.class);
                    intent.putExtra("entity", messageText.getText().toString());
                    intent.putExtra("subject", HomeViewModel.subjectEng[HomeViewModel.selectSubject]);
                    v.getContext().startActivity(intent);
                }
            });
        }
        void bind(EntityListAdapter.Entity entity) {
            messageText.setText(entity.text);
        }
    }


    private static class Entity {
        String text;
        Entity (String text){
            this.text = text;
        }
    }

    private List<app.edu_kg.pages.home.EntityListAdapter.Entity> entityList;

    public EntityListAdapter() {
        entityList = new ArrayList<app.edu_kg.pages.home.EntityListAdapter.Entity>();
    }

    public void addEntity(String label, String category) {
        entityList.add(new app.edu_kg.pages.home.EntityListAdapter.Entity(label + "\n" + category));
    }

    public void clearEntity() {
        entityList.clear();
    }


    @Override
    public int getItemCount() {
        return entityList.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_card_left, parent, false);
        return new EntityListAdapter.EntityHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EntityListAdapter.Entity message = entityList.get(position);
        ((EntityListAdapter.EntityHolder) holder).bind(message);
    }
}