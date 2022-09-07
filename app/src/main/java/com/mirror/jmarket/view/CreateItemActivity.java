package com.mirror.jmarket.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.adapter.HomeItemPhotoAdapter;
import com.mirror.jmarket.classes.Item;
import com.mirror.jmarket.databinding.ActivityCreateItemBinding;
import com.mirror.jmarket.viewmodel.ItemViewModel;
import com.mirror.jmarket.viewmodel.LoginViewModel;

import java.util.ArrayList;

public class CreateItemActivity extends AppCompatActivity {

    public static final String TAG = "CreateItemActivity";

    // View Binding
    private ActivityCreateItemBinding binding;

    // ViewModel
    private ItemViewModel itemViewModel;

    // Firebase User;
    private FirebaseUser user;

    // photo
    private ArrayList<String> itemPhotos;
    private Uri tempPhotoUri;

    // adpater
    private HomeItemPhotoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        user = MainActivity.USER;
        itemPhotos = new ArrayList<>();

        itemViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ItemViewModel.class);
        itemViewModel.getItemSave().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.progress.setVisibility(View.GONE);
                if (aBoolean) {
                    finish();
                    overridePendingTransition(R.anim.none, R.anim.fadeout_left);
                }
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.photoItemRecyclerView.setLayoutManager(layoutManager);
        binding.photoItemRecyclerView.setHasFixedSize(true);

        adapter = new HomeItemPhotoAdapter();
        binding.photoItemRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new HomeItemPhotoAdapter.onItemClickListener() {
            @Override
            public void onItemClick( int position) {
                itemPhotos.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, itemPhotos.size());
                binding.photoCount.setText(String.valueOf(itemPhotos.size()));
            }
        });

        binding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                getPhotoLauncher.launch(intent);
            }
        });


        binding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                binding.progress.setVisibility(View.VISIBLE);
//                Item item = new Item(null, null, null, null, null, null, null);
//                itemViewModel.createItem(item);
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

    ActivityResultLauncher<Intent> getPhotoLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        tempPhotoUri = intent.getData();

                        if (tempPhotoUri != null) {
                            itemPhotos.add(tempPhotoUri.toString());
                            adapter.setPhotoUris(itemPhotos);
                            tempPhotoUri = null;
                            binding.photoCount.setText(String.valueOf(itemPhotos.size()));
                        }
                    }
                }
            });
}