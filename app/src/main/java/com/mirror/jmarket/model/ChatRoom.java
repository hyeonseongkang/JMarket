package com.mirror.jmarket.model;

import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

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

    @BindingAdapter("unReadChatCount")
    public static void setUnReadChatCount(TextView textView,  int unReadChatCount) {
        textView.setText(String.valueOf(unReadChatCount));
    }

    @BindingAdapter("unReadChatCountBackground")
    public static void setUnReadChatCountBackround(RelativeLayout relativeLayout, int unReadChatCount) {
        if (unReadChatCount > 0) {
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setVisibility(View.INVISIBLE);
        }
    }

    @BindingAdapter("itemImageUri")
    public static void loadItemImage(ImageView imageView, String photoUri) {
        Log.d("HEllO", photoUri);
        Glide.with(imageView.getContext())
                .load(Uri.parse(photoUri))
                .into(imageView);
    }

    @BindingAdapter("profileImageUri")
    public static void loadProfileImage(ImageView imageView, String photoUri) {
        Log.d("ChatRoom", photoUri);
        Glide.with(imageView.getContext())
                .load(Uri.parse(photoUri))
                .into(imageView);
    }
}
