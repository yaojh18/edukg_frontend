package app.edu_kg.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView.*;

import java.util.List;

import app.edu_kg.R;
import app.edu_kg.utils.Constant;

public class SubjectGridAdapter extends Adapter<ViewHolder> {

    public static class SubjectHolder extends ViewHolder implements View.OnClickListener{
        OnSubjectClickListener onSubjectClickListener;
        View view;
        ImageView image;
        TextView text;

        SubjectHolder(View subjectView, OnSubjectClickListener listener) {
            super(subjectView);
            subjectView.setOnClickListener(this);
            onSubjectClickListener = listener;
            view = subjectView;
            image = subjectView.findViewById(R.id.subject_item_image);
            text = subjectView.findViewById(R.id.subject_item_text);

        }
        void bind(Subject subject) {
            if (subject.isOut)
                image.setImageResource(R.drawable.subject_add);
            else
                image.setImageResource(subject.imageId);
            text.setText(subject.name);
            view.setSelected(subject.isSelected || subject.isOut);
        }

        @Override
        public void onClick(View view) {
            onSubjectClickListener.onSubjectClick(getAdapterPosition());
        }
    }

    public static class Subject{
        public int imageId;
        public String name;
        public Constant.SUBJECT_NAME id;
        public boolean isSelected;
        public boolean isOut;
        public Subject(String name, int imageId, Constant.SUBJECT_NAME id){
            this.name = name;
            this.imageId = imageId;
            this.id = id;
            this.isSelected = false;
            this.isOut = false;
        }
        public Subject(String name, int imageId, Constant.SUBJECT_NAME id, boolean isSelected, boolean isOut){
            this.name = name;
            this.imageId = imageId;
            this.id = id;
            this.isSelected = isSelected;
            this.isOut = isOut;
        }
    }

    private List<Subject> subjectList;
    private OnSubjectClickListener onSubjectClickListener;


    public SubjectGridAdapter(List<Subject> SubjectList, OnSubjectClickListener onSubjectClickListener) {
        this.subjectList = SubjectList;
        this.onSubjectClickListener = onSubjectClickListener;
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_item, parent, false);
        return new SubjectHolder(view, onSubjectClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subject message = subjectList.get(position);
        ((SubjectHolder) holder).bind(message);
    }

    public interface OnSubjectClickListener{
        void onSubjectClick(int position);
    }
}