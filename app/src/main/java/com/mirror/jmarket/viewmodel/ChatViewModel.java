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

    private MutableLiveData<List<HashMap<List<String>, List<Chat>>>> myChats;

    private MutableLiveData<List<Chat>> chats;

    private MutableLiveData<List<ChatRoom>> chatRooms;

    private MutableLiveData<Boolean> visited;

    private MutableLiveData<Boolean> createChatRoom;

    private MutableLiveData<HashMap<List<String>, Integer>> unReadChatCount;

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
        chats = repository.getChats();
    }

    public MutableLiveData<List<HashMap<List<String>, List<Chat>>>> getMyChats() { return myChats; }

    public MutableLiveData<List<Chat>> getChats() { return chats; }

    // public MutableLiveData<List<List<Chat>>> getMyChats() { return myChats; }

    public MutableLiveData<List<ChatRoom>> getMyChatRooms() { return chatRooms; }

    public MutableLiveData<Boolean> getVisited() { return visited; }

    public MutableLiveData<Boolean> getCreateChatRoom() { return createChatRoom; }

    public MutableLiveData<HashMap<List<String>, Integer>> getUnReadChatCount() {
        return unReadChatCount;
    }

    public MutableLiveData<Boolean> getLeaveChatRoom() { return leaveChatRoom; }

    public MutableLiveData<Boolean> getMyLeaveChatRoom() { return myLeaveChatRoom; }

    public void getUnReadChatCount(String myUid) {
        repository.getUnReadChatCount(myUid);
    }

    public void setUnReadChatCount(String myUid, String userUid, String itemKey, int unReadChatCount) {
        repository.setUnReadChatCount(myUid, userUid, itemKey, unReadChatCount);
    }

    public void setVisited(String myUid, String userUid, String itemKey, boolean visit) {
        repository.setVisited(myUid, userUid, itemKey, visit);
    }

    public void getVisited( String userUid, String myUid, String itemKey) {
        repository.getVisited(userUid, myUid, itemKey);
    }

    public void allMessageChecked(String myUid, String userUid, String itemKey) {
        repository.allMessageChecked(myUid, userUid, itemKey);
    }

    public void sendMessage(String sender, String receiver, String itemKey, Chat chat, String lastSendUser) {
        repository.sendMessage(sender, receiver, itemKey, chat, lastSendUser);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDate() {
        return repository.getDate();
    }

    /*
    public void setChatRoom(String uid, String sellerUid, ChatRoom chatRoom, User myUser, User otherUser) {
        repository.setChatRoom(uid, sellerUid, chatRoom, myUser, otherUser);
    }

     */

    public void setChatRoom(String uid, String sellerUid, String itemKey, ChatRoom chatRoom, User myUser, User otherUser) {
        repository.setChatRoom(uid, sellerUid, itemKey, chatRoom, myUser, otherUser);
    }

    public void getMyChatRooms(String myUid) {
        repository.getMyChatRooms(myUid);
    }

    public void getMyChats(String myUid) { repository.getMyChats(myUid); }

    public void getMyChats(String myUid, String userUid, String itemKey) { repository.getMyChats(myUid, userUid, itemKey);}

    public void setLeaveChatRoom(String myUid, String userUid, String itemKey) {
        repository.setLeaveChatRoom(myUid, userUid, itemKey);
    }

    public void getLeaveChatRoom(String userUid, String myUid, String itemKey) {
        repository.getLeaveChatRoom(userUid, myUid, itemKey);
    }

    public void testDelete() {
        repository.testDelete();
    }

}
