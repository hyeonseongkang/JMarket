package com.mirror.jmarket.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
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

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyReceivedReviewListFragment()).commit();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                if (position == 0) {
                    // 내가 받은 리뷰
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyReceivedReviewListFragment()).commit();
                } else if (position == 1) {
                    // 내가 작성한 리뷰
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyWrittenReviewListFragment()).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // button
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });
    }
}