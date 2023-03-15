package com.mirror.jmarket.model;

public class LastMessage {
    private String message;
    private String user;
    private String date;
    private String time;
    private boolean checked;

    public LastMessage() {}

    public LastMessage(String message, String user, String date, String time, boolean checked) {
        this.message = message;
        this.user = user;
        this.date = date;
        this.time = time;
        this.checked = checked;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public String getDate() { return date;}

    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean getChecked() {
        return checked;
    }
}
