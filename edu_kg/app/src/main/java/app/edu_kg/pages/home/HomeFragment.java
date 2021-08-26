package app.edu_kg.pages.home;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import app.edu_kg.MainActivity;
import app.edu_kg.MainViewModel;
import app.edu_kg.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private MainViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Context context = view.getContext();
        homeViewModel = ((MainActivity) context).mainViewModel;

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}