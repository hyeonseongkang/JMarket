package com.mirror.jmarket.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mirror.jmarket.R;
import com.mirror.jmarket.classes.Chat;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemAdapter.MyViewHolder>{

    private List<Chat> chats = new ArrayList<>();
    private String myUid = new String();
    private String userPhoto = new String(); // 상대방 profile photo

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_my_chat_item, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_user_chat_item, parent, false);
        }

        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemViewType(int position) {
        if(chats.get(position).getSender().equals(myUid)) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Chat chat = chats.get(position);
        Chat beforeChat;

        holder.userNickName.setVisibility(View.VISIBLE);
        holder.userNickName.setText("");

        if (userPhoto.length() > 0) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(userPhoto))
                    .into(holder.userPhoto);
        }

        if (position > 0) {
            beforeChat = chats.get(position - 1);
            if (chat.getSender().equals(beforeChat.getSender()) || chat.getReceiver().equals(beforeChat.getReceiver()) )
                holder.userNickName.setVisibility(View.GONE);
        }

        if (chat.getSender().equals(myUid))
            holder.userNickName.setVisibility(View.GONE);
        holder.userNickName.setText(chat.getMyNickName());
        holder.message.setText(chat.getMessage());
        holder.time.setText(chat.getTime());
        /*
        holder.messageChecked.setText(chat.getChecked() == true ? "" : "1");
         */
    }

    @Override
    public int getItemCount() { return chats == null ? 0 : chats.size(); }

    public void setChats(List<Chat> chats, String myUid, String userPhoto) {
        this.chats = chats;
        this.myUid = myUid;
        this.userPhoto = userPhoto;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userPhoto;
        private TextView userNickName;
        private TextView message;
        private TextView time;
        private TextView messageChecked;

        public MyViewHolder(View itemView) {
            super(itemView);

            userPhoto = itemView.findViewById(R.id.userPhoto);
            userNickName = itemView.findViewById(R.id.userNickName);
            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            messageChecked = itemView.findViewById(R.id.messageChecked);
        }
    }
}
