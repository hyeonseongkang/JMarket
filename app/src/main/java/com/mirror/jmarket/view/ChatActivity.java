package com.mirror.jmarket.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.adapter.ChatItemAdapter;
import com.mirror.jmarket.adapter.ChatListItemAdapter;
import com.mirror.jmarket.classes.Chat;
import com.mirror.jmarket.classes.ChatRoom;
import com.mirror.jmarket.classes.Item;
import com.mirror.jmarket.classes.LastMessage;
import com.mirror.jmarket.classes.User;
import com.mirror.jmarket.databinding.ActivityChatBinding;
import com.mirror.jmarket.viewmodel.ChatViewModel;
import com.mirror.jmarket.viewmodel.ItemViewModel;
import com.mirror.jmarket.viewmodel.UserManagerViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public static final String TAG = "ChatActivity";

    // view binding
    private ActivityChatBinding binding;

    // view model
    private ChatViewModel chatViewModel;

    // FirebaseUser
    private FirebaseUser user;

    // adapter
    private ChatItemAdapter adapter;


    // ChatRoom Info
    private String uid; // 상대 uid
    private String itemTitle;
    private String myNickName;
    private String userPhoto; // 상대 Profile Photo

    private boolean visited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        user = MainActivity.USER;

        Intent getIntent = getIntent();
        uid = getIntent.getStringExtra("uid");
        itemTitle = getIntent.getStringExtra("itemTitle");
        myNickName = getIntent.getStringExtra("myNickName");
        userPhoto = getIntent.getStringExtra("userPhoto");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setHasFixedSize(true);
        adapter = new ChatItemAdapter();
        binding.recyclerView.setAdapter(adapter);

        binding.itemTitle.setText(itemTitle);

        //

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

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.none, R.anim.fadeout_left);
            }
        });
    }

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
    }
}