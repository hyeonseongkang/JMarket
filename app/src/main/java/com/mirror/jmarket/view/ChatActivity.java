package com.mirror.jmarket.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.classes.Chat;
import com.mirror.jmarket.classes.ChatRoom;
import com.mirror.jmarket.databinding.ActivityChatBinding;
import com.mirror.jmarket.viewmodel.ChatViewModel;

public class ChatActivity extends AppCompatActivity {

    public static final String TAG = "ChatActivity";

    // view binding
    private ActivityChatBinding binding;

    // view model
    private ChatViewModel chatViewModel;

    // FirebaseUser
    private FirebaseUser user;

    // ChatRoom Info
    private String uid; // 상대 uid
    private String itemTitle;

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

        Log.d("ChatActivity 내 uid", user.getUid());
        Log.d("ChatActivity 상대방 uid", uid);
        binding.itemTitle.setText(itemTitle);

        chatViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ChatViewModel.class);
        chatViewModel.getMyChats(user.getUid());


        // button
        binding.sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.message.getText().toString();

                if (TextUtils.isEmpty(message))
                    return;

                // String user, String message, String date, String time, boolean checked
                Chat chat = new Chat(uid, message, "", "", false);
                chatViewModel.sendMessage(user.getUid(), uid, chat, uid);
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
}