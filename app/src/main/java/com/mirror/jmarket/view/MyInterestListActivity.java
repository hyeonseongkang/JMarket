package com.mirror.jmarket.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.adapter.HomeItemAdapter;
import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.databinding.ActivityMyInterestListBinding;
import com.mirror.jmarket.viewmodel.ItemViewModel;

import java.util.List;

public class MyInterestListActivity extends AppCompatActivity {

    public static final String TAG = "MyInterestListActivity";

    private ActivityMyInterestListBinding binding;

    // view model
    private ItemViewModel itemViewModel;

    // adapter
    private HomeItemAdapter adapter;

    private FirebaseUser user = MainActivity.USER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyInterestListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
        adapter = new HomeItemAdapter();
        binding.recyclerView.setAdapter(adapter);

        itemViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ItemViewModel.class);
        itemViewModel.getMyInterestItems(user.getUid());
        itemViewModel.getMyInterestItems().observe(MyInterestListActivity.this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                if (items != null) {
                    adapter.setItems(items, true);
                }
            }
        });


        adapter.setOnItemClickListener(new HomeItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                Intent intent = new Intent(MyInterestListActivity.this, DetailItemActivity.class);
                intent.putExtra("key", item.getKey());
                startActivity(intent);
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