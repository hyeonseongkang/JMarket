package com.mirror.jmarket.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.ActivityMainBinding;
import com.mirror.jmarket.viewmodel.ChatViewModel;
import com.mirror.jmarket.viewmodel.LoginViewModel;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    public static FirebaseUser USER;

    private ActivityMainBinding mainBinding;

    private LoginViewModel loginViewModel;
    private ChatViewModel chatViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
        loginViewModel.loginCheck();
        //loginViewModel.logout();
        USER = loginViewModel.getFirebaseUser().getValue();


        BadgeDrawable badgeDrawable = mainBinding.bottomNavigation.getOrCreateBadge(R.id.chat);
        badgeDrawable.setBackgroundColor(Color.RED);
        badgeDrawable.setBadgeTextColor(Color.WHITE);
        badgeDrawable.setMaxCharacterCount(4);

        chatViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ChatViewModel.class);
        //chatViewModel.testDelete();
        chatViewModel.getMyChats(USER.getUid());
        chatViewModel.getMyChatRooms(USER.getUid());
        chatViewModel.getUnReadChatCount(USER.getUid());
        chatViewModel.getUnReadChatCount().observe(this, new Observer<HashMap<List<String>, Integer>>() {
            @Override
            public void onChanged(HashMap<List<String>, Integer> hashMap) {
                int count = 0;
                for (List<String> keys: hashMap.keySet()) {
                    chatViewModel.setUnReadChatCount(USER.getUid(), keys.get(0), keys.get(1), hashMap.get(keys));
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
        /*
        chatViewModel.getUnReadChatCount().observe(this, new Observer<HashMap<String, Integer>>() {
            @Override
            public void onChanged(HashMap<String, Integer> hashMap) {
                int count = 0;
                for (String key : hashMap.keySet()) {
                    chatViewModel.setUnReadChatCount(USER.getUid(), key, hashMap.get(key));
                    System.out.println("확인하지 않은 채팅 개수: " + key + " " + hashMap.get(key));
                    count += hashMap.get(key);
                }

                System.out.println("total Count: " + count);
                if (count > 0) {
                    badgeDrawable.setNumber(count);
                    badgeDrawable.setVisible(true);
                } else {
                    badgeDrawable.clearNumber();
                    badgeDrawable.setVisible(false);
                }

            }
        });

         */


        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();

        mainBinding.bottomNavigation.setOnItemSelectedListener(onItemSelectedListener);
    }

    NavigationBarView.OnItemSelectedListener onItemSelectedListener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()) {
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
                    return true;

                case R.id.chat:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new ChatFragment()).commit();
                    return true;

                case R.id.mypage:
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MyPageFragment()).commit();
                    return true;
            }
            return false;
        }
    };
}