package com.mirror.jmarket.repository;

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
import com.mirror.jmarket.model.Chat;
import com.mirror.jmarket.model.ChatRoom;
import com.mirror.jmarket.model.LastMessage;
import com.mirror.jmarket.model.User;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.rxjava3.android.MainThreadDisposable;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ChatRepository {
    public static final String TAG = "ChatRepository";

    private static ChatRepository instance;

    private Application application;

    private ChatRepository(Application application) {
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

    public static synchronized ChatRepository getInstance(Application application) {
        if (instance == null) {
            instance = new ChatRepository(application);
        }
        return instance;
    }

    private DatabaseReference chatsRef;
    private DatabaseReference chatRoomsRef;
    private ValueEventListener myChatsValueEventListener;
    private ValueEventListener getVisitedValueEventListener;
    private ValueEventListener getAllMessageCheckedValueEventListener1;
    private ValueEventListener getAllMessageCheckedValueEventListener2;
    private ValueEventListener getLeaveChatRoomValueEventListener;
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


    // 채팅방에 들어갔을 때 true
    public void setVisited(String myUid, String userUid, String itemKey, boolean visit) {
        chatRoomsRef.child(myUid).child(userUid).child(itemKey).child("visited").setValue(visit);
    }

    // 상대방이 채팅방에 들어와 있는지 확인
    public void getVisited(String userUid, String myUid, String itemKey) {

        getVisitedValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(Boolean.class) != null) {
                    boolean visit = snapshot.getValue(Boolean.class);
                    visited.setValue(visit);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        chatRoomsRef.child(userUid).child(myUid).child(itemKey).child("visited").addValueEventListener(getVisitedValueEventListener);
        /*
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

         */
    }

    public void removeGetVisitedValueEventListener(String userUid, String myUid, String itemKey) {
        if (getVisitedValueEventListener != null) {
            chatRoomsRef.child(userUid).child(myUid).child(itemKey).child("visited").removeEventListener(getVisitedValueEventListener);
        }
    }

    /*
    채팅방에 입장하면 상대가 내게 보낸 모든 메시지의 checked 값을 true로 변경,
    채팅 데이터는 myUid/userUid/ChatData && userUid/myUid/ChatData 양쪽 uid 하위 모두에 저장되니 myUid 와 userUid(상대) 하위의 값을 모두 바꿔줘야함
     */
    public void allMessageChecked(String myUid, String userUid, String itemKey) {

        getAllMessageCheckedValueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);

                    if (chat.getSender().equals(userUid)) {
                        snapshot1.getRef().child("checked").setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        chatsRef.child(myUid).child(userUid).child(itemKey).addListenerForSingleValueEvent(getAllMessageCheckedValueEventListener1);
        /*
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
         */

        getAllMessageCheckedValueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);

                    if (chat.getSender().equals(userUid)) {
                        snapshot1.getRef().child("checked").setValue(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        chatsRef.child(userUid).child(myUid).child(itemKey).addListenerForSingleValueEvent(getAllMessageCheckedValueEventListener2);
        /*
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
         */

    }

    public void removeGetAllMessageCheckedValueEventListener(String myUid, String userUid, String itemKey) {
        if (getAllMessageCheckedValueEventListener1 != null) {
            chatsRef.child(userUid).child(myUid).child(itemKey).removeEventListener(getAllMessageCheckedValueEventListener1);
        }

        if (getAllMessageCheckedValueEventListener2 != null) {
            chatsRef.child(userUid).child(myUid).child(itemKey).removeEventListener(getAllMessageCheckedValueEventListener2);
        }
    }

    // 읽지 않은 채팅 수
    public void getUnReadChatCount(String myUid) {
        HashMap<List<String>, Integer> hashMap = new LinkedHashMap<>();
        // chats / myUid / .. 값들에 변경사항이 일어나면 호출
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


                        // keyList에는 나와 채팅하는 상대의 uid(userUid)와 어떤 item으로 부터 채팅이 시작됐는지 확인하기 위한 itemKey가 저장됨
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

                            // 내가 보낸 메시지가 아니라면
                            if (!(chat.getSender().equals(myUid))) {
                                // 확인을 하지 않았다면 count 증가
                                if (!(chat.getChecked())) {
                                    count++;
                                }
                            }
                            /*
                            keyList -> 상대 uid, itemKey
                            count -> 읽지 않은 채팅 수
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

    // 각 채팅방 읽지 않은 채팅 수
    public void setUnReadChatCount(String myUid, String userUid, String itemKey, int unReadChatCount) {
        chatRoomsRef.child(myUid).child(userUid).child(itemKey).child("unReadChatCount").setValue(unReadChatCount);
    }


    // 채팅방 만들기
    /*
    채팅방 db 구조

    chatRooms
              내 uid
                     상대 uid(1)
                               itemKey(1)
                                            ChatRoom
                               itemKey(2)
                                            ChatRoom
                               ....

                     상대 uid(2)
                               itemKey(1)
                                            ChatRoom
                               .....

     위와 같이 db 구조를 설계한 이유는 상대가 여러 아이템을 판매할 수 있고 아이템 하나당 하나의 채팅방이 생성돼야 하기 때문에
     내 uid 하위에 상대 uid를 놓고 상대 uid 하위에 상대가 판매하는 itemKey들을 넣음
     */
    public void setChatRoom(String uid, String sellerUid, String itemKey, ChatRoom chatRoom, User myUser, User otherUser) {
        // uid와 sellerUid가 같다면 return
        if (uid.equals(sellerUid))
            return;

        chatRoomsRef.child(uid).child(sellerUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                // 인자 값으로 넘어온 itemKey로 채팅방이 이미 만들어져 있나 확인
                boolean alreadyItemChatRoom = false;
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    if (snapshot1.getKey().equals(itemKey))
                        alreadyItemChatRoom = true;
                }

                // itemKey에 해당하는 채팅방이 만들어져 있지 않다면
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

                    // 채팅방 db에 저장
                    chatRoomsRef.child(uid).child(sellerUid).child(itemKey).setValue(chatRoom1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                chatRoomsRef.child(sellerUid).child(uid).child(itemKey).setValue(chatRoom2).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (task.isSuccessful()) createChatRoom.setValue(true);
                                        else createChatRoom.setValue(false);
                                    }
                                });
                            } else {
                                chatRoomsRef.child(uid).child(sellerUid).child(itemKey).removeValue();
                            }
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

    // 내 채팅방 리스트 가져오기
    public void getMyChatRooms(String myUid) {
        // chatRooms / myUid / 하위 Ref들에 추가, 변경, 삭제등이 감지되면 호출
        chatRoomsRef.child(myUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                // 채팅방이 추가 됐을 때
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    ChatRoom chatRoom = snapshot1.getValue(ChatRoom.class);
                    Log.d("snapshot1.key ", snapshot1.getKey());

                    // 채팅방을 만들고 실제 메시지를 남겼을 경우에만 추가
                    if (chatRoom.getLastMessage().getMessage().length() > 0) {
                        // 채팅방을 나가지 않았다면 채팅방 추가하기
                        if (!chatRoom.isLeaveChatRoom()) {
                            chatRoomList.add(0, chatRoom);
                        }
                    }
                }
                chatRooms.setValue(chatRoomList);
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                // 변경된 ref의 상위 key가 들어옴
                /*
                ex
                myUid
                    userUid1
                        itemKey1 {...}
                        itemKey2 {...}

                    userUid2
                        itemKey1 {...}

                위와 같은 구조에서 userUid1/itemKey1가 변경되면 userUid1이 snapshot으로 들어옴

                 */
                //Log.d("onChildChanged", snapshot.getRef().toString());

                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    //Log.d("onChildChanged/", snapshot1.getRef().toString());
                    ChatRoom chatRoom = snapshot1.getValue(ChatRoom.class);

                    boolean leaveChatRoom = false;
                    // chatRoomList가 비어있으면 저장
                    if (chatRoomList.size() == 0) {
                        chatRoomList.add(chatRoom);
                        continue;
                    }

                    boolean cond = true;
                    int position = -1;


                    for (int i = 0; i < chatRoomList.size(); i++) {
                        // 채팅방 리스트에서 이번에 변경된 채팅방이 존재하는지 판별
                        if (chatRoomList.get(i).getUser().getUid().equals(chatRoom.getUser().getUid()) &&
                            chatRoomList.get(i).getKey().equals(chatRoom.getKey())) {

                            // 만약 채팅방을 나갔다면
                            if (chatRoom.isLeaveChatRoom())
                                leaveChatRoom = true;

                            cond = false;
                            position = i;
                            
                            break;
                        }
                    }

                    // 채팅방을 나갔다면 채팅방 리스트에서 현재 채팅방에 해당하는 position을 삭제
                    if (leaveChatRoom) {
                        chatRoomList.remove(position);
                        continue;
                    }

                    boolean addChatRoom = false;

                    // cond == true -> 채팅방 리스트에 이번에 변경된 채팅방이 존재하지 않음
                    if (cond) {

                        if (chatRoom.getLastMessage().getMessage().length() > 0) {
                            // 마지막을 보낸 메시지가 있다면 채팅방 리스트 가장 위쪽에 추가
                            chatRoomList.add(0, chatRoom);
                            addChatRoom = true;
                        }

                    } else {
                        // 채팅방 리스트에 이번에 변경된 채팅방이 존재함
                        // A와 채팅을 하는 여러 사람들 중 가장 최근 A에게 메시지를 보낸 사람이 A의 채팅방 리스트의 가장 상단에 보여주는 부분
                        if (chatRoom.isFirstUp()) {
                            // 메시지 도착
                            chatRoomList.remove(position);
                            chatRoomList.add(0, chatRoom);
                            snapshot1.getRef().child("firstUp").setValue(false);
                            addChatRoom = true;
                        }
                    }

                    /*
                    if (addChatRoom) {
                        // 채팅방이 추가 되었다면 알림을 보냄
                        if (!chatRoom.getVisited()) {
                            // 내가 채팅방을 열고 있지 않을 경우에만 알림을 보냄
                            String userName = chatRoom.getUser().getNickName().length() > 0 ? chatRoom.getUser().getNickName() : chatRoom.getUser().getEmail();
                            if (!(chatRoom.getLastMessage().getChecked())) {
                                snapshot1.getRef().child("lastMessage").child("checked").setValue(true);
                                showChatNoti(userName, chatRoom.getLastMessage().getMessage());
                            }
                        }
                    }

                     */


                }

                chatRooms.setValue(chatRoomList);
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


    // 메시지 보내기
    /*
    채팅 db 구조
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

        String pushKey1 = chatsRef.push().getKey();
        String pushKey2 = chatsRef.push().getKey() ;
        chatsRef.child(sender).child(receiver).child(itemKey).child(pushKey1).setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    chatsRef.child(receiver).child(sender).child(itemKey).child(pushKey2).setValue(chat).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // String message, String user, String time
                                boolean checked = chat.getChecked();
                                LastMessage lastMessage = new LastMessage(chat.getMessage(), lastSendUser, dateTime[0], dateTime[1], checked);
                                chatRoomsRef.child(receiver).child(sender).child(itemKey).child("lastMessage").setValue(lastMessage);
                                chatRoomsRef.child(receiver).child(sender).child(itemKey).child("firstUp").setValue(true);

                                // 보내는 사람은 lastMessage checked -> true
                                lastMessage.setChecked(true);
                                chatRoomsRef.child(sender).child(receiver).child(itemKey).child("lastMessage").setValue(lastMessage);
                                chatRoomsRef.child(sender).child(receiver).child(itemKey).child("firstUp").setValue(true);
                            } else {
                                chatsRef.child(sender).child(receiver).child(itemKey).child(pushKey1).removeValue();
                                return;
                            }
                        }
                    });
                }
            }
        });
    }


          /*
        Service 부분으로 넘어가서 백그라운드에서 계속 동작돼야 하는 메서드임
        내 uid 하위 변경사항을 체크해야 되기 때문에 상대 uid는 빼야함
        List<List<Chat>>을 LiveData에 넣고 ChatAcitivity에서 상대 uid와 LiveData에 있는 uid를 비교해서 List<Chat> 하나만을 adpater에 넘겨야함
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

        myChatsValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    Chat chat = snapshot1.getValue(Chat.class);
                    Log.d(TAG, chat.getMessage().toString());
                    chatList.add(chat);
                }
                chats.setValue(chatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 데이터 조회가 취소되었을 때 호출되는 콜백 메서드
            }
        };
        chatsRef.child(myUid).child(userUid).child(itemKey).addValueEventListener(myChatsValueEventListener);

        /*
        // 기존
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
         */

    }

    public void removeMyChatsValueEventListener(String myUid, String userUid, String itemKey) {
        if (myChatsValueEventListener != null) {
            Log.d(TAG, "Listener 해제");
            chatsRef.child(myUid).child(userUid).child(itemKey).removeEventListener(myChatsValueEventListener);
        }
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

    // 채팅방 나가기
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

    // 채팅방 나갔는지 확인
    public void getLeaveChatRoom(String userUid, String myUid, String itemKey) {
        getLeaveChatRoomValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean value = snapshot.getValue(Boolean.class);

                if (value) {
                    leaveChatRoom.setValue(true);
                } else {
                    leaveChatRoom.setValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        chatRoomsRef.child(userUid).child(myUid).child(itemKey).child("leaveChatRoom").addValueEventListener(getLeaveChatRoomValueEventListener);
        /*
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
         */

    }

    public void removeGetLeaveChatRoomValueEventListener(String userUid, String myUid, String itemKey) {
        if (getLeaveChatRoomValueEventListener != null) {
            chatRoomsRef.child(userUid).child(myUid).child(itemKey).child("leaveChatRoom").removeEventListener(getLeaveChatRoomValueEventListener);
        }
    }
}

