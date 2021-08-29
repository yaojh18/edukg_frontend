package app.edu_kg.pages.profile;

import app.edu_kg.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import app.edu_kg.MainActivity;
import app.edu_kg.MainViewModel;
import app.edu_kg.databinding.FragmentProfileBinding;
import app.edu_kg.pages.user.HistoryActivity;
import app.edu_kg.pages.user.LogActivity;
import app.edu_kg.pages.user.ModifyActivity;
import app.edu_kg.utils.Constant;

public class ProfileFragment extends Fragment {

    private MainViewModel profileViewModel;
    private FragmentProfileBinding binding;
    private ActivityResultLauncher<Intent> launcher;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent data = result.getData();

                    if (result.getResultCode() == Activity.RESULT_OK){
                        if (data.getStringExtra("func_type").equals("login")){
                            Toolbar logMenuToolbar = binding.logMenu;
                            MenuItem logIn = logMenuToolbar.getMenu().getItem(0);
                            MenuItem logOut = logMenuToolbar.getMenu().getItem(1);
                            logIn.setVisible(false);
                            logOut.setVisible(true);

                            profileViewModel.username = data.getStringExtra("username");
                            profileViewModel.token = data.getStringExtra("token");
                            TextView username = binding.userName;
                            username.setText(profileViewModel.username);
                        }
                    }
                }
            });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        MainActivity context = (MainActivity) view.getContext();
        profileViewModel = context.mainViewModel;

        Toolbar logMenuToolbar = binding.logMenu;
        TextView username = binding.userName;
        username.setText(profileViewModel.username);

        logMenuToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){

                    case(R.id.log_in):
                        Intent intent = new Intent(context, LogActivity.class);
                        intent.putExtra("func_type", "login");
                        launcher.launch(intent);
                        return true;

                    case(R.id.log_out):
                        AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                                .setTitle("你确定要登出吗？")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        item.setVisible(false);
                                        MenuItem logIn = logMenuToolbar.getMenu().getItem(0);
                                        logIn.setVisible(true);
                                        profileViewModel.username = "未登录";
                                        profileViewModel.token = "";
                                        username.setText(profileViewModel.username);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) { }
                                }).show();
                        return true;
                    default:
                        return false;
                }
            }
        });
        LinearLayout changeProfile = binding.changeProfile;
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileViewModel.token.equals("")){
                    Intent intent = new Intent(context, LogActivity.class);
                    intent.putExtra("func_type", "login");
                    launcher.launch(intent);
                }
                else{
                    Intent intent = new Intent(context, ModifyActivity.class);
                    intent.putExtra("func_type", "modify");
                    intent.putExtra("token", profileViewModel.token);
                    launcher.launch(intent);
                }

            }
        });
        LinearLayout userHistory = binding.userHistory;
        userHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileViewModel.token.equals("")){
                    Intent intent = new Intent(context, LogActivity.class);
                    intent.putExtra("func_type", "login");
                    launcher.launch(intent);
                }
                else{
                    Intent intent = new Intent(context, HistoryActivity.class);
                    intent.putExtra("token", profileViewModel.token);
                    intent.putExtra("page_type", Constant.HISTORY_PAGE);
                    startActivity(intent);
                }
            }
        });
        LinearLayout userCollection = binding.userCollection;
        userCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileViewModel.token.equals("")){
                    Intent intent = new Intent(context, LogActivity.class);
                    intent.putExtra("func_type", "login");
                    launcher.launch(intent);
                }
                else{
                    Intent intent = new Intent(context, HistoryActivity.class);
                    intent.putExtra("token", profileViewModel.token);
                    intent.putExtra("page_type", Constant.COLLECTION_PAGE);
                    startActivity(intent);
                }

            }
        });
        LinearLayout exerciseRec = binding.exerciseRec;
        exerciseRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (profileViewModel.token.equals("")){
                    Intent intent = new Intent(context, LogActivity.class);
                    intent.putExtra("func_type", "login");
                    launcher.launch(intent);
                }
                else{
                    Intent intent = new Intent(context, HistoryActivity.class);
                    intent.putExtra("token", profileViewModel.token);
                    intent.putExtra("page_type", Constant.RECOMMENDATION_PAGE);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}