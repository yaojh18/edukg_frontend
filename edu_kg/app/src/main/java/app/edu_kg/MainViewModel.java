package app.edu_kg;

import androidx.lifecycle.ViewModel;

import app.edu_kg.pages.helper.MessageListAdapter;

public class MainViewModel extends ViewModel {
    public MessageListAdapter adapter;

    public MainViewModel(){
        adapter = new MessageListAdapter();
    }
}
