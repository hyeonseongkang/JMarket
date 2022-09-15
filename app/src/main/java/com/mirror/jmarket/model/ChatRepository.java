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
import com.mirror.jmarket.classes.ChatTemp;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRepository {
    public static final String TAG = "ChatRepository";

    private Application application;

    private DatabaseReference chatsRef;
    private DatabaseReference usersRef;
    private DatabaseReference chatRoomsRef;


    private List<List<Chat>> myChats;

    private MutableLiveData<List<ChatRoom>> chatRooms;
    private List<ChatRoom> chatRoomList;


    public ChatRepository(Application application) {
        this.application = application;
        chatsRef = FirebaseDatabase.getInstance().getReference("chats");
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        chatRoomsRef = FirebaseDatabase.getInstance().getReference("chatRooms");
        myChats = new ArrayList<>();

        chatRooms = new MutableLiveData<>();
        chatRoomList = new ArrayList<>();

    }

    public MutableLiveData<List<ChatRoom>> getMyChatRooms() { return chatRooms;}


    public void setChatRoom(String uid, String sellerUid, ChatRoom chatRoom) {

        if (uid.equals(sellerUid))
            return;

        chatRoomsRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
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
                    chatRoom1.setDate(chatRoom.getDate());
                    chatRoom1.setLastMessage(chatRoom.getLastMessage());
                    chatRoom1.setUid(sellerUid);
                    ChatRoom chatRoom2 = new ChatRoom();
                    chatRoom2.setItem(chatRoom.getItem());
                    chatRoom2.setDate(chatRoom.getDate());
                    chatRoom2.setLastMessage(chatRoom.getLastMessage());
                    chatRoom2.setUid(uid);
                    String currentDate = getDate();
                    chatRoom.setDate(currentDate);
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
                    System.out.println("1212112" + " " + snapshot1.getValue(ChatRoom.class));
                    ChatRoom chatRoom = snapshot1.getValue(ChatRoom.class);
                    chatRoomList.add(chatRoom);
                    Log.d(TAG, chatRoom.getDate() + " GetChatRoom");
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

        /*
                chatsRef.child("vO3Igea5wFb8SutxiQMVDgTG1iJ2").child("TkzEZBw8iTdQGU7ppquiaS4ZOR73").push().setValue(chat1);
        chatsRef.child("vO3Igea5wFb8SutxiQMVDgTG1iJ2").child("TkzEZBw8iTdQGU7ppquiaS4ZOR73").push().setValue(chat2);
        chatsRef.child("vO3Igea5wFb8SutxiQMVDgTG1iJ2").child("TkzEZBw8iTdQGU7ppquiaS4ZOR73").push().setValue(chat3);
         */
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getMyChats(String uid) {
        List<List<Chat>> chatList = new ArrayList<>();

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

        chatsRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Log.d("1", String.valueOf(snapshot.getValue()));
                myChats.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Log.d(TAG, snapshot1.getKey());
                    List<Chat> temp = new ArrayList<>();
                    Log.d("ChatValue", snapshot1.getValue().toString());

                    for (DataSnapshot snapshot2: snapshot1.getChildren()) {
                        Chat chat = snapshot2.getValue(Chat.class);
                        chat.printChatData("HELLO!!");
                        temp.add(chat);
                    }
                    myChats.add(temp);
                }

                for (List<Chat> chats: myChats) {
                    for (Chat chat: chats) {
                        chat.printChatData("END");
                    }
                }
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

