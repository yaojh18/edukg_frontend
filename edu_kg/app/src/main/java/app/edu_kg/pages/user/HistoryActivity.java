package app.edu_kg.pages.user;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.appbar.MaterialToolbar;

import app.edu_kg.R;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_page);

        MaterialToolbar toolbar = findViewById(R.id.list_page);
        toolbar.setTitle("浏览历史");

    }
}