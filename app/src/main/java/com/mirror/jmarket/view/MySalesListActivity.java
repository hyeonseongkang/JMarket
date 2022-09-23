package com.mirror.jmarket.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.ActivityMySalesListBinding;
import com.mirror.jmarket.viewmodel.ItemViewModel;

public class MySalesListActivity extends AppCompatActivity {

    public static final String TAG = "MySalesListActivity";

    // view binding
    private ActivityMySalesListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMySalesListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyOnSalesListFragment()).commit();

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                if (position == 0) {
                    // 거래중
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyOnSalesListFragment()).commit();
                } else if (position == 1) {
                    // 판매완료
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyCompleteSalesListFragment()).commit();
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