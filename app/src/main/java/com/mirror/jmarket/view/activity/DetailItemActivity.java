package com.mirror.jmarket.view.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.adapter.DetailPhotoItemAdapter;
import com.mirror.jmarket.databinding.ActivityMainBinding;
import com.mirror.jmarket.model.ChatRoom;
import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.model.LastMessage;
import com.mirror.jmarket.model.User;
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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_item);

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        init();
        initObserve();
        initListener();

    }

    void init() {

        Intent getIntent = getIntent();
        key = getIntent.getStringExtra("key");

        user = MainActivity.USER;

        userManagerViewModel = new ViewModelProvider(this).get(UserManagerViewModel.class);
        //userManagerViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(UserManagerViewModel.class);
        userManagerViewModel.getUserProfile(user.getUid());

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        // chatViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ChatViewModel.class);

        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        // itemViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ItemViewModel.class);
        itemViewModel.getItem(key);

        // 현재 아이템 삭제했을 경우 종료
        itemViewModel.setDeleteItemState(false);

        // 현재 아이템을 누른 uid가 아이템의 좋아요를 눌렀는지 확인
        itemViewModel.getLike(key, user.getUid());


        // horizontal recyclerview
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setHasFixedSize(true);

        adapter = new DetailPhotoItemAdapter();
        binding.recyclerView.setAdapter(adapter);
    }

    void initObserve() {
        // 채팅방이 만들어지면 ChatActivity로 이동
        chatViewModel.getCreateChatRoom().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(DetailItemActivity.this, ChatActivity.class);
                    intent.putExtra("itemKey", key);
                    intent.putExtra("uid", sellerUid);
                    intent.putExtra("itemTitle", currentItem.getTitle());
                    intent.putExtra("myNickName",  myUser.getNickName().length() <= 0 ? user.getEmail() : myUser.getNickName());
                    intent.putExtra("userNickName", otherUser.getNickName().length() > 0 ? otherUser.getNickName() : otherUser.getEmail());
//                intent.putExtra("userPhoto", currentItem.sellerProfileUri());
                    intent.putExtra("userPhoto", "null");

                    startActivity(intent);
                    finish();
                }

            }
        });

        itemViewModel.getLike().observe(DetailItemActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                //Log.d(TAG, aBoolean.toString());
                if (aBoolean)
                    binding.like.setBackgroundResource(R.drawable.red_heart);
                else
                    binding.like.setBackgroundResource(R.drawable.basic_heart);
            }
        });

        itemViewModel.getDeleteItemState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(DetailItemActivity.this, "삭제 완료", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

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
                sellerUid = item.getId(); // 판매자 uid

                binding.setItem(item);

                if (sellerUid.equals(user.getUid())) {
                    binding.deleteItem.setVisibility(View.VISIBLE);
                }
                //  binding.title.setText(item.getTitle());
                //  binding.content.setText(item.getContent());
                //  binding.price.setText(item.getPrice() + "원");

                userManagerViewModel.getOtherUserProfile(sellerUid); // 판매자 profile 가져옴

                User sellerUser = item.getUser();
                if (sellerUser != null) {
                    String sellerProfileUri = sellerUser.getPhotoUri();
                    binding.userName.setText(sellerUser.getNickName());

                    // 판매자 profile 사진이 있으면 가져오고 아니면 기본 이미지
                    if (!(sellerProfileUri.equals(""))) {
                        Glide.with(DetailItemActivity.this)
                                .load(sellerProfileUri)
                                .into(binding.sellerProfile);
                    }
                }



                boolean priceOffer = item.isPriceOffer();

                if (priceOffer)
                    binding.priceOffer.setText("가격제안가능");
                else
                    binding.priceOffer.setText("가격제안불가능");


                ArrayList<String> photoUrls = item.getPhotoUrls();
                adapter.setPhotoUris(photoUrls);

            }
        });
    }

    void initListener() {
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

        binding.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailItemActivity.this)
                        .setTitle("중고 물건 삭제")
                        .setMessage(currentItem.getTitle() + "를 삭제 하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                itemViewModel.deleteItem(currentItem.getKey());
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }

}