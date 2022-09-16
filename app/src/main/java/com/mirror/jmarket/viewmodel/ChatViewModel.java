package com.mirror.jmarket.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mirror.jmarket.classes.Chat;
import com.mirror.jmarket.classes.ChatRoom;
import com.mirror.jmarket.model.ChatRepository;

import java.util.HashMap;
import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private ChatRepository repository;

    private MutableLiveData<List<HashMap<String, List<Chat>>>> myChats;

    // private MutableLiveData<List<List<Chat>>> myChats;

    private MutableLiveData<List<ChatRoom>> chatRooms;

    public ChatViewModel(Application application) {
        super(application);
        repository = new ChatRepository(application);
        myChats = repository.getMyChats();
        chatRooms = repository.getMyChatRooms();
    }

    public MutableLiveData<List<HashMap<String, List<Chat>>>> getMyChats() { return myChats; }

    // public MutableLiveData<List<List<Chat>>> getMyChats() { return myChats; }

    public MutableLiveData<List<ChatRoom>> getMyChatRooms() { return chatRooms; }

    public void sendMessage(String sender, String receiver, Chat chat, String lastSendUser) {
        repository.sendMessage(sender, receiver, chat, lastSendUser);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDate() {
        return repository.getDate();
    }

    public void setChatRoom(String uid, String sellerUid, ChatRoom chatRoom) {
        repository.setChatRoom(uid, sellerUid, chatRoom);
    }

    public void getMyChatRooms(String uid) {
        repository.getMyChatRooms(uid);
    }

    public void getMyChats(String myUid) { repository.getMyChats(myUid); }

}
