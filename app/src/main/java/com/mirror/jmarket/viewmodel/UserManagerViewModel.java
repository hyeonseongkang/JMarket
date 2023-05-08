package com.mirror.jmarket.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mirror.jmarket.model.User;
import com.mirror.jmarket.repository.ItemRepository;
import com.mirror.jmarket.repository.UserManagerRepository;

import java.util.List;

public class UserManagerViewModel extends AndroidViewModel {

    private UserManagerRepository repository;

    public LiveData<User> userProfile;

    private LiveData<User> otherUserProfile;

    public LiveData<List<User>> usersProfile;

    public LiveData<Boolean> updateValid;

    public UserManagerViewModel(Application application) {
        super(application);
        //repository = new UserManagerRepository(application);
        repository = UserManagerRepository.getInstance(application);
        userProfile = repository.getUserProfile();
        otherUserProfile = repository.getOtherUserProfile();
        usersProfile = repository.getUsersProfile();
        updateValid = repository.getUpdateValid();

        //updateValid.setValue(false);

    }

    public LiveData<User> getUserProfile() {
        return userProfile;
    }

    public LiveData<User> getOtherUserProfile() { return otherUserProfile; }

    public LiveData<List<User>> getUsersProfile() { return usersProfile; }

    public LiveData<Boolean> getUpdateValid() { return updateValid; }

    public void getUserProfile(String uid) { repository.getUserProfile(uid);}

    public void getOtherUserProfile(String uid) { repository.getOtherUserProfile(uid);}

    public void updateUserProfile(User user) {
        repository.updateUserProfile(user);
    }

    public void getUsersProfile(List<String> uids) { repository.getUsersProfile(uids);}
}
