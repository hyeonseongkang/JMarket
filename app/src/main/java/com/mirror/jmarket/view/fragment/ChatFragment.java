package com.mirror.jmarket.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.mirror.jmarket.model.ChatRoom;
import com.mirror.jmarket.model.User;
import com.mirror.jmarket.databinding.FragmentChatBinding;
import com.mirror.jmarket.view.activity.ChatActivity;
import com.mirror.jmarket.view.activity.MainActivity;
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
                intent.putExtra("uid", user.getUid()); // 상대방 uid
                intent.putExtra("itemTitle", chatRoom.getItem().getTitle());
                intent.putExtra("myNickName", myNickName);
                intent.putExtra("userNickName", user.getNickName().length() > 0 ? user.getNickName() : user.getEmail());
                intent.putExtra("userPhoto", user.getPhotoUri());
                Log.d(TAG, user.getPhotoUri() + " 보낸 userphoto");
                startActivity(intent);
            }
        });
        
        /*

        Firebase에 chatItems Reference 만들고 하위 Item으로
        Users
        ItemPhoto -> 판매 사진
        lastMessage -> 채팅방 리스트에 표시될 메시지
        추가 하고 chatItems references 가져오기

         */
        // chatViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(ChatViewModel.class);
       // requireActivity() -> viewmodel 공유
        /*
getActivity()는 Fragment가 연결된 Activity가 없는 상태에서 호출되면 null을 반환하기 때문에, Fragment의 생명주기에 따라 null이 반환될 가능성이 있습니다. 따라서 Fragment에서 Activity의 참조를 사용할 때는 getActivity()의 반환값이 null인지 확인하고 사용해야 합니다.
requireActivity()는 이와 달리 Fragment가 연결된 Activity가 없는 경우에는 예외(IllegalStateException)를 발생시키므로, Activity의 참조를 안전하게 가져올 수 있습니다. 따라서 requireActivity()를 사용하는 것이 안전한 방법입니다.
         */

        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        chatViewModel.getMyChatRooms().observe(getActivity(), new Observer<List<ChatRoom>>() {
            @Override
            public void onChanged(List<ChatRoom> chatRooms) {
                Log.d(TAG, "getMyChatRooms");
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