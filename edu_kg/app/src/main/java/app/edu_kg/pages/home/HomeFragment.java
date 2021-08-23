package app.edu_kg.pages.home;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;


import app.edu_kg.R;
import app.edu_kg.databinding.FragmentHomeBinding;
import app.edu_kg.pages.search.SearchActivity;

public class HomeFragment extends Fragment {
    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    private LinearLayout mGallery;
    private int[] mImgIds;
    private LayoutInflater mInflater;

    private View selectedSubject;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        Context context = view.getContext();
        mInflater = inflater;
        initSubjectListData(5);
        initSubjectListView(view);
        initSearchButton(view);
        return view;
    }

    private void initSubjectListData(int item_number) {
        mImgIds = new int[item_number];
        for (int i = 0; i < item_number; ++i) {
            mImgIds[i] = R.drawable.menu_home;
        }
    }

    private void initSubjectListView(View mainView) {
        mGallery = (LinearLayout) mainView.findViewById(R.id.id_gallery);

        for (int i = 0; i < mImgIds.length; i++) {
            View view = mInflater.inflate(R.layout.subject_item,
                    mGallery, false);
            ImageView img = (ImageView) view.findViewById(R.id.id_index_gallery_item_image);
            img.setImageResource(mImgIds[i]);
            TextView txt = (TextView) view.findViewById(R.id.id_index_gallery_item_text);
            txt.setText("some info");
            txt.setTextColor(Color.BLACK);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedSubject.setBackgroundColor(Color.rgb(255, 255, 255));
                    selectedSubject = v;
                    v.setBackgroundColor(Color.rgb(255, 0, 0));
                }
            });
            mGallery.addView(view);
            if(i == 0) {
                selectedSubject = view;
            }
        }
    }

    private void initSearchButton(View v) {
        Button btn = (Button)v.findViewById(R.id.search_bar);
        btn.setOnClickListener(v1 -> {
            Log.e("test", "search button clicked");
            Intent i = new Intent(getActivity(), SearchActivity.class);
            startActivity(i);
        });
    }

    private void initEntityList() {

    }
}