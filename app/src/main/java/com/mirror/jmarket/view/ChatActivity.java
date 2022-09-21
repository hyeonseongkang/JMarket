package com.mirror.jmarket.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.adapter.ChatItemAdapter;
import com.mirror.jmarket.classes.Chat;
import com.mirror.jmarket.classes.CompleteUser;
import com.mirror.jmarket.classes.Item;
import com.mirror.jmarket.databinding.ActivityChatBinding;
import com.mirror.jmarket.viewmodel.ChatViewModel;
import com.mirror.jmarket.viewmodel.ItemViewModel;

import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public static final String TAG = "ChatActivity";

    // view binding
    private ActivityChatBinding binding;

    // view model
    private ChatViewModel chatViewModel;
    private ItemViewModel itemViewModel;

    // FirebaseUser
    private FirebaseUser user;

    // adapter
    private ChatItemAdapter adapter;


    // current item
    private Item currentItem;

    // ChatRoom Info
    private String itemKey;
    private String uid; // 상대 uid
    private String itemTitle;
    private String myNickName;
    private String userPhoto; // 상대 Profile Photo

    private boolean visited;

    private int completeDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        user = MainActivity.USER;

        Intent getIntent = getIntent();
        itemKey = getIntent.getStringExtra("itemKey");
        uid = getIntent.getStringExtra("uid");
        itemTitle = getIntent.getStringExtra("itemTitle");
        myNickName = getIntent.getStringExtra("myNickName");
        userPhoto = getIntent.getStringExtra("userPhoto");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
        adapter = new ChatItemAdapter();
        binding.recyclerView.setAdapter(adapter);

        binding.userNickName.setText(itemTitle);

        // viewModel
        itemViewModel = new ViewModelProvider(this ,new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ItemViewModel.class);
        itemViewModel.getItem(itemKey);
        itemViewModel.getItem().observe(this, new Observer<Item>() {
            @Override
            public void onChanged(Item item) {
                currentItem = item;
            }
        });
        itemViewModel.getComplete(uid, user.getUid(), itemKey);
        itemViewModel.getComplete().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    completeDeal = 1;
                    binding.completeLayout.setVisibility(View.VISIBLE);
                    binding.completeDealLayout.setVisibility(View.GONE);
                    binding.itemInfoLayout.setVisibility(View.VISIBLE);

                    Glide.with(ChatActivity.this)
                            .load(currentItem.getFirstPhotoUri())
                            .into(binding.itemPhoto);

                    binding.itemTitle.setText(currentItem.getTitle());
                    binding.itemPrice.setText(currentItem.getPrice());
                } else {
                    completeDeal = 2;
                    binding.completeLayout.setVisibility(View.VISIBLE);
                    binding.itemInfoLayout.setVisibility(View.GONE);
                    binding.completeDealLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.completeDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompleteUser completeUser = new CompleteUser(uid, user.getUid());
                itemViewModel.setComplete(user.getUid(), uid, itemKey, completeUser);
            }
        });

        chatViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ChatViewModel.class);
        chatViewModel.getMyChats(user.getUid());
        chatViewModel.getMyChats().observe(this, new Observer<List<HashMap<String, List<Chat>>>>() {
            @Override
            public void onChanged(List<HashMap<String, List<Chat>>> hashMaps) {
                for (HashMap<String, List<Chat>> map : hashMaps) {
                    if (map.containsKey(uid)) {
                        List<Chat> chat = map.get(uid);
                        adapter.setChats(chat, user.getUid(), uid, userPhoto);
                        binding.recyclerView.scrollToPosition(chat.size() - 1);
                        break;
                    }
                }
            }
        });


        chatViewModel.allMessageChecked(user.getUid(), uid);
        chatViewModel.setVisited(user.getUid(), uid, true);
        chatViewModel.getVisited(uid, user.getUid());
        chatViewModel.getVisited().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                visited = aBoolean;
            }
        });


        // button
        binding.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.message.getText().toString();

                if (TextUtils.isEmpty(message))
                    return;

                if (visited) {
                    Log.d(TAG, "상대가 채팅방에 있습니다.");
                } else {
                    Log.d(TAG, "상대가 채팅방에 없습니다.");
                }

                // String userNickName, String sender, String receiver, String message, String date, String time, boolean checked
                Chat chat = new Chat(myNickName, user.getUid(), uid, message, "", "", visited);
                chatViewModel.sendMessage(user.getUid(), uid, chat, user.getUid());
                binding.message.setText("");
            }
        });

        binding.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, ChatMenuActivity.class);
                intent.putExtra("itemKey", itemKey);
                chatMenuLauncher.launch(intent);
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

    ActivityResultLauncher<Intent> chatMenuLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();

                        String data = intent.getStringExtra("data");

                        if (data.equals("complete")) {
                            // 거래 완료
                            if (completeDeal == 1) {
                                Toast.makeText(ChatActivity.this, "이미 거래 완료가 되었습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (completeDeal == 2) {
                                Toast.makeText(ChatActivity.this, "이미 거래 요청이 왔습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                CompleteUser completeUser = new CompleteUser(user.getUid(), "");
                                itemViewModel.setComplete(user.getUid(), uid, itemKey, completeUser);
                            }



                        } else if (data.equals("out")) {
                            // 채팅방 나가기

                        }

                    }
                }
            });


    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        chatViewModel.setVisited(user.getUid(), uid, false);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        chatViewModel.setVisited(user.getUid(), uid, false);
    }
}