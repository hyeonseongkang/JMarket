package com.mirror.jmarket.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.adapter.DetailPhotoItemAdapter;
import com.mirror.jmarket.classes.ChatRoom;
import com.mirror.jmarket.classes.Item;
import com.mirror.jmarket.classes.LastMessage;
import com.mirror.jmarket.classes.User;
import com.mirror.jmarket.databinding.ActivityDetailItemBinding;
import com.mirror.jmarket.viewmodel.ChatViewModel;
import com.mirror.jmarket.viewmodel.ItemViewModel;
import com.mirror.jmarket.viewmodel.UserManagerViewModel;

import java.util.ArrayList;

public class DetailItemActivity extends AppCompatActivity {

    public static final String TAG = "DetailItemActivity";

    // view binding
    private ActivityDetailItemBinding binding;

    // viewModel
    private ItemViewModel itemViewModel;
    private ChatViewModel chatViewModel;
    private UserManagerViewModel userManagerViewModel;

    // adapter
    DetailPhotoItemAdapter adapter;

    // Firebase User
    private FirebaseUser user;

    String key;
    String sellerUid;

    private Item currentItem;

    private User myUser;
    private User otherUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        Intent getIntent = getIntent();
        key = getIntent.getStringExtra("key");

        user = MainActivity.USER;

        userManagerViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagerViewModel.class);
        userManagerViewModel.getUserProfile(user.getUid());
        userManagerViewModel.getUserProfile().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                myUser = user;
            }
        });


        userManagerViewModel.getOtherUserProfile().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                otherUser = user;
            }
        });

        itemViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ItemViewModel.class);
        itemViewModel.getItem(key);
        itemViewModel.getItem().observe(this, new Observer<Item>() {
            @Override
            public void onChanged(Item item) {
                currentItem = item;
                /*
                sellerProfile
                userName
                title
                content
                like
                price
                priceOffer
                chatButton
                 */
                // title, content, price, photoList
                sellerUid = item.getId();
                binding.title.setText(item.getTitle());
                binding.content.setText(item.getContent());
                binding.price.setText(item.getPrice() + "원");
                binding.userName.setText(item.getSellerName());

                userManagerViewModel.getOtherUserProfile(sellerUid);

                String sellerProfielUri = item.getSellerProfileUri();

                if (!(sellerProfielUri.equals("null")))
                    Glide.with(DetailItemActivity.this)
                    .load(sellerProfielUri)
                    .into(binding.sellerProfile);


                boolean priceOffer = item.isPriceOffer();

                if (priceOffer)
                    binding.priceOffer.setText("가격제안가능");
                else
                    binding.priceOffer.setText("가격제안불가능");


                ArrayList<String> photoKeys = item.getPhotoKeys();
                adapter.setPhotoUris(photoKeys);

            }
        });

        itemViewModel.getLike(key, user.getUid());
        itemViewModel.getLike().observe(DetailItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(TAG, aBoolean.toString());
                if (aBoolean)
                    binding.like.setBackgroundResource(R.drawable.red_heart);
                else
                    binding.like.setBackgroundResource(R.drawable.basic_heart);
            }
        });

        chatViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ChatViewModel.class);
        chatViewModel.getCreateChatRoom().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Intent intent = new Intent(DetailItemActivity.this, ChatActivity.class);
                intent.putExtra("itemKey", key);
                intent.putExtra("uid", sellerUid);
                intent.putExtra("itemTitle", currentItem.getTitle());
                intent.putExtra("myNickName",  myUser.getNickName().length() <= 0 ? user.getEmail() : myUser.getNickName());
                intent.putExtra("userNickName", otherUser.getNickName().length() > 0 ? otherUser.getNickName() : otherUser.getEmail());
                intent.putExtra("userPhoto", currentItem.getSellerProfileUri());
                startActivity(intent);
                finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setHasFixedSize(true);

        adapter = new DetailPhotoItemAdapter();
        binding.recyclerView.setAdapter(adapter);

        binding.chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LastMessage lastMessage = new LastMessage("", "", "", "", false);
                ChatRoom chatRoom = new ChatRoom(key, null, currentItem, lastMessage, true, 0, false, false);
                chatViewModel.setChatRoom(user.getUid(), sellerUid, currentItem.getKey(), chatRoom, myUser, otherUser);

            }
        });

        binding.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemViewModel.setLike(key, user.getUid());
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