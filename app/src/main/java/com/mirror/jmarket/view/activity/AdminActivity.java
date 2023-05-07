package com.mirror.jmarket.view.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mirror.jmarket.databinding.ActivityAdminBinding;
import com.mirror.jmarket.model.Item;
import com.mirror.jmarket.model.Test;
import com.mirror.jmarket.model.User;
import com.mirror.jmarket.utils.RxAndroidUtils;
import com.mirror.jmarket.viewmodel.AdminViewModel;
import com.mirror.jmarket.viewmodel.LoginViewModel;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private static final String TAG = "TEST";
    private ActivityAdminBinding binding;

    private AdminViewModel adminViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initObserve();
        initListener();
        Log.d(TAG, "onCreate");
    }

    void init() {
        adminViewModel = new ViewModelProvider(this).get(AdminViewModel.class);
        adminViewModel.getUsers();
        adminViewModel.getItems();
        adminViewModel.getTests();


    }

    void initObserve() {
        adminViewModel.getLiveDataUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                for (User user: users) {
                    Log.d(TAG, user.toString());
                }
            }
        });

        adminViewModel.getLiveDataItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(List<Item> items) {
                for (Item item: items) {
                    Log.d(TAG, item.toString());
                }
            }
        });

        adminViewModel.getLiveDataTests().observe(this, new Observer<List<Test>>() {
            @Override
            public void onChanged(List<Test> tests) {
                Log.d(TAG, "Test Observe !!");
                for (Test test: tests) {
                    Log.d(TAG, test.name + " " + test.age);
                }
            }
        });
    }

    void initListener() {
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
       // adminViewModel.removeListener();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.d(TAG, "Restart");
        //adminViewModel.getTests();

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }
}