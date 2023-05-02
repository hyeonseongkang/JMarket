package com.mirror.jmarket.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.mirror.jmarket.repository.AdminRepository;
import com.mirror.jmarket.repository.ChatRepository;

public class AdminViewModel extends AndroidViewModel {

   private AdminRepository repository;

   public AdminViewModel(@NonNull Application application) {
      super(application);
      repository = AdminRepository.getInstance(application);
   }
}
