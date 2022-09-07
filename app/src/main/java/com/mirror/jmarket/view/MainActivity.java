package com.mirror.jmarket.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.R;
import com.mirror.jmarket.databinding.ActivityMainBinding;
import com.mirror.jmarket.viewmodel.LoginViewModel;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    public static FirebaseUser USER;

    private ActivityMainBinding mainBinding;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
        loginViewModel.loginCheck();
        USER = loginViewModel.getFirebaseUser().getValue();
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