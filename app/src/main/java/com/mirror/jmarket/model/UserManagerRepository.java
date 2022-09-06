package com.mirror.jmarket.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mirror.jmarket.classes.User;

import org.jetbrains.annotations.NotNull;

public class UserManagerRepository {

    public static final String TAG = "UserManagerRepository";

    public Application application;

    private MutableLiveData<User> userProfile;

    private MutableLiveData<Boolean> updateValid;


    // Firebase Database
    private DatabaseReference myRef;

    public UserManagerRepository(Application application) {
        this.application = application;
        userProfile = new MutableLiveData<>();
        updateValid = new MutableLiveData<>();
        myRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public MutableLiveData<User> getUserProfile() { return userProfile; }

    public MutableLiveData<Boolean> getUpdateValid() { return updateValid; }

    public void updateUserProfile(String uid, String nickName, String photoUri) {
        myRef.child(uid).child("nickName").setValue(nickName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    myRef.child(uid).child("photoUri").setValue(photoUri).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if (task.isSuccessful())
                                updateValid.setValue(true);
                            else
                                updateValid.setValue(false);
                        }
                    });
                }
            }
        });
    }
}
