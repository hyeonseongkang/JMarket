package com.mirror.jmarket.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.ActivityMyReviewListBinding;

public class MyReviewListActivity extends AppCompatActivity {

    public static final String TAG = "MyReviewListActivity";

    // view binding
    private ActivityMyReviewListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyReviewListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Received Review
        // written review

        overridePendingTransition(R.anim.fadein_left, R.anim.none);
    }
}