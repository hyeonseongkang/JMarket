package com.mirror.jmarket.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.R;
import com.mirror.jmarket.model.User;
import com.mirror.jmarket.databinding.ActivityEditProfileBinding;
import com.mirror.jmarket.viewmodel.UserManagerViewModel;

public class EditProfileActivity extends AppCompatActivity {

    public static final String TAG = "EditProfileActivity";


    // View Binding
    private ActivityEditProfileBinding binding;

    // ViewModel
    private UserManagerViewModel userManagerViewModel;

    private String uid;
    private Uri tempPhotoUri;
    private String nickName;
    private String pw; // delete;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        // 카메라 권한
        if (ContextCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
            }
        }

        userManagerViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagerViewModel.class);
        userManagerViewModel.updateValid.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Log.d(TAG, "update success user profile");
                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.none, R.anim.fadeout_left);
                } else {
                    Log.d(TAG, "update fail user profile");
                }
            }
        });

        Intent getIntent = getIntent();
        uid = getIntent.getStringExtra("uid");
        tempPhotoUri = null;
        nickName = null;

        userManagerViewModel.getUserProfile(uid);
        userManagerViewModel.getUserProfile().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (!(user.getPhotoUri().equals(""))) {
                    //tempPhotoUri = Uri.parse(user.getPhotoUri());
                    Glide.with(EditProfileActivity.this)
                            .load(Uri.parse(user.getPhotoUri()))
                            .into(binding.userPhoto);
                }

                if (!(user.getNickName().equals(""))) {
                    nickName = user.getNickName();
                    binding.userNickName.setText(user.getNickName());
                }

                email = user.getEmail();
                pw = user.getPassword();
                binding.userEmail.setText(user.getEmail());
                binding.progress.setVisibility(View.GONE);
            }
        });

        binding.userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                getPhotoLauncher.launch(intent);
            }
        });

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickName = binding.userNickName.getText().toString();

                if (TextUtils.isEmpty(nickName))
                    return;

                if (tempPhotoUri == null) {
                    tempPhotoUri = Uri.parse("");
                }
                // String uid, String email, String password, String nickName, String photoUri
                userManagerViewModel.updateUserProfile(new User(uid, email, pw, nickName, tempPhotoUri.toString()));
                binding.progress.setVisibility(View.VISIBLE);
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });
    }

    ActivityResultLauncher<Intent> getPhotoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        tempPhotoUri = intent.getData();

                        if (tempPhotoUri != null) {
                            Glide.with(EditProfileActivity.this)
                                    .load(tempPhotoUri)
                                    .into(binding.userPhoto);
                        }
                    }
                }
            });
}