package com.mirror.jmarket.classes;

import android.util.Log;

public class Chat {
    private String myNickName;
    private String sender;
    private String receiver;
    private String message;
    private String date;
    private String time;
    private boolean checked;

    public Chat() {}

    public Chat(String myNickName, String sender, String receiver, String message, String date, String time, boolean checked) {
        this.myNickName = myNickName;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.date = date;
        this.time = time;
        this.checked = checked;
    }

    public String getMyNickName() { return myNickName; }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() { return time; }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void printChatData(String TAG) {
        Log.d(TAG, getSender() + " " + getReceiver() + " " + getMessage() + " " + getDate() + " " + getTime() + " " + getChecked());
    }
}
