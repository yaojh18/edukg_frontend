package app.edu_kg.pages.profile;

import app.edu_kg.DataApplication;
import app.edu_kg.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import app.edu_kg.databinding.FragmentProfileBinding;
import app.edu_kg.pages.test.TestActivity;
import app.edu_kg.pages.user.HistoryActivity;
import app.edu_kg.pages.user.LogActivity;
import app.edu_kg.pages.user.ModifyActivity;
import app.edu_kg.utils.Constant;

public class ProfileFragment extends Fragment {

    private DataApplication localData;
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

                            localData.username = data.getStringExtra("username");
                            localData.token = data.getStringExtra("token");
                            TextView username = binding.userName;
                            username.setText(localData.username);
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
        localData = (DataApplication) context.getApplicationContext();

        TextView username = binding.userName;
        username.setText(localData.username);

        // set menu
        Toolbar logMenuToolbar = binding.logMenu;
        MenuItem logIn = logMenuToolbar.getMenu().getItem(0);
        MenuItem logOut = logMenuToolbar.getMenu().getItem(1);
        if (localData.token.equals("")){
            logOut.setVisible(false);
            logIn.setVisible(true);
        }
        else{
            logOut.setVisible(true);
            logIn.setVisible(false);
        }

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
                                        logOut.setVisible(false);
                                        logIn.setVisible(true);
                                        localData.username = "未登录";
                                        localData.token = "";
                                        username.setText(localData.username);
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

        // set 4 redirect link
        LinearLayout changeProfile = binding.changeProfile;
        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localData.token.equals("")){
                    Intent intent = new Intent(context, LogActivity.class);
                    intent.putExtra("func_type", "login");
                    launcher.launch(intent);
                }
                else{
                    Intent intent = new Intent(context, ModifyActivity.class);
                    intent.putExtra("func_type", "modify");
                    intent.putExtra("token", localData.token);
                    launcher.launch(intent);
                }

            }
        });
        LinearLayout userHistory = binding.userHistory;
        userHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localData.token.equals("")){
                    Intent intent = new Intent(context, LogActivity.class);
                    intent.putExtra("func_type", "login");
                    launcher.launch(intent);
                }
                else{
                    Intent intent = new Intent(context, HistoryActivity.class);
                    intent.putExtra("token", localData.token);
                    intent.putExtra("page_type", Constant.HISTORY_PAGE);
                    startActivity(intent);
                }
            }
        });
        LinearLayout userCollection = binding.userCollection;
        userCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localData.token.equals("")){
                    Intent intent = new Intent(context, LogActivity.class);
                    intent.putExtra("func_type", "login");
                    launcher.launch(intent);
                }
                else{
                    Intent intent = new Intent(context, HistoryActivity.class);
                    intent.putExtra("token", localData.token);
                    intent.putExtra("page_type", Constant.COLLECTION_PAGE);
                    startActivity(intent);
                }

            }
        });
        LinearLayout exerciseRec = binding.exerciseRec;
        exerciseRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localData.token.equals("")){
                    Intent intent = new Intent(context, LogActivity.class);
                    intent.putExtra("func_type", "login");
                    launcher.launch(intent);
                }
                else{
                    Intent intent = new Intent(context, TestActivity.class);
                    intent.putExtra("token", localData.token);
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