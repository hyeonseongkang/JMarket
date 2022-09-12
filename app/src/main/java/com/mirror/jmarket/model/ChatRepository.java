package com.mirror.jmarket.model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.jmarket.classes.Chat;
import com.mirror.jmarket.classes.ChatRoom;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChatRepository {
    public static final String TAG = "ChatRepository";

    private Application application;

    private DatabaseReference myRef;

    private List<ChatRoom> chatRooms;

    public ChatRepository(Application application) {
        this.application = application;
        myRef = FirebaseDatabase.getInstance().getReference("chats");
        chatRooms = new ArrayList<>();
    }


    public void getChatRoom(String uid) {
        myRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Log.d(TAG, snapshot1.getValue().toString());
                }
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
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chatRooms.clear();
                boolean alreadyUser = false;
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    ChatRoom chatRoom = snapshot1.getValue(ChatRoom.class);
                    chatRooms.add(chatRoom);
                    ArrayList<String> chatRoomUsers = chatRoom.getUsers();
                    if (chatRoomUsers.contains(sellerUid)) {
                        alreadyUser = true;
                    }
                }

                if (!alreadyUser) {
                    ArrayList<String> chatRoomUsers = new ArrayList<>();
                    chatRoomUsers.add(uid);
                    chatRoomUsers.add(sellerUid);
                    String key = myRef.push().getKey();
                    myRef.child(uid).child(key).setValue(new ChatRoom(key, chatRoomUsers, null));
                    myRef.child(sellerUid).child(key).setValue(new ChatRoom(key, chatRoomUsers, null));

                    Chat chat = new Chat(uid, "test", "2022-09-22");
                    addMessage(uid, sellerUid, chat);
                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public void addMessage(String sender, String receiver, Chat chat) {
        myRef.child(sender).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    ChatRoom chatRoom = snapshot1.getValue(ChatRoom.class);
                    ArrayList<String> chatRoomUsers = chatRoom.getUsers();
                    ArrayList<Chat> chats;
                    if (chatRoom.getChats() == null) {
                        chats = new ArrayList<>();
                    } else {
                        chats = chatRoom.getChats();
                    }

                    if (chatRoomUsers.contains(receiver)) {
                        chats.add(chat);
                        ChatRoom chatRoom1 = new ChatRoom(chatRoom.getKey(), chatRoomUsers, chats);
                        myRef.child(sender).child(chatRoom.getKey()).child("chats").push().setValue(chat);
                        myRef.child(receiver).child(chatRoom.getKey()).child("chats").push().setValue(chat);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
}
