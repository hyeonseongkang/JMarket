package com.mirror.jmarket.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.ActivityChatBinding;
import com.mirror.jmarket.viewmodel.ChatViewModel;

public class ChatActivity extends AppCompatActivity {

    public static final String TAG = "ChatActivity";

    // view binding
    private ActivityChatBinding binding;

    // view model
    private ChatViewModel chatViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ChatViewModel.class);

    }
}