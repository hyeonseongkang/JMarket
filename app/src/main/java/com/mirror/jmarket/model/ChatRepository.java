package com.mirror.jmarket.model;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.jmarket.classes.Chat;
import com.mirror.jmarket.classes.ChatRoom;
import com.mirror.jmarket.classes.LastMessage;
import com.mirror.jmarket.classes.User;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChatRepository {
    public static final String TAG = "ChatRepository";

    private Application application;

    private DatabaseReference chatsRef;
    private DatabaseReference usersRef;
    private DatabaseReference chatRoomsRef;


    private MutableLiveData<List<HashMap<String, List<Chat>>>> myChats;
    private List<HashMap<String, List<Chat>>> myChatList;
    /*
     private MutableLiveData<List<List<Chat>>> myChats;
    private List<List<Chat>> myChatList;
     */

    private MutableLiveData<List<ChatRoom>> chatRooms;
    private List<ChatRoom> chatRoomList;


    public ChatRepository(Application application) {
        this.application = application;
        chatsRef = FirebaseDatabase.getInstance().getReference("chats");
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        chatRoomsRef = FirebaseDatabase.getInstance().getReference("chatRooms");

        myChats = new MutableLiveData<>();
        myChatList = new ArrayList<>();

        chatRooms = new MutableLiveData<>();
        chatRoomList = new ArrayList<>();

    }

    public MutableLiveData<List<HashMap<String, List<Chat>>>> getMyChats() { return myChats; }

    // public MutableLiveData<List<List<Chat>>> getMyChats() { return myChats; }

    public MutableLiveData<List<ChatRoom>> getMyChatRooms() { return chatRooms;}


    public void setChatRoom(String uid, String sellerUid, ChatRoom chatRoom, User myUser, User otherUser) {

        if (uid.equals(sellerUid))
            return;

        chatRoomsRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                boolean alreadyUser = false;
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    if (snapshot1.getKey().equals(sellerUid))
                        alreadyUser = true;
                }

                if (!alreadyUser) {
                    ChatRoom chatRoom1 = new ChatRoom();
                    chatRoom1.setItem(chatRoom.getItem());
                    chatRoom1.setLastMessage(chatRoom.getLastMessage());
                    chatRoom1.setUser(otherUser);
                    ChatRoom chatRoom2 = new ChatRoom();
                    chatRoom2.setItem(chatRoom.getItem());
                    chatRoom2.setLastMessage(chatRoom.getLastMessage());
                    chatRoom2.setUser(myUser);
                    chatRoomsRef.child(uid).child(sellerUid).setValue(chatRoom1);
                    chatRoomsRef.child(sellerUid).child(uid).setValue(chatRoom2);
//                    chatsRef.child(uid).child(sellerUid).child("createDate").setValue(currentDate);
//                    chatsRef.child(sellerUid).child(uid).child("createDate").setValue(currentDate);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void getMyChatRooms(String uid) {
        chatRoomsRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chatRoomList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    ChatRoom chatRoom = snapshot1.getValue(ChatRoom.class);
                    Log.d("ChatRooms", "modification");
                    chatRoomList.add(chatRoom);
                }
                chatRooms.setValue(chatRoomList);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void sendMessage(String sender, String receiver, Chat chat, String lastSendUser) {
        String[] dateTime = getDate().split(" ");
        chat.setDate(dateTime[0]);
        chat.setTime(dateTime[1]);
        chatsRef.child(sender).child(receiver).push().setValue(chat);
        chatsRef.child(receiver).child(sender).push().setValue(chat);

        // String message, String user, String time
        LastMessage lastMessage = new LastMessage(chat.getMessage(), lastSendUser, dateTime[1]);
        chatRoomsRef.child(sender).child(receiver).child("lastMessage").setValue(lastMessage);
        chatRoomsRef.child(receiver).child(sender).child("lastMessage").setValue(lastMessage);


        /*
                chatsRef.child("vO3Igea5wFb8SutxiQMVDgTG1iJ2").child("TkzEZBw8iTdQGU7ppquiaS4ZOR73").push().setValue(chat1);
        chatsRef.child("vO3Igea5wFb8SutxiQMVDgTG1iJ2").child("TkzEZBw8iTdQGU7ppquiaS4ZOR73").push().setValue(chat2);
        chatsRef.child("vO3Igea5wFb8SutxiQMVDgTG1iJ2").child("TkzEZBw8iTdQGU7ppquiaS4ZOR73").push().setValue(chat3);
         */
    }

    public void getMyChats(String myUid) {
        /*
        // Database Test
        // String user, String message, String date, boolean checked
        String[] c1 = getDate().split(" ");
        String[] c2 = getDate().split(" ");
        String[] c3 = getDate().split(" ");

        Chat chat1 = new Chat("vO3Igea5wFb8SutxiQMVDgTG1iJ2", "테스트1", c1[0], c1[1], false);
        Chat chat2 = new Chat("TkzEZBw8iTdQGU7ppquiaS4ZOR73", "테스트2", c2[0], c2[1], false);
        Chat chat3 = new Chat("vO3Igea5wFb8SutxiQMVDgTG1iJ2", "테스트3", c3[0], c3[1], false);
        chatsRef.child("vO3Igea5wFb8SutxiQMVDgTG1iJ2").child("TkzEZBw8iTdQGU7ppquiaS4ZOR73").push().setValue(chat1);
        chatsRef.child("vO3Igea5wFb8SutxiQMVDgTG1iJ2").child("TkzEZBw8iTdQGU7ppquiaS4ZOR73").push().setValue(chat2);
        chatsRef.child("vO3Igea5wFb8SutxiQMVDgTG1iJ2").child("TkzEZBw8iTdQGU7ppquiaS4ZOR73").push().setValue(chat3);
         */

        /*
        Service 부분으로 넘어가서 백그라운드에서 계속 동작돼야 하는 메서드임
        내 uid 하위 변경사항을 체크해야 되기 때문에 상대 uid는 빼야함
        List<List<Chat>>을 LiveData에 넣고 ChatAcitivity에서 상대 uid와 LiveData에 있는 uid를 비교해서 List<Chat> 하나만을 adpater에 넘겨야함
         */
        chatsRef.child(myUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                myChatList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Log.d(TAG, snapshot1.getKey());
                    List<Chat> chats = new ArrayList<>();
                    for (DataSnapshot snapshot2: snapshot1.getChildren()) {
                        Chat chat = snapshot2.getValue(Chat.class);
                        chats.add(chat);
                    }
                    HashMap<String, List<Chat>> hashMap = new HashMap<>();
                    hashMap.put(snapshot1.getKey(), chats);

                    myChatList.add(hashMap);
                }

                myChats.setValue(myChatList);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public String getDate() {
        /*
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String date = now.format(formatter);
         */
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = formatter.format(now);
        return date;
    }

    /*
    private class GetMyChatUsersTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... uid) {
            Log.d("TASK", "2");
            usersRef.child(uid[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    User tempUser = snapshot.getValue(User.class);
                    Log.d("TASK", tempUser.getEmail());
                    myChatUsers.add(tempUser);
                }


                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void val) {
            super.onPostExecute(val);
            Log.d("TASK", "END");
        }
    }
    */
}

