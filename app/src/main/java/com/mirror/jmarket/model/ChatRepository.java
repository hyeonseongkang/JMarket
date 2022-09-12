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

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatRepository {
    public static final String TAG = "ChatRepository";

    private Application application;

    private DatabaseReference myRef;

    private List<ChatRoom> chatRooms;
    private List<Chat> chats;

    private MutableLiveData<List<String>> chatUsers;
    private ArrayList<String> users;

    public ChatRepository(Application application) {
        this.application = application;
        myRef = FirebaseDatabase.getInstance().getReference("chats");
        chatRooms = new ArrayList<>();
        chats = new ArrayList<>();
        chatUsers = new MutableLiveData<>();
        users = new ArrayList<>();
    }

    public MutableLiveData<List<String>> getChatUsers() {return chatUsers; }


    public void getChatRoom(String uid) {
        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    users.add(snapshot1.getKey());
                }
                chatUsers.setValue(users);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void setChatRoom(String uid, String sellerUid) {

        if (uid.equals(sellerUid))
            return;

        myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chatRooms.clear();
                chats.clear();
                boolean alreadyUser = false;
                Log.d(TAG, String.valueOf(alreadyUser));
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);
                    chats.add(chat);
                    if (snapshot1.getKey().equals(sellerUid)) {
                        alreadyUser = true;
                    }
                }
                Log.d(TAG, String.valueOf(alreadyUser));
                if (!alreadyUser) {
                    Log.d(TAG, "Hello!");
                    String date = getDate();
                    myRef.child(uid).child(sellerUid).child("createDate").setValue(date);
                    myRef.child(sellerUid).child(uid).child("createDate").setValue(date);
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public void sendMessage(String sender, String receiver, Chat chat) {
        myRef.child(sender).child(receiver).child("chats").push().setValue(chat);
        myRef.child(receiver).child(sender).child("chats").push().setValue(chat);

//        myRef.child(sender).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                for (DataSnapshot snapshot1: snapshot.getChildren()) {
//                    ChatRoom chatRoom = snapshot1.getValue(ChatRoom.class);
//                    ArrayList<String> chatRoomUsers = chatRoom.getUsers();
//                    ArrayList<Chat> chats;
//                    if (chatRoom.getChats() == null) {
//                        chats = new ArrayList<>();
//                    } else {
//                        chats = chatRoom.getChats();
//                    }
//
//                    if (chatRoomUsers.contains(receiver)) {
//                        chats.add(chat);
//                        ChatRoom chatRoom1 = new ChatRoom(chatRoom.getKey(), chatRoomUsers, chats);
//                        myRef.child(sender).child(chatRoom.getKey()).child("chats").push().setValue(chat);
//                        myRef.child(receiver).child(chatRoom.getKey()).child("chats").push().setValue(chat);
//                        break;
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//
//            }
//        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDate() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = now.format(formatter);
        return date;
    }
}
