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
import com.mirror.jmarket.view.MyPageFragment;

import java.util.HashMap;
import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private ChatRepository repository;

    private MutableLiveData<List<HashMap<String, List<Chat>>>> myChats;

    private MutableLiveData<List<ChatRoom>> chatRooms;

    private MutableLiveData<Boolean> visited;

    private MutableLiveData<Boolean> createChatRoom;

    private MutableLiveData<HashMap<String, Integer>> unReadChatCount;

    private MutableLiveData<Boolean> leaveChatRoom;
    private MutableLiveData<Boolean> myLeaveChatRoom;

    public ChatViewModel(Application application) {
        super(application);
        repository = new ChatRepository(application);
        myChats = repository.getMyChats();
        chatRooms = repository.getMyChatRooms();
        visited = repository.getVisited();
        createChatRoom = repository.getCreateChatRoom();
        unReadChatCount = repository.getUnReadChatCount();
        leaveChatRoom = repository.getLeaveChatRoom();
        myLeaveChatRoom = repository.getMyLeaveChatRoom() ;
    }

    public MutableLiveData<List<HashMap<String, List<Chat>>>> getMyChats() { return myChats; }

    // public MutableLiveData<List<List<Chat>>> getMyChats() { return myChats; }

    public MutableLiveData<List<ChatRoom>> getMyChatRooms() { return chatRooms; }

    public MutableLiveData<Boolean> getVisited() { return visited; }

    public MutableLiveData<Boolean> getCreateChatRoom() { return createChatRoom; }

    public MutableLiveData<HashMap<String, Integer>> getUnReadChatCount() {
        return unReadChatCount;
    }

    public MutableLiveData<Boolean> getLeaveChatRoom() { return leaveChatRoom; }

    public MutableLiveData<Boolean> getMyLeaveChatRoom() { return myLeaveChatRoom; }

    public void getUnReadChatCount(String myUid) {
        repository.getUnReadChatCount(myUid);
    }

    public void setUnReadChatCount(String myUid, String userUid, int unReadChatCount) {
        repository.setUnReadChatCount(myUid, userUid, unReadChatCount);
    }

    public void setVisited(String myUid, String userUid, String itemKey, boolean visit) {
        repository.setVisited(myUid, userUid, itemKey, visit);
    }

    public void getVisited( String userUid, String myUid, String itemKey) {
        repository.getVisited(userUid, myUid, itemKey);
    }

    public void allMessageChecked(String myUid, String userUid) {
        repository.allMessageChecked(myUid, userUid);
    }

    public void sendMessage(String sender, String receiver, String itemKey, Chat chat, String lastSendUser) {
        repository.sendMessage(sender, receiver, itemKey, chat, lastSendUser);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDate() {
        return repository.getDate();
    }

    public void setChatRoom(String uid, String sellerUid, ChatRoom chatRoom, User myUser, User otherUser) {
        repository.setChatRoom(uid, sellerUid, chatRoom, myUser, otherUser);
    }

    public void setChatRoom(String uid, String sellerUid, String itemKey, ChatRoom chatRoom, User myUser, User otherUser) {
        repository.setChatRoom(uid, sellerUid, itemKey, chatRoom, myUser, otherUser);
    }

    public void getMyChatRooms(String myUid) {
        repository.getMyChatRooms(myUid);
    }

    public void getMyChats(String myUid) { repository.getMyChats(myUid); }

    public void setLeaveChatRoom(String myUid, String userUid) {
        repository.setLeaveChatRoom(myUid, userUid);
    }

    public void getLeaveChatRoom(String userUid, String myUid, String itemKey) {
        repository.getLeaveChatRoom(userUid, myUid, itemKey);
    }

}
