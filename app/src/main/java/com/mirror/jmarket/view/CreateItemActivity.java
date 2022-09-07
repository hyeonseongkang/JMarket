package com.mirror.jmarket.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.ActivityCreateItemBinding;

public class CreateItemActivity extends AppCompatActivity {

    public static final String TAG = "CreateItemActivity";

    // View Binding
    private ActivityCreateItemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);


        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });

    }
}