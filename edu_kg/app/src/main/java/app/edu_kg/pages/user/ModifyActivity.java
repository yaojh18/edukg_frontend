package app.edu_kg.pages.user;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

import app.edu_kg.DataApplication;
import app.edu_kg.R;
import app.edu_kg.utils.Constant;
import app.edu_kg.utils.Request;

public class ModifyActivity extends AppCompatActivity {

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        Intent intent = getIntent();
        token = intent.getStringExtra("token");

        MaterialToolbar logMenu = findViewById(R.id.change_profile_menu);
        logMenu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        TextView submit = findViewById(R.id.change_profile_submit);
        EditText oldPassword = findViewById(R.id.change_profile_old_password);
        EditText newPassword = findViewById(R.id.change_profile_new_password);
        TextView hint = findViewById(R.id.change_profile_hint);


        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Constant.MODIFY_RESPONSE_SUCCESS || msg.what == Constant.MODIFY_RESPONSE_FAIL){
                    String obj = (String) msg.obj;
                    hint.setText(obj);
                }
            }
        };

        // init submit
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPasswordText = oldPassword.getText().toString();
                String newPasswordText = newPassword.getText().toString();
                if (!oldPasswordText.equals("") && !newPasswordText.equals(""))
                    Request.changePassword(oldPasswordText, newPasswordText, token, handler);
            }
        });
    }

}