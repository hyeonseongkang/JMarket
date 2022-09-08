package com.mirror.jmarket.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.adapter.HomeItemPhotoAdapter;
import com.mirror.jmarket.classes.User;
import com.mirror.jmarket.databinding.FragmentMypageBinding;
import com.mirror.jmarket.viewmodel.LoginViewModel;
import com.mirror.jmarket.viewmodel.UserManagerViewModel;

import org.jetbrains.annotations.NotNull;

public class MyPageFragment extends Fragment {

    private static final String TAG = "MyPageFragment";

    // ViewBinding
    private FragmentMypageBinding binding;

    // Firebase User
    private FirebaseUser user;

    private String photoUri;
    private String nickName;

    // ViewModel
    private UserManagerViewModel userManagerViewModel;
    private LoginViewModel loginViewModel;


    public MyPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMypageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        user = MainActivity.USER;


        userManagerViewModel = new ViewModelProvider(requireActivity()).get(UserManagerViewModel.class);
        userManagerViewModel.getUserProfile(user.getUid());
        userManagerViewModel.getUserProfile().observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d(TAG, "Hello!@!@" + user.getPhotoUri() + " " + user.getNickName());
                if (!(user.getPhotoUri().equals(""))) {
                    photoUri = user.getPhotoUri();
                    Glide.with(getActivity())
                            .load(Uri.parse(user.getPhotoUri()))
                            .into(binding.userPhoto);
                } else {
                    photoUri = null;
                }

                if (!(user.getNickName().equals(""))) {
                    nickName = user.getNickName();
                    binding.userNickName.setText(user.getNickName());
                } else {
                    nickName = null;
                    binding.userNickName.setText("닉네임을 설정해 주세요.");
                }

                binding.userEmail.setText(user.getEmail());

            }
        });

        binding.userEmail.setText(user.getEmail());

        binding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("uid", user.getUid());
                editLauncher.launch(intent);
            }
        });

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginViewModel.logout();
            }
        });

    }

    ActivityResultLauncher<Intent> editLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        userManagerViewModel.getUserProfile(user.getUid());
                    }
                }
            });

}