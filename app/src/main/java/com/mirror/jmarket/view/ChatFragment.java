package com.mirror.jmarket.view;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.classes.Chat;
import com.mirror.jmarket.databinding.FragmentChatBinding;
import com.mirror.jmarket.viewmodel.ChatViewModel;
import com.mirror.jmarket.viewmodel.LoginViewModel;
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

        chatViewModel = new ViewModelProvider(requireActivity()).get(ChatViewModel.class);
        chatViewModel.getChatRoom(user.getUid());
        chatViewModel.getChatUsers().observe(getActivity(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> users) {
                for (String uid: users) {
                    Log.d(TAG, uid);
                }
            }
        });

        chatBinding.sendMessage.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                chatViewModel.sendMessage(user.getUid(), "vO3Igea5wFb8SutxiQMVDgTG1iJ2", new Chat(user.getUid(), "안녕!", chatViewModel.getDate()));
            }
        });
    }
}