package com.mirror.jmarket.adapter;

import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.R;
import com.mirror.jmarket.model.Chat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.MyViewHolder>{

    private List<Chat> chats = new ArrayList<>();
    private String myUid = new String();
    private String userUid = new String(); // 상대방 uid
    private String userPhoto; // 상대방 profile photo

    private String[] days = {"월요일", "화요일", "수요일", "목요일", "금요일", "토요일", "일요일"};

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        // 채팅 보낸 사람에 따라 layout 나눔
        if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_my_chat_item, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_user_chat_item, parent, false);
        }

        return new MyViewHolder(itemView, viewType);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Chat chat = chats.get(position);
        Chat prevChat;
        Chat nextChat;

        // 날짜 표시 visible
        holder.dateLayout.setVisibility(View.VISIBLE);
        holder.date.setVisibility(View.VISIBLE);
        String[] date = chat.getDate().split("-");

        LocalDate localDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
        DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int dayOfWeekNum = dayOfWeek.getValue();
        holder.date.setText(date[0] + "년 " + date[1] + "월 " + date[2] + "일 " + days[dayOfWeekNum - 1]);

        holder.userNickName.setVisibility(View.VISIBLE);
        holder.time.setVisibility(View.VISIBLE);
        holder.userPhoto.setVisibility(View.VISIBLE);


        Glide.with(holder.itemView.getContext())
                .load(R.drawable.basic_profile_photo)
                .into(holder.userPhoto);

        if (userPhoto.length() > 0 && !userPhoto.equals("null")) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(userPhoto))
                    .into(holder.userPhoto);
        }

        // 채팅 데이터가 1개 이상일때
        if (position > 0) {
            // (position - 1)번째 채팅 데이터와 position번째 채팅 데이터를 비교해 같은 유저가 보낸 채팅일 경우
            prevChat = chats.get(position - 1);
            if (chat.getSender().equals(prevChat.getSender()) || chat.getReceiver().equals(prevChat.getReceiver())) {
                holder.userNickName.setVisibility(View.GONE);
                holder.userPhoto.setVisibility(View.GONE);
            }

            // position 채팅 데이터의 날짜와 이전 채팅 데이터의 날짜가 같다면 현재 position에 해당하는 view의 date는 숨김
            if (chat.getDate().equals(prevChat.getDate())) {
                holder.dateLayout.setVisibility(View.GONE);
            }


        }

        // 현재 position + 1가 전체 채팅 사이즈 보다 크다면 nextChat을 가져옴
        if (chats.size() > position + 1) {
            nextChat = chats.get(position + 1);
            // 현재 position의 chat time과 다음 position의 chat time이 같으면서 보낸 사람이 같을 경우 현재 view에는 시간 표시 안함 -> 마지막 chat data의 시간만 표시
            if (chat.getTime().equals(nextChat.getTime()) && chat.getSender().equals(nextChat.getSender())) {
                holder.time.setVisibility(View.GONE);
            }
        }

        if (chat.getSender().equals(myUid))
            holder.userNickName.setVisibility(View.GONE);

        holder.userNickName.setText(chat.getMyNickName());
        holder.message.setText(chat.getMessage());
        holder.time.setText(chat.getTime());

        holder.messageChecked.setText(chat.getChecked() == true ? "" : "1");
    }

    @Override
    public int getItemCount() { return chats == null ? 0 : chats.size(); }

    public void setChats(List<Chat> chats, String myUid, String userUid, String userPhoto) {
        this.chats = chats;
        this.myUid = myUid;
        this.userUid = userUid;
        this.userPhoto = userPhoto;
        Log.d("ChatAdapterIte", userPhoto + " q받은 userphoto");
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userPhoto;
        private TextView userNickName;
        private TextView message;
        private TextView time;
        private TextView messageChecked;

        private RelativeLayout dateLayout;
        private TextView date;

        public MyViewHolder(View itemView, int viewType) {
            super(itemView);


            userPhoto = itemView.findViewById(R.id.userPhoto);
            userNickName = itemView.findViewById(R.id.userNickName);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            messageChecked = itemView.findViewById(R.id.messageChecked);

            dateLayout = itemView.findViewById(R.id.dateLayout);
            date = itemView.findViewById(R.id.date);

        }
    }
}
