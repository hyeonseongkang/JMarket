package com.mirror.jmarket.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.model.Test;
import com.mirror.jmarket.model.User;
import com.mirror.jmarket.repository.AdminRepository;
import com.mirror.jmarket.repository.ChatRepository;

import java.util.List;

public class AdminViewModel extends AndroidViewModel {

   private AdminRepository repository;

   public MutableLiveData<List<User>> usersProfile;
   public MutableLiveData<List<Item>> items;
   public MutableLiveData<List<Test>> tests;

   public AdminViewModel(@NonNull Application application) {
      super(application);
      //repository = AdminRepository.getInstance(application);
      repository = new AdminRepository(application);
      usersProfile = repository.getLiveDataUsers();
      items = repository.getLiveDataItems();
      tests = repository.getLiveDataTests();
   }

   public MutableLiveData<List<User>> getLiveDataUsers() {
      return usersProfile;
   }

   public MutableLiveData<List<Item>> getLiveDataItems() { return items; }

   public MutableLiveData<List<Test>> getLiveDataTests() { return tests; }

   public void getUsers() {
      repository.getUsers();
   }

   public void getItems() { repository.getItems();}

   public void getTests() { repository.getTests();}

   public void removeListener() {
      repository.removeValueEventListener();
   }

   public void setTests() { repository.setTests();}
}
