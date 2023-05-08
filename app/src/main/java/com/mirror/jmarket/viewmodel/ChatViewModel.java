package com.mirror.jmarket.viewmodel;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mirror.jmarket.model.Chat;
import com.mirror.jmarket.model.ChatRoom;
import com.mirror.jmarket.model.User;
import com.mirror.jmarket.repository.ChatRepository;

import java.util.HashMap;
import java.util.List;

public class ChatViewModel extends AndroidViewModel {

    private ChatRepository repository;

    private LiveData<List<HashMap<List<String>, List<Chat>>>> myChats;

    private LiveData<List<Chat>> chats;

    private LiveData<List<ChatRoom>> chatRooms;

    private LiveData<Boolean> visited;

    private LiveData<Boolean> createChatRoom;

    private LiveData<HashMap<List<String>, Integer>> unReadChatCount;

    private LiveData<Boolean> leaveChatRoom;
    private LiveData<Boolean> myLeaveChatRoom;

    public ChatViewModel(Application application) {
        super(application);
        repository = ChatRepository.getInstance(application);
        //repository = new ChatRepository(application);
        myChats = repository.getMyChats();
        chatRooms = repository.getMyChatRooms();
        visited = repository.getVisited();
        createChatRoom = repository.getCreateChatRoom();
        unReadChatCount = repository.getUnReadChatCount();
        leaveChatRoom = repository.getLeaveChatRoom();
        myLeaveChatRoom = repository.getMyLeaveChatRoom() ;
        chats = repository.getChats();


//        visited.setValue(false);
//        createChatRoom.setValue(false);
//        leaveChatRoom.setValue(false);
//        myLeaveChatRoom.setValue(false);
    }

    public LiveData<List<HashMap<List<String>, List<Chat>>>> getMyChats() { return myChats; }

    public LiveData<List<Chat>> getChats() { return chats; }

    // public MutableLiveData<List<List<Chat>>> getMyChats() { return myChats; }

    public LiveData<List<ChatRoom>> getMyChatRooms() { return chatRooms; }

    public LiveData<Boolean> getVisited() { return visited; }

    public LiveData<Boolean> getCreateChatRoom() { return createChatRoom; }

    public LiveData<HashMap<List<String>, Integer>> getUnReadChatCount() {
        return unReadChatCount;
    }

    public LiveData<Boolean> getLeaveChatRoom() { return leaveChatRoom; }

    public LiveData<Boolean> getMyLeaveChatRoom() { return myLeaveChatRoom; }

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

    public void removeMyChatsValueEventListener(String myUid, String userUid, String itemkey) {
        repository.removeMyChatsValueEventListener(myUid, userUid, itemkey);
    }

    public void removeGetVisitedValueEventListener(String userUid, String myUid, String itemKey) {
        repository.removeGetVisitedValueEventListener(userUid, myUid, itemKey);
    }

    public void removeGetAllMessageCheckedValueEventListener(String myUid, String userUid, String itemKey) {
        repository.removeGetAllMessageCheckedValueEventListener(myUid, userUid, itemKey);
    }

    public void removeGetLeaveChatRoomValueEventListener(String userUid, String myUid, String itemKey) {
        repository.removeGetLeaveChatRoomValueEventListener(userUid, myUid, itemKey);
    }

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
