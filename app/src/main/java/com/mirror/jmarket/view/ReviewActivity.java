package com.mirror.jmarket.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.R;
import com.mirror.jmarket.classes.Item;
import com.mirror.jmarket.classes.Review;
import com.mirror.jmarket.databinding.ActivityReviewBinding;
import com.mirror.jmarket.viewmodel.ItemViewModel;

public class ReviewActivity extends AppCompatActivity {

    public static final String TAG = "ReviewActivity";

    private ActivityReviewBinding binding;

    // view model
    private ItemViewModel itemViewModel;

    private String itemKey;
    private String userUid; // 상대 uid
    private String userNickName; // 상대 nickName

    private Item currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        // get Intent
        Intent intent = getIntent();
        itemKey = intent.getStringExtra("itemKey");
        userUid = intent.getStringExtra("userUid");
        userNickName = intent.getStringExtra("userNickName");
        binding.userNickName.setText(userNickName);

        // view model
        itemViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ItemViewModel.class);
        itemViewModel.getItem(itemKey);
        itemViewModel.getItem().observe(this, new Observer<Item>() {
            @Override
            public void onChanged(Item item) {
                currentItem = item;
                binding.itemTitle.setText(item.getTitle());

                Glide.with(ReviewActivity.this)
                        .load(item.getFirstPhotoUri())
                        .into(binding.itemPhoto);
            }
        });

        itemViewModel.getReviewComplete().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(ReviewActivity.this, "리뷰 작성 완료", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.none, R.anim.fadeout_left);
                } else {
                    Toast.makeText(ReviewActivity.this, "이미 리뷰를 작성했습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(R.anim.none, R.anim.fadeout_left);
                }
            }
        });


        // button

        binding.writeReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String reviewText = binding.reviewText.getText().toString();

                if (TextUtils.isEmpty(reviewText))
                    return;

                Review review = new Review(currentItem, reviewText);
                itemViewModel.setReview(userUid, review);
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