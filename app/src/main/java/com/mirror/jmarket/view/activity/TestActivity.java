package com.mirror.jmarket.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.ActivityAdminBinding;
import com.mirror.jmarket.databinding.ActivityTestBinding;
import com.mirror.jmarket.viewmodel.AdminViewModel;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = AdminActivity.class.getSimpleName();
    private ActivityTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AdminViewModel adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminViewModel.setTests();
                Log.d(TAG, "call!!");
            }
        });
    }
}