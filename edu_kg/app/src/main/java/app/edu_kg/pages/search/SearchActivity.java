package app.edu_kg.pages.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONObject;

import app.edu_kg.R;
import app.edu_kg.databinding.FragmentHelperBinding;
import app.edu_kg.pages.search.SearchViewModel;
import app.edu_kg.utils.request;

import app.edu_kg.databinding.ActivitySearchBinding;

public class SearchActivity extends AppCompatActivity {

    private SearchViewModel searchViewModel;
    private ActivitySearchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("test", "jump into SearchActivity");
        super.onCreate(savedInstanceState);
        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View rootView = binding.getRoot();
        setContentView(rootView);
        initSearch();
    }

    private void initSearch() {
        TextInputLayout SearchInputLayout = binding.searchInputLayout;
        SearchInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = binding.type.getSelectedItem().toString();
                String subject = binding.subject.getSelectedItem().toString();
                String order = binding.order.getSelectedItem().toString();


            }
        });
    }

}