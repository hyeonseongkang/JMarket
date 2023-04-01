package com.mirror.jmarket.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.model.User;
import com.mirror.jmarket.repository.ItemRepository;
import com.mirror.jmarket.repository.LoginRepository;

import org.jetbrains.annotations.NotNull;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository repository;
    private LiveData<FirebaseUser> firebaseUser;
    private LiveData<Boolean> loginValid;
    private LiveData<Boolean> signUpValid;

    public LoginViewModel(@NonNull @NotNull Application application) {
        super(application);
        //repository = new LoginRepository(application);
        repository = LoginRepository.getInstance(application);
        firebaseUser = repository.getFirebaseUser();
        loginValid = repository.getLoginValid();
        signUpValid = repository.getSignUpValid();
    }

    public LiveData<FirebaseUser> getFirebaseUser() { return firebaseUser;}

    public LiveData<Boolean> getLoginValid() { return loginValid; }

    public LiveData<Boolean> getSignUpValid() { return signUpValid; }

    public void login(User user) { repository.login(user); }

    public void signUp(User user) { repository.signUp(user); }

    public void logout() { repository.logout(); }

    public void loginCheck() { repository.loginCheck(); }
}
