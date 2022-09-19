package com.mirror.jmarket.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mirror.jmarket.classes.Chat;
import com.mirror.jmarket.classes.ChatRoom;
import com.mirror.jmarket.classes.User;
import com.mirror.jmarket.model.ChatRepository;

import java.util.HashMap;
import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private ChatRepository repository;

    private MutableLiveData<List<HashMap<String, List<Chat>>>> myChats;

    // private MutableLiveData<List<List<Chat>>> myChats;

    private MutableLiveData<List<ChatRoom>> chatRooms;

    private MutableLiveData<Boolean> visited;

    private MutableLiveData<Boolean> createChatRoom;

    private MutableLiveData<HashMap<String, Integer>> unReadChatCount;


    public ChatViewModel(Application application) {
        super(application);
        repository = new ChatRepository(application);
        myChats = repository.getMyChats();
        chatRooms = repository.getMyChatRooms();
        visited = repository.getVisited();
        createChatRoom = repository.getCreateChatRoom();
        unReadChatCount = repository.getUnReadChatCount();
    }

    public MutableLiveData<List<HashMap<String, List<Chat>>>> getMyChats() { return myChats; }

    // public MutableLiveData<List<List<Chat>>> getMyChats() { return myChats; }

    public MutableLiveData<List<ChatRoom>> getMyChatRooms() { return chatRooms; }

    public MutableLiveData<Boolean> getVisited() { return visited; }

    public MutableLiveData<Boolean> getCreateChatRoom() { return createChatRoom; }

    public MutableLiveData<HashMap<String, Integer>> getUnReadChatCount() {
        return unReadChatCount;
    }

    public void unReadChatCount(String myUid) {
        repository.unReadChatCount(myUid);
    }

    public void setVisited(String myUid, String userUid, boolean visit) {
        repository.setVisited(myUid, userUid, visit);
    }

    public void getVisited( String userUid, String myUid) {
        repository.getVisited(userUid, myUid);
    }

    public void allMessageChecked(String myUid, String userUid) {
        repository.allMessageChecked(myUid, userUid);
    }

    public void sendMessage(String sender, String receiver, Chat chat, String lastSendUser) {
        repository.sendMessage(sender, receiver, chat, lastSendUser);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDate() {
        return repository.getDate();
    }

    public void setChatRoom(String uid, String sellerUid, ChatRoom chatRoom, User myUser, User otherUser) {
        repository.setChatRoom(uid, sellerUid, chatRoom, myUser, otherUser);
    }

    public void getMyChatRooms(String uid) {
        repository.getMyChatRooms(uid);
    }

    public void getMyChats(String myUid) { repository.getMyChats(myUid); }

}
