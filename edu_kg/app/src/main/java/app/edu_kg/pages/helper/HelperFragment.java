package app.edu_kg.pages.helper;
import app.edu_kg.MainActivity;
import app.edu_kg.MainViewModel;
import app.edu_kg.utils.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import app.edu_kg.databinding.FragmentHelperBinding;
import app.edu_kg.utils.adapter.SubjectGridAdapter;

public class HelperFragment extends Fragment implements SubjectGridAdapter.OnSubjectClickListener {

    private MainViewModel helperViewModel;
    private FragmentHelperBinding binding;
    private SubjectGridAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHelperBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Context context = view.getContext();
        helperViewModel = ((MainActivity) context).mainViewModel;

        RecyclerView messageRecycler = binding.messageBoard;

        messageRecycler.setLayoutManager(new LinearLayoutManager(context));
        messageRecycler.setAdapter(helperViewModel.helperMessageAdapter);

        EditText helperInput = binding.helperInput;

        //handle message received
        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int message_num = msg.what;
                if (message_num == Constant.MESSAGE_LIST_RESPONSE){
                    String message = (String) msg.obj;
                    helperViewModel.helperMessageAdapter.addRobotMessage(message);
                    binding.messageBoard.scrollToPosition(helperViewModel.helperMessageAdapter.getItemCount() - 1);
                }
            }
        };

        // init input text
        helperInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    String text = helperInput.getText().toString();
                    if (!text.equals("")){
                        helperViewModel.helperMessageAdapter.addUserMessage(text);
                        helperInput.setText("");
                        messageRecycler.smoothScrollToPosition(helperViewModel.helperMessageAdapter.getItemCount() - 1);
                        String subject = null;
                        if (helperViewModel.helperSubjectSelected < helperViewModel.helperSubjectList.size())
                            subject = Functional.subjChe2Eng(helperViewModel.helperSubjectList.get(helperViewModel.helperSubjectSelected).name);
                        Request.inputQuestion(text, subject, handler);
                    }
                }
                return false;
            }
        });

        TextInputLayout helperInputLayout = binding.helperInputLayout;
        helperInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = helperInput.getText().toString();
                if (!text.equals("")){
                    helperViewModel.helperMessageAdapter.addUserMessage(text);
                    helperInput.setText("");
                    messageRecycler.smoothScrollToPosition(helperViewModel.helperMessageAdapter.getItemCount() - 1);
                    String subject = null;
                    if (helperViewModel.helperSubjectSelected < helperViewModel.helperSubjectList.size())
                        subject = Functional.subjChe2Eng(helperViewModel.helperSubjectList.get(helperViewModel.helperSubjectSelected).name);
                    Request.inputQuestion(text, subject, handler);
                }
            }
        });

        RecyclerView subjectRecycler = binding.helperToolbar;
        subjectRecycler.setLayoutManager(new GridLayoutManager(context, 5));
        helperViewModel.helperSubjectAdapter = new SubjectGridAdapter(helperViewModel.helperSubjectList,this);
        subjectRecycler.setAdapter(helperViewModel.helperSubjectAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSubjectClick(int position) {
        List<SubjectGridAdapter.Subject> subjectList = helperViewModel.helperSubjectList;
        SubjectGridAdapter.Subject subject = subjectList.get(position);

        switch (subject.id){
            case FOLD:
                subjectList.remove(position);
                subjectList.remove(position - 1);
                subjectList.remove(position - 2);
                subjectList.remove(position - 3);
                subjectList.remove(position - 4);
                subjectList.remove(position - 5);
                subjectList.add(Constant.helperSubjectMap.get(Constant.SUBJECT_NAME.UNFOLD));
                break;
            case UNFOLD:
                subjectList.remove(position);
                subjectList.add(Constant.helperSubjectMap.get(Constant.SUBJECT_NAME.CHEMISTRY));
                subjectList.add(Constant.helperSubjectMap.get(Constant.SUBJECT_NAME.BIOLOGY));
                subjectList.add(Constant.helperSubjectMap.get(Constant.SUBJECT_NAME.POLITICS));
                subjectList.add(Constant.helperSubjectMap.get(Constant.SUBJECT_NAME.HISTORY));
                subjectList.add(Constant.helperSubjectMap.get(Constant.SUBJECT_NAME.GEOGRAPHY));
                subjectList.add(Constant.helperSubjectMap.get(Constant.SUBJECT_NAME.FOLD));
                break;
            default:
                if (helperViewModel.helperSubjectSelected == position){
                    subject.isSelected = false;
                    helperViewModel.helperSubjectSelected = 100;
                }
                else{
                    if (helperViewModel.helperSubjectSelected < subjectList.size()){
                        SubjectGridAdapter.Subject oldSubject = subjectList.get(helperViewModel.helperSubjectSelected);
                        oldSubject.isSelected = false;
                    }
                    subject.isSelected = true;
                    helperViewModel.helperSubjectSelected = position;
                }
        }
        helperViewModel.helperSubjectAdapter.notifyDataSetChanged();
    }
}