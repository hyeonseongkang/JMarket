package com.mirror.jmarket.view;

import android.content.Intent;
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
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.FragmentMypageBinding;
import com.mirror.jmarket.viewmodel.LoginViewModel;

import org.jetbrains.annotations.NotNull;

public class MyPageFragment extends Fragment {

    private static final String TAG = "MyPageFragment";

    private FragmentMypageBinding binding;
    private FirebaseUser user;

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

        LoginViewModel loginViewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);
        loginViewModel.loginCheck();
        user = loginViewModel.getFirebaseUser().getValue();

        binding.userEmail.setText(user.getEmail());

        binding.editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("uid", user.getUid());
                startActivity(intent);
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

                    }
                }
            });

}