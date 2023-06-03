package com.mirror.jmarket.model;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.databinding.BindingAdapter;

import com.mirror.jmarket.R;

import java.time.DayOfWeek;
import java.time.LocalDate;

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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @BindingAdapter("chatDate")
    public static void setChatDate(TextView textView, String dateStr) {
        String year = "년";
        String month = "월";
        String day = "일";
        String[] date = dateStr.split("-");
        LocalDate localDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int dayOfWeekNum = dayOfWeek.getValue();

        String[] days = new String[]{
                "월요일",
                "화요일",
                "수요일",
                "목요일",
                "금요일",
                "토요일",
                "일요일"};

        textView.setText(date[0] + year + " " + date[1] + month + " " + date[2] + day + " " + days[dayOfWeekNum - 1]);
    }

    @BindingAdapter("unReadChat")
    public static void unReadChat(TextView textView, boolean condi) {
        if (condi) {
            textView.setText("");
        } else {
            textView.setText("1");
        }
    }

    @BindingAdapter("unReadChatCountBackground")
    public static void setUnReadChatCountBackround(RelativeLayout relativeLayout, int unReadChatCount) {
        if (unReadChatCount > 0) {
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setVisibility(View.INVISIBLE);
        }
    }
}
