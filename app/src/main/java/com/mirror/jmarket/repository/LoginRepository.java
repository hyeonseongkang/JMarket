package com.mirror.jmarket.repository;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mirror.jmarket.model.User;

import org.jetbrains.annotations.NotNull;

public class LoginRepository {

    public final static String TAG = "LoginRepository";

    private Application application;

    private static LoginRepository instance;

    public static synchronized LoginRepository getInstance(Application application) {
        if (instance == null) {
            instance = new LoginRepository(application);
        }
        return instance;
    }

    private LoginRepository(Application application) {
        this.application = application;
        Log.d(TAG, "생성자입니다.");
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = new MutableLiveData<>();
        loginValid = new MutableLiveData<>();
        signUpValid = new MutableLiveData<>();
        myRef = FirebaseDatabase.getInstance().getReference("users");
    }

    private MutableLiveData<Boolean> loginValid;
    private MutableLiveData<Boolean> signUpValid;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private MutableLiveData<FirebaseUser> firebaseUser;

    // Firebase Database
    private DatabaseReference myRef;

    public MutableLiveData<FirebaseUser> getFirebaseUser() { return firebaseUser; }

    public MutableLiveData<Boolean> getLoginValid() { return loginValid; }

    public MutableLiveData<Boolean> getSignUpValid() { return signUpValid; }

    public void login(User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(application, "입력사항을 확인해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 이메일 로그인 요청
        mAuth.signInWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            /*
                            로그인 성공
                            FirebaseUser에 현재 user정보 넣고 loginValid에 true를 할당해 loginActivity에서 MainActivity로 넘어갈 수 있도록 함
                             */
                            mUser = mAuth.getCurrentUser();
                            firebaseUser.setValue(mUser);
                            loginValid.setValue(true);
                            Log.d(TAG, "로그인 성공");
                        } else {
                            // 로그인 실패
                            loginValid.setValue(false);
                            Log.d(TAG, "로그인 실패");
                        }
                    }
                });
    }

    public void signUp(User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(application, "입력사항을 확인해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int index = email.indexOf("@");
        String emailCheck = email.substring(index);

        if (!(emailCheck.equals("@jbnu.ac.kr"))) {
            Toast.makeText(application, "전북대 메일로만 가입할 수 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 이메일 회원가입 요청
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 가입 성공
                            /*
                            회원가입 성공 시 FirebaseUser에 현재 user정보 할당 및 Firebase Database Users Ref에 인자값으로 온 User정보 저장
                             */
                            mUser = mAuth.getCurrentUser();
                            firebaseUser.setValue(mUser);
                            // String uid, String email, String password, String nickName, String photoUri
                            myRef.child(mUser.getUid()).setValue(new User(mUser.getUid(), email, password, "", "")).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        signUpValid.setValue(true);
                                    } else {
                                        Log.d(TAG, "user 데이터 저장 실패");
                                    }
                                }
                            });

                        } else {
                            // 가입 실패
                            signUpValid.setValue(false);
                            Log.d(TAG, "회원가입 실패");
                        }
                    }
                });
    }

    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    public void loginCheck() {
        Log.d(TAG, "loginCheck" + " " + application);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            firebaseUser.setValue(currentUser);
            loginValid.setValue(true);
        } else {
            loginValid.setValue(false);
        }
    }

}
