package com.mirror.jmarket.adapter;

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
            myChatViewHolder.dateLayout.setVisibility(View.VISIBLE);
            myChatViewHolder.date.setVisibility(View.VISIBLE);
            myChatViewHolder.time.setVisibility(View.VISIBLE);
            myChatViewHolder.date.setText(date[0] + year + " " + date[1] + month + " " + date[2] + day + " " + days[dayOfWeekNum - 1]);

            // 채팅 데이터가 1개 이상일때
            if (position > 0) {
                // (position - 1)번째 채팅 데이터와 position번째 채팅 데이터를 비교해 같은 유저가 보낸 채팅일 경우
                prevChat = chats.get(position - 1);

                // position 채팅 데이터의 날짜와 이전 채팅 데이터의 날짜가 같다면 현재 position에 해당하는 view의 date는 숨김
                if (chat.getDate().equals(prevChat.getDate())) {
                    myChatViewHolder.dateLayout.setVisibility(View.GONE);
                }
            }

            // 현재 position + 1가 전체 채팅 사이즈 보다 크다면 nextChat을 가져옴
            if (chats.size() > position + 1) {
                nextChat = chats.get(position + 1);
                // 현재 position의 chat time과 다음 position의 chat time이 같으면서 보낸 사람이 같을 경우 현재 view에는 시간 표시 안함 -> 마지막 chat data의 시간만 표시
                if (chat.getTime().equals(nextChat.getTime()) && chat.getSender().equals(nextChat.getSender())) {
                    myChatViewHolder.time.setVisibility(View.GONE);
                }
            }

            myChatViewHolder.message.setText(chat.getMessage());
            myChatViewHolder.time.setText(chat.getTime());

            myChatViewHolder.messageChecked.setText(chat.getChecked() == true ? "" : "1");

        } else {
            OtherChatViewHolder otherChatViewHolder = (OtherChatViewHolder) holder;
            otherChatViewHolder.bind(chat);
            otherChatViewHolder.dateLayout.setVisibility(View.VISIBLE);
            otherChatViewHolder.date.setVisibility(View.VISIBLE);
            otherChatViewHolder.userNickName.setVisibility(View.VISIBLE);
            otherChatViewHolder.time.setVisibility(View.VISIBLE);
            otherChatViewHolder.date.setText(date[0] + year + " " + date[1] + month + " " + date[2] + day + " " + days[dayOfWeekNum - 1]);

            otherChatViewHolder.userPhoto.setVisibility(View.VISIBLE);

            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.basic_profile_photo)
                    .into(otherChatViewHolder.userPhoto);

            if (userPhoto.length() > 0 && !userPhoto.equals("null")) {
                Glide.with(holder.itemView.getContext())
                        .load(Uri.parse(userPhoto))
                        .into(otherChatViewHolder.userPhoto);
            }

            // 채팅 데이터가 1개 이상일때
            if (position > 0) {
                // (position - 1)번째 채팅 데이터와 position번째 채팅 데이터를 비교해 같은 유저가 보낸 채팅일 경우
                prevChat = chats.get(position - 1);
                if (chat.getSender().equals(prevChat.getSender()) || chat.getReceiver().equals(prevChat.getReceiver())) {
                    otherChatViewHolder.userNickName.setVisibility(View.GONE);
                    otherChatViewHolder.userPhoto.setVisibility(View.GONE);
                }

                // position 채팅 데이터의 날짜와 이전 채팅 데이터의 날짜가 같다면 현재 position에 해당하는 view의 date는 숨김
                if (chat.getDate().equals(prevChat.getDate())) {
                    otherChatViewHolder.dateLayout.setVisibility(View.GONE);
                }


            }

            // 현재 position + 1가 전체 채팅 사이즈 보다 크다면 nextChat을 가져옴
            if (chats.size() > position + 1) {
                nextChat = chats.get(position + 1);
                // 현재 position의 chat time과 다음 position의 chat time이 같으면서 보낸 사람이 같을 경우 현재 view에는 시간 표시 안함 -> 마지막 chat data의 시간만 표시
                if (chat.getTime().equals(nextChat.getTime()) && chat.getSender().equals(nextChat.getSender())) {
                    otherChatViewHolder.time.setVisibility(View.GONE);
                }
            }

            otherChatViewHolder.userNickName.setText(chat.getMyNickName());
            otherChatViewHolder.message.setText(chat.getMessage());
            otherChatViewHolder.time.setText(chat.getTime());

            otherChatViewHolder.messageChecked.setText(chat.getChecked() == true ? "" : "1");
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

        private TextView message;
        private TextView time;
        private TextView messageChecked;

        private RelativeLayout dateLayout;
        private TextView date;

        public MyChatViewHolder(AdapterMyChatItemBinding myChatItemBinding) {
            super(myChatItemBinding.getRoot());
            this.myChatItemBinding = myChatItemBinding;

            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            messageChecked = itemView.findViewById(R.id.messageChecked);

            dateLayout = itemView.findViewById(R.id.dateLayout);
            date = itemView.findViewById(R.id.date);

        }

        void bind(Chat chat) {
            myChatItemBinding.setVariable(BR.chat, chat);
        }
    }

    class OtherChatViewHolder extends RecyclerView.ViewHolder {

        AdapterUserChatItemBinding userChatItemBinding;
        private CircleImageView userPhoto;
        private TextView userNickName;
        private TextView message;
        private TextView time;
        private TextView messageChecked;

        private RelativeLayout dateLayout;
        private TextView date;


        public OtherChatViewHolder(AdapterUserChatItemBinding userChatItemBinding) {
            super(userChatItemBinding.getRoot());
            this.userChatItemBinding = userChatItemBinding;

            userPhoto = itemView.findViewById(R.id.userPhoto);
            userNickName = itemView.findViewById(R.id.userNickName);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            messageChecked = itemView.findViewById(R.id.messageChecked);

            dateLayout = itemView.findViewById(R.id.dateLayout);
            date = itemView.findViewById(R.id.date);

        }

        void bind(Chat chat) {
            userChatItemBinding.setVariable(BR.chat, chat);
        }
    }
}
