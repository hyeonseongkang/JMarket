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
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.adapter.ChatItemAdapter;
import com.mirror.jmarket.model.Chat;
import com.mirror.jmarket.model.CompleteUser;
import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.databinding.ActivityChatBinding;
import com.mirror.jmarket.viewmodel.ChatViewModel;
import com.mirror.jmarket.viewmodel.ItemViewModel;

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
    private String userNickName; // 상대 nickName
    private String userPhoto; // 상대 Profile Photo

    private boolean visited;

    private int completeDeal;

    private boolean leaveChatRoom; // 상대방이 채팅방을 나갔는지 확인, true -> 나감, false -> 안나감

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        init();
        initObserve();
        initListener();

    }

    void init() {
        user = MainActivity.USER;

        Intent getIntent = getIntent();
        itemKey = getIntent.getStringExtra("itemKey");
        uid = getIntent.getStringExtra("uid");
        itemTitle = getIntent.getStringExtra("itemTitle");
        myNickName = getIntent.getStringExtra("myNickName");
        userNickName = getIntent.getStringExtra("userNickName");
        userPhoto = getIntent.getStringExtra("userPhoto");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
        adapter = new ChatItemAdapter(this);
        binding.recyclerView.setAdapter(adapter);

        binding.userNickName.setText(userNickName); // 채팅 상대방 닉네임

        // item viewModel
        // itemViewModel = new ViewModelProvider(this ,new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ItemViewModel.class);
        itemViewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        itemViewModel.getItem(itemKey);
        itemViewModel.getComplete(uid, user.getUid(), itemKey);

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        // chatViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ChatViewModel.class);
        chatViewModel.getMyChats(user.getUid(), uid, itemKey);

        // 상대방이 채팅방을 나간경우
        chatViewModel.getLeaveChatRoom(uid, user.getUid(), itemKey);

        // 메시지 읽음 처리
        chatViewModel.allMessageChecked(user.getUid(), uid, itemKey);

        // 채팅방을 나가지 않았으면 현재 해당 채팅방에 방문하고 있다는 표시
        if (!leaveChatRoom)
            chatViewModel.setVisited(user.getUid(), uid, itemKey, true);

        chatViewModel.getVisited(uid, user.getUid(), itemKey);
    }

    void initObserve() {
        itemViewModel.getItem().observe(this, new Observer<Item>() {
            @Override
            public void onChanged(Item item) {
                currentItem = item;
            }
        });


        itemViewModel.getComplete().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    // 거래 완료
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
                    // 거래 완료 요청
                    completeDeal = 2;
                    binding.completeLayout.setVisibility(View.VISIBLE);
                    binding.itemInfoLayout.setVisibility(View.GONE);
                    binding.completeDealLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        chatViewModel.getChats().observe(this, new Observer<List<Chat>>() {
            @Override
            public void onChanged(List<Chat> chats) {
                if (userPhoto == null || userPhoto.equals("null")) {
                    userPhoto = "null";
                }
                for (Chat chat: chats) {
                    Log.d(TAG, chat.getMessage());
                }
                adapter.setChats(chats, user.getUid(), uid, userPhoto);
                binding.recyclerView.scrollToPosition(chats.size() - 1);
            }
        });


        chatViewModel.getLeaveChatRoom().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    leaveChatRoom = true;
                    binding.leaveChatRoomUser.setVisibility(View.VISIBLE);
                } else {
                    leaveChatRoom = false;
                }
            }
        });

        // 내가 채팅방을 나간경우
        chatViewModel.getMyLeaveChatRoom().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    finish();
                    overridePendingTransition(R.anim.none, R.anim.fadeout_left);
                }
            }
        });


        chatViewModel.getVisited().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                visited = aBoolean;
            }
        });

    }

    void initListener() {
        binding.completeDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CompleteUser completeUser = new CompleteUser(currentItem, uid, user.getUid(), currentItem.getId());
                itemViewModel.setComplete(user.getUid(), uid, itemKey, completeUser);
            }
        });

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

                // 상대방이 채팅방을 나갔을 경우
                if (leaveChatRoom) {
                    Toast.makeText(ChatActivity.this, "상대방이 채팅방을 나갔습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // String userNickName, String sender, String receiver, String message, String date, String time, boolean checked
                Chat chat = new Chat(myNickName, user.getUid(), uid, message, "", "", visited);
                chatViewModel.sendMessage(user.getUid(), uid, itemKey, chat, user.getUid());
                binding.message.setText("");
            }
        });

        binding.reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, ReviewActivity.class);
                intent.putExtra("itemKey", itemKey);
                intent.putExtra("userUid", uid); // 상대 uid
                intent.putExtra("userNickName", userNickName); // 상대 nickName
                startActivity(intent);

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
                chatViewModel.removeGetVisitedValueEventListener(user.getUid(), uid, itemKey);
                chatViewModel.removeGetAllMessageCheckedValueEventListener(user.getUid(), uid, itemKey);
                chatViewModel.removeGetLeaveChatRoomValueEventListener(user.getUid(), uid, itemKey);
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
                                CompleteUser completeUser = new CompleteUser(currentItem, user.getUid(), "", currentItem.getId());
                                itemViewModel.setComplete(user.getUid(), uid, itemKey, completeUser);
                            }



                        } else if (data.equals("out")) {
                            // 채팅방 나가기
                            chatViewModel.setLeaveChatRoom(user.getUid(), uid, itemKey);
                        }

                    }
                }
            });


    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        if (!leaveChatRoom) {
            chatViewModel.setVisited(user.getUid(), uid, itemKey, false);
            chatViewModel.removeMyChatsValueEventListener(user.getUid(), uid, itemKey);
        }

    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (!leaveChatRoom){
            chatViewModel.setVisited(user.getUid(), uid, itemKey, true);
            chatViewModel.getMyChats(user.getUid(), uid, itemKey);
        }

    }

}