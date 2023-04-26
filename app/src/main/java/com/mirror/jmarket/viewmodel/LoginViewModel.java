package com.mirror.jmarket.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.mirror.jmarket.model.User;
import com.mirror.jmarket.repository.ItemRepository;
import com.mirror.jmarket.repository.LoginRepository;

import org.jetbrains.annotations.NotNull;

import io.reactivex.rxjava3.core.Observable;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository repository;
    private LiveData<FirebaseUser> firebaseUser;
    private MutableLiveData<Boolean> loginValid;
    private MutableLiveData<Boolean> signUpValid;

    public LoginViewModel(@NonNull @NotNull Application application) {
        super(application);
        //repository = new LoginRepository(application);
        repository = LoginRepository.getInstance(application);
        //  firebaseUser = repository.getFirebaseUser();
        //  loginValid = repository.getLoginValid();
        // signUpValid = repository.getSignUpValid();

        loginValid.setValue(false);
        signUpValid.setValue(false);
    }

    public LiveData<FirebaseUser> getFirebaseUser() {
        if (firebaseUser == null) {
            firebaseUser = repository.getFirebaseUser();
        }
        return firebaseUser;
    }

    public LiveData<Boolean> getLoginValid() {
        if (loginValid == null) {
            loginValid = repository.getLoginValid();
        }
        return loginValid;
    }

    public LiveData<Boolean> getSignUpValid() {
        if (signUpValid == null) {
            signUpValid = repository.getSignUpValid();
        }
        return signUpValid;
    }

    public void login(User user) {
        repository.login(user);
    }

    public void signUp(User user) {
        repository.signUp(user);
    }

    public void logout() {
        repository.logout();
    }

    public void loginCheck() {
        if (loginValid == null) {
            loginValid = repository.getLoginValid();
        }

        if (firebaseUser == null) {
            firebaseUser = repository.getFirebaseUser();
        }
        repository.loginCheck();
    }
}
