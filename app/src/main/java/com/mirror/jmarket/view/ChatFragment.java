package com.mirror.jmarket.view;

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

            }
        });

        /*

        Firebase에 chatItems Reference 만들고 하위 Item으로
        Users
        ItemPhoto -> 판매 사진
        lastMessage -> 채팅방 리스트에 표시될 메시지
        추가 하고 chatItems references 가져오기

         */
        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        chatViewModel.getMyChatRooms().observe(getActivity(), new Observer<List<ChatRoom>>() {
            @Override
            public void onChanged(List<ChatRoom> chatRooms) {
                adapter.setChatRooms(chatRooms);
            }
        });


//        chatBinding.sendMessage.setOnClickListener(new View.OnClickListener(){
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            public void onClick(View v) {
//                chatViewModel.sendMessage(user.getUid(), "vO3Igea5wFb8SutxiQMVDgTG1iJ2", new Chat(user.getUid(), "안녕!", chatViewModel.getDate()));
//            }
//        });
    }
}