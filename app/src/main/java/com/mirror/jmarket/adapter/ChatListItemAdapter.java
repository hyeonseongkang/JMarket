package com.mirror.jmarket.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.R;
import com.mirror.jmarket.classes.ChatRoom;
import com.mirror.jmarket.classes.Item;
import com.mirror.jmarket.classes.LastMessage;
import com.mirror.jmarket.classes.User;

import java.util.ArrayList;
import java.util.List;

public class ChatListItemAdapter extends RecyclerView.Adapter<ChatListItemAdapter.MyViewHolder>{

    private List<ChatRoom> chatRooms = new ArrayList<>();
    private onItemClickListener listener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_chat_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatRoom chatRoom = chatRooms.get(position);
        Item item = chatRoom.getItem();
        LastMessage lastMessage = chatRoom.getLastMessage();
        User user = chatRoom.getUser();

        holder.userNickName.setText(user.getNickName().length() <= 0 ? user.getEmail() : user.getNickName());
        holder.lastMessage.setText(lastMessage.getMessage());

        String[] lastMessageDate = lastMessage.getDate().split("-");
        String month = Integer.parseInt(lastMessageDate[1]) >= 10 ? lastMessageDate[1] : lastMessageDate[1].substring(1);
        String day = lastMessageDate[2];

        holder.unReadChatCountLayout.setVisibility(View.GONE);

        if (chatRoom.getUnReadChatCount() > 0) {
            holder.unReadChatCountLayout.setVisibility(View.VISIBLE);
            holder.unReadChatCount.setText(String.valueOf(chatRoom.getUnReadChatCount()));
        }

        holder.lastMessageDate.setText(month + "월" + day + "일");

        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(item.getFirstPhotoUri()))
                .into(holder.photo);

        String userPhoto = user.getPhotoUri();

        Glide.with(holder.itemView.getContext())
                .load(userPhoto.length() > 0 ? Uri.parse(user.getPhotoUri()) : R.drawable.basic_profile_photo)
                .into(holder.userPhoto);

    }

    @Override
    public int getItemCount() { return chatRooms == null ? 0 : chatRooms.size(); }

    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView photo;
        private ImageView userPhoto;
        private TextView userNickName;
        private TextView lastMessage;

        private TextView lastMessageDate;
        private TextView unReadChatCount;
        private RelativeLayout unReadChatCountLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.photo);
            userPhoto = itemView.findViewById(R.id.userPhoto);
            userNickName = itemView.findViewById(R.id.userNickName);
            lastMessage = itemView.findViewById(R.id.lastMessage);

            lastMessageDate = itemView.findViewById(R.id.lastMessageDate);
            unReadChatCount = itemView.findViewById(R.id.unReadChatCount);
            unReadChatCountLayout = itemView.findViewById(R.id.unReadChatCountLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(chatRooms.get(position));
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(ChatRoom chatRoom);
    }

    public void setOnItemClickListener(onItemClickListener listener) { this.listener = listener; }
}
