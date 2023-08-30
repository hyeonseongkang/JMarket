package com.mirror.jmarket.view.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.adapter.recyclerview.HomeItemPhotoAdapter;
import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.model.User;
import com.mirror.jmarket.databinding.ActivityCreateItemBinding;
import com.mirror.jmarket.viewmodel.ItemViewModel;
import com.mirror.jmarket.viewmodel.UserManagerViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CreateItemActivity extends AppCompatActivity {

    public static final String TAG = "CreateItemActivity";

    // View Binding
    private ActivityCreateItemBinding binding;

    // ViewModel
    private ItemViewModel itemViewModel;
    private UserManagerViewModel userManagerViewModel;

    // Firebase User;
    private FirebaseUser user;

    // User Info
    private String userName;
    private String userProfile;

    // photo
    private ArrayList<String> itemPhotos;
    private Uri tempPhotoUri;

    // adpater
    private HomeItemPhotoAdapter adapter;

    private DecimalFormat decimalFormat = new DecimalFormat("#,###");
    private String priceFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        init();
        initObserve();
        initListener();
    }

    void init() {
        user = MainActivity.USER;
        itemPhotos = new ArrayList<>();

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);

        userManagerViewModel = new ViewModelProvider(this).get(UserManagerViewModel.class);
        // userManagerViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagerViewModel.class);
        userManagerViewModel.getUserProfile(user.getUid());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.photoItemRecyclerView.setLayoutManager(layoutManager);
        binding.photoItemRecyclerView.setHasFixedSize(true);

        adapter = new HomeItemPhotoAdapter();
        binding.photoItemRecyclerView.setAdapter(adapter);

    }

    void initObserve() {
        itemViewModel.getItemSave().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(TAG, "aaaa" + aBoolean);
                binding.progress.setVisibility(View.GONE);
                if (aBoolean) {
                    Toast.makeText(CreateItemActivity.this, "등록완료", Toast.LENGTH_SHORT).show();

                    finish();
                    overridePendingTransition(R.anim.none, R.anim.fadeout_left);
                }
            }
        });

        userManagerViewModel.getUserProfile().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.getNickName() == null || user.getNickName().length() <= 0) {
                    userName = user.getEmail();
                } else {
                    userName = user.getNickName();
                }

                if (user.getPhotoUri() == null || user.getPhotoUri().length() <= 0) {
                    userProfile = "null";
                } else {
                    userProfile = user.getPhotoUri();
                }


            }
        });
    }

    void initListener() {
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });

        // 아이템 생성
        binding.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = binding.title.getText().toString();
                String price = binding.price.getText().toString();
                Boolean priceOffer = binding.priceOffer.isChecked();
                String content = binding.content.getText().toString();

                if (itemPhotos.size() == 0) return;

                binding.progress.setVisibility(View.VISIBLE);
                //String id, String title, String price, boolean priceOffer, String content, ArrayList<String> photoKeys, String key, String firstPhotoUri, String sellerProfileUri, String sellerName, ArrayList<String> likes
                Item item = new Item(user.getUid(), title, price, priceOffer, content, itemPhotos, null, "", itemPhotos.get(0), user.getUid(), null, false);
                itemViewModel.createItem(item);
            }
        });

        binding.price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString()) && !s.toString().equals(priceFormat)) {
                    priceFormat = decimalFormat.format(Double.parseDouble(s.toString().replaceAll(",", "")));
                    binding.price.setText(priceFormat);
                    binding.price.setSelection(priceFormat.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // 아이템 사진 추가
        binding.gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                getPhotoLauncher.launch(intent);
            }
        });

        // 아이템 사진 추가 했다가 취소
        adapter.setOnItemClickListener(new HomeItemPhotoAdapter.onItemClickListener() {
            @Override
            public void onItemClick( int position) {
                itemPhotos.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, itemPhotos.size());
                binding.photoCount.setText(String.valueOf(itemPhotos.size()));
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

                        // 갤러리에서 사진 데이터를 가져와서 null check 후 값이 있다면 itemPhotos에 담음 후에 아이템 생성할 때 itemPhotos를 photo key로 바꿔 저장함
                        if (tempPhotoUri != null) {
                            itemPhotos.add(tempPhotoUri.toString());
                            adapter.setPhotoUris(itemPhotos);
                            tempPhotoUri = null;
                            binding.photoCount.setText(String.valueOf(itemPhotos.size()));
                        }
                    }
                }
            });

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        //itemViewModel.getItemSave().removeObservers(CreateItemActivity.this);
    }
}