package com.mirror.jmarket.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.mirror.jmarket.R;
import com.mirror.jmarket.adapter.DetailItemPhotoAdapter;
import com.mirror.jmarket.classes.Item;
import com.mirror.jmarket.databinding.ActivityDetailItemBinding;
import com.mirror.jmarket.viewmodel.ItemViewModel;

import java.util.ArrayList;

public class DetailItemActivity extends AppCompatActivity {

    public static final String TAG = "DetailItemActivity";

    // view binding
    private ActivityDetailItemBinding binding;

    // viewModel
    private ItemViewModel itemViewModel;

    // adapter
    DetailItemPhotoAdapter adapter;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        Intent getIntent = getIntent();
        key = getIntent.getStringExtra("key");

        itemViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ItemViewModel.class);
        itemViewModel.getItem(key);
        itemViewModel.getItem().observe(this, new Observer<Item>() {
            @Override
            public void onChanged(Item item) {
                // title, content, price, photoList
                binding.title.setText(item.getTitle());
                binding.content.setText(item.getContent());
                binding.price.setText(item.getPrice());

                ArrayList<String> photoKeys = item.getPhotoKeys();
                adapter.setPhotoUris(photoKeys);

            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setHasFixedSize(true);

        adapter = new DetailItemPhotoAdapter();
        binding.recyclerView.setAdapter(adapter);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });


    }
}