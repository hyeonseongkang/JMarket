package com.mirror.jmarket.model;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

public class ChatRoom {
    private String key;
    private String userUid; // 상대방 uid
    private User user; // 상대방 user
    private Item item;
    private LastMessage lastMessage;
    private boolean visited;
    private int unReadChatCount;
    private boolean leaveChatRoom;
    private boolean firstUp;

    public ChatRoom() {}

    public ChatRoom(String key, String userUid, Item item, LastMessage lastMessage, boolean visited, int unReadChatCount, boolean leaveChatRoom, boolean firstUp) {
        this.key = key;
        this.userUid = userUid;
        this.item = item;
        this.lastMessage = lastMessage;
        this.visited = visited;
        this.unReadChatCount = unReadChatCount;
        this.leaveChatRoom = leaveChatRoom;
        this.firstUp = firstUp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public LastMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean getVisited() {
        return visited;
    }

    public void setUnReadChatCount(int count) {
        this.unReadChatCount = count;
    }

    public int getUnReadChatCount() {
        return unReadChatCount;
    }

    public boolean isLeaveChatRoom() {
        return leaveChatRoom;
    }

    public boolean isFirstUp() {
        return firstUp;
    }

    public void setFirstUp(boolean firstUp) {
        this.firstUp = firstUp;
    }

    @BindingAdapter("date")
    public static void setDate(TextView textView, LastMessage lastMessage) {
        String getToday = getDate();
        String[] getTodayString = getToday.split("-");
        String[] getSaveDateString = lastMessage.getDate().split("-");


        LocalDateTime today = null;
        LocalDateTime saveData = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            today = LocalDateTime.of(Integer.parseInt(getTodayString[0]), Integer.parseInt(getTodayString[1]), Integer.parseInt(getTodayString[2]), 0, 0, 0);
            saveData = LocalDateTime.of(Integer.parseInt(getSaveDateString[0]), Integer.parseInt(getSaveDateString[1]), Integer.parseInt(getSaveDateString[2]), 0, 0, 0);
            if (today.isAfter(saveData)) {
                textView.setText(lastMessage.getDate());
            } else {
                textView.setText(lastMessage.getTime());
            }
        } else {
            textView.setText(lastMessage.getTime());
        }
    }

    public static String getDate() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date = formatter.format(now);
        return date;
    }

    @BindingAdapter("unReadChatCount")
    public static void setUnReadChatCount(TextView textView,  int unReadChatCount) {
        textView.setText(String.valueOf(unReadChatCount));
    }

    @BindingAdapter("unReadChatCountBackground")
    public static void setUnReadChatCountBackground(RelativeLayout relativeLayout, int unReadChatCount) {
        if (unReadChatCount > 0) {
            relativeLayout.setVisibility(View.VISIBLE);
            System.out.println();
        } else {
            relativeLayout.setVisibility(View.INVISIBLE);
        }
    }

    @BindingAdapter("itemImageUri")
    public static void loadItemImage(ImageView imageView, String photoUri) {
        Glide.with(imageView.getContext())
                .load(Uri.parse(photoUri))
                .into(imageView);
    }

    @BindingAdapter("profileImageUri")
    public static void loadProfileImage(ImageView imageView, String photoUri) {
        Glide.with(imageView.getContext())
                .load(Uri.parse(photoUri))
                .into(imageView);
    }
}
