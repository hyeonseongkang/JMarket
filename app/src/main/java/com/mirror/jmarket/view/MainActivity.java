package com.mirror.jmarket.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, new MypageFragment()).commit();
                    return true;
            }
            return false;
        }
    };
}