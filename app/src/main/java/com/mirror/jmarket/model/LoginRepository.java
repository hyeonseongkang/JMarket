package com.mirror.jmarket.model;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.classes.User;

import org.jetbrains.annotations.NotNull;

public class LoginRepository {

    public final static String TAG = "LoginRepository";

    private Application application;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private MutableLiveData<FirebaseUser> firebaseUser;
    private MutableLiveData<Boolean> loginValid;

    public LoginRepository(Application application) {
        this.application = application;
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = new MutableLiveData<>();
        loginValid = new MutableLiveData<>();
    }

    public MutableLiveData<FirebaseUser> getFirebaseUser() { return firebaseUser; }

    public MutableLiveData<Boolean> getLoginValid() { return loginValid; }

    public void login(User user) {
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mUser = mAuth.getCurrentUser();
                            loginValid.setValue(true);
                            Log.d(TAG, "로그인 성공");
                        } else {
                            loginValid.setValue(false);
                            Log.d(TAG, "로그인 실패");
                        }
                    }
                });
    }

    public void register(User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 가입 성공
                            mUser = mAuth.getCurrentUser();
                            Log.d(TAG, "회원가입 성공");
                        } else {
                            // 가입 실패
                            Log.d(TAG, "회원가입 실패");
                        }
                    }
                });
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public void loginCheck() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            firebaseUser.setValue(currentUser);
            loginValid.setValue(true);
        } else {
            loginValid.setValue(false);
        }
    }

}
