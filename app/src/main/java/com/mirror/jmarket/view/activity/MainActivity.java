package com.mirror.jmarket.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.ActivityMainBinding;
import com.mirror.jmarket.factory.LoginViewModelFactory;
import com.mirror.jmarket.repository.LoginRepository;
import com.mirror.jmarket.service.ChatService;
import com.mirror.jmarket.view.fragment.ChatFragment;
import com.mirror.jmarket.view.fragment.HomeFragment;
import com.mirror.jmarket.view.fragment.MyPageFragment;
import com.mirror.jmarket.viewmodel.ChatViewModel;
import com.mirror.jmarket.viewmodel.LoginViewModel;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    public static FirebaseUser USER;

    // view binding
    private ActivityMainBinding mainBinding;

    // view model
    private LoginViewModel loginViewModel;
    private ChatViewModel chatViewModel;

    // chat badge
    BadgeDrawable badgeDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        init();
        initObserve();
    }

    void init() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        // loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
        loginViewModel.loginCheck();
        USER = loginViewModel.getFirebaseUser().getValue();

        // bottom navigation 채팅 아이콘에 badge 달기
        badgeDrawable = mainBinding.bottomNavigation.getOrCreateBadge(R.id.chat);
        badgeDrawable.setBackgroundColor(Color.RED); // 빨간색 bg
        badgeDrawable.setBadgeTextColor(Color.WHITE); // 흰색 text
        badgeDrawable.setMaxCharacterCount(4); // 9999
        badgeDrawable.clearNumber();
        badgeDrawable.setVisible(false);

        // chat view model
        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        //  chatViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ChatViewModel.class);
        //chatViewModel.testDelete();
        //chatViewModel.getMyChats(USER.getUid()); // 내 채팅 가져오기
        chatViewModel.getMyChatRooms(FirebaseAuth.getInstance().getUid()); // 내 채팅방 가져오기
        chatViewModel.getUnReadChatCount(FirebaseAuth.getInstance().getUid()); // 읽지 않은 채팅 갯수 가져오기

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        mainBinding.bottomNavigation.setOnItemSelectedListener(onItemSelectedListener);

    }

    void initObserve() {
        // observe 통해 view model - model(repository)의 LiveData unReadChatCount를 관찰하다 변경이 생기면 호출됨
        chatViewModel.getUnReadChatCount().observe(this, new Observer<HashMap<List<String>, Integer>>() {
            @Override
            public void onChanged(HashMap<List<String>, Integer> hashMap) {
                int count = 0;
                for (List<String> keys : hashMap.keySet()) {
                    // keys.get(0) -> 상대 uid, keys.get(1) -> itemKey
                    // hashMap.get(keys) -> count

                    // 상대 uid, itemKey를 활용해 채팅방 마다 읽지 않은 채팅 수 나타내기
                    //   Log.d(TAG, keys.toString() + "!@!@!@ @ " + hashMap.get(keys));
                    chatViewModel.setUnReadChatCount(FirebaseAuth.getInstance().getUid(), keys.get(0), keys.get(1), hashMap.get(keys));
                    count += hashMap.get(keys);
                }

                if (count > 0) {
                    badgeDrawable.setNumber(count);
                    badgeDrawable.setVisible(true);
                } else {
                    badgeDrawable.clearNumber();
                    badgeDrawable.setVisible(false);
                }
            }
        });
    }



    NavigationBarView.OnItemSelectedListener onItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    Log.d(TAG, "MOVE HOME");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                    return true;

                case R.id.chat:
                    Log.d(TAG, "MOVE CHAT");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new ChatFragment()).commit();
                    return true;

                case R.id.mypage:
                    Log.d(TAG, "MOVE MYPAGE");
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyPageFragment()).commit();
                    return true;
            }
            return false;
        }
    };

}