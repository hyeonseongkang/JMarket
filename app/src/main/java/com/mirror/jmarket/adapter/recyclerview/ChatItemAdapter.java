package com.mirror.jmarket.adapter.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.BR;
import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.AdapterMyChatItemBinding;
import com.mirror.jmarket.databinding.AdapterUserChatItemBinding;
import com.mirror.jmarket.model.Chat;
import com.mirror.jmarket.model.ChatRoom;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context mContext;
    private List<Chat> chats = new ArrayList<>();
    private String myUid = new String();
    private String userUid = new String(); // 상대방 uid
    private String userPhoto; // 상대방 profile photo

    private String[] days;

    public ChatItemAdapter(Context context) {
        this.mContext = context;

        days = new String[]{
                mContext.getString(R.string.Monday),
                mContext.getString(R.string.Tuesday),
                mContext.getString(R.string.Wednesday),
                mContext.getString(R.string.Thursday),
                mContext.getString(R.string.Friday),
                mContext.getString(R.string.Saturday),
                mContext.getString(R.string.Sunday)};
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        AdapterMyChatItemBinding myChatItemBinding;
        AdapterUserChatItemBinding userChatItemBinding;
        // 채팅 보낸 사람에 따라 layout 나눔
        if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_my_chat_item, parent, false);
            myChatItemBinding = AdapterMyChatItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MyChatViewHolder(myChatItemBinding);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_user_chat_item, parent, false);
            userChatItemBinding = AdapterUserChatItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new OtherChatViewHolder(userChatItemBinding);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {
        Chat chat = chats.get(position);
        Chat prevChat;
        Chat nextChat;
        // 날짜 표시 visible
        String[] date = chat.getDate().split("-");

        LocalDate localDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int dayOfWeekNum = dayOfWeek.getValue();

        String year = mContext.getString(R.string.year);
        String month = mContext.getString(R.string.month);
        String day = mContext.getString(R.string.day);

        if (holder.getItemViewType() == 1) {
            MyChatViewHolder myChatViewHolder = (MyChatViewHolder) holder;
            myChatViewHolder.bind(chat);

        } else {
            OtherChatViewHolder otherChatViewHolder = (OtherChatViewHolder) holder;
            otherChatViewHolder.bind(chat);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 내가 보낸 채팅이면 1 아니면 2
        if(chats.get(position).getSender().equals(myUid)) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() { return chats == null ? 0 : chats.size(); }

    public void setChats(List<Chat> chats, String myUid, String userUid, String userPhoto) {
        this.chats = chats;
        this.myUid = myUid;
        this.userUid = userUid;
        this.userPhoto = userPhoto;
        notifyDataSetChanged();
    }

    class MyChatViewHolder extends RecyclerView.ViewHolder {

        AdapterMyChatItemBinding myChatItemBinding;

        public MyChatViewHolder(AdapterMyChatItemBinding myChatItemBinding) {
            super(myChatItemBinding.getRoot());
            this.myChatItemBinding = myChatItemBinding;

        }

        void bind(Chat chat) {
            myChatItemBinding.setVariable(BR.chat, chat);
        }
    }

    class OtherChatViewHolder extends RecyclerView.ViewHolder {

        AdapterUserChatItemBinding userChatItemBinding;


        public OtherChatViewHolder(AdapterUserChatItemBinding userChatItemBinding) {
            super(userChatItemBinding.getRoot());
            this.userChatItemBinding = userChatItemBinding;

        }

        void bind(Chat chat) {
            userChatItemBinding.setVariable(BR.chat, chat);
        }
    }
}
