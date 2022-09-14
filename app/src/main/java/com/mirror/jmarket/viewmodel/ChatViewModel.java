package com.mirror.jmarket.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mirror.jmarket.classes.Chat;
import com.mirror.jmarket.classes.ChatRoom2;
import com.mirror.jmarket.classes.User;
import com.mirror.jmarket.model.ChatRepository;

import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private ChatRepository repository;

    private MutableLiveData<List<String>> chatUsers;

    private MutableLiveData<List<User>> getUsersProfile;

    private MutableLiveData<List<ChatRoom2>> chatRoom2s;

    public ChatViewModel(Application application) {
        super(application);
        repository = new ChatRepository(application);
        chatUsers = repository.getChatUsers();
        getUsersProfile = repository.getUsersProfile();
        chatRoom2s = repository.getMyChatRooms();
    }

    public MutableLiveData<List<String>> getChatUsers() { return chatUsers; }

    public MutableLiveData<List<User>> getUsersProfile() { return getUsersProfile; }

    public MutableLiveData<List<ChatRoom2>> getMyChatRooms() { return chatRoom2s; }

    public void getChatRoom(String uid) {
        repository.getChatRoom(uid);
    }

    public void getMyChatUser(String uid) {
        repository.getMyChatUsers(uid);
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

    public void setChatRoom2(String uid, String sellerUid, ChatRoom2 chatRoom2) {
        repository.setChatRoom2(uid, sellerUid, chatRoom2);
    }

    public void getMyChatRooms(String uid) {
        repository.getMyChatRooms(uid);
    }

}
