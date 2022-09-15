package com.mirror.jmarket.classes;

import android.util.Log;

public class Chat {
    private String user;
    private String message;
    private String date;
    private String time;
    private boolean checked;

    public Chat() {}

    public Chat(String user, String message, String date, String time, boolean checked) {
        this.user = user;
        this.message = message;
        this.date = date;
        this.time = time;
        this.checked = checked;
    }

    public String getUser() {
        return user;
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

    public void printChatData(String TAG) {
        Log.d(TAG, getUser() + " " + getMessage() + " " + getDate() + " " + getTime() + " " + getChecked());
    }
}
