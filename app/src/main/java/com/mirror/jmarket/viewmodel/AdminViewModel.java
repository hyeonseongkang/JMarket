package com.mirror.jmarket.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.model.User;
import com.mirror.jmarket.repository.AdminRepository;
import com.mirror.jmarket.repository.ChatRepository;

import java.util.List;

public class AdminViewModel extends AndroidViewModel {

   private AdminRepository repository;

   public MutableLiveData<List<User>> usersProfile;
   public MutableLiveData<List<Item>> items;

   public AdminViewModel(@NonNull Application application) {
      super(application);
      repository = AdminRepository.getInstance(application);
      usersProfile = repository.getLiveDataUsers();
      items = repository.getLiveDataItems();
   }

   public MutableLiveData<List<User>> getLiveDataUsers() {
      return usersProfile;
   }

   public MutableLiveData<List<Item>> getLiveDataItems() { return items; }

   public void getUsers() {
      repository.getUsers();
   }

   public void getItems() { repository.getItems();}
}
