package com.mirror.jmarket.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.mirror.jmarket.R;
import com.mirror.jmarket.viewmodel.LoginViewModel;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;

public class SplashActivity extends AppCompatActivity {

    public static String TAG = "LoadingActivity";

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);

        startLoading();
    }

    @Override
    public void onStart() {
        super.onStart();
        loginViewModel.loginCheck();
    }

    public void startLoading() {
        loginViewModel.getLoginValid().observe(SplashActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
               // loginViewModel.getLoginValid().removeObservers(SplashActivity.this);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        loginViewModel.getLoginValid().removeObservers(SplashActivity.this);
    }

}