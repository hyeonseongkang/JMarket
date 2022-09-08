package com.mirror.jmarket.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mirror.jmarket.classes.User;
import com.mirror.jmarket.model.LoginRepository;
import com.mirror.jmarket.model.UserManagerRepository;

import org.jetbrains.annotations.NotNull;

public class UserManagerViewModel extends AndroidViewModel {

    private UserManagerRepository repository;

    public MutableLiveData<User> userProfile;

    public MutableLiveData<Boolean> updateValid;

    public UserManagerViewModel(Application application) {
        super(application);
        repository = new UserManagerRepository(application);
        userProfile = repository.getUserProfile();
        updateValid = repository.getUpdateValid();
    }

    public MutableLiveData<User> getUserProfile() {
        return userProfile;
    }

    public MutableLiveData<Boolean> getUpdateValid() { return updateValid; }

    public void getUserProfile(String uid) { repository.getUserProfile(uid);}

    public void updateUserProfile(User user) {
        repository.updateUserProfile(user);
    }
}