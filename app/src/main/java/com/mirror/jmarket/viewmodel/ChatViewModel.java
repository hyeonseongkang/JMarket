package com.mirror.jmarket.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.mirror.jmarket.model.ChatRepository;

public class ChatViewModel extends AndroidViewModel {

    private ChatRepository repository;

    public ChatViewModel(Application application) {
        super(application);
        repository = new ChatRepository(application);
    }
}
