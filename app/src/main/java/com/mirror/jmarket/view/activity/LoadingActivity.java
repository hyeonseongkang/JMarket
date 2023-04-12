package com.mirror.jmarket.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.mirror.jmarket.R;
import com.mirror.jmarket.viewmodel.LoginViewModel;

public class LoadingActivity extends AppCompatActivity {

    public static String TAG = "LoadingActivity";

    private LoginViewModel loginViewModel;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        mHandler = new Handler();
        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);

        startLoading();

    }

    @Override
    public void onStart() {
        super.onStart();
        loginViewModel.loginCheck();
    }

    public void startLoading() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loginViewModel.getLoginValid().observe(LoadingActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        Log.d(TAG, "problem!!!!!!!!!@!@!@!!!~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        if (aBoolean) {
                            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                           // mHandler.removeCallbacksAndMessages(null);
                        } else {
                            Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                           // mHandler.removeCallbacksAndMessages(null);
                        }
                    }
                });
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }
}