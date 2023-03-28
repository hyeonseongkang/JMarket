package com.mirror.jmarket.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mirror.jmarket.R;
import com.mirror.jmarket.model.User;
import com.mirror.jmarket.databinding.ActivitySignUpBinding;
import com.mirror.jmarket.viewmodel.LoginViewModel;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";

    private ActivitySignUpBinding signUpBinding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());
        overridePendingTransition(R.anim.fadein_left, R.anim.none);

        loginViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LoginViewModel.class);
        loginViewModel.getLoginValid().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignUpActivity.this, "회원가입에 실패 했습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        signUpBinding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signUpBinding.userEmail.getText().toString();
                String password = signUpBinding.userPassword.getText().toString();

                loginViewModel.signUp(new User(email, password));
            }
        });

        signUpBinding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}