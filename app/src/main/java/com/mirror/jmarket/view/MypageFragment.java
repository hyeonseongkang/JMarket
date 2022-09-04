package com.mirror.jmarket.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.FragmentMypageBinding;

public class MypageFragment extends Fragment {

    private static final String TAG = "ChatFragment";

    private FragmentMypageBinding mypageBinding;

    public MypageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mypageBinding = FragmentMypageBinding.inflate(inflater, container, false);
        return mypageBinding.getRoot();
    }
}