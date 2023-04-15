package com.mirror.jmarket.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.mirror.jmarket.R;
import com.mirror.jmarket.factory.LoginViewModelFactory;
import com.mirror.jmarket.repository.LoginRepository;
import com.mirror.jmarket.viewmodel.LoginViewModel;

public class SplashActivity extends AppCompatActivity {

    public static String TAG = "LoadingActivity";

    private LoginViewModel loginViewModel;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();
       loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
//        LoginRepository loginRepository = LoginRepository.getInstance(getApplication());
//        LoginViewModelFactory loginViewModelFactory = new LoginViewModelFactory(loginRepository);
//        loginViewModel = new ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel.class);

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
                loginViewModel.getLoginValid().observe(SplashActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        Log.d(TAG, "problem!!!!!!!!!@!@!@!!!~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                        if (aBoolean) {
                            loginViewModel.getLoginValid().removeObservers(SplashActivity.this);
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                           // mHandler.removeCallbacksAndMessages(null);
                        } else {
                            loginViewModel.getLoginValid().removeObservers(SplashActivity.this);
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
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
        Toast.makeText(this, "Destroy", Toast.LENGTH_SHORT).show();
        //loginViewModel.getLoginValid().removeObservers(this);
        //mHandler.removeCallbacksAndMessages(null);
        mHandler = null;
    }
}