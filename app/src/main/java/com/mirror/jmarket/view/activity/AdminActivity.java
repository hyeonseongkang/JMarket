package com.mirror.jmarket.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mirror.jmarket.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}