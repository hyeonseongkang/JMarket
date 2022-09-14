package com.mirror.jmarket.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        holder.itemTitle.setText(item.getTitle());
        holder.lastMessage.setText(lastMessage.getMessage());
        Glide.with(holder.itemView.getContext())
                .load(Uri.parse(item.getFirstPhotoUri()))
                .into(holder.photo);

    }

    @Override
    public int getItemCount() { return chatRooms == null ? 0 : chatRooms.size(); }

    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView photo;
        private TextView itemTitle;
        private TextView lastMessage;

        public MyViewHolder(View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.photo);
            itemTitle = itemView.findViewById(R.id.itemTitle);
            lastMessage = itemView.findViewById(R.id.lastMessage);

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
