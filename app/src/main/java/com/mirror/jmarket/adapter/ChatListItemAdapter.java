package com.mirror.jmarket.adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.BR;
import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.AdapterChatListItemBinding;
import com.mirror.jmarket.model.ChatRoom;
import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.model.LastMessage;
import com.mirror.jmarket.model.User;

import java.util.ArrayList;
import java.util.List;

public class ChatListItemAdapter extends RecyclerView.Adapter<ChatListItemAdapter.MyViewHolder>{

    private List<ChatRoom> chatRooms = new ArrayList<>();
    private onItemClickListener listener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterChatListItemBinding binding = AdapterChatListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ChatRoom chatRoom = chatRooms.get(position);
        holder.bind(chatRoom);
    }

    @Override
    public int getItemCount() { return chatRooms == null ? 0 : chatRooms.size(); }

    public void setChatRooms(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        AdapterChatListItemBinding binding;

        public MyViewHolder(AdapterChatListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(chatRooms.get(position));
                    }
                }
            });
        }

        void bind(ChatRoom chatRoom) {
            binding.setVariable(BR.chatRoom, chatRoom);
        }
    }

    public interface onItemClickListener {
        void onItemClick(ChatRoom chatRoom);
    }

    public void setOnItemClickListener(onItemClickListener listener) { this.listener = listener; }
}
