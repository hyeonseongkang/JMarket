package com.mirror.jmarket.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mirror.jmarket.databinding.ActivityAdminBinding;
import com.mirror.jmarket.model.User;
import com.mirror.jmarket.utils.RxAndroidUtils;
import com.mirror.jmarket.viewmodel.AdminViewModel;
import com.mirror.jmarket.viewmodel.LoginViewModel;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private static final String TAG = AdminActivity.class.getSimpleName();
    private ActivityAdminBinding binding;

    private AdminViewModel adminViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initObserve();
    }

    void init() {
        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        adminViewModel.getUsers();
    }

    void initObserve() {
        adminViewModel.getUsersProfile().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user: users) {
                    Log.d(TAG, user.toString());
                }
            }
        });
    }
}