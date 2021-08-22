package app.edu_kg.pages.helper;
import app.edu_kg.MainActivity;
import app.edu_kg.MainViewModel;
import app.edu_kg.R;
import app.edu_kg.utils.*;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

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

//        InputMethodManager inputMethod = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        RecyclerView messageRecycler = binding.messageBoard;

        messageRecycler.setLayoutManager(new LinearLayoutManager(context));
        messageRecycler.setAdapter(helperViewModel.adapter);

        EditText helperInput = binding.helperInput;
        helperInput.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER) {
                    String text = helperInput.getText().toString();
                    if (!text.equals("")){
                        helperViewModel.adapter.addUserMessage(text);
                        String response = request.inputQuestion(text);
                        helperViewModel.adapter.addRobotMessage(response);
                        helperInput.setText("");
//                        inputMethod.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                        if (messageRecycler.canScrollVertically(1))
                        messageRecycler.scrollToPosition(helperViewModel.adapter.getItemCount() - 1);
                    }
                }
                return false;
            }
        });
        helperInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                Log.v("Focus", String.valueOf(hasFocus));
                if (hasFocus)
                    messageRecycler.scrollToPosition(helperViewModel.adapter.getItemCount() - 1);
            }
        });
        TextInputLayout helperInputLayout = binding.helperInputLayout;
        helperInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = helperInput.getText().toString();
                if (!text.equals("")){
                    helperViewModel.adapter.addUserMessage(text);
                    String response = request.inputQuestion(text);
                    helperViewModel.adapter.addRobotMessage(response);
                    helperInput.setText("");
//                    inputMethod.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                    if (messageRecycler.canScrollVertically(1))
                    messageRecycler.scrollToPosition(helperViewModel.adapter.getItemCount() - 1);
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