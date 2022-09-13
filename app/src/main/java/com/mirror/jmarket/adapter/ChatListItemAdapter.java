package com.mirror.jmarket.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mirror.jmarket.R;
import com.mirror.jmarket.classes.User;

import java.util.ArrayList;
import java.util.List;

public class ChatListItemAdapter extends RecyclerView.Adapter<ChatListItemAdapter.MyViewHolder>{

    private List<User> users = new ArrayList<>();
    private String lastMessage = new String();
    private onItemClickListener listener;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_chat_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = users.get(position);
        String userName = user.getNickName() == null ? user.getEmail() : user.getNickName();
        holder.userNickName.setText(userName);
        holder.lastMessage.setText(lastMessage);

    }

    @Override
    public int getItemCount() { return users == null ? 0 : users.size(); }

    public void setUsers(List<User> users, String lastMessage) {
        this.users = users;
        this.lastMessage = lastMessage;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView photo;
        private TextView userNickName;
        private TextView lastMessage;

        public MyViewHolder(View itemView) {
            super(itemView);

            photo = itemView.findViewById(R.id.photo);
            userNickName = itemView.findViewById(R.id.userNickName);
            lastMessage = itemView.findViewById(R.id.lastMessage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(users.get(position));
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(User user);
    }

    public void setOnItemClickListener(onItemClickListener listener) { this.listener = listener; }
}
