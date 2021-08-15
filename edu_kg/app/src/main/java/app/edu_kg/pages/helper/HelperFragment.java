package app.edu_kg.pages.helper;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import app.edu_kg.R;
import app.edu_kg.databinding.FragmentHelperBinding;

public class HelperFragment extends Fragment {

    private HelperViewModel helperViewModel;
    private FragmentHelperBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        helperViewModel =
                new ViewModelProvider(this).get(HelperViewModel.class);

        binding = FragmentHelperBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHelper;
        helperViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}