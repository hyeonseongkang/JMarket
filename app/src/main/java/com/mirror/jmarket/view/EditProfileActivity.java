package com.mirror.jmarket.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.ActivityEditProfileBinding;
import com.mirror.jmarket.viewmodel.UserManagerViewModel;

public class EditProfileActivity extends AppCompatActivity {

    public static final String TAG = "EditProfileActivity";

    private ActivityEditProfileBinding binding;

    private UserManagerViewModel userManagerViewModel;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        userManagerViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagerViewModel.class);
        userManagerViewModel.updateValid.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Log.d(TAG, "update success user profile");
                    finish();
                    overridePendingTransition(R.anim.none, R.anim.fadeout_left);
                } else {
                    Log.d(TAG, "update fail user profile");
                }
            }
        });

        Intent getIntent = getIntent();
        uid = getIntent.getStringExtra("uid");
        binding.userEmail.setText(uid);

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nickName = binding.userNickName.getText().toString();

                if (TextUtils.isEmpty(nickName))
                    return;

                userManagerViewModel.updateUserProfile(uid, nickName, null);

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
}