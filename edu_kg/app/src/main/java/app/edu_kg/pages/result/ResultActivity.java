package app.edu_kg.pages.result;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import app.edu_kg.R;
import app.edu_kg.pages.result.ResultViewModel;
import app.edu_kg.utils.Request;

import app.edu_kg.databinding.ActivityResultBinding;

public class ResultActivity extends AppCompatActivity {

    private ResultViewModel ResultViewModel;
    private ActivityResultBinding binding;

    private final String historyDir = "history.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("test", "jump into ResultActivity");
        super.onCreate(savedInstanceState);
        ResultViewModel = new ViewModelProvider(this).get(ResultViewModel.class);
        this.getSupportActionBar().hide();
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        initSearch(intent);
        initResult(view, intent);
    }

    private void initSearch(Intent intent) {
        TextInputLayout searchInputLayout = binding.searchInputLayout;
        searchInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchInput = binding.searchInput.getText().toString();
                String type = binding.type.getSelectedItem().toString();
                String subject = binding.subject.getSelectedItem().toString();
                String order = binding.order.getSelectedItem().toString();
                addHistory(searchInput);
                Intent intent = new Intent(ResultActivity.this, ResultActivity.class);
                intent.putExtra("searchInput", searchInput);
                intent.putExtra("type", type);
                intent.putExtra("subject", subject);
                intent.putExtra("order", order);
                startActivity(intent);
            }
        });
    }

    private void initResult(View view, Intent intent) {
        RecyclerView resultRecycler = binding.board;
        resultRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        resultRecycler.setAdapter(ResultViewModel.adapter);
        String[] result = loadResult(intent);
        ResultViewModel.adapter.clearResult();
        if(result != null) {
            for(int i = 0; i < result.length ; ++i) {
                ResultViewModel.adapter.addResult(result[i]);
            }
        }
    }

    private String[] loadResult(Intent intent) {
        return null;
    }

    private void addHistory(String text) {
        try {
            FileOutputStream out = openFileOutput(historyDir, Context.MODE_APPEND);
            out.write((text + "\n").getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
            Log.e("test", "add successfully");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", e.toString());
        }
    }

}