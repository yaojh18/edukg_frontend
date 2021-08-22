package app.edu_kg.pages.profile;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.w3c.dom.Text;

import app.edu_kg.MainActivity;
import app.edu_kg.MainViewModel;
import app.edu_kg.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private MainViewModel profileViewModel;
    private FragmentProfileBinding binding;

    @Override


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Context context = view.getContext();
        profileViewModel = ((MainActivity) context).mainViewModel;
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}