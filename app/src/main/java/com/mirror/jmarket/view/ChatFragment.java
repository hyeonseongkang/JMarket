package com.mirror.jmarket.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.FragmentChatBinding;


public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";

    private FragmentChatBinding chatBinding;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        chatBinding = FragmentChatBinding.inflate(inflater, container, false);
        return chatBinding.getRoot();
    }
}