package app.edu_kg.pages.user;

import app.edu_kg.utils.*;
import app.edu_kg.R;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;

public class LogActivity extends AppCompatActivity {

    private final boolean LOGIN = true;
    private boolean mode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        Intent intent = getIntent();

        MaterialToolbar logMenu = findViewById(R.id.log_menu);
        logMenu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        TextView submit = findViewById(R.id.log_submit);
        EditText username = findViewById(R.id.log_username);
        EditText password = findViewById(R.id.log_password);
        TextView hint = findViewById(R.id.log_hint);

        @SuppressLint("HandlerLeak")
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Constant.LOGIN_RESPONSE_SUCCESS || msg.what == Constant.REGISTER_RESPONSE_SUCCESS){
                    String obj = (String) msg.obj;
                    intent.putExtra("username", username.getText().toString());
                    intent.putExtra("token", obj);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                else if (msg.what == Constant.LOGIN_RESPONSE_FAIL || msg.what == Constant.REGISTER_RESPONSE_FAIL){
                    String obj = (String) msg.obj;
                    hint.setText(obj);
                }
            }
        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mode == LOGIN)
                    Request.Login(username.getText().toString(), password.getText().toString(), handler);
                else
                    Request.Register(username.getText().toString(), password.getText().toString(), handler);
            }
        });

        TextView title = findViewById(R.id.log_title);
        TextView toRegister = findViewById(R.id.log_to_register);
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mode = !mode;
                if (mode == LOGIN){
                    title.setText("登录");
                    toRegister.setText("没有账号？点此注册");
                    submit.setText("登录");
                }
                else {
                    title.setText("注册");
                    toRegister.setText("已有账号？点此登录");
                    submit.setText("注册");
                }
            }
        });
    }
}