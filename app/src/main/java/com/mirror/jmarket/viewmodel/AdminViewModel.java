package com.mirror.jmarket.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mirror.jmarket.model.User;
import com.mirror.jmarket.repository.AdminRepository;
import com.mirror.jmarket.repository.ChatRepository;

import java.util.List;

public class AdminViewModel extends AndroidViewModel {

   private AdminRepository repository;

   public MutableLiveData<List<User>> usersProfile;

   public AdminViewModel(@NonNull Application application) {
      super(application);
      repository = AdminRepository.getInstance(application);
      usersProfile = repository.getUsersProfile();
   }

   public MutableLiveData<List<User>> getUsersProfile() {
      return usersProfile;
   }

   public void getUsers() {
      repository.getUsers();
   }
}
