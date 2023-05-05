package com.mirror.jmarket.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mirror.jmarket.model.User;
import com.mirror.jmarket.databinding.ActivityLoginBinding;
import com.mirror.jmarket.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivity";

    private ActivityLoginBinding loginBinding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        init();
        initObserve();
        initListener();

    }

    void init() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    void initObserve() {
        loginViewModel.getLoginValid().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(TAG, "HELLO!!!!!!!!");
                if (aBoolean) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    void initListener() {
        loginBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = loginBinding.userEmail.getText().toString();
                String password = loginBinding.userPassword.getText().toString();

                loginViewModel.login(new User(email, password));
            }
        });
        loginBinding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        loginViewModel.getLoginValid().removeObservers(this);
    }
}