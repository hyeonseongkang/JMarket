package com.mirror.jmarket.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.adapter.HomeItemAdapter;
import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.databinding.ActivityMyBuyListBinding;
import com.mirror.jmarket.viewmodel.ItemViewModel;

import java.util.List;

public class MyBuyListActivity extends AppCompatActivity {

    public static final String TAG = "MyBuyListActivity";

    // view binding
    private ActivityMyBuyListBinding binding;

    // view model
    private ItemViewModel itemViewModel;

    // adapter
    private HomeItemAdapter adapter;

    private FirebaseUser user = MainActivity.USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyBuyListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
        adapter = new HomeItemAdapter();
        binding.recyclerView.setAdapter(adapter);

        itemViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ItemViewModel.class);
        itemViewModel.getMyBuyItems(user.getUid());
        itemViewModel.getMyBuyItems().observe(MyBuyListActivity.this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                if (items != null) {
                    adapter.setItems(items, false);
                }
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