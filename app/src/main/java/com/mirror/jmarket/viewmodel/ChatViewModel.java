package com.mirror.jmarket.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mirror.jmarket.classes.Chat;
import com.mirror.jmarket.model.ChatRepository;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private ChatRepository repository;

    private MutableLiveData<List<String>> chatUsers;

    public ChatViewModel(Application application) {
        super(application);
        repository = new ChatRepository(application);
        chatUsers = repository.getChatUsers();
    }

    public MutableLiveData<List<String>> getChatUsers() { return chatUsers; }

    public void getChatRoom(String uid) {
        repository.getChatRoom(uid);
    }

    public void setChatRoom(String uid, String sellerUid) {
        repository.setChatRoom(uid, sellerUid);
    }

    public void sendMessage(String sender, String receiver, Chat chat) {
        repository.sendMessage(sender, receiver, chat);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDate() {
        return repository.getDate();
    }

}
