package com.mirror.jmarket.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mirror.jmarket.model.User;
import com.mirror.jmarket.repository.ItemRepository;
import com.mirror.jmarket.repository.UserManagerRepository;

import java.util.List;

public class UserManagerViewModel extends AndroidViewModel {

    private UserManagerRepository repository;

    public MutableLiveData<User> userProfile;

    private MutableLiveData<User> otherUserProfile;

    public MutableLiveData<List<User>> usersProfile;

    public MutableLiveData<Boolean> updateValid;

    public UserManagerViewModel(Application application) {
        super(application);
        //repository = new UserManagerRepository(application);
        repository = UserManagerRepository.getInstance(application);
        userProfile = repository.getUserProfile();
        otherUserProfile = repository.getOtherUserProfile();
        usersProfile = repository.getUsersProfile();
        updateValid = repository.getUpdateValid();

        updateValid.setValue(false);

    }

    public MutableLiveData<User> getUserProfile() {
        return userProfile;
    }

    public MutableLiveData<User> getOtherUserProfile() { return otherUserProfile; }

    public MutableLiveData<List<User>> getUsersProfile() { return usersProfile; }

    public MutableLiveData<Boolean> getUpdateValid() { return updateValid; }

    public void getUserProfile(String uid) { repository.getUserProfile(uid);}

    public void getOtherUserProfile(String uid) { repository.getOtherUserProfile(uid);}

    public void updateUserProfile(User user) {
        repository.updateUserProfile(user);
    }

    public void getUsersProfile(List<String> uids) { repository.getUsersProfile(uids);}
}
