package com.mirror.jmarket.model;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.jmarket.R;
import com.mirror.jmarket.classes.Chat;
import com.mirror.jmarket.classes.ChatRoom;
import com.mirror.jmarket.classes.LastMessage;
import com.mirror.jmarket.classes.User;
import com.mirror.jmarket.view.MyPageFragment;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class ChatRepository {
    public static final String TAG = "ChatRepository";

    private Application application;

    private DatabaseReference chatsRef;
    private DatabaseReference chatRoomsRef;

    private MutableLiveData<List<HashMap<List<String>, List<Chat>>>> myChats;
    private List<HashMap<List<String>, List<Chat>>> myChatList;

    private MutableLiveData<List<Chat>> chats;
    private List<Chat> chatList;

    private MutableLiveData<List<ChatRoom>> chatRooms;
    private List<ChatRoom> chatRoomList;

    private MutableLiveData<Boolean> visited;

    private MutableLiveData<Boolean> createChatRoom;

    private MutableLiveData<HashMap<List<String>, Integer>> unReadChatCount;

    private MutableLiveData<Boolean> leaveChatRoom;
    private MutableLiveData<Boolean> myLeaveChatRoom;

    public ChatRepository(Application application) {
        this.application = application;
        chatsRef = FirebaseDatabase.getInstance().getReference("chats");
        chatRoomsRef = FirebaseDatabase.getInstance().getReference("chatRooms");

        myChats = new MutableLiveData<>();
        myChatList = new ArrayList<>();

        chats = new MutableLiveData<>();
        chatList = new ArrayList<>();

        chatRooms = new MutableLiveData<>();
        chatRoomList = new ArrayList<>();

        visited = new MutableLiveData<>();

        createChatRoom = new MutableLiveData<>();

        unReadChatCount = new MutableLiveData<>();

        leaveChatRoom = new MutableLiveData<>();

        myLeaveChatRoom = new MutableLiveData<>();

    }

    public MutableLiveData<List<HashMap<List<String>, List<Chat>>>> getMyChats() {
        return myChats;
    }

    public MutableLiveData<List<Chat>> getChats() { return chats; }

    public MutableLiveData<List<ChatRoom>> getMyChatRooms() {
        return chatRooms;
    }

    public MutableLiveData<Boolean> getCreateChatRoom() {
        return createChatRoom;
    }

    public MutableLiveData<Boolean> getVisited() {
        return visited;
    }

    public MutableLiveData<HashMap<List<String>, Integer>> getUnReadChatCount() { return unReadChatCount; }

    public MutableLiveData<Boolean> getLeaveChatRoom() {
        return leaveChatRoom;
    }

    public MutableLiveData<Boolean> getMyLeaveChatRoom() {
        return myLeaveChatRoom;
    }


    // ???????????? ???????????? ??? true
    public void setVisited(String myUid, String userUid, String itemKey, boolean visit) {
        chatRoomsRef.child(myUid).child(userUid).child(itemKey).child("visited").setValue(visit);
    }

    // ???????????? ???????????? ????????? ????????? ??????
    public void getVisited(String userUid, String myUid, String itemKey) {
        chatRoomsRef.child(userUid).child(myUid).child(itemKey).child("visited").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.getValue(Boolean.class) != null) {
                    boolean visit = snapshot.getValue(Boolean.class);
                    visited.setValue(visit);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    /*
    ???????????? ???????????? ????????? ?????? ?????? ?????? ???????????? checked ?????? true??? ??????,
    ?????? ???????????? myUid/userUid/ChatData && userUid/myUid/ChatData ?????? uid ?????? ????????? ???????????? myUid ??? userUid(??????) ????????? ?????? ?????? ???????????????
     */
    public void allMessageChecked(String myUid, String userUid, String itemKey) {
        chatsRef.child(myUid).child(userUid).child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);

                    if (chat.getSender().equals(userUid)) {
                        snapshot1.getRef().child("checked").setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        chatsRef.child(userUid).child(myUid).child(itemKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);

                    if (chat.getSender().equals(userUid)) {
                        snapshot1.getRef().child("checked").setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    // ?????? ?????? ?????? ???
    public void getUnReadChatCount(String myUid) {
        HashMap<List<String>, Integer> hashMap = new LinkedHashMap<>();
        // chats / myUid / .. ????????? ??????????????? ???????????? ??????
        /*
        chats
                myUid
                        userUid(1)
                                    itemKey(1)
                                               pushKey(1)
                                                          Chat(class)
                                               pushKey(2)
                                                          Chat(class)
                                               pushKey(3)
                                                          Chat(class)

                                    itemKey(2)
                                               pushKey(1)
                                                          Chat(class)
                                    ...
         */
        chatsRef.child(myUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                // userUids
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                    // itemKeys
                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                        int count = 0;


                        // keyList?????? ?????? ???????????? ????????? uid(userUid)??? ?????? item?????? ?????? ????????? ??????????????? ???????????? ?????? itemKey??? ?????????
                        List<String> keyList = new ArrayList<>();
                        keyList.add(snapshot1.getKey());
                        boolean first = true;

                        // pushKeys
                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                            Chat chat = snapshot3.getValue(Chat.class);
                            if (first) {
                                // itemKey
                                keyList.add(snapshot2.getKey());
                                first = false;
                            }

                            // ?????? ?????? ???????????? ????????????
                            if (!(chat.getSender().equals(myUid))) {
                                // ????????? ?????? ???????????? count ??????
                                if (!(chat.getChecked())) {
                                    count++;
                                }
                            }
                            /*
                            keyList -> ?????? uid, itemKey
                            count -> ?????? ?????? ?????? ???
                             */
                            hashMap.put(keyList, count);
                        }

                    }
                }
                unReadChatCount.setValue(hashMap);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    // ??? ????????? ?????? ?????? ?????? ???
    public void setUnReadChatCount(String myUid, String userUid, String itemKey, int unReadChatCount) {
        chatRoomsRef.child(myUid).child(userUid).child(itemKey).child("unReadChatCount").setValue(unReadChatCount);
    }


    // ????????? ?????????
    /*
    ????????? db ??????

    chatRooms
              ??? uid
                     ?????? uid(1)
                               itemKey(1)
                                            ChatRoom
                               itemKey(2)
                                            ChatRoom
                               ....

                     ?????? uid(2)
                               itemKey(1)
                                            ChatRoom
                               .....

     ?????? ?????? db ????????? ????????? ????????? ????????? ?????? ???????????? ????????? ??? ?????? ????????? ????????? ????????? ???????????? ???????????? ?????? ????????? ??? uid ????????? ?????? uid??? ?????? ?????? uid ????????? ????????? ???????????? itemKey?????? ??????
     */
    public void setChatRoom(String uid, String sellerUid, String itemKey, ChatRoom chatRoom, User myUser, User otherUser) {
        // uid??? sellerUid??? ????????? return
        if (uid.equals(sellerUid))
            return;

        chatRoomsRef.child(uid).child(sellerUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                // ?????? ????????? ????????? itemKey??? ???????????? ?????? ???????????? ?????? ??????
                boolean alreadyItemChatRoom = false;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.getKey().equals(itemKey))
                        alreadyItemChatRoom = true;
                }

                // itemKey??? ???????????? ???????????? ???????????? ?????? ?????????
                if (!alreadyItemChatRoom) {
                    ChatRoom chatRoom1 = new ChatRoom();
                    chatRoom1.setKey(itemKey);
                    chatRoom1.setItem(chatRoom.getItem());
                    chatRoom1.setLastMessage(chatRoom.getLastMessage());
                    chatRoom1.setUser(otherUser);
                    chatRoom1.setVisited(true);
                    ChatRoom chatRoom2 = new ChatRoom();
                    chatRoom2.setKey(itemKey);
                    chatRoom2.setItem(chatRoom.getItem());
                    chatRoom2.setLastMessage(chatRoom.getLastMessage());
                    chatRoom2.setUser(myUser);

                    // ????????? db??? ??????
                    chatRoomsRef.child(uid).child(sellerUid).child(itemKey).setValue(chatRoom1);
                    chatRoomsRef.child(sellerUid).child(uid).child(itemKey).setValue(chatRoom2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) createChatRoom.setValue(true);
                            else createChatRoom.setValue(false);
                        }
                    });
                } else {
                    createChatRoom.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public void testDelete() {
        chatsRef.removeValue();
        chatRoomsRef.removeValue();
    }

    // ??? ????????? ????????? ????????????
    public void getMyChatRooms(String myUid) {
        // chatRooms / myUid / ?????? Ref?????? ??????, ??????, ???????????? ???????????? ??????
        chatRoomsRef.child(myUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                // ???????????? ?????? ?????? ???
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ChatRoom chatRoom = snapshot1.getValue(ChatRoom.class);
                    Log.d("snapshot1.key ", snapshot1.getKey());

                    // ???????????? ????????? ?????? ???????????? ????????? ???????????? ??????
                    if (chatRoom.getLastMessage().getMessage().length() > 0) {
                        // ???????????? ????????? ???????????? ????????? ????????????
                        if (!chatRoom.isLeaveChatRoom()) {
                            chatRoomList.add(0, chatRoom);
                        }
                    }
                }
                chatRooms.setValue(chatRoomList);
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                // ????????? ref??? ?????? key??? ?????????
                /*
                ex
                myUid
                    userUid1
                        itemKey1 {...}
                        itemKey2 {...}

                    userUid2
                        itemKey1 {...}

                ?????? ?????? ???????????? userUid1/itemKey1??? ???????????? userUid1??? snapshot?????? ?????????

                 */
                //Log.d("onChildChanged", snapshot.getRef().toString());

                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    //Log.d("onChildChanged/", snapshot1.getRef().toString());
                    ChatRoom chatRoom = snapshot1.getValue(ChatRoom.class);

                    boolean leaveChatRoom = false;
                    // chatRoomList??? ??????????????? ??????
                    if (chatRoomList.size() == 0) {
                        chatRoomList.add(chatRoom);
                        continue;
                    }

                    boolean cond = true;
                    int position = -1;


                    for (int i = 0; i < chatRoomList.size(); i++) {
                        // ????????? ??????????????? ????????? ????????? ???????????? ??????????????? ??????
                        if (chatRoomList.get(i).getUser().getUid().equals(chatRoom.getUser().getUid()) &&
                            chatRoomList.get(i).getKey().equals(chatRoom.getKey())) {

                            // ?????? ???????????? ????????????
                            if (chatRoom.isLeaveChatRoom())
                                leaveChatRoom = true;

                            cond = false;
                            position = i;
                            
                            break;
                        }
                    }

                    // ???????????? ???????????? ????????? ??????????????? ?????? ???????????? ???????????? position??? ??????
                    if (leaveChatRoom) {
                        chatRoomList.remove(position);
                        continue;
                    }

                    boolean addChatRoom = false;

                    // cond == true -> ????????? ???????????? ????????? ????????? ???????????? ???????????? ??????
                    if (cond) {

                        if (chatRoom.getLastMessage().getMessage().length() > 0) {
                            // ???????????? ?????? ???????????? ????????? ????????? ????????? ?????? ????????? ??????
                            chatRoomList.add(0, chatRoom);
                            addChatRoom = true;
                        }

                    } else {
                        // ????????? ???????????? ????????? ????????? ???????????? ?????????
                        // A??? ????????? ?????? ?????? ????????? ??? ?????? ?????? A?????? ???????????? ?????? ????????? A??? ????????? ???????????? ?????? ????????? ???????????? ??????
                        if (chatRoom.isFirstUp()) {
                            // ????????? ??????
                            chatRoomList.remove(position);
                            chatRoomList.add(0, chatRoom);
                            snapshot1.getRef().child("firstUp").setValue(false);
                            addChatRoom = true;
                        }
                    }

                    if (addChatRoom) {
                        // ???????????? ?????? ???????????? ????????? ??????
                        if (!chatRoom.getVisited()) {
                            // ?????? ???????????? ?????? ?????? ?????? ???????????? ????????? ??????
                            String userName = chatRoom.getUser().getNickName().length() > 0 ? chatRoom.getUser().getNickName() : chatRoom.getUser().getEmail();
                            if (!(chatRoom.getLastMessage().getChecked())) {
                                snapshot1.getRef().child("lastMessage").child("checked").setValue(true);
                                showChatNoti(userName, chatRoom.getLastMessage().getMessage());
                            }
                        }
                    }
                }

                chatRooms.setValue(chatRoomList);
                for (int i = 0; i < chatRoomList.size(); i++) {
                    System.out.println(i + "?????? ????????? " + chatRoomList.get(i).getKey());
                }


            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    // ????????? ?????????
    /*
    ?????? db ??????
    chats
            myUid
                    userUid
                            itemKey(1)
                                        push(1)
                                                Chat
                                        push(2)
                                                Chat
                                        push(3)
                                                Chat
                                        ....
                            itemKey(2)
                                        push(1)
                                                Chat
                                        push(2)
                                                Chat
                                        push(3)
                                                Chat
                                        ....
     */
    public void sendMessage(String sender, String receiver, String itemKey, Chat chat, String lastSendUser) {
        String[] dateTime = getDate().split(" ");
        chat.setDate(dateTime[0]);
        chat.setTime(dateTime[1]);

        chatsRef.child(sender).child(receiver).child(itemKey).push().setValue(chat);
        chatsRef.child(receiver).child(sender).child(itemKey).push().setValue(chat);

        // String message, String user, String time
        boolean checked = chat.getChecked();
        LastMessage lastMessage = new LastMessage(chat.getMessage(), lastSendUser, dateTime[0], dateTime[1], checked);
        chatRoomsRef.child(receiver).child(sender).child(itemKey).child("lastMessage").setValue(lastMessage);
        chatRoomsRef.child(receiver).child(sender).child(itemKey).child("firstUp").setValue(true);

        // ????????? ????????? lastMessage checked -> true
        lastMessage.setChecked(true);
        chatRoomsRef.child(sender).child(receiver).child(itemKey).child("lastMessage").setValue(lastMessage);
        chatRoomsRef.child(sender).child(receiver).child(itemKey).child("firstUp").setValue(true);
    }


          /*
        Service ???????????? ???????????? ????????????????????? ?????? ???????????? ?????? ????????????
        ??? uid ?????? ??????????????? ???????????? ?????? ????????? ?????? uid??? ?????????
        List<List<Chat>>??? LiveData??? ?????? ChatAcitivity?????? ?????? uid??? LiveData??? ?????? uid??? ???????????? List<Chat> ???????????? adpater??? ????????????
         */

    public void getMyChats(String myUid) {
        chatsRef.child(myUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                myChatList.clear();
                // userUids
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    // itemKeys
                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                        HashMap<List<String>, List<Chat>> hashMap = new HashMap<>();
                        List<String> keys = new ArrayList<>();
                        keys.add(snapshot1.getKey()); // userUid
                        keys.add(snapshot2.getKey()); // itemKey;
                        List<Chat> chats = new ArrayList<>();
                        // chats
                        for (DataSnapshot snapshot3 : snapshot2.getChildren()) {
                            Chat chat = snapshot3.getValue(Chat.class);
                            chats.add(chat);
                        }
                        hashMap.put(keys, chats);
                        myChatList.add(hashMap);
                    }
                }
                myChats.setValue(myChatList);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    public void getMyChats(String myUid, String userUid, String itemKey) {
        chatsRef.child(myUid).child(userUid).child(itemKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);
                    chatList.add(chat);
                }
                chats.setValue(chatList);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }


    public String getDate() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date = formatter.format(now);
        return date;
    }

    public void showChatNoti(String senderUser, String message) {
        try {
            NotificationManager notificationManager = (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                String channelID = "channel_01";
                String channelName = "MyChannel01";

                NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);

                notificationManager.createNotificationChannel(channel);

                builder = new NotificationCompat.Builder(application, channelID);
            }

            builder.setSmallIcon(android.R.drawable.ic_menu_view);

            builder.setContentTitle(senderUser);
            builder.setContentText(message);
            Bitmap bm = BitmapFactory.decodeResource(application.getResources(), R.drawable.chat);
            builder.setLargeIcon(bm);

            Notification notification = builder.build();
            notificationManager.notify(1, notification);
        } catch (Exception e) {

        }

    }

    // ????????? ?????????
    public void setLeaveChatRoom(String myUid, String userUid, String itemKey) {
        chatRoomsRef.child(myUid).child(userUid).child(itemKey).child("leaveChatRoom").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    myLeaveChatRoom.setValue(true);
                }
            }
        });
    }

    // ????????? ???????????? ??????
    public void getLeaveChatRoom(String userUid, String myUid, String itemKey) {
        chatRoomsRef.child(userUid).child(myUid).child(itemKey).child("leaveChatRoom").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                boolean value = snapshot.getValue(Boolean.class);

                if (value) {
                    leaveChatRoom.setValue(true);
                } else {
                    leaveChatRoom.setValue(false);
                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
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

