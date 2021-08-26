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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import app.edu_kg.databinding.FragmentHelperBinding;

public class HelperFragment extends Fragment {

    private MainViewModel helperViewModel;
    private FragmentHelperBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHelperBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Context context = view.getContext();
        helperViewModel = ((MainActivity) context).mainViewModel;

        RecyclerView messageRecycler = binding.messageBoard;

        messageRecycler.setLayoutManager(new LinearLayoutManager(context));
        messageRecycler.setAdapter(helperViewModel.adapter);

        EditText helperInput = binding.helperInput;

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                int message_num = msg.what;
                if (message_num == Constant.MESSAGE_LIST_RESPONSE){
                    String message = (String) msg.obj;
                    helperViewModel.adapter.addRobotMessage(message);
                    binding.messageBoard.scrollToPosition(helperViewModel.adapter.getItemCount() - 1);
                }
            }
        };

        helperInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    String text = helperInput.getText().toString();
                    if (!text.equals("")){
                        helperViewModel.adapter.addUserMessage(text);
                        helperInput.setText("");
                        messageRecycler.scrollToPosition(helperViewModel.adapter.getItemCount() - 1);
                        Request.inputQuestion(text, handler);
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
                    helperViewModel.adapter.addUserMessage(text);
                    helperInput.setText("");
                    messageRecycler.scrollToPosition(helperViewModel.adapter.getItemCount() - 1);
                    Request.inputQuestion(text, handler);
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}