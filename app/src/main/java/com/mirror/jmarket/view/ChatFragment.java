package com.mirror.jmarket.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.adapter.ChatListItemAdapter;
import com.mirror.jmarket.classes.Chat;
import com.mirror.jmarket.classes.ChatRoom;
import com.mirror.jmarket.classes.Item;
import com.mirror.jmarket.classes.User;
import com.mirror.jmarket.databinding.FragmentChatBinding;
import com.mirror.jmarket.viewmodel.ChatViewModel;
import com.mirror.jmarket.viewmodel.UserManagerViewModel;

import java.util.List;


public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";

    // view binding
    private FragmentChatBinding chatBinding;

    // viewModel
    private ChatViewModel chatViewModel;
    private UserManagerViewModel userManagerViewModel;

    // Firebase User
    private FirebaseUser user;

    // Adpater
    private ChatListItemAdapter adapter;

    // myNickName
    private String myNickName;



    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chatBinding = FragmentChatBinding.inflate(inflater, container, false);
        return chatBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = MainActivity.USER;

        chatBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatBinding.recyclerView.setHasFixedSize(true);
        adapter = new ChatListItemAdapter();
        chatBinding.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ChatListItemAdapter.onItemClickListener() {
            @Override
            public void onItemClick(ChatRoom chatRoom) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                User user = chatRoom.getUser();
                intent.putExtra("itemKey", chatRoom.getItem().getKey());
                intent.putExtra("uid", user.getUid()); // ????????? uid
                intent.putExtra("itemTitle", chatRoom.getItem().getTitle());
                intent.putExtra("myNickName", myNickName);
                intent.putExtra("userNickName", user.getNickName().length() > 0 ? user.getNickName() : user.getEmail());
                intent.putExtra("userPhoto", user.getPhotoUri());
                startActivity(intent);
            }
        });
        
        /*

        Firebase??? chatItems Reference ????????? ?????? Item??????
        Users
        ItemPhoto -> ?????? ??????
        lastMessage -> ????????? ???????????? ????????? ?????????
        ?????? ?????? chatItems references ????????????

         */
        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        chatViewModel.getMyChatRooms().observe(getActivity(), new Observer<List<ChatRoom>>() {
            @Override
            public void onChanged(List<ChatRoom> chatRooms) {
                adapter.setChatRooms(chatRooms);
            }
        });

        userManagerViewModel = new ViewModelProvider(requireActivity()).get(UserManagerViewModel.class);
        userManagerViewModel.getUserProfile(user.getUid());
        userManagerViewModel.getUserProfile().observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                myNickName = user.getNickName().equals("") ? user.getEmail() : user.getNickName() ;
            }
        });

    }
}