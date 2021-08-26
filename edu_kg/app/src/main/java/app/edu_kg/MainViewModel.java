package app.edu_kg;

import androidx.lifecycle.ViewModel;

import app.edu_kg.utils.adapter.*;

public class MainViewModel extends ViewModel {
    public MessageListAdapter adapter;
    public String username = "未登录";
    public String token = "";

    public MainViewModel(){
        adapter = new MessageListAdapter();
    }
}
