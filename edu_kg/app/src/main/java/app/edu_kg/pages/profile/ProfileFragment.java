package app.edu_kg.pages.profile;

import app.edu_kg.DataApplication;
import app.edu_kg.R;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.net.URL;
import java.util.Objects;

import app.edu_kg.MainActivity;
import app.edu_kg.databinding.FragmentProfileBinding;
import app.edu_kg.pages.test.TestActivity;
import app.edu_kg.pages.user.HistoryActivity;
import app.edu_kg.pages.user.LogActivity;
import app.edu_kg.pages.user.ModifyActivity;
import app.edu_kg.utils.Constant;
import app.edu_kg.utils.Functional;
import app.edu_kg.utils.InstanceIO;

public class ProfileFragment extends Fragment {

    private DataApplication localData;
    private FragmentProfileBinding binding;
    private ActivityResultLauncher<Intent> loginLauncher;
    private ActivityResultLauncher<Intent> profileLauncher;
    private ActivityResultLauncher<Intent> backgroundLauncher;

    private MainActivity context;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = Objects.requireNonNull(result.getData());
                        Menu menu = binding.logMenu.getMenu();

                        menu.getItem(0).setVisible(false);
                        menu.getItem(1).setVisible(true);
                        menu.getItem(2).setVisible(true);
//                        menu.getItem(3).setVisible(true);

                        localData.username = data.getStringExtra("username");
                        localData.token = data.getStringExtra("token");
                        TextView username = binding.userName;
                        username.setText(localData.username);

                        localData.profile = InstanceIO.loadBitmap(context, localData.username + "+profile");
//                        localData.background = InstanceIO.loadBitmap(context, localData.username + "+background");
                        if (localData.profile != null)
                            binding.userProfile.setImageBitmap(localData.profile);
//                        if (localData.background != null)
//                            binding.userBackground.setImageBitmap(localData.background);
                    }
                }
            });
        profileLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = Objects.requireNonNull(result.getData());
                            Uri imageUri = data.getData();
                            Bitmap image = Functional.getContactBitmapFromURI(context, imageUri);
                            localData.profile = image;
                            InstanceIO.saveBitmap(context, image, localData.username + "+profile");
                            binding.userProfile.setImageBitmap(image);
                        }
                    }
                });
        backgroundLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
//                            Intent data = Objects.requireNonNull(result.getData());
//                            Uri imageUri = data.getData();
//                            Bitmap image = Functional.getContactBitmapFromURI(context, imageUri);
//                            localData.background = image;
//                            InstanceIO.saveBitmap(context, image, localData.username + "+background");
//                            binding.userBackground.setImageBitmap(image);
                        }
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = (MainActivity) view.getContext();
        localData = (DataApplication) context.getApplicationContext();

        binding.userName.setText(localData.username);

        if (localData.userStateChanged){
            localData.userStateChanged = false;
            localData.profile = InstanceIO.loadBitmap(context, localData.username + "+profile");
//            localData.background = InstanceIO.loadBitmap(context, localData.username + "+background");
        }
        if (localData.profile != null)
            binding.userProfile.setImageBitmap(localData.profile);
//        if (localData.background != null)
//            binding.userBackground.setImageBitmap(localData.background);

        // set menu
        Toolbar logMenuToolbar = binding.logMenu;
        Menu menu = logMenuToolbar.getMenu();
        MenuItem logIn = menu.getItem(0);
        MenuItem logOut = menu.getItem(1);
        MenuItem picture = menu.getItem(2);
        MenuItem background = menu.getItem(3);

        if (!localData.token.equals("")){
            logIn.setVisible(false);
            logOut.setVisible(true);
            picture.setVisible(true);
//            background.setVisible(true);
        }
        else{
            logIn.setVisible(true);
            logOut.setVisible(false);
            picture.setVisible(false);
            background.setVisible(false);
        }

        logMenuToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch(item.getItemId()){

                    case(R.id.log_in): {
                        Intent intent = new Intent(context, LogActivity.class);
                        loginLauncher.launch(intent);
                        return true;
                    }

                    case(R.id.log_out): {
                        AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                                .setTitle("你确定要登出吗？")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        logIn.setVisible(true);
                                        logOut.setVisible(false);
                                        picture.setVisible(false);
                                        background.setVisible(false);
                                        localData.username = "未登录";
                                        localData.token = "";
                                        binding.userName.setText(localData.username);

                                        localData.profile = null;
//                                        localData.background = null;
//                                        binding.userBackground.setImageResource(R.drawable.login_background);
                                        binding.userProfile.setImageResource(R.drawable.menu_profile);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) { }
                                }).show();
                        return true;
                    }

                    case (R.id.change_user_profile): {
                        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        profileLauncher.launch(gallery);
                        return true;
                    }
                    case (R.id.change_user_background): {
                        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        backgroundLauncher.launch(gallery);
                        return true;
                    }
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
                    loginLauncher.launch(intent);
                }
                else{
                    Intent intent = new Intent(context, ModifyActivity.class);
                    intent.putExtra("token", localData.token);
                    startActivity(intent);
                }

            }
        });
        LinearLayout userHistory = binding.userHistory;
        userHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localData.token.equals("")){
                    Intent intent = new Intent(context, LogActivity.class);
                    loginLauncher.launch(intent);
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
                    loginLauncher.launch(intent);
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
                    loginLauncher.launch(intent);
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