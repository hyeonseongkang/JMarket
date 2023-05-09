package com.mirror.jmarket.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mirror.jmarket.R;
import com.mirror.jmarket.model.ChatRoom;
import com.mirror.jmarket.view.activity.MainActivity;

import org.jetbrains.annotations.NotNull;

public class ChatService extends Service {

    private static String TAG = ChatService.class.getSimpleName();



    public ChatService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //showChatNotification();
        String myUid = intent.getStringExtra("myUid");
        chatNoti(myUid);
        return START_STICKY;
      //  return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }



    // 내 채팅방 리스트 가져오기
    public void chatNoti(String myUid) {
        // chatRooms / myUid / 하위 Ref들에 추가, 변경, 삭제등이 감지되면 호출
        DatabaseReference chatRoomsRef = FirebaseDatabase.getInstance().getReference("chatRooms");
        chatRoomsRef.child(myUid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {


                for (DataSnapshot snapshot1: snapshot.getChildren()) {
                    //Log.d("onChildChanged/", snapshot1.getRef().toString());
                    ChatRoom chatRoom = snapshot1.getValue(ChatRoom.class);
                    if (chatRoom.getLastMessage().getMessage().length() > 0 || chatRoom.isFirstUp()) {
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

    public void showChatNoti(String senderUser, String message) {
        try {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                String channelID = "channel_01";
                String channelName = "chat_channel_id";

                NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);

                notificationManager.createNotificationChannel(channel);

                builder = new NotificationCompat.Builder(this, channelID);
            }

            builder.setSmallIcon(android.R.drawable.ic_menu_view);

            builder.setContentTitle(senderUser);
            builder.setContentText(message);
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.chat);
            builder.setLargeIcon(bm);

            Notification notification = builder.build();
            notificationManager.notify(1, notification);
        } catch (Exception e) {

        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}